package com.example.stock.controller;

import com.example.stock.model.StockVO;
import com.example.stock.service.StockService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Log4j2
public class StockController {
    @Autowired
    private StockService stockService;

    @Value("${trickers}")
    private List<String> trickers;

    @GetMapping("/")
    public String getAllStockData(Model model){
        log.info("Request received");
        List<StockVO> stocks = new ArrayList<>();
        Map<String,StockVO> stockMap = new HashMap<>();

        //trickers.forEach(ticker->stocks.add(stockService.getStockData(ticker)));
        trickers.parallelStream().forEach(ticker->stockMap.put(ticker, stockService.getStockData(ticker)));
        trickers.forEach(ticker->stocks.add(stockMap.get(ticker)));

        model.addAttribute("stocks", stocks);

        return "home";
    }
}
