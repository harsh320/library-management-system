package com.example.navigation.activity;

import java.io.Serializable;

public class add_user_data implements Serializable {
    private String name;
    private String sid; private String email; private String path;
    private String phone_number;
    private String Preference;
   private String key,fine;

    public add_user_data(String name, String sid, String email, String path, String phone_number, String preference,String key,String fine) {
        this.fine=fine;
        this.name = name;
        this.sid = sid;
        this.email = email;
        this.path = path;
        this.phone_number = phone_number;
        this.key=key;

        Preference = preference;
    }

    public String getFine() {
        return fine;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSid() {
        return sid;
    }

    public String getEmail() {
        return email;
    }

    public String getPath() {
        return path;
    }

    public String getPhone_number() {
        return phone_number;
    }


    public String getPreference() {
        return Preference;
    }
}
