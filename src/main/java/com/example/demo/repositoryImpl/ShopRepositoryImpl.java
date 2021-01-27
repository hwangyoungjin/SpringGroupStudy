package com.example.demo.repositoryImpl;

import com.example.demo.config.NaverProperties;
import com.example.demo.model.ResponseShop;
import com.example.demo.model.Shop;
import com.example.demo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShopRepositoryImpl implements ShopRepository {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    NaverProperties naverProperties;

    @Override
    public List<Shop> findByQuery(String query) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Naver-Client-Id",naverProperties.getClientId());
        httpHeaders.add("X-Naver-Client-Secret",naverProperties.getClientSecret());

        String url = naverProperties.getShopUrl()+"?query="+query;
        return restTemplate.exchange(url, HttpMethod.GET,new HttpEntity<>(httpHeaders), ResponseShop.class)
                .getBody() //ResponseShop 객체로 요청 결과 (응답) 바인딩됨
                .getItems()//ResponseShop의 item
                .stream()
                .map(m->Shop.builder() //item의 데이터를 Shop 객체로 바인딩하기
                .title(m.getTitle())
                .link(m.getLink())
                .image(m.getImage())
                .iprice(m.getIprice())
                .hprice(m.getHprice())
                .productId(m.getProductId())
                .build())
                .collect(Collectors.toList()); //List<Shop> 으로 return
    }
}
