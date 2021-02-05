package com.example.demo.provider.cache;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Aspect
@Component
public class CachingAspectProvider {
    private final CustomCacheManager cacheManager;

    public CachingAspectProvider(CustomCacheManager customCacheManager){
        this.cacheManager = customCacheManager;
    }

    //TODO: 클린 코드
    @Around("@annotation(com.example.demo.provider.cache.LookAsideCaching) && @annotation(target)") //애노테이션 파라미터 받기 위해 @annotation(target)정의
    public Object handlerLookAsideCaching(ProceedingJoinPoint joinPoint, LookAsideCaching target) throws Throwable {

        if (StringUtils.isEmpty(target.value())) {
            return joinPoint.proceed();
        }


        String cacheName = target.value(); //파라미터로 받은 target
        String cacheKey = getKey(joinPoint, target); // taget의 key에 대한 값을 가져온다.

        System.out.println("==========Cache 실행됨=========");
        System.out.println("targat 파라미터의 key = "+ target.key());
        System.out.println("targat 파라미터의 value = "+ cacheName);
        System.out.println("target 파라미너 key에 대한 값"+ cacheKey);



        return findInCaches(cacheName, cacheKey)
                .orElseGet(() -> {
                    try {
                        Object data = joinPoint.proceed();
                        putInCache(cacheName, cacheKey, data).thenAccept(isSave -> {
                            if(isSave) { log.info(""); }
                            else { log.error(""); }
                        });
                        return data;
                    } catch (Throwable throwable) {
                        //TODO: 예외 처리 구문 추가
                        log.error("error...");
                        throw new RuntimeException("error...");
                    }
                });
    }

    private String getKey(final ProceedingJoinPoint joinPoint, final LookAsideCaching target) {
        if ("NONE".equals(target.key())) { // key가 없다면
            return "simpleKey";
        } else {
            CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature(); //호출되는 메서드의 정보를 불러온다
            Arrays.stream(joinPoint.getArgs()).forEach(System.out::println);

            int keyIndex = Arrays.asList(codeSignature.getParameterNames()).indexOf(target.key()); // key의 위치 인덱스
            return String.valueOf(joinPoint.getArgs()[keyIndex]); // 해당위치의 값을 가져온다.
        }
    }

    private Optional<Object> findInCaches(final String name, final String key) {
        //name 해당하는 cache를 받아서 key에 해당하는 데이터를 Optional 타입으로 반환
        return cacheManager.getChche(name).lookup(key);
    }

    private CompletableFuture<Boolean> putInCache(final String name, final String key, final Object data) {
        //TODO: 비동기로 동작하지만, Common Pool의 쓰레드를 사용함..
        // Common pool 을 사용하지 않도록 개선해야 함. 스터디 7주차 쯤, 비동기, 병렬 프로그래밍 스터디 예정
        return CompletableFuture.supplyAsync(() -> cacheManager.getChche(name).put(key, data));
    }
}
