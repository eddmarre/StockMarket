package com.example.stockmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Eddie
public class MainActivity extends AppCompatActivity {
EditText enteredText;
CandleStickChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enteredText=findViewById(R.id.enterSymbolText);
        chart=(CandleStickChart) findViewById(R.id.chart);
    }

    public void ConnectButton(View view) {
        Intent intent=new Intent(MainActivity.this,CompanyStockInformation.class);
        intent.putExtra("SYMBOL",enteredText.getText().toString());
        startActivity(intent);

    }

    public void ShowCompaniesButton(View view) {
        Intent switchActivity=new Intent(MainActivity.this,ShowCompanies.class);
        startActivity(switchActivity);
    }

}