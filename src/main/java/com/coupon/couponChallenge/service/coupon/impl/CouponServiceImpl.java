package com.coupon.couponChallenge.service.coupon.impl;

import com.coupon.couponChallenge.dto.coupon.ItemConsultDTO;
import com.coupon.couponChallenge.service.coupon.CouponService;
import com.coupon.couponChallenge.util.constants.CouponConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Log4j2
@Service
public class CouponServiceImpl implements CouponService {
    public List<String> calculate(Map<String, Float> items, Float amount){
        log.info(CouponConstants.Message.START_CALCULATE_COUPON_PRODUCTS);
        List<String> prod_items = new ArrayList<>();
        PriorityQueue<ItemConsultDTO> minItemPrice = new PriorityQueue<>();
        for(String key: items.keySet()){
            try{
                minItemPrice.add(new ItemConsultDTO(key, items.get(key)));
            }catch (Exception e){
                log.error(e);
            }
        }
        float total = 0;
        boolean stillSearching = true;
        ItemConsultDTO tempItem;
        while(stillSearching && !minItemPrice.isEmpty()){
            if (minItemPrice.peek().getPrice() > amount) {
                stillSearching = false;
                continue;
            }
            if (amount < minItemPrice.peek().getPrice() + total) {
                stillSearching = false;
            }else{
                total += minItemPrice.peek().getPrice();
                tempItem = minItemPrice.poll();
                prod_items.add(tempItem.getId() + ";" + tempItem.getPrice());
            }

        }
        log.info(CouponConstants.Message.FINISH_CALCULATE_COUPON_PRODUCTS);
        return prod_items;
    }

    public Map<String, Float> getItemsPrice(List<String> favorite_items){
        log.info(CouponConstants.Message.START_GETTING_PRODUCTS_PRICES);
        Map<String, Float> items = new HashMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String url = "";
        ItemConsultDTO item;
        for(String item_id: favorite_items){
            try {
                url = CouponConstants.Urls.PRODUCT_CONSULT_URL + item_id;
                item = new ItemConsultDTO(restTemplate.getForObject(url, String.class));

                if (Objects.nonNull(item)){
                    items.put(item.getId(), item.getPrice());
                }else {
                    log.info(CouponConstants.Message.CANT_GET_PRODUCT_INFORMATION+ item_id);
                }
            }catch (Exception e){
                log.info(CouponConstants.Message.ERROR_GETTING_PRODUCT_INFORMATION + item_id);
                log.error(e);
            }
        }
        log.info(CouponConstants.Message.FINISH_GETTING_PRODUCTS_PRICES);
        return items;
    }
}
