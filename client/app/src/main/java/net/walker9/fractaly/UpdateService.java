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

/**
 * Created by joel on 2/27/17.
 */
public class UpdateService extends IntentService {
    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (int i = 0; i < 4; i++) {
            File file = new File(getFilesDir(), "render_" + i + ".png");
            if (!file.exists()) {
                RenderService.request(this, i);
            } else {
                InputStream input_stream = null;
                try {
                    input_stream = new FileInputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                }

                set_wallpaper(input_stream);

                file.delete();
                RenderService.request(this, i);
                return;
            }
        }
    }

    private void set_wallpaper(InputStream input_stream) {
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            myWallpaperManager.setStream(input_stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void prepare(Context context) {
        for (int i = 0; i < 4; i++) {
            File file = new File(context.getFilesDir(), "render_" + i + ".png");
            if (!file.exists()) {
                RenderService.request(context, i);
            }
        }
    }
}
