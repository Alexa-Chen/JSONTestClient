package com.neusoft.chenmo.jsontest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tv1,tv2;
    private Button getJson_button,prase_button;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    tv1.setText(json_str.toString());

            }
        }
    };
    StringBuilder json_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv2 = (TextView) findViewById(R.id.tv2);
        getJson_button = (Button) findViewById(R.id.getJson_button);
        prase_button = (Button) findViewById(R.id.prase_button);

        getJson_button.setOnClickListener(this);
        prase_button.setOnClickListener(this);

    }

    private void getjson() {
        json_str = new StringBuilder();
        (new Thread() {
            public void run() {
                HttpClient client = new HttpClient();
                String reply = null;
                try {
                    reply = client.get("http://192.168.103.117:8080/JsonTest/JsonServlet", "UTF-8");
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
                json_str.append(reply);
                handler.sendEmptyMessage(1);
            }
        }).start();


    }
    private void parsejson() {
        try {
            JSONObject json = new JSONObject(tv1.getText().toString());
            String name = json.getString("Name");
            String date = json.getString("Date");
            String press = json.getString("Press");
            String price = json.getString("Price");

            JSONObject other = (JSONObject) json.get("Other");
            String target =  other.getString("Target");
            String License = other.getString("License");

            tv2.setText(String.format("Name: %s\nDate: %s\nPress: %s\nPrice: %s\nTarget: %s\nLicense:%s\n",
                    name, date, press, price,target,License));

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getJson_button:
                getjson();

                break;

            case R.id.prase_button:

                parsejson();
                break;
        }
    }
}
