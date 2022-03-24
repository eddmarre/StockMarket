package com.example.stockmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

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
import java.util.List;

import io.github.mainstringargs.alphavantagescraper.AlphaVantageConnector;
import io.github.mainstringargs.alphavantagescraper.TimeSeries;
import io.github.mainstringargs.alphavantagescraper.output.AlphaVantageException;
import io.github.mainstringargs.alphavantagescraper.output.timeseries.Daily;


public class MainActivity extends AppCompatActivity {
TextView sampleText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sampleText=findViewById(R.id.sampleText);
        //key 50BZM4QNXYEYF7K3

    }

    public void ConnectButton(View view) throws IOException{
        //create Async Task
        StockSearchTask stockSearchTask=new StockSearchTask();
        //Set Search Symbol
        stockSearchTask.SetSymbol("MSFT");
        //Start api connection
        stockSearchTask.execute();
    }

    public class StockSearchTask extends AsyncTask<String,String,String>
    {
    String result;
    String SYMBOL;
    public void SetSymbol(String symbol)
    {
        SYMBOL=symbol;
    }
    //Start connecting to internet and retrieve data
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();

            URL url;

            HttpURLConnection urlConnection = null;
            try {
                String uri = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+SYMBOL+"&apikey=50BZM4QNXYEYF7K3";
                url= new URL(uri);
                urlConnection=(HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String inputLine = bufferedReader.readLine();
                while (inputLine != null) {
                    sb.append(inputLine);
                    inputLine = bufferedReader.readLine();
                }

                result= sb.toString();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally {
                urlConnection.disconnect();
            }
            return sb.toString();
        }
        //after internet connection is established
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //sampleText.setText(result);
            try {
                JSONObject root = new JSONObject(result);
                JSONObject metaData=root.getJSONObject("Meta Data");
                JSONObject timeSeriesDaily=root.getJSONObject("Time Series (Daily)");
                JSONArray dates =timeSeriesDaily.names();
                StringBuilder allStockInfo=new StringBuilder();
                //put all data into a list for
               for (int i=0;i<dates.length();i++)
               {
                   String stockDate=dates.getString(i);
                   String openValue=timeSeriesDaily.getJSONObject(stockDate).getString("1. open");
                   String highValue=timeSeriesDaily.getJSONObject(stockDate).getString("2. high");
                   String lowValue=timeSeriesDaily.getJSONObject(stockDate).getString("3. low");
                   String closeValue=timeSeriesDaily.getJSONObject(stockDate).getString("4. close");
                   String volumeValue=timeSeriesDaily.getJSONObject(stockDate).getString("5. volume");
                   String totalStock=stockDate+"\nopen: "+openValue+"\nhigh: "+highValue+"\nlow: "+lowValue+"\nclose: "+closeValue+"\nvolume: "+volumeValue+"\n";
                   allStockInfo.append(totalStock);
                   //TODO put data into classes for each company and so forth
               }
                sampleText.setText(allStockInfo.toString());
                //sampleText.setText(metaData.getString("2. Symbol"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}