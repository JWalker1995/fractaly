package net.walker9.fractaly;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
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
    private static final String TAG = "UpdateService";

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent");

        if (new PersistentData(this).get_enabled()) {
            update(this);
        }
    }

    public static void update(Context context) {
        for (int i = 0; i < 8; i++) {
            if (Math.random() < 0.5) {
                if (use_render(context)) {break;}
            } else {
                if (use_image(context)) {break;}
            }
        }
    }

    private static boolean use_render(Context context) {
        if (!new PersistentData(context).get_fractal_galaxy_enabled()) {
            return false;
        }

        for (int i = 0; i < 8; i++) {
            File file = new File(context.getFilesDir(), "render_" + i + ".png");
            Log.d(TAG, file.getName() + " exists=" + file.exists());

            if (file.exists()) {
                InputStream input_stream = null;
                try {
                    input_stream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return false;
                }

                Log.d(TAG, "Setting wallpaper to " + file.getAbsolutePath() + "...");
                set_wallpaper(context, input_stream);

                if (!file.delete()) {
                    Log.e(TAG, "Could not delete file " + file.getAbsolutePath());
                }
                RenderService.request(context, i);
                return true;
            } else {
                RenderService.request(context, i);
            }
        }

        Log.i(TAG, "Not using render because there aren't any");
        return false;
    }

    private static boolean use_image(Context context) {
        ArrayList<String> active_images = new PersistentData(context).get_active_images();
        if (active_images.isEmpty()) {
            Log.i(TAG, "Not using image because there aren't any");
            return false;
        }

        Random random = new Random();
        int index = random.nextInt(active_images.size());
        String path = active_images.get(index);

        InputStream input_stream = null;
        try {
            input_stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

        Log.d(TAG, "Setting wallpaper to " + path + "...");
        set_wallpaper(context, input_stream);

        return true;
    }

    private static void set_wallpaper(Context context, InputStream input_stream) {
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
        try {
            myWallpaperManager.setStream(input_stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prepare_renders(Context context) {
        for (int i = 0; i < 8; i++) {
            File file = new File(context.getFilesDir(), "render_" + i + ".png");
            Log.d(TAG, file.getName() + " exists=" + file.exists());

            if (!file.exists()) {
                RenderService.request(context, i);
            }
        }
    }
}
