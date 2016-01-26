package com.example.herbster.howismars.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by herbster on 1/25/2016.
 */
public class MarsWeatherArchive {

    private List<SingleMarsWeatherReport> mReportsList;

    public MarsWeatherArchive() {
        mReportsList = new ArrayList<SingleMarsWeatherReport>();
    }

    public List<SingleMarsWeatherReport> getAsList() {
        return mReportsList;
    }

    public synchronized boolean addMarsReport(SingleMarsWeatherReport report) {
        return mReportsList.add(report);
    }

    public synchronized boolean removeMarsReport(SingleMarsWeatherReport report) {
        return mReportsList.remove(report);
    }

    public Iterator<SingleMarsWeatherReport> iterator() {
        return mReportsList.iterator();
    }

    public int getNumberReports() {
        return mReportsList.size();
    }

    public void clean() {
        mReportsList.clear();
    }
}
