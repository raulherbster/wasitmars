package com.example.herbster.howismars;

import android.test.InstrumentationTestCase;

import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.MarsArchive;
import com.example.herbster.howismars.model.SingleMarsReport;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {

    private final String FILE_NAME_SINGLE_REPORT = "single.json";

    private final String FILE_NAME_ARCHIVE = "archive.json";

    public ApplicationTest() {
    }

    private String parseDateToString(Date dateOrig, String formatString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
        return dateformat.format(dateOrig);
    }

    public void testParseArchive() {
        MarsJSONParser parser = MarsJSONParser.getInstance();
        MarsArchive archive = null;
        try {
            archive = parser.parseArchiveResponse(getInstrumentation().getContext().getAssets().open(FILE_NAME_ARCHIVE));
            assertNotNull(archive);
            assertEquals(10,archive.getNumberReports());
        } catch (IOException e) {
            fail("Should not  here");
        }
    }

    public void testParseSingleReport() {
        MarsJSONParser parser = MarsJSONParser.getInstance();

        SingleMarsReport report = parser.parseSingleReportResponse(null);
        assertNull(report);

        try {
            SingleMarsReport singleReport = (SingleMarsReport) parser.parseSingleReportResponse(getInstrumentation().getContext().getAssets().open(FILE_NAME_SINGLE_REPORT));
            assertEquals("2016-01-20",parseDateToString(singleReport.getTerrestrialDate(), MarsJSONParser.DATE_FORMAT));
            assertEquals(1229, singleReport.getSol().intValue());
            assertEquals(97.0,singleReport.getLs().doubleValue());
            assertEquals(-88.0,singleReport.getMinTemp().doubleValue());
            assertEquals(-126.4,singleReport.getMinTempFah().doubleValue());
            assertEquals(-26.0,singleReport.getMaxTemp().doubleValue());
            assertEquals(-14.8,singleReport.getMaxTempFah().doubleValue());
            assertEquals(832.0,singleReport.getPressure().doubleValue());
            assertEquals("Higher",singleReport.getPressureString());
            assertNull(singleReport.getAbsHumidity());
            assertNull(singleReport.getWindSpeed());
            assertEquals("--", singleReport.getWindDirection());
            assertEquals("Sunny", singleReport.getAtmoOpacity());
            assertEquals("Month 4",singleReport.getSeason());
            assertEquals("2016-01-20T11:51:00Z",parseDateToString(singleReport.getSunrise(), MarsJSONParser.DATE_TIME_FORMAT));
            assertEquals("2016-01-20T23:33:00Z",parseDateToString(singleReport.getSunset(), MarsJSONParser.DATE_TIME_FORMAT));
        } catch (IOException e) {
            fail("Error on opening the test sample file.");
        }
    }
}