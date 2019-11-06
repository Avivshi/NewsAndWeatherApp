package com.example.newsandweathertake2.Model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Article {

    private String title;
    private String urlToImage;
    private String description;
    private String url;

    public Article(String title, String urlToImage, String description, String url) {
        this.title = title;
        this.urlToImage = urlToImage;
        this.description = description;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public void setUrlToImage(String urlToImage) {
        this.urlToImage = urlToImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
