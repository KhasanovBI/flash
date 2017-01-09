package ru.khasanov.cache;


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


// По заданию не написано какой кэш, лучше, конечно, LRU Memory cache
public class FileCache {
    private static final int CAPACITY = 1024;
    private static final Logger logger = LoggerFactory.getLogger(FileCache.class);
    private Map<Path, byte[]> entity;
    private Path rootDirectoryPath;
    private Thread watcher;

    public FileCache(String rootDirectory) {
        rootDirectoryPath = Paths.get(rootDirectory);
        entity = new ConcurrentLinkedHashMap.Builder<Path, byte[]>().maximumWeightedCapacity(CAPACITY).build();
        watcher = new FileCacheWatcher(rootDirectoryPath, this);
        runWatcher();
        logger.info("Init cache.");
    }

    public byte[] get(Path resourcePath) {
        return entity.get(resourcePath);
    }

    public void set(Path resourcePath, byte[] resourceData) {
        entity.put(resourcePath, resourceData);
    }

    public void delete(Path resourcePath) {
        entity.remove(resourcePath);
    }

    private void runWatcher() {
        watcher.start();
    }

    private void stopWatcher() {
        watcher.interrupt();
    }
}
