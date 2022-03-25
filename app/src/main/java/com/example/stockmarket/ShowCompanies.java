package com.example.stockmarket;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
public class ShowCompanies extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_companies);

        //Create async task
        ShowCompanies.CompanySearchTask companySearchTask = new ShowCompanies.CompanySearchTask();
        //Start api connection
        companySearchTask.execute();
    }

    //Eddie
    private void CreateButtons(ArrayList<String> companyName, int length, ArrayList<String> companySymbol) {
        setContentView(R.layout.activity_show_companies);
        ScrollView scrollView = new ScrollView(this);
        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        for (int i = 0; i < length; i++) {
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            ll.setTag(i);

            Button b = new Button(this);
            ;
            b.setTag(i);
            b.setText(companyName.get(i) + "\n(" + companySymbol.get(i) + ")");
            b.setWidth(2000);
            //created custom class that will take in index for parameters
            b.setOnClickListener(new CustomOnClickListener(i) {
                @Override
                public void onClick(View view) {

                    String symbol = companySymbol.get(index);
                    Intent intent = new Intent(ShowCompanies.this, CompanyStockInformation.class);
                    intent.putExtra("SYMBOL", symbol);
                    startActivity(intent);
                }
            });

            ll.addView(b);
            mainLayout.addView(ll);
        }
        scrollView.addView(mainLayout);
        setContentView(scrollView);
    }

    //Eddie
    public class CompanySearchTask extends AsyncTask<String, String, String> {
        String result;

        //Start connecting to internet and retrieve data
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder sb = new StringBuilder();
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                String uri = "https://pkgstore.datahub.io/core/nyse-other-listings/nyse-listed_json/data/e8ad01974d4110e790b227dc1541b193/nyse-listed_json.json";
                url = new URL(uri);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

                InputStreamReader inputStreamReader = new InputStreamReader(bufferedInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String inputLine = bufferedReader.readLine();
                while (inputLine != null) {
                    sb.append(inputLine);
                    inputLine = bufferedReader.readLine();
                }
                result = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
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
                //data stored in array format
                JSONArray root = new JSONArray(result);
                ArrayList<String> companySymbols = new ArrayList<>();
                ArrayList<String> companyNames = new ArrayList<>();
                for (int i = 0; i < root.length(); i++) {
                    JSONObject companyInfo = root.getJSONObject(i);
                    companySymbols.add(companyInfo.getString("ACT Symbol"));
                    companyNames.add(companyInfo.getString("Company Name"));
                }
                CreateButtons(companyNames, companyNames.size(), companySymbols);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}