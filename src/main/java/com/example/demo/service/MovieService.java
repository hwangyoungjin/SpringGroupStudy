package com.example.demo.service;

import com.example.demo.model.Movie;
import com.example.demo.model.MovieGroup;
import com.example.demo.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class MovieService {

    //캐싱용으로 사용 할 자료구조
    static HashMap<String, List<Movie>> hm = new HashMap<>();

    @Autowired
    MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    public List<Movie> search(final String query){
        List<Movie> movies = null;
        if(hm.containsKey(query)){ // 자료구조에 있는지 검색
            //있다면 받아오기
            movies = hm.get(query);
            System.out.println(query+"결과 캐시에서 가져옴");
        }
        else{ //없으면 네이버 Open API 호출
            MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
            movies = movieGroup.getListOrderRating();
            hm.put(query,movies);
            System.out.println(query+"결과 네이버 OpenAPI 에서 가져옴");
        }
        return movies;
    }

    //관리자 강제 Update용
    public List<Movie> cacheUpdate(final String query) {
        MovieGroup movieGroup = new MovieGroup(movieRepository.findByQuery(query));
        List<Movie> movies = movieGroup.getListOrderRating();
        hm.put(query, movies);
        System.out.println(query+" 결과 Update");
        return movies;
    }
}
