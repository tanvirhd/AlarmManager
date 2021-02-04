package com.example.alarmmanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcast extends BroadcastReceiver {
    private static final String TAG = "AlarmBroadcast";
    private static final int NOTIFICATION_ID=1010;

    Context context;
    PowerManager.WakeLock mWakeLock;
    MediaPlayer mediaPlayer;
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: Broadcasting.................................");
        this.context=context;

        String sms=intent.getStringExtra("sms");
        startActivity(sms);
    }

    private void startActivity(String sms) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "startActivity:if called");
            NotificationManager notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
            assert notificationManager != null;

            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                mChannel.setSound(sound, attributes);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

            builder.setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alarm Manager Tester")
                    .setContentText(sms)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(openScreen(NOTIFICATION_ID,sms), true)
                    .setAutoCancel(true)
                    .setOngoing(true);

            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);

        } else {
            Log.d(TAG, "startActivity: else called");
            Intent intent1=new Intent(context,ActivityFireAlarm.class);
            intent1.putExtra("sms",sms);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent1);
        }

    }



    /*private void startActivity(String sms) {

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Log.d(TAG, "startActivity: if called");
            NotificationManager notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();

            String CHANNEL_ID = BuildConfig.APPLICATION_ID.concat("_notification_id");
            String CHANNEL_NAME = BuildConfig.APPLICATION_ID.concat("_notification_name");
            assert notificationManager != null;

            NotificationChannel mChannel = notificationManager.getNotificationChannel(CHANNEL_ID);
            if (mChannel == null) {
                mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                mChannel.setSound(sound, attributes);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

            builder.setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle("Alarm Manager Tester")
                    .setContentText(sms)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_CALL)
                    .setFullScreenIntent(openScreen(NOTIFICATION_ID,sms), true)
                    .setAutoCancel(true)
                    .setOngoing(true);

            Notification notification = builder.build();
            notificationManager.notify(NOTIFICATION_ID, notification);
        } else {
            Log.d(TAG, "startActivity: else called");
            Intent intent1=new Intent(context,ActivityFireAlarm.class);
            intent1.putExtra("sms",sms);
            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

    }*/

    private PendingIntent openScreen(int notificationId,String sms) {
        Log.d(TAG, "openScreen: called");
        Intent fullScreenIntent = new Intent(context, ActivityFireAlarm.class);
        fullScreenIntent.putExtra("notificationId", notificationId);
        fullScreenIntent.putExtra("sms",sms);
        return PendingIntent.getActivity(context, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /*private void showNotification(){
        Intent fullScreenIntent = new Intent(context, DismissActivityNew.class);
        fullScreenIntent.putExtra(context.getResources().getString(R.string.parcel),alarmId);

        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(context, 0,
                fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    getChannelName(),
                    IMPORTANCE);

            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_alarm_on)
                .setContentTitle(alarmId==0?"Sunrise Alarm":"Sunset Alarm")
                .setContentText(context.getString(R.string.dismiss_alarm_notification_body, getCurrentTime()))
                .setAutoCancel(true)
                .addAction(getDismissNotificationAction())
                .setFullScreenIntent(fullScreenPendingIntent, true);

        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
    }*/
}
