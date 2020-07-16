package com.example.mytodolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;

// Creates reminder notifications on the set due date
public class AlertReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // retrieving the task details from intent
        Bundle bundle = intent.getBundleExtra("notification");
        int task_id = bundle.getInt("task_id");
        String task_name = bundle.getString("task_name");

        // getting instance of the NotificationHandler class to create and send the notification
        NotificationHandler notificationHandler = new NotificationHandler(context);
        NotificationCompat.Builder builder = notificationHandler.getNotification(task_name);
        notificationHandler.getManager().notify(task_id, builder.build());
    }
}
