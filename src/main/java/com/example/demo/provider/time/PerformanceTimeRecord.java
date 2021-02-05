package com.example.demo.provider.time;

import com.example.demo.model.Movie;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.List;

@Retention(RetentionPolicy.RUNTIME) //애노테이션 얼마나 유지할지
@Target(ElementType.METHOD)
public @interface PerformanceTimeRecord {
}
