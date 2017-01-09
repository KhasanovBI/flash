package ru.khasanov.cache;


import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


// Лучше, конечно, LRU Memory cache
public class FileCache {

    private static final Logger logger = LoggerFactory.getLogger(FileCache.class);
    private static final int CAPACITY = 1024;
    private Map<Path, byte[]> entity;
    private Thread watcher;

    public FileCache(String rootDirectory) {
        entity = new ConcurrentLinkedHashMap.Builder<Path, byte[]>().maximumWeightedCapacity(CAPACITY).build();
        watcher = new FileCacheWatcher(Paths.get(rootDirectory), this);
        runWatcher();
        logger.info("Cache initialized");
    }

    public byte[] get(Path resourcePath) {
        byte[] resourceData = entity.get(resourcePath);
        if (resourceData != null) {
            logger.debug("Key found: " + resourcePath);
        }
        return resourceData;
    }

    public void set(Path resourcePath, byte[] resourceData) {
        entity.put(resourcePath, resourceData);
        logger.debug("Set key: " + resourcePath);
    }

    public void delete(Path resourcePath) {
        entity.remove(resourcePath);
        logger.debug("Delete key: " + resourcePath);
    }

    private void runWatcher() {
        watcher.start();
    }

    private void stopWatcher() {
        watcher.interrupt();
    }
}
