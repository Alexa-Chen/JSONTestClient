package com.neusoft.chenmo.jsontest;

/**
 * Created by chenmo on 2015/5/11.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Tony
 *
 */
public class HttpClient {

    CookieManager ca = new CookieManager();
    String sessionID = "";

    public String get(String url, String charset) throws IOException {
        //try {
            String key = "";
            String cookieVal = "";

            URL httpURL = new URL(url);
            HttpURLConnection http = (HttpURLConnection) httpURL
                    .openConnection();
            if (!sessionID.equals("")) {
                http.setRequestProperty("Cookie", sessionID);
            }
            for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("set-cookie")) {
                    cookieVal = http.getHeaderField(i);
                    cookieVal = cookieVal.substring(
                            0,
                            cookieVal.indexOf(";") > -1 ? cookieVal
                                    .indexOf(";") : cookieVal.length() - 1);
                    sessionID = sessionID + cookieVal + ";";
                }
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    http.getInputStream(), charset));

            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
            br.close();
            return sb.toString();
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        //return null;
    }

    public String post(String url, String data, String charset) {
        Log.v("response","post()");
        try {
            URL httpURL = new URL(url);
            String key = null;
            String cookieVal = null;
            HttpURLConnection http = (HttpURLConnection) httpURL
                    .openConnection();
            Log.v("response", "m1");
            http.setDoOutput(true);
            http.setDoInput(true);
            if (!sessionID.equals("")) {
                http.setRequestProperty("Cookie", sessionID);
            }else{
                http.setRequestProperty("Cookie", "");
            }
            Log.v("response", "m2");
            OutputStream os= http.getOutputStream();

            Log.v("response","m2.0");
            OutputStreamWriter osw=new OutputStreamWriter(os,charset);
            Log.v("response","m2.1");
            BufferedWriter bw = new BufferedWriter(
                    osw
            );
            Log.v("response", "m3");
            bw.write(data);
            bw.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    http.getInputStream(), charset));
            StringBuilder sb = new StringBuilder();
            String temp = null;
            while ((temp = br.readLine()) != null) {
                sb.append(temp);
                sb.append("\n");
            }
            Log.v("response","m4");
            br.close();
            for (int i = 1; (key = http.getHeaderFieldKey(i)) != null; i++) {
                if (key.equalsIgnoreCase("set-cookie")) {
                    cookieVal = http.getHeaderField(i);
                    cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                    sessionID = sessionID + cookieVal + ";";
                }
            }
            Log.v("response","yes");
            return sb.toString();

        } catch (Exception e) {

            e.printStackTrace();
            //Log.v("response", e.getMessage());
        }
        Log.v("response","null string");
        return null;
    }
}