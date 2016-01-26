package com.example.herbster.howismars.communication;

import java.net.HttpURLConnection;

/**
 * Created by herbster on 1/25/2016.
 */
public class RequestResponse {

    private int mResponseCode;

    private String mContent;

    public RequestResponse() {
        mResponseCode = HttpURLConnection.HTTP_OK;
    }

    public int getResponseCode() {
        return mResponseCode;
    }

    public String getContent() {
        return mContent;
    }

    public void setResponseCode(int responseCode) {
        this.mResponseCode = responseCode;
    }

    public void setContent(String content) {
        this.mContent = content;
    }

    public boolean isError() {
        return getResponseCode() != HttpURLConnection.HTTP_OK && getResponseCode() != HttpURLConnection.HTTP_ACCEPTED;
    }
}
