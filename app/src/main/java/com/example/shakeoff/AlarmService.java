package com.example.shakeoff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Binder;
import androidx.annotation.Nullable;
import android.util.Log;
import java.util.Calendar;

public class AlarmService extends Service {
    private int hour, setHour, minute, time;
    private boolean alarmSet, isPM;
    private Calendar currentTime; // When currentTime reaches alarmTime, sound the alarm
    private Intent alarmIntent;
    private final String TAG = "AlarmService.java: ";

    public AlarmService() {
    }

    class AlarmServiceBinder extends Binder {
        public AlarmService getService() {
            return AlarmService.this;
        }
    }

    private IBinder binder = new AlarmServiceBinder();

    public int onStartCommand(Intent intent, int flags, int startID) {
        Log.i(TAG, "onStartCommand: startID == " + Thread.currentThread().getId());
        alarmSet = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startAlarmTimer();
            }
        }).start();

        return START_STICKY;
    }

    void startAlarmTimer() {
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        currentTime = Calendar.getInstance();

        while (alarmSet) {
            try {
                Thread.sleep(1000);
                currentTime.setTimeInMillis(System.currentTimeMillis());
                String currentTimeStr = "(" + currentTime.get(Calendar.HOUR_OF_DAY)
                        + ") " + currentTime.get(Calendar.HOUR) + ":"
                        + currentTime.get(Calendar.MINUTE);
                Log.i(TAG, "Current time: " + currentTimeStr);
                sendBroadcast(intent);
            } catch (InterruptedException e) {

            }
        }
    }

    /*
    alarm = new Timer();
    alarm.schedule(new TimerTask() {
        @Override
        public void run() {
            startActivity(alarmIntent);
        }
    }, time);
     */

    private void stopAlarmTimer() {
        alarmSet = false;
    }

    public Calendar getCurrentTime() {
        return currentTime;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAlarmTimer();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }
}
