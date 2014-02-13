package com.gmail.mazinva.RSSReaderSQL;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent myIntent = new Intent(context, MyService.class);
        intent.putExtra("task", "UPDATE");
        PendingIntent pendingIntent = PendingIntent.getService(context, 0, myIntent,
                                                               PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                  System.currentTimeMillis() + 5000, 15 * 60 * 1000, pendingIntent);
    }
}