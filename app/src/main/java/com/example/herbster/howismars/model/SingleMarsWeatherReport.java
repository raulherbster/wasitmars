package com.example.herbster.howismars.model;

import java.util.Date;

/**
 * Created by herbster on 1/25/2016.
 */
public class SingleMarsWeatherReport {

    private Date mTerrestrialDate;
    private Integer mSol;
    private Double mLs;
    private Double mMinTemp;
    private Double mMinTempFah;
    private Double mMaxTemp;
    private Double mMaxTempFah;
    private Double mPressure;
    private String mPressureString;
    private Double mAbsHumidity;
    private Double mWindSpeed;
    private String mWindDirection;
    private String mAtmoOpacity;
    private String mSeason;
    private Date mSunrise;
    private Date mSunset;

    public SingleMarsWeatherReport() {

    }

    public Date getTerrestrialDate() {
        return mTerrestrialDate;
    }

    public Integer getSol() {
        return mSol;
    }

    public Double getLs() {
        return mLs;
    }

    public Double getMinTemp() {
        return mMinTemp;
    }

    public Double getMinTempFah() {
        return mMinTempFah;
    }

    public Double getMaxTemp() {
        return mMaxTemp;
    }

    public Double getMaxTempFah() {
        return mMaxTempFah;
    }

    public Double getPressure() {
        return mPressure;
    }

    public String getPressureString() {
        return mPressureString;
    }

    public Double getAbsHumidity() {
        return mAbsHumidity;
    }

    public Double getWindSpeed() {
        return mWindSpeed;
    }

    public String getWindDirection() {
        return mWindDirection;
    }

    public String getAtmoOpacity() {
        return mAtmoOpacity;
    }

    public String getSeason() {
        return mSeason;
    }

    public Date getSunrise() {
        return mSunrise;
    }

    public Date getSunset() {
        return mSunset;
    }

    public void setTerrestrialDate(Date terrestrialDate) {
        this.mTerrestrialDate = terrestrialDate;
    }

    public void setSol(Integer sol) {
        this.mSol = sol;
    }

    public void setLs(Double mLs) {
        this.mLs = mLs;
    }

    public void setMinTemp(Double mMinTemp) {
        this.mMinTemp = mMinTemp;
    }

    public void setMinTempFah(Double minTempFah) {
        this.mMinTempFah = minTempFah;
    }

    public void setMaxTemp(Double maxTemp) {
        this.mMaxTemp = maxTemp;
    }

    public void setMaxTempFah(Double maxTempFah) {
        this.mMaxTempFah = maxTempFah;
    }

    public void setPressure(Double pressure) {
        this.mPressure = pressure;
    }

    public void setPressureString(String pressureString) {
        this.mPressureString = pressureString;
    }

    public void setAbsHumidity(Double absHumidity) {
        this.mAbsHumidity = absHumidity;
    }

    public void setWindSpeed(Double windSpeed) {
        this.mWindSpeed = windSpeed;
    }

    public void setWindDirection(String windDirection) {
        this.mWindDirection = windDirection;
    }

    public void setAtmoOpacity(String atmoOpacity) {
        this.mAtmoOpacity = atmoOpacity;
    }

    public void setSeason(String season) {
        this.mSeason = season;
    }

    public void setSunrise(Date sunrise) {
        this.mSunrise = sunrise;
    }

    public void setSunset(Date sunset) {
        this.mSunset = sunset;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof SingleMarsWeatherReport))
            return false;
        SingleMarsWeatherReport report = (SingleMarsWeatherReport)o;
        return this.getTerrestrialDate().equals(report.getTerrestrialDate());
    }
}
