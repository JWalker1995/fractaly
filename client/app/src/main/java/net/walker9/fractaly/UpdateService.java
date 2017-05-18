package net.walker9.fractaly;

import android.app.IntentService;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;

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
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (MainActivity.get_persistent_data().get_enabled()) {
            update(this);
        }
    }

    public static void update(Context context) {
        while (true) {
            if (Math.random() < 0.5) {
                if (use_render(context)) {break;}
            } else {
                if (use_image(context)) {break;}
            }
        }
    }

    private static boolean use_render(Context context) {
        for (int i = 0; i < 4; i++) {
            File file = new File(context.getFilesDir(), "render_" + i + ".png");
            if (!file.exists()) {
                RenderService.request(context, i);
            } else {
                InputStream input_stream = null;
                try {
                    input_stream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    MainActivity.get_error_manager().error(e);
                    return false;
                }

                set_wallpaper(context, input_stream);

                file.delete();
                RenderService.request(context, i);
                return true;
            }
        }

        return false;
    }

    private static boolean use_image(Context context) {
        ArrayList<String> active_images = MainActivity.get_persistent_data().get_active_images();
        Random random = new Random();
        int index = random.nextInt(active_images.size());
        String path = active_images.get(index);

        InputStream input_stream = null;
        try {
            input_stream = new FileInputStream(path);
        } catch (FileNotFoundException e) {
            MainActivity.get_error_manager().error(e);
            return false;
        }
        set_wallpaper(context, input_stream);

        return true;
    }

    private static void set_wallpaper(Context context, InputStream input_stream) {
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
        try {
            myWallpaperManager.setStream(input_stream);
        } catch (IOException e) {
            MainActivity.get_error_manager().error(e);
        }
    }

    public static void prepare_renders(Context context) {
        for (int i = 0; i < 4; i++) {
            File file = new File(context.getFilesDir(), "render_" + i + ".png");
            if (!file.exists()) {
                RenderService.request(context, i);
            }
        }
    }
}
