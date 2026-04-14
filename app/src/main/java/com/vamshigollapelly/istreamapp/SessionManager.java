package com.vamshigollapelly.istreamapp;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences("istream_prefs", Context.MODE_PRIVATE);
    }

    public void saveUser(int userId, String username) {
        prefs.edit().putInt("userId", userId).putString("username", username).apply();
    }

    public int getUserId() {
        return prefs.getInt("userId", -1);
    }

    public String getUsername() {
        return prefs.getString("username", null);
    }

    public boolean isLoggedIn() {
        return getUserId() != -1;
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}