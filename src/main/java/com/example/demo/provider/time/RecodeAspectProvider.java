package com.example.demo.provider.time;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class RecodeAspectProvider {

    //PointCut = @Around에 정의한 애노테이션을 사용한 메소드에만 해당 코드가
    // 메소드 수행 전처리 후처리 사용된다
    @Around("@annotation(com.example.demo.provider.time.PerformanceTimeRecord)")
    public Object logExcutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("time 체크 시작");
        long start = System.currentTimeMillis();

        //가장 중요한 구문으로 실제 애노테이션이 붙은 메소드의 내용이 실행된다.
        Object proceed = joinPoint.proceed();

        long executionTime = System.currentTimeMillis() - start;
        System.out.println("결과 시간 = " + executionTime);

        //@Slf4j로 log 사용
        log.info(joinPoint.getSignature()+ "executed in "+ executionTime + "ms");

        //반드시 proceed의 결과를 return 해주어야 한다.
        return proceed;
    }
}
