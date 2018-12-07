package com.gawe.tpkom.Model;

public class User {
    private String email,status,nama,time;

    public User() {
    }

    public User(String email, String status, String nama, String time) {
        this.email = email;
        this.nama = status;
        this.status = nama;
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}


