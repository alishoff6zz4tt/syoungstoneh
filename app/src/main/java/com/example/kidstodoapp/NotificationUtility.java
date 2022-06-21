package com.example.kidstodoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationUtility {

    private static Context applicationContext;
    private static NotificationChannel upcomingTaskChannel;
    private static NotificationChannel taskCompletedChannel;
    private static final int UPCOMING_TASK_ID_INT = 1;
    private static final int TASK_COMPLETED_ID_INT = 2;
    private static final String UPCOMING_TASK_ID_STRING = "Upcoming Task";
    private static final String TASK_COMPLETED_ID_STRING = "Task Completed";

    public static void setApplicationContext(Context context) {
        applicationContext = context;
        initializeChannels();
    }

    public static void sendTaskCompletedNotification(ToDoEntry toDoEntry) {
        String title = "Your child just completed '" + toDoEntry.getEntryName() + "'!";
        String text = "Click here to confirm task completion.";
        sendNotification(title, text, TASK_COMPLETED_ID_STRING, TASK_COMPLETED_ID_INT);
    }

    private static void sendUpcomingTaskNotification(ToDoEntry toDoEntry) {
        String title = "It's almost time to " + toDoEntry.getEntryName() + "!";
        String text = toDoEntry.getEntryName() + " is coming up in half an hour! Are you ready?";
        sendNotification(title, text, UPCOMING_TASK_ID_STRING, UPCOMING_TASK_ID_INT);
    }

    private static void initializeChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = applicationContext.getSystemService(NotificationManager.class);
            upcomingTaskChannel = new NotificationChannel(
                    UPCOMING_TASK_ID_STRING, UPCOMING_TASK_ID_STRING,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            taskCompletedChannel = new NotificationChannel(
                    TASK_COMPLETED_ID_STRING, TASK_COMPLETED_ID_STRING,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            manager.createNotificationChannel(upcomingTaskChannel);
            manager.createNotificationChannel(taskCompletedChannel);
        }
    }

    private static void sendNotification(String title, String text, String channelId, int notificationId) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(applicationContext, channelId);
        builder.setContentTitle(title);
        builder.setContentText(text);
        builder.setSmallIcon(R.drawable.ic_launcher_background);
        builder.setAutoCancel(true);

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(applicationContext);
        managerCompat.notify(notificationId,builder.build());

    }

}
