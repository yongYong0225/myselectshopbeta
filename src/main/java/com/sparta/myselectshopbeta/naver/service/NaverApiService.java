package com.sparta.myselectshopbeta.naver.service;

import com.sparta.myselectshopbeta.naver.dto.ItemDto;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class NaverApiService {

    public List<ItemDto> searchItems(String query) {
        RestTemplate rest = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        //네이버에서 받은 아이디와 시크릿 넣기
        headers.add("X-Naver-Client-Id", "cBhypd6hJNXAdTEszbo9");
        headers.add("X-Naver-Client-Secret", "FdhccukEOY");
        String body = "";

        HttpEntity<String> requestEntity = new HttpEntity<String>(body, headers);
        //15query -> 15개의 결과를 보여줄거다
        ResponseEntity<String> responseEntity = rest.exchange("https://openapi.naver.com/v1/search/shop.json?display=15&query=" + query , HttpMethod.GET, requestEntity, String.class);

        HttpStatus httpStatus = responseEntity.getStatusCode();
        int status = httpStatus.value();
        log.info("NAVER API Status Code : " + status);
        //이쪽으로 값을 가져옴
        String response = responseEntity.getBody();
        //아래 메소드로 리턴
        return fromJSONtoItems(response);
    }

    //dto로 변환해주는 메소드
    public List<ItemDto> fromJSONtoItems(String response) {

        JSONObject rjson = new JSONObject(response);
        JSONArray items  = rjson.getJSONArray("items"); //json에서 item쪽만 뽑아옴
        List<ItemDto> itemDtoList = new ArrayList<>(); //리스트 생성

        for (int i=0; i<items.length(); i++) { //아이템의 갯수만큼 넣어줌
            JSONObject itemJson = items.getJSONObject(i);
            ItemDto itemDto = new ItemDto(itemJson);
            itemDtoList.add(itemDto);
        }

        return itemDtoList;
    }
}