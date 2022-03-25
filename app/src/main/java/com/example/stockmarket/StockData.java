package com.example.stockmarket;

import java.time.LocalDateTime;

public class StockData {
    private String dateTime;
    private final double open;
    private final double high;
    private final double low;
    private final double close;
    private final int volume;


    public StockData(String dateTime, double open, double high, double low, double close,
                     int volume) {
        this.dateTime = dateTime;
        this.open = open;
        this.high = high;
        this.low = low;
        this.close = close;
        this.volume = volume;
    }

    public String getDateTime() {
        return dateTime;
    }

    public double getOpen() {
        return open;
    }

    public double getHigh() {
        return high;
    }

    public double getLow() {
        return low;
    }

    public double getClose() {
        return close;
    }

    public int getVolume() {
        return volume;
    }

    public String ToString()
    {
       return  dateTime + "\n"+open+"\n"+high+"\n"+low+"\n"+close+"\n"+volume+"\n";
    }

}