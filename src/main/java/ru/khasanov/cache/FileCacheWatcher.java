package ru.khasanov.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;

/**
 * Created by bulat on 08.01.17.
 */
public class FileCacheWatcher extends Thread {
    private static final Logger logger = LoggerFactory.getLogger(FileCacheWatcher.class);

    private final Path rootDirectoryPath;
    private final WeakReference<FileCache> cacheWeakReference;

    public FileCacheWatcher(Path rootDirectoryPath, FileCache fileCache) {
        super("FileCacheWatcher");
        this.rootDirectoryPath = rootDirectoryPath;
        this.cacheWeakReference = new WeakReference<>(fileCache);
    }

    @Override
    public void run() {
        WatchService watcher = null;
        try {
            watcher = rootDirectoryPath.getFileSystem().newWatchService();
            rootDirectoryPath.register(watcher,
                    StandardWatchEventKinds.ENTRY_CREATE,
                    StandardWatchEventKinds.ENTRY_DELETE,
                    StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        logger.info("Run watcher.");
        while (true) {
            // TODO
        }
    }
}
