package com.example.alarmmanager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.example.alarmmanager.databinding.ActivityMainBinding;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public static int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;
    private static final String TAG = "debig:MainActivity";
    private ActivityMainBinding binding;
    String alarmtime;
    MyAlarmManager myAlarmManager;
    PowerManager.WakeLock mWakeLock;
    PowerManager powerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        myAlarmManager = new MyAlarmManager(MainActivity.this);
        powerManager = (PowerManager)getSystemService(Context.POWER_SERVICE);

        //this will open auto start screen where user can enable autorun permission for your app
        String manufacturer = "xiaomi";
        if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }

        /*if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !Settings.canDrawOverlays(getApplicationContext())) {
            RequestPermission();
        }*/

        binding.btnSetalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.editText.getText().toString().equals("")){
                    alarmtime = binding.editText.getText().toString();
                    myAlarmManager.setSingleAlarm(binding.editText.getText().toString());

                    //todo needs to release this wake lock when not needed
                    mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,TAG);
                    mWakeLock.setReferenceCounted(false);
                    mWakeLock.acquire();
                }
            }
        });

        binding.btnSeteverydayalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(!binding.editText.getText().toString().equals("")){
                   alarmtime = binding.editText.getText().toString();
                   myAlarmManager.setEveryDAyAlarm(binding.editText.getText().toString());

                   //todo needs to release this wake lock when not needed
                   mWakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,TAG);
                   mWakeLock.setReferenceCounted(false);
                   mWakeLock.acquire();
               }
            }
        });
    }

   /* private void RequestPermission() {
        // Check if Android M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Show alert dialog to the user saying a separate permission is needed
            // Launch the settings activity if the user prefers
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getApplicationContext().getPackageName()));
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    Log.d(TAG, "onActivityResult: permission denied");
                } else {
                    Log.d(TAG, "onActivityResult: permission granted");
                }

            }
        }
    }*/
}