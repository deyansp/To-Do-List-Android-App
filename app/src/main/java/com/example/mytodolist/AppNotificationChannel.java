package com.example.mytodolist;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class AppNotificationChannel extends Application {
    public static final String CHANNEL_1_ID = "Task Reminder Channel";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels() {
        // checking if th SDK is at least version 26 - Oreo
        // as it requires notification channels
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 =  new NotificationChannel(
                    CHANNEL_1_ID,
                    "Task Reminder",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("This channel is responsible for sending reminders for pending tasks");

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null)
                manager.createNotificationChannel(channel1);
        }
    }
}
