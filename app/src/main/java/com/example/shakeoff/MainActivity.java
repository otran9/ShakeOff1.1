package com.example.shakeoff;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.content.Intent;
import android.os.Bundle;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/*
COMPONENTS
Motion Detection: Make an activity that changes a value when shaken
Alarm: Implement an alarm to do something at a time. (CRUD) Set multiple alarms to go off at many times
 */

public class MainActivity extends AppCompatActivity {
    int hours, minutes, seconds, time;
    EditText hrText, minText, secText;
    Button button;
    Button testAlarm;
    Switch AM_PM;
    Timer alarm;
    boolean timerSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hours = 0;
        minutes = 0;
        seconds = 0;
        time = 0;
        hrText = findViewById(R.id.hour);
        minText = findViewById(R.id.minute);
        button = findViewById(R.id.button);
        testAlarm = findViewById(R.id.test);
        AM_PM = findViewById(R.id.AM_PM);
        timerSet = false;
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());

        cal.clear();

        hrText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    hours = 0;
                    return;
                }
                hours = Integer.parseInt(charSequence.toString());
                updateTime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        minText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    minutes = 0;
                    return;
                }
                minutes = Integer.parseInt(charSequence.toString());
                if (minutes > 59) {
                    minutes = 0;
                }
                updateTime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);

            @Override
            public void onClick(View v) {
                alarm = new Timer();
                alarm.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(alarmIntent);
                    }
                }, time);
            }
        });

        testAlarm.setOnClickListener(new View.OnClickListener() {
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);

            @Override
            public void onClick(View view) {
                alarm = new Timer();
                alarm.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        startActivity(alarmIntent);
                    }
                }, 5000);
            }
        });
    }

    void updateTime() {
        time = (3600000 * hours) + (60000 * minutes);
    }
    /*
     */
}
