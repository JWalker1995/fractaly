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
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.Calendar;

import me.nereo.multi_image_selector.MultiImageSelector;

public class MainActivity extends AppCompatActivity {

    static PersistentData persistent_data;
    static ErrorManager error_manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        persistent_data = new PersistentData(this);
        error_manager = new ErrorManager();

        CheckBox enable_checkbox = (CheckBox) findViewById(R.id.enable);
        enable_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                persistent_data.set_enabled(value);
            }
        });

        CheckBox enable_fractal_galaxy_checkbox = (CheckBox) findViewById(R.id.enable_fractal_galaxy);
        enable_fractal_galaxy_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                persistent_data.set_fractal_galaxy_enabled(value);
            }
        });

        Button select_images_button = (Button) findViewById(R.id.select_images);
        select_images_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        Button update_now_button = (Button) findViewById(R.id.update_now);
        update_now_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateService.update(MainActivity.this);
            }
        });

        UpdateServiceScheduler.schedule(this);
    }

    public static PersistentData get_persistent_data() {
        return persistent_data;
    }
    public static ErrorManager get_error_manager() { return error_manager; }
}
