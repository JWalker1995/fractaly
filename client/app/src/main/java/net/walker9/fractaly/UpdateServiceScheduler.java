package net.walker9.fractaly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by joel on 2/27/17.
 */

public class UpdateServiceScheduler extends BroadcastReceiver {

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

        Calendar date = new GregorianCalendar();
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);
        date.add(Calendar.DAY_OF_MONTH, 1);

        AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_manager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                date.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY,
                make_updateservice_intent(context));
    }

    public static void cancel(Context context) {
        AlarmManager alarm_manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarm_manager.cancel(make_updateservice_intent(context));
    }
}