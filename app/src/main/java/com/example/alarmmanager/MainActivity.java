package com.example.alarmmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.alarmmanager.databinding.ActivityMainBinding;

import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {
    public static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE= 2323;
    public static final int ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE= 2324;
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

        if(!powerManager.isIgnoringBatteryOptimizations("com.example.alarmmanager")){
            requestPermission();
        }

        addAutoStartup();

        //this will open auto start screen where user can enable autorun permission for your app
        /*String manufacturer = "xiaomi";
        if(manufacturer.equalsIgnoreCase(android.os.Build.MANUFACTURER)) {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            startActivity(intent);
        }*/

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
    }*/

    void requestPermission(){
        Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS, Uri.parse("package:" + getApplicationContext().getPackageName()));
        startActivityForResult(intent, ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE);
    }

    private void addAutoStartup() {

        try {
            Intent intent = new Intent();
            String manufacturer = android.os.Build.MANUFACTURER;
            Toast.makeText(this, ""+"MANUFACTURER : "+manufacturer, Toast.LENGTH_SHORT).show();
            if ("xiaomi".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            } else if ("oppo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.coloros.safecenter", "com.coloros.safecenter.permission.startup.StartupAppListActivity"));
            } else if ("vivo".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.BgStartUpManagerActivity"));
            } else if ("Letv".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.letv.android.letvsafe", "com.letv.android.letvsafe.AutobootManageActivity"));
            } else if ("Honor".equalsIgnoreCase(manufacturer)||"Huawei".equalsIgnoreCase(manufacturer)) {
                intent.setComponent(new ComponentName("com.huawei.systemmanager", "com.huawei.systemmanager.optimize.process.ProtectActivity"));
            }

            List<ResolveInfo> list = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if  (list.size() > 0) {
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e("exc" , String.valueOf(e));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode){
            case ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (!Settings.canDrawOverlays(getApplicationContext())) {
                        Log.d(TAG, "onActivityResult: permission denied");
                    } else {
                        Log.d(TAG, "onActivityResult: permission granted");
                    }

                }
                break;
            case ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS_REQUEST_CODE:
                switch (resultCode){
                    case RESULT_OK:
                        Log.d(TAG, "onActivityResult: Permission granted.App not optimized.");
                        break;
                    case RESULT_CANCELED:
                        new AlertDialog.Builder(this)
                                .setTitle("Allow app to run in background")
                                .setMessage( "Alarm manager needs to have permission to run in background to perform properly, unexpected behavious of alarm may occur otherwise." )
                                .setPositiveButton( "Allow" , new
                                        DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick (DialogInterface paramDialogInterface , int paramInt) {
                                                requestPermission();
                                            }
                                        })
                                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MainActivity.this, "Alarm may not work properly", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show() ;
                        Log.d(TAG, "onActivityResult: RESULT_CANCELED");
                        break;
                }
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }
}