package com.example.inclass13;

import java.util.List;

public class Locations {
    List<Location> points;
    String title;

    @Override
    public String toString() {
        return "Locations{" +
                "points=" + points +
                ", title='" + title + '\'' +
                '}';
    }

    public List<Location> getPoints() {
        return points;
    }

    public void setPoints(List<Location> points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Locations(List<Location> points, String title) {
        this.points = points;
        this.title = title;
    }
}
