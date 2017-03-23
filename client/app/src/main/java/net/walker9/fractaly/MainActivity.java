package net.walker9.fractaly;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.Calendar;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity {

    static Activity active_activity;
    static PersistentData persistent_data;
    static ErrorManager error_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        persistent_data = new PersistentData(this);
        error_manager = new ErrorManager();

        UpdateServiceScheduler.schedule(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active_activity = this;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active_activity = null;
    }

    public static Activity get_active_activity() { return active_activity; }
    public static PersistentData get_persistent_data() {
        return persistent_data;
    }
    public static ErrorManager get_error_manager() { return error_manager; }
}
