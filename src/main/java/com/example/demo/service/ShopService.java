package com.example.demo.service;

import com.example.demo.model.Shop;
import com.example.demo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShopService {
    @Autowired
    ShopRepository shopRepository;


    public List<Shop> search(final String query){
        return shopRepository.findByQuery(query);
    }

    //이름정렬
    public List<Shop> getOrderName(String query){
        List<Shop> shops = shopRepository.findByQuery(query);
        return shops.stream().filter(a->!a.getLprice().equals(0)).sorted().collect(Collectors.toList());
    }
}
