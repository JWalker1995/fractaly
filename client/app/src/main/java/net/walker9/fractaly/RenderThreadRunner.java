package net.walker9.fractaly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joel on 5/16/17.
 */

public class RenderThreadRunner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.ACTION_POWER_CONNECTED")) {

        }

        if (intent.getAction().equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {

        }
    }
}
