package com.example.demo.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@Builder
@Getter
@Setter
public class Shop implements Comparable<Shop> {
    private String title;
    private String link;
    private String image;
    private Long lprice; //최저가
    private Long hprice; //최고가
    private Long productId; //상품 id
    
    //기준정렬 만들기 - 최저가 오름차순
    @Override
    public int compareTo(Shop o) {
        return this.lprice > o.lprice ? 1 : -1;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", image='" + image + '\'' +
                ", lprice=" + lprice +
                ", hprice=" + hprice +
                ", productId=" + productId +
                '}';
    }
}
