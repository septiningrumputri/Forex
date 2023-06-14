package com.septiningrum_202102248.forex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.text.DecimalFormat;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    private ProgressBar loadingProgressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TextView aedTextView, afnTextView, allTextView, amdTextView, angTextView, aoaTextView, arsTextView, audTextView, awgTextView, aznTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout1);
        aedTextView = (TextView) findViewById(R.id.aedTextView);
        afnTextView = (TextView) findViewById(R.id.afnTextView);
        allTextView = (TextView) findViewById(R.id.allTextView);
        amdTextView = (TextView) findViewById(R.id.amdTextView);
        angTextView = (TextView) findViewById(R.id.angTextView);
        aoaTextView = (TextView) findViewById(R.id.aoaTextView);
        arsTextView = (TextView) findViewById(R.id.arsTextView);
        audTextView = (TextView) findViewById(R.id.audTextView);
        awgTextView = (TextView) findViewById(R.id.awgTextView);
        aznTextView = (TextView) findViewById(R.id.aznTextView);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loadingProgressBar);

        initSwipeRefreshLayout();
        initForex();
    }
    private void initSwipeRefreshLayout(){
        swipeRefreshLayout.setOnRefreshListener(() -> {
            initForex();

            swipeRefreshLayout.setRefreshing(false);
        });
    }

    public String formatNumber(double number, String format){
        DecimalFormat decimalFormat = new DecimalFormat();
        return decimalFormat.format(number);
    }

    private void initForex(){
        loadingProgressBar.setVisibility(TextView.VISIBLE);

        String url = "https://openexchangerates.org/api/latest.json?app_id=5a232a5199b44d708d31b2e1665a03b7";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Gson gson = new Gson();
                RootModel rootModel = gson.fromJson(new String(responseBody), RootModel.class);
                RatesModel ratesModel = rootModel.getRatesModel();

                double aed = ratesModel.getARS() / ratesModel.getAED();
                double afn = ratesModel.getARS() / ratesModel.getAFN();
                double all = ratesModel.getARS() / ratesModel.getALL();
                double amd = ratesModel.getARS() / ratesModel.getAMD();
                double ang = ratesModel.getARS() / ratesModel.getANG();
                double aoa = ratesModel.getARS() / ratesModel.getAOA();
                double aud = ratesModel.getARS() / ratesModel.getAUD();
                double awg = ratesModel.getARS() / ratesModel.getAWG();
                double azn = ratesModel.getARS() / ratesModel.getAZN();
                double arz = ratesModel.getARS();

                aedTextView.setText(formatNumber(aed,"###, ##0, ##"));
                afnTextView.setText(formatNumber(afn,"###, ##0, ##"));
                allTextView.setText(formatNumber(all,"###, ##0, ##"));
                amdTextView.setText(formatNumber(amd,"###, ##0, ##"));
                angTextView.setText(formatNumber(ang,"###, ##0, ##"));
                aoaTextView.setText(formatNumber(aoa,"###, ##0, ##"));
                audTextView.setText(formatNumber(aud,"###, ##0, ##"));
                awgTextView.setText(formatNumber(awg,"###, ##0, ##"));
                aznTextView.setText(formatNumber(azn,"###, ##0, ##"));
                arsTextView.setText(formatNumber(arz,"###, ##0, ##"));

                loadingProgressBar.setVisibility(TextView.INVISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), error.getMessage(),Toast.LENGTH_LONG).show();

                loadingProgressBar.setVisibility(TextView.INVISIBLE);

            }
        });
    }
}