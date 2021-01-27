package com.example.demo.repository;

import com.example.demo.model.Shop;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository {
    List<Shop> findByQuery(String query);
}
