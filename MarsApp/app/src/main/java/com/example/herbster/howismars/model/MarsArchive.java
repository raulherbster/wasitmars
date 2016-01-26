package com.example.herbster.howismars.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by herbster on 1/25/2016.
 */
public class MarsArchive {

    private List<SingleMarsReport> mReportsList;

    public MarsArchive() {
        mReportsList = new ArrayList<SingleMarsReport>();
    }

    public List<SingleMarsReport> getAsList() {
        return mReportsList;
    }

    public synchronized boolean addMarsReport(SingleMarsReport report) {
        return mReportsList.add(report);
    }

    public synchronized boolean removeMarsReport(SingleMarsReport report) {
        return mReportsList.remove(report);
    }

    public Iterator<SingleMarsReport> iterator() {
        return mReportsList.iterator();
    }

    public int getNumberReports() {
        return mReportsList.size();
    }

    public void clean() {
        mReportsList.clear();
    }
}
