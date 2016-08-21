package com.example.vidhiraj.sample;

/**
 * Created by lenovo on 21/08/2016.
 */
public class DailyTeachData {

    public String standard;
    public String chapter;
    public String date;
    public String points;

    public DailyTeachData(String standard, String chapter, String date, String points, int id) {
        this.standard = standard;
        this.chapter = chapter;
        this.date = date;
        this.points = points;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  int id;

    public DailyTeachData(String standard, String chapter, String date, String points) {
        this.standard = standard;
        this.chapter = chapter;
        this.date = date;
        this.points = points;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }


public DailyTeachData()
{

}
    public DailyTeachData(String standard, String chapter) {
        this.standard = standard;
        this.chapter = chapter;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getChapter() {
        return chapter;
    }

    public void setChapter(String chapter) {
        this.chapter = chapter;
    }
}
