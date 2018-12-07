package com.gawe.tpkom.Model;

public class register {
    String id;
    String atasan;
    String name;
    String email;
    String role;

    public register(String id, String atasan, String name, String email, String role) {
        this.id = id;
        this.atasan = atasan;
        this.name = name;
        this.email = email;
        this.role = role;
    }

    public String getId() {
        return id;
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

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }
}
