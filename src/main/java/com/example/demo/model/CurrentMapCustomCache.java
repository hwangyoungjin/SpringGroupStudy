package com.example.demo.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class CurrentMapCustomCache {

    private final String name;
    private final ConcurrentMap<String, Object> store;

    public CurrentMapCustomCache(String name, ConcurrentHashMap<String,Object> store){
        this.name = name;
        this.store = store;
    }

    //캐시 데이터를 key를 이용해서 찾는다. 없으면 Optional.empty 반환
    public Optional<Object> lookup(Object key){
        return Optional.ofNullable(this.store.get(key));
    }

    //캐시 데이터 저장
    public boolean put(String key, Object value){
        this.store.put(key,value);
        return true;
    }

    public String getName() {
        return name;
    }

    public ConcurrentMap<String, Object> getStore() {
        return store;
    }
}

