package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class MyAlarmManager  {
    private static final String TAG = "MyAlarmManager";
    private static final int ALARM_BROADCAST_REQUEST_CODE=999;
    private static final int ALARM_BROADCAST_REQUEST_CODE1=777;
    AlarmManager alarmManager;
    Context context;

    public MyAlarmManager(Context context) {
        this.context=context;
        alarmManager= (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void setSingleAlarm(String alarmtime){
        int hour=Integer.valueOf(alarmtime.split(":")[0]);
        int min=Integer.valueOf(alarmtime.split(":")[1]);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            Log.d(TAG, "setSingleAlarm: one day added");
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent=new Intent(context,AlarmBroadcast.class);
        intent.putExtra("sms","single alarm");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,ALARM_BROADCAST_REQUEST_CODE,intent,0);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        Toast.makeText(context, "Single alarm set successfully.", Toast.LENGTH_SHORT).show();
    }

    public void setEveryDAyAlarm(String alarmtime){
        int hour=Integer.valueOf(alarmtime.split(":")[0]);
        int min=Integer.valueOf(alarmtime.split(":")[1]);
        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.before(Calendar.getInstance())) {
            Log.d(TAG, "setSingleAlarm: one day added");
            calendar.add(Calendar.DATE, 1);
        }

        Intent intent=new Intent(context,AlarmBroadcast.class);
        intent.putExtra("sms","Repeating alarm");
        PendingIntent pendingIntent=PendingIntent.getBroadcast(context,ALARM_BROADCAST_REQUEST_CODE1,intent,0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),AlarmManager.INTERVAL_HALF_HOUR,pendingIntent);
        Toast.makeText(context, "Repeating alarm set successfully.", Toast.LENGTH_SHORT).show();
    }
}
