package ru.khasanov.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.*;


/**
 * Created by bulat on 08.01.17.
 */
public class FileCacheWatcher extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FileCacheWatcher.class);
    private WatchService watcher;
    private final WeakReference<FileCache> fileCacheWeakReference;
    private final Map<WatchKey, Path> keys = new HashMap<>();
    private boolean trace = false;

    public FileCacheWatcher(Path rootDirectoryPath, FileCache fileCache) {
        super("FileCacheWatcher");
        fileCacheWeakReference = new WeakReference<>(fileCache);

        try {
            watcher = rootDirectoryPath.getFileSystem().newWatchService();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        logger.info("Initial recursive register");
        recursiveRegister(rootDirectoryPath);
        // enable trace after initial registration
        this.trace = true;

    }

    @SuppressWarnings("unchecked")
    static <T> WatchEvent<T> cast(WatchEvent<?> event) {
        return (WatchEvent<T>) event;
    }

    private void register(Path dirPath) {
        WatchKey watchKey = null;
        try {
            watchKey = dirPath.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (trace) {
            Path prev = keys.get(watchKey);
            if (prev == null) {
                logger.info("Register: " + dirPath);
            } else {
                if (!dirPath.equals(prev)) {
                    logger.info(String.format("Update: %s -> %s", prev, dirPath));
                }
            }
        }
        keys.put(watchKey, dirPath);
    }

    private void recursiveRegister(final Path start) {
        try {
            Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult preVisitDirectory(Path dirPath, BasicFileAttributes attrs) throws IOException {
                    register(dirPath);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void dropCacheEntry(Path resourcePath) {
        fileCacheWeakReference.get().delete(resourcePath);
    }

    void processEvents() {
        while (true) {
            WatchKey watchKey;
            try {
                watchKey = watcher.take();
            } catch (InterruptedException x) {
                return;
            }

            Path dir = keys.get(watchKey);
            if (dir == null) {
                logger.error("WatchKey not recognized!!");
                continue;
            }

            for (WatchEvent<?> event : watchKey.pollEvents()) {
                WatchEvent.Kind kind = event.kind();
                WatchEvent<Path> ev = cast(event);
                Path relativePath = ev.context();
                Path resolvedPath = dir.resolve(relativePath);
                logger.debug(String.format("%s: %s", event.kind().name(), resolvedPath));
                if (kind == ENTRY_CREATE) {
                    if (Files.isDirectory(resolvedPath, NOFOLLOW_LINKS)) {
                        recursiveRegister(resolvedPath);
                    }
                } else {
                    // При OVERFLOW тоже очищаем
                    dropCacheEntry(resolvedPath);
                }
            }

            boolean valid = watchKey.reset();
            if (!valid) {
                keys.remove(watchKey);
                if (keys.isEmpty()) {
                    break;
                }
            }
        }
    }


    @Override
    public void run() {
        logger.info("Run");
        processEvents();
    }
}
