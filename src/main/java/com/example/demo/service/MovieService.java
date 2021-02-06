package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieGroup;
import com.example.demo.provider.cache.LookAsideCaching;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    //@LookAsideCaching(value = "cache::movies",key = "query")
    @Cacheable(value = "cache::movies::query")
    public List<Movie> search(final String query){
        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
        return movieGroup.getListOrderRating();
    }


    //관리자 강제 Update용
    public List<Movie> cacheUpdate(final String query) {
        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
        List<Movie> movies = movieGroup.getListOrderRating();
        return movies;
    }

}
