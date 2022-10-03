package com.coupon.couponChallenge.dto.coupon;

import com.google.gson.Gson;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class ItemConsultDTO implements Serializable, Comparable<ItemConsultDTO> {
    @NotNull
    private String id;

    @NotNull
    private Float price;

    public ItemConsultDTO(String key, Float price) {
        super();
        this.id =key;
        this.price = price;
    }

    public ItemConsultDTO(String jsonString) {
        super();
        Gson g = new Gson();
        ItemConsultDTO p = g.fromJson(jsonString, ItemConsultDTO.class);
        this.id =p.getId();
        this.price = p.getPrice();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    @Override
    public int compareTo(ItemConsultDTO item) {
        return this.getPrice().compareTo(item.getPrice());
    }
}
