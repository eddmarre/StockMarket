package com.example.stockmarket;

import java.time.LocalDateTime;

public class StockData {
    private String dateTime;
    private final float open;
    private final float high;
    private final float low;
    private final float close;
    private final int volume;


    public StockData(String dateTime, float open, float high, float low, float close,
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

    public float getOpen() {
        return open;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getClose() {
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