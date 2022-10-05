package com.example.kidstodoapp;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class NotificationHandler extends BroadcastReceiver {

    public static String NOTIFICATION_ID = "notification-id";
    public static String NOTIFICATION = "notification";

    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager =
                (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(NOTIFICATION);
        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        notificationManager.notify(id, notification);
    }

    public static void scheduleNotification(ToDoEntry toDoEntry, Context context) {
        Notification.Builder notificationBuilder = new Notification.Builder(context);
        notificationBuilder.setContentTitle(toDoEntry.getEntryName() + " is due soon!");
        notificationBuilder.setContentText("Complete " + toDoEntry.getEntryName() + " by " +
                toDoEntry.getDateTimeString() + " and earn $" + toDoEntry.getPointValue() + "!");
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        Notification notification = notificationBuilder.build();
        Intent notificationIntent = new Intent(context, NotificationHandler.class);
        notificationIntent.putExtra(NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NOTIFICATION, notification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        long notificationTimeMillis = toDoEntry.getDateTimeMillis() - 1800000;
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(
                AlarmManager.RTC_WAKEUP, notificationTimeMillis, pendingIntent);
        Log.d("INFO", "notificationTimeMillis: " + notificationTimeMillis + ", System.currentTimeMillis(): " + System.currentTimeMillis());
    }

    public static void updateNotification(ToDoEntry toDoEntry) {

    }

    public static void cancelNotification(ToDoEntry toDoEntry) {

    }

}
