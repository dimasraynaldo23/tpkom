package com.gawe.tpkom.Model;

public class Tracking {
    private String  email,uid,nama,lat,lng,time;

    public Tracking() {

    }

    public Tracking(String email, String uid, String nama, String lat, String lng,String time) {
        this.email = email;
        this.uid = uid;
        this.nama = nama;
        this.lat = lat;
        this.lng = lng;
        this.time = time;


    }



    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
