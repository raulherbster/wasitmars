package com.example.herbster.howismars.communication;

import android.app.Service;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by herbster on 1/25/2016.
 */
public class ServiceRequest {

    private static final String TAG = "ServiceRequest";
    public static final String GET_REQUEST = "GET";

    private URL mURL;

    private ServiceRequest(URL url) {
        mURL = url;
    }

    public static ServiceRequest create(String url) {
        try {
            URL aURL = new URL(url);
            return new ServiceRequest(aURL);
        } catch (MalformedURLException e) {
            Log.e(TAG,"Wrong URL format");
            return null;
        }
    }

    private String getContentAsString(InputStream is) throws IOException {
        String contentAsString = null;
        try {
            contentAsString = readIt(is);
        } catch (IOException e) {
            Log.e(TAG,"Could not read the content");
        } finally {
            is.close();
        }
        return contentAsString;
    }

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {
        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line = "";
        while ((line = r.readLine()) != null) {
            total.append(line);
        }
        return total.toString();
    }

    public RequestResponse doGet() {

        RequestResponse response = new RequestResponse();
        try {
            HttpURLConnection conn = (HttpURLConnection) mURL.openConnection();
            conn.setReadTimeout(30000);
            conn.setConnectTimeout(30000);
            conn.setRequestMethod(GET_REQUEST);
            conn.connect();

            response.setResponseCode(conn.getResponseCode());
            String content = getContentAsString(conn.getInputStream());
            response.setContent(content);

            return response;
        } catch (IOException e) {
            Log.e(TAG,"Fail on connection : " + e.getLocalizedMessage());
            return null;
        }
    }

}
