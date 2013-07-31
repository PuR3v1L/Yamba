package com.marakana.yamba;

import android.app.Service;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by PuR3v1L on 30/7/2013.
 */
public class UpdaterService extends Service {
    static final String TAG = "UpdaterService";
    static final int DELAY = 60000; // a minute
    DbHelper dbHelper;
    SQLiteDatabase db;
    private boolean runFlag = false; //
    private Updater updater;
    private YambaApplication yamba;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.yamba = (YambaApplication) getApplication();
        Log.d(TAG, "onCreated");
        this.updater = new Updater();
        dbHelper = new DbHelper(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStarted");
        this.runFlag = true;
        this.updater.start();
        this.yamba.setServiceRunning(true);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.runFlag = false;
        this.updater.interrupt();
        this.updater = null;
        this.yamba.setServiceRunning(false);
        Log.d(TAG, "onDestroyed");
    }

    private class Updater extends Thread { //

        public Updater() {
            super("UpdaterService-Updater");
        }

        @Override
        public void run() {
            UpdaterService updaterService = UpdaterService.this;
            while (updaterService.runFlag) {
                Log.d(TAG, "Running background thread");
                try {
                    YambaApplication yamba = (YambaApplication) updaterService
                            .getApplication(); //
                    int newUpdates = yamba.fetchStatusUpdates(); //
                    if (newUpdates > 0) { //
                        Log.d(TAG, "We have a new status");
                    }
                    Thread.sleep(DELAY);
                } catch (InterruptedException e) {
                    updaterService.runFlag = false;
                }
            }
        }
    } // Updater
}
