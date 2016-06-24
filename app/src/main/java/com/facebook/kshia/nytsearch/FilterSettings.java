package com.facebook.kshia.nytsearch;

/**
 * Created by kshia on 6/23/16.
 */
public class FilterSettings {
    private String begin_date;
    private String news_desk;
    private String sort;
    private String button_date;

    public FilterSettings() {
        begin_date = null;
        news_desk = null;
        sort = null;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public void setNews_desk(String news_desk) {
        this.news_desk = news_desk;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public String getNews_desk() {
        return news_desk;
    }

    public String getSort() {
        return sort;
    }

    public String getButton_date() {
        return button_date;
    }

    public void setButton_date(String button_date) {
        this.button_date = button_date;
    }
}
