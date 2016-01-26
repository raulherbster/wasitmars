package com.example.herbster.howismars.json;

import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import com.example.herbster.howismars.communication.RequestResponse;
import com.example.herbster.howismars.communication.ServiceRequest;
import com.example.herbster.howismars.model.MarsArchive;
import com.example.herbster.howismars.model.SingleMarsReport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;

/**
 * Created by herbster on 1/25/2016.
 */
public class MarsJSONParser {

    public static final String TAG_TERRESTRIAL_DATE = "terrestrial_date";
    public static final String TAG_SOL = "sol";
    public static final String TAG_LS = "ls";
    public static final String TAG_MIN_TEMP = "min_temp";
    public static final String TAG_MIN_TEMP_FAHRENHEIT = "min_temp_fahrenheit";
    public static final String TAG_MAX_TEMP = "max_temp";
    public static final String TAG_MAX_TEMP_FAHRENHEIT = "max_temp_fahrenheit";
    public static final String TAG_PRESSURE = "pressure";
    public static final String TAG_PRESSURE_STRING = "pressure_string";
    public static final String TAG_ABS_HUMIDITY = "abs_humidity";
    public static final String TAG_WIND_SPEED = "wind_speed";
    public static final String TAG_WIND_DIRECTION = "wind_direction";
    public static final String TAG_ATMO_OPACITY = "atmo_opacity";
    public static final String TAG_SEASON = "season";
    public static final String TAG_SUNRISE = "sunrise";
    public static final String TAG_SUNSET = "sunset";
    public static final String TAG_RESULTS = "results";
    public static final String TAG_COUNT = "count";
    public static final String TAG_PREVIOUS = "previous";
    public static final String TAG_NEXT = "next";
    public static final String TAG_REPORT = "report";
    private final String TAG = "MarsJSONParser";

    private static MarsJSONParser singleton = null;

    private List<String> mProviderNameList;
	
	private boolean mMultiplePlagesMode = false;

    private MarsArchive mCurrentMarsArchive = null;

    private MarsJSONParser() {
        mProviderNameList = new ArrayList<String>();
    }

    public synchronized static MarsJSONParser getInstance() {
        if (singleton == null)
            singleton = new MarsJSONParser();
        return singleton;
    }

    public SingleMarsReport parseSingleReportResponse(InputStream in) {
        if (in == null)
            return null;

        JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals(TAG_REPORT)) {
                    return this.parseSingleReport(jsonReader);
                }
            }
            jsonReader.endObject();
        } catch (IOException e) {
            Log.e(TAG,"Error while reading input stream");
            return null;
        }
        return null;
    }

    public MarsArchive parseArchiveResponse(InputStream in) {
        if (mCurrentMarsArchive == null)
            mCurrentMarsArchive = new MarsArchive();

        mCurrentMarsArchive.clean();
        mCurrentMarsArchive = new MarsArchive();



        parseArchiveResponseInner(in);
        return mCurrentMarsArchive;
    }

	public void parseArchiveResponseInner(InputStream in) {
        if (in == null)
            return;

        JsonReader jsonReader = new JsonReader(new InputStreamReader(in));
        try {
            jsonReader.beginObject();
            while (jsonReader.hasNext()) {
                String name = jsonReader.nextName();
                if (name.equals(TAG_NEXT)) {
                    String next = checkAndReadString(jsonReader);
					if (next != null) {
                        fetchNextPage(next);
                    }
                } else if (name.equals(TAG_PREVIOUS)) {
					// TODO nothing
                    checkAndReadString(jsonReader);
				} else if (name.equals(TAG_COUNT)) {
                    // TODO nothing
					checkAndReadInteger(jsonReader);
				} else if (name.equals(TAG_RESULTS)) {
					parseArrayReports(jsonReader);
				}
            }
            jsonReader.endObject();
        } catch (IOException e) {
            Log.e(TAG,"Error while reading input stream");
        }
    }

    private SingleMarsReport parseSingleReport(JsonReader reader) throws IOException {
        SingleMarsReport report = new SingleMarsReport();

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(TAG_TERRESTRIAL_DATE)) {
                Date date = dateFromString(checkAndReadString(reader),DATE_FORMAT);
                report.setTerrestrialDate(date);
            } else if (name.equals(TAG_SOL)) {
                report.setSol(checkAndReadInteger(reader));
            } else if (name.equals(TAG_LS)) {
                report.setLs(checkAndReadDouble(reader));
            } else if (name.equals(TAG_MIN_TEMP)) {
                report.setMinTemp(checkAndReadDouble(reader));
            } else if (name.equals(TAG_MIN_TEMP_FAHRENHEIT)) {
                report.setMinTempFah(checkAndReadDouble(reader));
            } else if (name.equals(TAG_MAX_TEMP)) {
                report.setMaxTemp(checkAndReadDouble(reader));
            } else if (name.equals(TAG_MAX_TEMP_FAHRENHEIT)) {
                report.setMaxTempFah(checkAndReadDouble(reader));
            } else if (name.equals(TAG_PRESSURE)) {
                report.setPressure(checkAndReadDouble(reader));
            } else if (name.equals(TAG_PRESSURE_STRING)) {
                report.setPressureString(checkAndReadString(reader));
            } else if (name.equals(TAG_ABS_HUMIDITY)) {
                report.setAbsHumidity(checkAndReadDouble(reader));
            } else if (name.equals(TAG_WIND_SPEED)) {
                report.setWindSpeed(checkAndReadDouble(reader));
            } else if (name.equals(TAG_WIND_DIRECTION)) {
                report.setWindDirection(checkAndReadString(reader));
            } else if (name.equals(TAG_ATMO_OPACITY)) {
                report.setAtmoOpacity(checkAndReadString(reader));
            } else if (name.equals(TAG_SEASON)) {
                report.setSeason(checkAndReadString(reader));
            } else if (name.equals(TAG_SUNRISE)) {
                Date date = dateFromString(checkAndReadString(reader),DATE_TIME_FORMAT);
                report.setSunrise(date);
            } else if (name.equals(TAG_SUNSET)) {
                Date date = dateFromString(checkAndReadString(reader),DATE_TIME_FORMAT);
                report.setSunset(date);
            }
        }
        reader.endObject();
        return report;
    }

	
	private void parseArrayReports(JsonReader reader) throws IOException {
		if (mCurrentMarsArchive == null)
            return;

        reader.beginArray();
        while (reader.hasNext()) {
            mCurrentMarsArchive.addMarsReport(parseSingleReport(reader));
        }
        reader.endArray();
	}

    private String checkAndReadString(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else
            return reader.nextString();
    }

    private Double checkAndReadDouble(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else
            return reader.nextDouble();
    }

    private Integer checkAndReadInteger(JsonReader reader) throws IOException {
        if (reader.peek() == JsonToken.NULL) {
            reader.nextNull();
            return null;
        } else
            return reader.nextInt();
    }

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    private Date dateFromString(String dateString, String formatString) {
        if (dateString == null)
            return null;

        SimpleDateFormat format = new SimpleDateFormat(formatString);
        try {
            return format.parse(dateString);
        } catch (ParseException e) {
            Log.e(TAG,"Could not parse date");
            return null;
        }
    }

    private void fetchNextPage(String nextPage) {
        ServiceRequest request = ServiceRequest.create(nextPage);
        if (request != null) {
            RequestResponse response = request.doGet();
            if (response!= null && !response.isError())
                parseArchiveResponse(new ByteArrayInputStream(response.getContent().getBytes()));
        }
    }
}
