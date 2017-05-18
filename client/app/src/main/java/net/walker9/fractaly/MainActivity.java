package net.walker9.fractaly;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import me.nereo.multi_image_selector.MultiImageSelector;
import me.nereo.multi_image_selector.MultiImageSelectorActivity;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    static PersistentData persistent_data;
    static ErrorManager error_manager;

    public static PersistentData get_persistent_data() {
        return persistent_data;
    }
    public static ErrorManager get_error_manager() { return error_manager; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        persistent_data = new PersistentData(this);

        final TextView log_textview = (TextView) findViewById(R.id.log);
        error_manager = new ErrorManager(log_textview);

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
                run_image_selector();
            }
        });

        Button update_now_button = (Button) findViewById(R.id.update_now);
        update_now_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UpdateService.update(MainActivity.this);
            }
        });

        Button clear_log_button = (Button) findViewById(R.id.clear_log);
        clear_log_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                log_textview.setText("");
            }
        });

        UpdateServiceScheduler.schedule(this);
    }

    static final int PICK_IMAGES_REQUEST = 1;

    public void run_image_selector() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        if (permissionGranted) {
            open_image_selector();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 200: {
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    open_image_selector();
                }
            }
        }
    }

    private void open_image_selector() {
        ArrayList<String> active_list = MainActivity.get_persistent_data().get_active_images();

        MultiImageSelector.create()
                .showCamera(true)
                .count(100)
                .origin(active_list)
                .start(this, PICK_IMAGES_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGES_REQUEST) {
            if (resultCode == RESULT_OK) {
                ArrayList<String> active_list = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                MainActivity.get_persistent_data().set_active_images(active_list);
            }
        }
    }
}
