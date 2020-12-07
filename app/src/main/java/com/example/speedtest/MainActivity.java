package com.example.speedtest;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jignesh13.speedometer.SpeedoMeterView;

import fr.bmartel.speedtest.SpeedTestReport;
import fr.bmartel.speedtest.SpeedTestSocket;
import fr.bmartel.speedtest.inter.ISpeedTestListener;
import fr.bmartel.speedtest.model.SpeedTestError;

public class MainActivity extends AppCompatActivity
{
    SpeedoMeterView speedoMeterView ;
    TextView textView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        speedoMeterView = findViewById(R.id.speedometerview);
        textView = findViewById(R.id.textView);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                speedoMeterView.setSpeed(0, true);

                SpeedTestTask speedTestTask = new SpeedTestTask();
                speedTestTask.execute();

            }
        });
    }
    public class SpeedTestTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {



            SpeedTestSocket speedTestSocket = new SpeedTestSocket();

            speedTestSocket.addSpeedTestListener(new ISpeedTestListener() {

                @Override
                public void onCompletion(SpeedTestReport report) {
                    Log.v("speedtest", "[COMPLETED] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[COMPLETED] rate in bit/s   : " + report.getTransferRateBit());
                }

                @Override
                public void onError(SpeedTestError speedTestError, String errorMessage) {
                }

                @Override
                public void onProgress(float percent, SpeedTestReport report) {
                   final int deger = (int) report.getTransferRateBit().intValue()/1000000;


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            speedoMeterView.setSpeed(deger,true);
                            textView.setText(deger+""+ "Mbps");
                        }
                    });

                    Log.v("speedtest", "[PROGRESS] progress : " + percent + "%");
                    Log.v("speedtest", "[PROGRESS] rate in octet/s : " + report.getTransferRateOctet());
                    Log.v("speedtest", "[PROGRESS] rate in bit/s   : " + report.getTransferRateBit());
                }
            });

            //speedTestSocket.startDownload("http://ipv4.ikoula.testdebit.info/1M.iso");
            speedTestSocket.startFixedDownload("http://ipv4.ikoula.testdebit.info/100M.iso", 10000);

            return null;
        }
    }
}