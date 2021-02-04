package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cache")
public class CacheAPIController {

    @Autowired
    MovieService movieService;

    @GetMapping("/movies/{query}")
    public List<Movie> movieDataUpdate(@PathVariable String query){
        return movieService.cacheUpdate(query);
    }
}
