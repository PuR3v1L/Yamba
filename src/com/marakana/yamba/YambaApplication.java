package com.marakana.yamba;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import winterwell.jtwitter.Twitter;

/**
 * Created by PuR3v1L on 30/7/2013.
 */
public class YambaApplication extends Application implements SharedPreferences.OnSharedPreferenceChangeListener {


    private static final String TAG = YambaApplication.class.getSimpleName();
    public Twitter twitter; //
    private SharedPreferences prefs;
    private boolean serviceRunning;

    @Override
    public void onCreate() { //
        super.onCreate();
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs.registerOnSharedPreferenceChangeListener(this);
        Log.i(TAG, "onCreated");
    }

    public boolean isServiceRunning() { //
        return serviceRunning;
    }

    public void setServiceRunning(boolean serviceRunning) { //
        this.serviceRunning = serviceRunning;
    }

    @Override
    public void onTerminate() { //
        super.onTerminate();
        Log.i(TAG, "onTerminated");
    }

    public synchronized Twitter getTwitter() { //
        if (this.twitter == null) {
            String username = this.prefs.getString("username", "");
            String password = this.prefs.getString("password", "");
            String apiRoot = prefs.getString("apiRoot",
                    "http://yamba.marakana.com/api");
            if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
                    && !TextUtils.isEmpty(apiRoot)) {
                this.twitter = new Twitter(username, password);
                this.twitter.setAPIRootUrl(apiRoot);
            }
        }
        return this.twitter;
    }

    public synchronized void onSharedPreferenceChanged(
            SharedPreferences sharedPreferences, String key) { //
        this.twitter = null;
    }
}
