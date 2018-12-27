package com.example.stock.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class StockVO {
    private String ticker;
    private String rank;
    private String price;
    private String htmlText;
}
