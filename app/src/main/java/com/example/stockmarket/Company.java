package com.example.stockmarket;

import java.util.ArrayList;

public class Company {

    private String companySymbol;
    private ArrayList<StockData> companyStockPrices;

    public Company(String companySymbol, ArrayList<StockData> companyStockPrices) {
        this.companySymbol = companySymbol;
        this.companyStockPrices = companyStockPrices;
    }}
