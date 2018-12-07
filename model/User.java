package com.gawe.tpkom.Model;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;
//DataSnapshot { key = 5, value = {password=29121997, role=Atasan, atasan=C, statusAcc=0, name=Sebi, id=5, email=sebi@gmail.com} }
public class User  {
    private String email, status, time;
    String id;
    String atasan;
    String name;
    String role;
    String password;

    public User(String id, String atasan, String name, String email, String password, String role) {
        this.id = id;
        this.atasan = atasan;
        this.name = name;
        this.email = email;
        this.role = role;
        this.password = password;
    }

    public User() {
    }

    public User(String email, String status, String name, String time) {
        this.email = email;
        this.name = status;
        this.status = name;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAtasan() {
        return atasan;
    }

    public void setAtasan(String atasan) {
        this.atasan = atasan;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", id);
        result.put("Nama", name);
        result.put("Email", email);
        result.put("Role", role);
        return result;
    }
}