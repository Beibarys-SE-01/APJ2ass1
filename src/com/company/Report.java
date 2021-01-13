package com.company;

import java.time.LocalDateTime;

public class Report {
    String id;
    String name;
    int numOfArticles;
    long avg_content_length;
    long content_length;
    LocalDateTime published_from;
    LocalDateTime published_to;

    public Report(String name, String id, int content_length, LocalDateTime published_from, LocalDateTime published_to) {
        this.name = name;
        this.id = id;
        this.content_length = content_length;
        this.published_from = published_from;
        this.published_to = published_to;
        numOfArticles = 1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getNumOfArticles() {
        return numOfArticles;
    }

    public void setNumOfArticles(int numOfArticles) {
        this.numOfArticles = numOfArticles;
    }

    public long getAvg_content_length() {
        return avg_content_length;
    }

    public void setAvg_content_length(long avg_content_length) {
        this.avg_content_length = avg_content_length;
    }

    public long getContent_length() {
        return content_length;
    }

    public void setContent_length(long content_length) {
        this.content_length = content_length;
    }

    public LocalDateTime getPublished_from() {
        return published_from;
    }

    public void setPublished_from(LocalDateTime published_from) {
        this.published_from = published_from;
    }

    public LocalDateTime getPublished_to() {
        return published_to;
    }

    public void setPublished_to(LocalDateTime published_to) {
        this.published_to = published_to;
    }

    public void setAvgContentLength() {
        avg_content_length = content_length/numOfArticles;
    }
}