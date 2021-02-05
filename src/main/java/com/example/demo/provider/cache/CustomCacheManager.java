package com.example.demo.provider.cache;

import com.example.demo.model.CurrentMapCustomCache;

import java.util.Collection;

public interface CustomCacheManager {
    //name 맞는 캐시 가져오기
    CurrentMapCustomCache getChche(String name);

    //현재 존재하는 cache name 받기
    Collection<String> getCacheStorageNames();
}
