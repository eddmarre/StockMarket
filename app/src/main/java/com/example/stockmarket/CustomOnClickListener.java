package com.example.stockmarket;

import android.view.View;
//Eddie
public class CustomOnClickListener implements View.OnClickListener {
    int index;
    //Custom constructor to pass in an integer as a parameter
    public CustomOnClickListener(int index)
    {
        this.index=index;
    }
    @Override
    public void onClick(View view) {

    }
}
