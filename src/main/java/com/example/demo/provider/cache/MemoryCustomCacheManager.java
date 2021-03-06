package com.example.demo.provider.cache;

import com.example.demo.model.CustomCache;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class MemoryCustomCacheManager implements CustomCacheManager {

    private final ConcurrentMap<String, CustomCache> cacheMap
            = new ConcurrentHashMap<>(16);

    @Override
    //name에 맞는 CustomCache를 가져온다.
    public CustomCache getChche(String name) {
        //cache 가져오기
        CustomCache cache = this.cacheMap.get(name);
        synchronized (this.cacheMap){ //데이터 일관성 유지
            cache = this.cacheMap.get(name); // Cache저장소에서 name에 해당하는 CustomCache를 찾는다
            if(cache == null){ // cache에 해당 name이 없다면 -> null 이라면
                cache = createConcurrentMapCustomCache(name); // 해당 name의 cache를 만들고
                this.cacheMap.put(name, cache); //cacheMap에 캐시 공간 넣는다.
            }
        }
        return cache;
    }

    //key-value 넣을 캐시 공간 생성
    protected CustomCache createConcurrentMapCustomCache(String name) {
        return new CustomCache(name, new ConcurrentHashMap<>(256));
    }

    @Deprecated
    @Override
    public Collection<String> getCacheStorageNames() {
        return Collections.unmodifiableSet(this.cacheMap.keySet());
    }
}
