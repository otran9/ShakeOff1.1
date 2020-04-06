package com.example.shakeoff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.content.Intent;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/*
COMPONENTS
Motion Detection: Make an activity that changes a value when shaken
Alarm: Implement an alarm to do something at a time. (CRUD) Set multiple alarms to go off at many times
 */

public class MainActivity extends AppCompatActivity {
    private int year, month, day, hour, setHour, minute, time;
    private EditText yearText, monthText, dayText, hrText, minText;
    private Button button;
    private Button testAlarm;
    private Switch AM_PM;
    private String AM_PM_String = " AM";
    private Timer alarm;
    private Intent alarmIntent, alarmServiceIntent;
    public PendingIntent alarmPendingIntent;
    private boolean alarmSet, isPM, isBound;
    private AlarmService alarmService;
    private ServiceConnection serviceConnection;
    private BroadcastReceiver alarmBR;
    private Calendar currentTime, alarmTime, tempCal;
    AlarmManager alarmManager;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        year = 0;
        month = 0;
        day = 0;
        time = 0;
         */
        hour = 0;
        setHour = 0;
        minute = 0;
        alarmSet = false;
        isPM = false;
        isBound = false;
        hrText = findViewById(R.id.hour);
        minText = findViewById(R.id.minute);
        button = findViewById(R.id.button);
        AM_PM = findViewById(R.id.AM_PM);
        testAlarm = findViewById(R.id.test);

        AM_PM.setText("AM");
        AM_PM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPM = true;
                    AM_PM.setText("PM");
                    AM_PM_String = " PM";
                } else {
                    isPM = false;
                    AM_PM.setText("AM");
                    AM_PM_String = " AM";
                }
                alarmSet = false;
            }
        });

        alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);
        alarmPendingIntent.getActivity(getApplicationContext(), 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        currentTime = Calendar.getInstance();
        alarmTime = Calendar.getInstance();
        updateAlarmTime();
        /*

        yearText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    year = 0;
                    return;
                }
                year = Integer.parseInt(charSequence.toString());
                if (year < cal.get(Calendar.YEAR) {
                    year = cal.get(Calendar.YEAR);
                }
                alarmSet = false;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        monthText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    month = 0;
                    return;
                }
                month = Integer.parseInt(charSequence.toString());
                if (month < cal.get(Calendar.MONTH) {
                    month = cal.get(Calendar.MONTH);
                }
                alarmSet = false;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
        dayText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    day = 0;
                    return;
                }
                month = Integer.parseInt(charSequence.toString());
                if (day < cal.get(Calendar.DAY_OF_MONTH) {
                    day = cal.get(Calendar.DAY_OF_MONTH);
                }
                alarmSet = false;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });
         */
        hrText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    hour = 12;
                    return;
                }
                hour = Integer.parseInt(charSequence.toString());
                if (hour > 12 || hour < 1) {
                    hour = 12;
                    hrText.setText("12");
                }
                alarmSet = false;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        minText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    minute = 0;
                    return;
                }
                minute = Integer.parseInt(charSequence.toString());
                if (minute > 59) {
                    minute = 59;
                    minText.setText("59");
                } else if (minute < 0) {
                    minute = 0;
                    minText.setText("00");
                }
                alarmSet = false;
            }
            @Override
            public void afterTextChanged(Editable editable) { }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!alarmSet) {
                    updateAlarmTime();
                    String message = "Current time ("
                            + currentTime.get(Calendar.HOUR_OF_DAY) + ") "
                            + currentTime.get(Calendar.HOUR) + ":"
                            + currentTime.get(Calendar.MINUTE)
                            + "\nAlarm set for ("
                            + alarmTime.get(Calendar.HOUR_OF_DAY) + ") "
                            + alarmTime.get(Calendar.HOUR) + ":"
                            + alarmTime.get(Calendar.MINUTE) + AM_PM_String;
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                    alarmSet = true;
                }
            }
        });

        testAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateAlarmTime();
                tempCal = Calendar.getInstance();
                String timeStr = "(" + tempCal.get(Calendar.HOUR_OF_DAY)
                        + ") " + tempCal.get(Calendar.HOUR) + ":"
                        + tempCal.get(Calendar.MINUTE)
                        + "\n(" + alarmTime.get(Calendar.HOUR_OF_DAY)
                        + ") " + alarmTime.get(Calendar.HOUR) + ":"
                        + alarmTime.get(Calendar.MINUTE);
                Toast.makeText(getApplicationContext(), timeStr, Toast.LENGTH_LONG).show();
                if (isTime(tempCal, alarmTime)) {
                    startActivity(alarmIntent);
                }
                /*
                alarm = new Timer();
                alarm.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(alarmIntent);
                    }
                }, 5000);
                */
            }
        });

        String timeStr = "(" + currentTime.get(Calendar.HOUR_OF_DAY)
                + ") " + currentTime.get(Calendar.HOUR) + ":"
                + currentTime.get(Calendar.MINUTE)
                + "\n(" + alarmTime.get(Calendar.HOUR_OF_DAY)
                + ") " + alarmTime.get(Calendar.HOUR) + ":"
                + alarmTime.get(Calendar.MINUTE);
        Toast.makeText(getApplicationContext(), timeStr, Toast.LENGTH_SHORT).show();

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (isTime(currentTime, alarmTime)) {
                    startActivity(alarmIntent);
                }
                handler.postDelayed(runnable, 5000);
                currentTime = Calendar.getInstance();
                String timeStr = "(" + currentTime.get(Calendar.HOUR_OF_DAY)
                        + ") " + currentTime.get(Calendar.HOUR) + ":"
                        + currentTime.get(Calendar.MINUTE)
                        + "\n(" + alarmTime.get(Calendar.HOUR_OF_DAY)
                        + ") " + alarmTime.get(Calendar.HOUR) + ":"
                        + alarmTime.get(Calendar.MINUTE);
                Toast.makeText(getApplicationContext(), timeStr, Toast.LENGTH_SHORT).show();
            }
        };
        handler.post(runnable);

        /*
        Toast.makeText(getApplicationContext(), "isTime: " + isTime(currentTime, alarmTime), Toast.LENGTH_SHORT).show();
        while(!isTime(currentTime, alarmTime)) {
            try {
                updateAlarmTime();
                if (isTime(currentTime, alarmTime)) {
                    startActivity(alarmIntent);
                }
                //Toast.makeText(getApplicationContext(), "while", Toast.LENGTH_SHORT).show();
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
         */
    }

    // Check if the current time matches the alarm time
    boolean isTime(Calendar currentTime, Calendar alarmTime) {
        boolean matchTime = false;
        if (currentTime.get(Calendar.HOUR_OF_DAY) == alarmTime.get(Calendar.HOUR_OF_DAY)) {
            if (currentTime.get(Calendar.MINUTE) == alarmTime.get(Calendar.MINUTE)) {
                matchTime = true;
            }
        }

        /*
        if (currentTime.get(Calendar.YEAR) == alarmTime.get(Calendar.YEAR)) {
            if (currentTime.get(Calendar.MONTH) == alarmTime.get(Calendar.MONTH)) {
                if (currentTime.get(Calendar.DAY_OF_MONTH) == alarmTime.get(Calendar.DAY_OF_MONTH)) {
                    if (currentTime.get(Calendar.HOUR_OF_DAY) == alarmTime.get(Calendar.HOUR_OF_DAY)) {
                        if (currentTime.get(Calendar.MINUTE) == alarmTime.get(Calendar.MINUTE)) {
                            matchTime = true;
                        }
                    }
                }
            }
        }
         */
        //Toast.makeText(getApplicationContext(), "isTime() " + matchTime, Toast.LENGTH_SHORT).show();
        return matchTime;
    }

    void startAlarm() {
        /*
        while(!isTime(currentTime, alarmTime)) {
            try {
                if (isTime(currentTime, alarmTime)) {
                    startActivity(alarmIntent);
                }
                Toast.makeText(getApplicationContext(), "while", Toast.LENGTH_SHORT).show();
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        }
         */
    }

    void updateAlarmTime() {
        if (isPM) {
            setHour = hour + 12;
        } else if (!isPM) {
            setHour = hour;
        }
        alarmTime.set(Calendar.HOUR, hour);
        alarmTime.set(Calendar.HOUR_OF_DAY, setHour);
        alarmTime.set(Calendar.MINUTE, minute);
        Toast.makeText(getApplicationContext(), "updateAlarmTime()", Toast.LENGTH_SHORT).show();
        //time = (3600000 * hour) + (60000 * minute);
    }

    void bindService() {
        if (serviceConnection == null) {
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    AlarmService.AlarmServiceBinder alarmServiceBinder = (AlarmService.AlarmServiceBinder)iBinder;
                    alarmService = alarmServiceBinder.getService();
                    isBound = true;
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    isBound = false;
                }
            };
        }
        bindService(alarmServiceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    void unBindService() {
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    /*
     */
}
