package com.example.mytodolist;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import java.util.Calendar;

// notification channel ID so that notifications can be sent on APIs 26+
import static com.example.mytodolist.AppNotificationChannel.CHANNEL_1_ID;

public class NotificationHandler extends ContextWrapper {
    // android class for creating and sending notifications
    private NotificationManager mManager;

    public NotificationHandler(Context base) {
        super(base);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
           AppNotificationChannel notificationChannel = new AppNotificationChannel();
        }
    }

    public NotificationManager getManager() {
        if (mManager == null) {
            mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return mManager;
    }

    // used by the AlertReceiver to create the notification
    public NotificationCompat.Builder getNotification(String taskName) {

        // intent for opening the app on notification click
        Intent openMainActivity = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent notificationIntent = PendingIntent.getActivity(getApplicationContext(), 0, openMainActivity, 0);

        return new NotificationCompat.Builder(getApplicationContext(), CHANNEL_1_ID)
                .setSmallIcon(R.drawable.ic_done)
                .setContentTitle(taskName)
                .setContentText("Due today")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setContentIntent(notificationIntent)
                .setAutoCancel(true);
    }

    // two methods used by the activities to schedule and cancel reminders
    public void scheduleNotification(Calendar c, ToDoTask task) {
        // alarm service used to schedule a notification
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent (getApplicationContext() , AlertReceiver.class);

        // adding the task details for the notification
        Bundle bundle = new Bundle();
        bundle.putInt("task_id", task.getId());
        bundle.putString("task_name", task.getTitle());
        intent.putExtra("notification", bundle);

        // each pending intent has the task id as its req code to uniquely identify them
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), task.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null)
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis() + 10000, pendingIntent);
    }

    public void cancelScheduledNotification (ToDoTask task) {
        // recreating the scheduled intent to cancel it
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
        Bundle bundle = new Bundle();
        bundle.putInt("task_id", task.getId());
        bundle.putString("task_name", task.getTitle());
        intent.putExtra("notification", bundle);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), task.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            pendingIntent.cancel();
        }
    }
}
