package com.example.demo.controller;

import com.example.demo.model.Movie;
import com.example.demo.model.Shop;
import com.example.demo.service.MovieService;
import com.example.demo.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    @Autowired
    private MovieService movieService;

    @Autowired
    private ShopService shopService;
    @GetMapping("/movies")
    public List<Movie> getMoviesByQuery (@RequestParam(name = "q") String query){ //q는 url 쿼리 name 값
         return movieService.search(query);
    }

    @GetMapping("/shops")
    public List<Shop> getShopsByQuery(@RequestParam(name = "s") String query){
        //return shopService.search(query);
        //이름정렬
        return shopService.getOrderName(query);
    }
}
