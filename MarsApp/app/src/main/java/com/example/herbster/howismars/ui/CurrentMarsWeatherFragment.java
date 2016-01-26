package com.example.herbster.howismars.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.herbster.howismars.R;
import com.example.herbster.howismars.communication.RequestResponse;
import com.example.herbster.howismars.communication.ServiceRequest;
import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.SingleMarsWeatherReport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by herbster on 1/26/2016.
 */
public class CurrentMarsWeatherFragment extends Fragment {

    private TextView tDateTextView;
    private TextView solTextView;
    private TextView lsTextView;
    private TextView minTempTextView;
    private TextView maxTempTextView;
    private TextView pressureTextView;
    private TextView windDegTextView;
    private TextView windSpeedTextView;
    private TextView seasonTextView;
    private TextView sunriseTextView;
    private TextView sunsetTextView;

    public CurrentMarsWeatherFragment() {

    }

    public static CurrentMarsWeatherFragment newInstance() {
        CurrentMarsWeatherFragment fragment = new CurrentMarsWeatherFragment();
        return fragment;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (!isNetworkAvailable()) {
            showNetworkErrorMessage();
        } else {
            FetchSingleDataTask fetchSingleDataTask = new FetchSingleDataTask();
            String url = getActivity().getResources().getString(R.string.uri_latest_service);
            fetchSingleDataTask.execute(url);
        }
    }

    private void showNetworkErrorMessage() {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(getActivity());

        dlgAlert.setMessage("Please, connect to the Internet");
        dlgAlert.setTitle("No Network Available");
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.current_fragment_main, container, false);

        tDateTextView = (TextView)rootView.findViewById(R.id.terrestrialDate);
        solTextView = (TextView)rootView.findViewById(R.id.sol);
        lsTextView = (TextView)rootView.findViewById(R.id.ls);
        minTempTextView = (TextView)rootView.findViewById(R.id.minTemp);
        maxTempTextView = (TextView)rootView.findViewById(R.id.maxTemp);
        pressureTextView = (TextView)rootView.findViewById(R.id.press);
        windDegTextView = (TextView)rootView.findViewById(R.id.windDeg);
        windSpeedTextView = (TextView)rootView.findViewById(R.id.windSpeed);
        seasonTextView = (TextView)rootView.findViewById(R.id.season);
        sunriseTextView = (TextView)rootView.findViewById(R.id.sunsise);
        sunsetTextView = (TextView)rootView.findViewById(R.id.sunset);

        return rootView;
    }

    private void updateUI(SingleMarsWeatherReport marsReport) {
        tDateTextView.setText(parseDateToString(marsReport.getTerrestrialDate(), MarsJSONParser.DATE_FORMAT));
        solTextView.setText(getCheckedStringData(marsReport.getSol()));
        lsTextView.setText(getCheckedStringData(marsReport.getLs()));
        String minTempInfo = marsReport.getMinTemp() + "C ( " + marsReport.getMinTempFah() + "F )";
        minTempTextView.setText(minTempInfo);
        String maxTempInfo = marsReport.getMaxTemp() + "C ( " + marsReport.getMaxTempFah() + "F )";
        maxTempTextView.setText(maxTempInfo);
        String pressureInfo = marsReport.getPressure() + " ( " + marsReport.getPressureString() + " )";
        pressureTextView.setText(pressureInfo);
        windDegTextView.setText(getCheckedStringData(marsReport.getWindDirection()));
        windSpeedTextView.setText(getCheckedStringData(marsReport.getWindSpeed()));
        seasonTextView.setText(getCheckedStringData(marsReport.getSeason()));
        sunriseTextView.setText(parseDateToString(marsReport.getSunrise(), MarsJSONParser.DATE_TIME_FORMAT));
        sunsetTextView.setText(parseDateToString(marsReport.getSunset(), MarsJSONParser.DATE_TIME_FORMAT));
    }

    private String getCheckedStringData(Object obj) {
        if (obj == null)
            return "";
        else
            return obj.toString();
    }

    private static String parseDateToString(Date dateOrig, String formatString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
        return dateformat.format(dateOrig);
    }

    class FetchSingleDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            if (params == null || params.length == 0)
                return "";

            ServiceRequest request = ServiceRequest.create(params[0]);
            if (request != null) {
                RequestResponse response = request.doGet();

                if (response != null && !response.isError()) {
                    return response.getContent();
                }  else
                    return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            InputStream stream = new ByteArrayInputStream(string.getBytes());
            SingleMarsWeatherReport marsReport = MarsJSONParser.getInstance().parseSingleReportResponse(stream);
            if (marsReport != null) {
                updateUI(marsReport);
            }
        }
    }
}

