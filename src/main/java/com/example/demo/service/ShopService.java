package com.example.demo.service;

import com.example.demo.model.Shop;
import com.example.demo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopService {
    @Autowired
    ShopRepository shopRepository;
    public List<Shop> search(final String query){
        return shopRepository.findByQuery(query);
    }
}
