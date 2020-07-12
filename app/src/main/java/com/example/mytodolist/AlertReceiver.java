package com.example.mytodolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getBundleExtra("notification");
        int task_id = bundle.getInt("task_id");
        String task_name = bundle.getString("task_name");

        NotificationHandler notificationHandler = new NotificationHandler(context);

        NotificationCompat.Builder builder = notificationHandler.getNotification(task_name);
        notificationHandler.getManager().notify(task_id, builder.build());
    }
}
