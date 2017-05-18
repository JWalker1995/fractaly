package net.walker9.fractaly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by joel on 2/27/17.
 */

public class UpdateServiceScheduler extends BroadcastReceiver {
    private static final String TAG = "UpdateServiceScheduler";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            schedule(context);
        }
    }

    private static PendingIntent make_updateservice_intent(Context context) {
        Intent intent = new Intent(context, UpdateService.class);
        return PendingIntent.getService(context, 0, intent, 0);
    }

    public static void schedule(Context context) {
        UpdateService.prepare_renders(context);

        Calendar date = Calendar.getInstance();
        date.setTimeInMillis(System.currentTimeMillis());
        date.set(Calendar.HOUR_OF_DAY, 24);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        Log.d(TAG, "Update scheduled for " + date.getTimeInMillis());

        AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_manager.setInexactRepeating(AlarmManager.RTC,
                date.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                make_updateservice_intent(context));
    }

    public static void cancel(Context context) {
        AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_manager.cancel(make_updateservice_intent(context));
    }
}