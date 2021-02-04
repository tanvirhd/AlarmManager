package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.alarmmanager.databinding.ActivityFireAlarmBinding;

public class ActivityFireAlarm extends AppCompatActivity {
    private ActivityFireAlarmBinding binding;
    AlarmManager alarmManager;
    MediaPlayer mediaPlayer;
    String sms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityFireAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        showOnLockedScreen();
        alarmManager= (AlarmManager) getSystemService( Context.ALARM_SERVICE);
        mediaPlayer=MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        mediaPlayer.start();

        sms=getIntent().getStringExtra("sms");
        binding.sms.setText(sms);

        binding.btnstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.stop();
                onBackPressed();
                finish();
            }
        });

        binding.cancelalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
    }

    void cancelAlarm(){
        Intent intent=new Intent(this,AlarmBroadcast.class);
        PendingIntent pendingIntent=PendingIntent.getBroadcast(this,777,intent,0);
        alarmManager.cancel(pendingIntent);
    }

    private void showOnLockedScreen() {
        final Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
    }
}