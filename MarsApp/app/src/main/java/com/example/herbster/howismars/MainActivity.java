package com.example.herbster.howismars;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.example.herbster.howismars.communication.RequestResponse;
import com.example.herbster.howismars.communication.ServiceRequest;
import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.MarsArchive;
import com.example.herbster.howismars.model.SingleMarsReport;
import com.example.herbster.howismars.ui.ItemFragment;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements ItemFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private static String parseDateToString(Date dateOrig, String formatString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
        return dateformat.format(dateOrig);
    }

    @Override
    public void onListFragmentInteraction(SingleMarsReport item) {

    }

    public static class CurrentFragment extends Fragment {

        TextView tDateTextView;
        TextView solTextView;
        TextView lsTextView;
        TextView minTempTextView;
        TextView maxTempTextView;
        TextView pressureTextView;
        TextView windDegTextView;
        TextView windSpeedTextView;
        TextView seasonTextView;
        TextView sunriseTextView;
        TextView sunsetTextView;

        public CurrentFragment() {

        }

        public static CurrentFragment newInstance() {
            CurrentFragment fragment = new CurrentFragment();
            return fragment;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            FetchSingleDataTask fetchSingleDataTask = new FetchSingleDataTask();
            String url = getActivity().getResources().getString(R.string.uri_latest_service);
            fetchSingleDataTask.execute(url);
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

        private void updateUI(SingleMarsReport marsReport) {
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
                SingleMarsReport marsReport = MarsJSONParser.getInstance().parseSingleReportResponse(stream);
                if (marsReport != null) {
                    updateUI(marsReport);
                }
            }
        }
    }

    public static class ArchiveFragment extends Fragment {

        public ArchiveFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ArchiveFragment newInstance() {
            ArchiveFragment fragment = new ArchiveFragment();
            return fragment;
        }

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            FetchArchiveDataTask fetchDataTask = new FetchArchiveDataTask();
            String url = getActivity().getResources().getString(R.string.uri_archive_service);
            fetchDataTask.execute(url);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.archive_fragment_item_list, container, false);
            return rootView;
        }

        class FetchArchiveDataTask extends AsyncTask<String,Void,String> {

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
                ParseRemoteDataTask parseRemoteData = new ParseRemoteDataTask();
                parseRemoteData.execute(stream);
            }
        }

        class ParseRemoteDataTask extends AsyncTask<InputStream,Void,MarsArchive> {

            @Override
            protected MarsArchive doInBackground(InputStream... params) {
                if (params == null || params.length == 0)
                    return null;

                MarsArchive marsReport = MarsJSONParser.getInstance().parseArchiveResponse(params[0]);
                int inea = marsReport.getNumberReports();
                ServiceRequest request = ServiceRequest.create("oi");

                return marsReport;
            }

            @Override
            protected void onPostExecute(MarsArchive archive) {
                updateArchiveList(archive);
            }
        }

        private void updateArchiveList(MarsArchive archive) {
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
			switch (position) {
				case 0:
					return CurrentFragment.newInstance();
				case 1:
					return ItemFragment.newInstance();
			}
			return null;            
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Current Weather";
                case 1:
                    return "History";
            }
            return null;
        }
    }
}
