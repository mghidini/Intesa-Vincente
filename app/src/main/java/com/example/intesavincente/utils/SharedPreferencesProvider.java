package com.example.intesavincente.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

//import com.google.android.gms.common.internal.Constants;
import com.example.intesavincente.utils.Constants;
public class SharedPreferencesProvider {

    private final Application mApplication;
    private final SharedPreferences sharedPref;

    public SharedPreferencesProvider(Application application) {
        this.mApplication = application;
        sharedPref = mApplication.getSharedPreferences(Constants.SHARED_PREFERENCES_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setAuthenticationToken(String token) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.AUTHENTICATION_TOKEN, token);
        editor.apply();
    }

    public void setUserId(String userId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.USER_ID, userId);
        editor.apply();
    }

    public void deleteAll() {
        sharedPref.edit().clear().apply();
    }
}
