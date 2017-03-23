package net.walker9.fractaly;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by joel on 2/27/17.
 */
public class UpdateService extends IntentService {
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String key = MainActivity.get_persistent_data().get_poll_key();
        if (key != null) {
            try_download_background(key);
        } else if (needs_to_request_background()) {
            String image_path = get_input_image_path();

            try {
                try_request_background(image_path);
            } catch (FileNotFoundException e) {
                MainActivity.get_error_manager().error(e);
                return;
            }
        }
    }

    private static boolean needs_to_request_background() {
        return System.currentTimeMillis() > MainActivity.get_persistent_data().get_next_request_timestamp();
    }

    private static String get_input_image_path() {
        ArrayList<String> active_images = MainActivity.get_persistent_data().get_active_images();

        Random random = new Random();
        int index = random.nextInt(active_images.size());
        return active_images.get(index);
    }

    private static void update_next_request_timestamp() {
        int request_interval_hours = 24;
        long inc = request_interval_hours * 60 * 60 * 1000;

        PersistentData pd = MainActivity.get_persistent_data();
        long timestamp = pd.get_next_request_timestamp();
        do {
            timestamp += inc;
        } while (timestamp < System.currentTimeMillis());

        pd.set_next_request_timestamp(timestamp);
    }

    private void try_request_background(String image_path) throws FileNotFoundException {
        InputStream req_data = new FileInputStream(image_path);
        InputStream reply_data;
        try {
            reply_data = HttpInterface.request("request", req_data);
            update_next_request_timestamp();
        } catch (HttpException e) {
            MainActivity.get_error_manager().error(e);
        }
    }

    private void try_download_background(String key) {
        JSONObject req_data = new JSONObject();
        try {
            req_data.put("key", key);
        } catch (JSONException e) {
            MainActivity.get_error_manager().error(e);
            return;
        }

        InputStream reply_data;
        try {
            reply_data = HttpInterface.request("poll", req_data);
        } catch (HttpException e) {
            MainActivity.get_error_manager().error(e);
            return;
        }

        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setStream(reply_data);
        } catch (IOException e) {
            MainActivity.get_error_manager().error(e);
        }
    }
}
