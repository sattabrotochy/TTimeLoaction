package com.shuvo.ttimeloaction.Class;

public class DataModel {

    String firebase_id,loaction,date,time;

    public DataModel() {
    }

    public DataModel(String firebase_id, String loaction, String date, String time) {
        this.firebase_id = firebase_id;
        this.loaction = loaction;
        this.date = date;
        this.time = time;
    }

    public String getFirebase_id() {
        return firebase_id;
    }

    public void setFirebase_id(String firebase_id) {
        this.firebase_id = firebase_id;
    }

    public String getLoaction() {
        return loaction;
    }

    public void setLoaction(String loaction) {
        this.loaction = loaction;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
