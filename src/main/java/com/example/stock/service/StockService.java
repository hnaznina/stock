package com.example.stock.service;

import com.example.stock.model.StockVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Log4j2
public class StockService {
    private RestTemplate restTemplate;
    private String url;
    private List<String> ranks;
    private Pattern pattern;

    @PostConstruct
    public void init(){
        restTemplate = new RestTemplate();
        url = "https://www.zacks.com/stock/quote/%s?q=%s";
        ranks = Arrays.asList("1-Strong Buy","2-Buy","3-Hold","4-Sell","5-Strong Sell");
        pattern = Pattern.compile("[$][0-9,\\,]{1,5}\\.\\d{2}");
    }

    public StockVO getStockData(String ticker){

        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/text");

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<String> response = restTemplate.exchange(
            String.format(url, ticker, ticker),
            HttpMethod.GET,
            entity,
            String.class
        );

        String stockHtmlText = response.getBody();
        //log.info(">>>"+stockHtmlText);
        return new StockVO(){{
            setTicker(ticker);
            setRank(extractRank(stockHtmlText));
            setPrice(extractPrice(stockHtmlText));
        }};
    }

    private String extractRank(String stockHtmlText){
        String rank = null;

        for(String r : ranks){
            if(stockHtmlText.contains(r)){
                rank = r;
                break;
            }
        }

        return rank;
    }

    private String extractPrice(String stockHtmlText){
        Matcher m = pattern.matcher(stockHtmlText);

        while (m.find()) {
            return m.group(0);
        }
        return null;
    }

}
