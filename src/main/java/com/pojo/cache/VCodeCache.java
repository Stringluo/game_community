package com.pojo.cache;

import com.alicp.jetcache.Cache;
import com.alicp.jetcache.anno.CreateCache;
import org.springframework.stereotype.Component;

public class VCodeCache {

    @CreateCache(name = "vCodeCache_", expire = 300)
    private Cache<String, String> vCodeCache;

    public String getVCodeCache(String key) {
        return vCodeCache.get(key);
    }

    public void setVCodeCache(String key, String vCode) {
        this.vCodeCache.put(key,vCode);
    }
}
