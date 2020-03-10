package com.example.shakeoff;

import androidx.appcompat.app.AppCompatActivity;

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
    int hour, setHour, minute, time;
    EditText hrText, minText, secText;
    Button button;
    Button testAlarm;
    Switch AM_PM;
    String AM_PM_String = " AM";
    Timer alarm;
    boolean timerSet, isPM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hour = 0;
        setHour = 0;
        minute = 0;
        time = 0;

        hrText = findViewById(R.id.hour);
        minText = findViewById(R.id.minute);
        button = findViewById(R.id.button);
        testAlarm = findViewById(R.id.test);

        AM_PM = findViewById(R.id.AM_PM);
        AM_PM.setText("AM");
        timerSet = false;
        isPM = false;

        /*
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        Toast.makeText(getApplicationContext(), ("Hour: " + cal.get(Calendar.HOUR_OF_DAY) + " Minute: " + cal.get(Calendar.MINUTE)), Toast.LENGTH_LONG).show();
        //cal.clear();
         */

        hrText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 0) {
                    hour = 0;
                    return;
                }
                hour = Integer.parseInt(charSequence.toString());
                if (hour > 12) {
                    hour = 12;
                }
                //updateTime();
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
                    minute = 0;
                    return;
                }
                minute = Integer.parseInt(charSequence.toString());
                if (minute > 59) {
                    minute = 59;
                }
                //updateTime();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        AM_PM.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isPM = true;
                    Toast.makeText(getApplicationContext(), ("isPM = true"), Toast.LENGTH_LONG).show();
                    AM_PM.setText("PM");
                    AM_PM_String = " PM";
                } else {
                    isPM = false;
                    Toast.makeText(getApplicationContext(), ("isPM = false"), Toast.LENGTH_LONG).show();
                    AM_PM.setText("AM");
                    AM_PM_String = " AM";
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            Intent alarmIntent = new Intent(getApplicationContext(), AlarmActivity.class);
            @Override
            public void onClick(View v) {
                if (isPM) {
                    setHour = hour + 12;
                } else {
                    setHour = hour;
                }
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(System.currentTimeMillis());
                int currentHour = cal.get(Calendar.HOUR_OF_DAY);
                int currentMinute = cal.get(Calendar.MINUTE);
                Toast.makeText(getApplicationContext(), ("Current hour: " + currentHour + " Current minute: " + currentMinute + "\n"
                                                    + "Hour: " + hour + " Set hour: " + setHour + " Minute: " + minute), Toast.LENGTH_LONG).show();
                if (isTime(currentHour, currentMinute, hour, minute)) {
                    startActivity(alarmIntent);
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

    boolean isTime(int currentHour, int currentMinute, int hour, int minute) {
        //Toast.makeText(getApplicationContext(), ("Alarm set for " + hour + ":" + minute + AM_PM_String), Toast.LENGTH_LONG).show();
        boolean isTime = false;
        if (setHour == currentHour) {
            if (minute == currentMinute) {
                isTime = true;
            }
        }
        return isTime;
    }

    void updateTime() {
        time = (3600000 * hour) + (60000 * minute);
    }
    /*
     */
}
