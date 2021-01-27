package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Builder
@Getter
@Setter
public class Shop {
    private String title;
    private String link;
    private String image;
    private Long iprice; //최저가
    private Long hprice; //최고가
    private Long productId; //상품 id
}
