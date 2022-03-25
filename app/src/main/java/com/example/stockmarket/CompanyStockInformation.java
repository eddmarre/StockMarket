package com.example.stockmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
//Eddie
public class CompanyStockInformation extends AppCompatActivity {
    TextView text;
    CandleStickChart chart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_stock_information);
        //Get data sent over through activity intent
        Bundle extras=getIntent().getExtras();
        String symbol="";
        if(extras!=null)
        {
            symbol=extras.getString("SYMBOL");
            text=findViewById(R.id.textView);
            text.setText(symbol);
        }

        chart=(CandleStickChart) findViewById(R.id.chart);

        //Create new async task
        CompanyStockSearchTask companyStockSearchTask=new CompanyStockSearchTask();
        //Set Search Symbol
        companyStockSearchTask.SetSymbol(symbol);
        //Start api connection
        companyStockSearchTask.execute();
    }
    //Eddie
    public class CompanyStockSearchTask extends AsyncTask<String,String,String>
    {
        String result;
        String SYMBOL;
        //MUST INSERT YOUR OWN KEY TO USE
        //GET KEY FROM https://www.alphavantage.co/support/#api-key
        String APIKey="50BZM4QNXYEYF7K3";

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
                String uri = "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol="+SYMBOL+"&apikey="+APIKey;
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
            //nonsense return because of String return type
            return sb.toString();
        }
        //after internet connection is established
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject root = new JSONObject(result);
                JSONObject metaData=root.getJSONObject("Meta Data");
                JSONObject timeSeriesDaily=root.getJSONObject("Time Series (Daily)");

                String companySymbol=metaData.getString("2. Symbol");
                String lastRefreshed=metaData.getString("3. Last Refreshed");
                String timeZone=metaData.getString("5. Time Zone");

                JSONArray dates =timeSeriesDaily.names();

                //put all data into a list
                ArrayList<StockData> dailyStock = new ArrayList<>();

                for (int i=0;i<dates.length();i++)
                {
                    String stockDate=dates.getString(i);
                    String openValue=timeSeriesDaily.getJSONObject(stockDate).getString("1. open");
                    String highValue=timeSeriesDaily.getJSONObject(stockDate).getString("2. high");
                    String lowValue=timeSeriesDaily.getJSONObject(stockDate).getString("3. low");
                    String closeValue=timeSeriesDaily.getJSONObject(stockDate).getString("4. close");
                    String volumeValue=timeSeriesDaily.getJSONObject(stockDate).getString("5. volume");

                    float iOpenValue= Float.parseFloat(openValue);
                    float iHighValue=Float.parseFloat(highValue);
                    float iLowValue=Float.parseFloat(lowValue);
                    float iCloseValue=Float.parseFloat(closeValue);
                    int iVolumeValue=Integer.parseInt(volumeValue);

                    //create a daily stock
                    dailyStock.add(new StockData(stockDate,iOpenValue,iHighValue,iLowValue,iCloseValue,iVolumeValue));
                }
                //create company's information from data obtained
                Company currentCompany= new Company(SYMBOL,dailyStock);
                //populate the chart with the company's data
                setCandleStickChart(currentCompany);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    //Eddie
    public void setCandleStickChart(Company company)
    {
        ArrayList<StockData> companyStock=company.getCompanyStockPrices();
        //gets all daily dates for company stock, shortens date to format MM-DD and adds it to list
        ArrayList<String> stockDate=new ArrayList<>();
        for (StockData stock:companyStock
        ) {
            String date=stock.getDateTime().substring(5);
            stockDate.add(date);
        }
        //X values for the candle chart
        ValueFormatter valueFormatter =new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                return stockDate.get((int) value);
            }
        };
        //Creates the graph entries from the api
        ArrayList<CandleEntry> candleEntries=new ArrayList<>();
        int i=0;
        for (StockData stock:companyStock
        ) {
            candleEntries.add(new CandleEntry(i,stock.getHigh(),stock.getLow(),stock.getOpen(),stock.getClose()));
            i++;
        }

        //sets up the chart's appearance and makes the company symbol the chart's label
        CandleDataSet candleDataSet=new CandleDataSet(candleEntries,company.getCompanySymbol());
        candleDataSet.setColor(Color.rgb(80,80,80));
        candleDataSet.setShadowColor(Color.rgb(0,80,0));
        candleDataSet.setShadowWidth(.8f);
        candleDataSet.setDecreasingColor(Color.rgb(100,0,0));
        candleDataSet.setDecreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setIncreasingColor(Color.rgb(0,80,0));
        candleDataSet.setIncreasingPaintStyle(Paint.Style.FILL);
        candleDataSet.setNeutralColor(Color.LTGRAY);
        candleDataSet.setDrawValues(false);

        //actually populates the chart with the appearance settings
        CandleData candleData= new CandleData(candleDataSet);
        chart.setData(candleData);
        chart.setBackgroundColor(Color.rgb(100,100,100));
        chart.animateXY(3000,3000);
        chart.getXAxis().setGranularity(1f);
        chart.getXAxis().setValueFormatter(valueFormatter);
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getXAxis().setDrawGridLines(false);
    }
}