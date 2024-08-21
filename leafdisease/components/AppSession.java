package com.samcore.leafdisease.components;

import android.content.Context;
import android.content.SharedPreferences;

public class AppSession {
    public static final String PREF_NAME="SessionPref";
    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    public static String KEY_IS_LOGGED_IN="isLoggedIn";
    public static String KEY_USER_NAME="userName";
    public static String KEY_USER_TYPE="userType";
    private static final String KEY_UID="uid";
    private static final String KEY_IS_ADMIN="isAdmin";

    public AppSession(Context context) {
       sharedPreferences= context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
       editor= sharedPreferences.edit();
    }

    public boolean getKeyIsLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN,false);
    }

    public void setKeyIsLoggedIn(boolean keyIsLoggedIn) {
       editor.putBoolean(KEY_IS_LOGGED_IN,keyIsLoggedIn);
       editor.apply();
    }

    public String getKeyUserName() {
        return sharedPreferences.getString(KEY_USER_NAME,"");
    }

    public void setKeyUserName(String keyUserName) {
       editor.putString(KEY_USER_NAME,keyUserName);
       editor.apply();
    }

    public  String getKeyUid() {
        return sharedPreferences.getString(KEY_UID,"");
    }

    public void setKeyUid(String keyUid) {
        editor.putString(KEY_UID,keyUid);
        editor.apply();
    }

    public boolean getKeyIsAdmin() {
        return sharedPreferences.getBoolean(KEY_IS_ADMIN,false);
    }

    public void setKeyIsAdmin(boolean keyIsAdmin) {
       editor.putBoolean(KEY_IS_ADMIN,keyIsAdmin);
       editor.apply();
    }

    public  String getKeyUserType() {
        return sharedPreferences.getString(KEY_USER_TYPE,"");
    }

    public  void setKeyUserType(String keyUserType) {
        editor.putString(KEY_USER_TYPE,keyUserType);
        editor.apply();
    }
}
