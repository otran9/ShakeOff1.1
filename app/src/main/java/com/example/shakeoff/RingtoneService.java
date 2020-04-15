package com.example.shakeoff;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class RingtoneService extends Service {
    static Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Uri ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(getBaseContext(), ringtoneUri);
        ringtone.play();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
    }
}
