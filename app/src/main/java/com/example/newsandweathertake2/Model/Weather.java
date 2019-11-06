package com.example.newsandweathertake2.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {

    private String city_name;
    private String country_code;
    private String icon;
    private String description;
    private String date;
    private double temp;

    public Weather(String name, String country, String icon, String description, String date, double temp) {
        this.city_name = name;
        this.country_code = country;
        this.icon = icon;
        this.description = description;
        this.date = parseDateToddMMyyyy(date);
        this.temp = temp;
    }

    public String getCity_name() {
        return city_name;
    }

    public String getCountry_code() {
        return country_code;
    }

    public String getIcon() {
        return icon;
    }

    public String getDescription() {
        return description;
    }

    public double getTemp() {
        return temp;
    }

    public String getDate() {
        return date;
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "EEE, d MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
