package net.walker9.fractaly;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import net.walker9.fractaly.shader.FractalGalaxy;
import net.walker9.fractaly.shader.Shader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by joel on 5/17/17.
 */

public class RenderTask extends AsyncTask<JobParameters, Void, JobParameters> {
    private JobService job_service;

    public RenderTask(JobService job_service) {
        this.job_service = job_service;
    }


    @Override
    protected JobParameters doInBackground(JobParameters... params) {
        assert(params.length == 1);
        push_render(params[0]);
        return params[0];
    }

    @Override
    protected void onPostExecute(JobParameters params) {
        job_service.jobFinished(params, false);
    }

    @Override
    protected void onCancelled(JobParameters params) {
        job_service.jobFinished(params, true);
    }

    public boolean stopJob(JobParameters params) {
        return !cancel(true);
    }


    private void push_render(JobParameters params) {
        File file = new File(job_service.getFilesDir(), "render_" + params.getJobId() + ".png");

        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return;
        }

        DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
        Bitmap bmp = render(dm.widthPixels, dm.heightPixels);
        if (bmp == null) {
            return;
        }

        bmp.compress(Bitmap.CompressFormat.PNG, 100, out);

        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap render(int size_x, int size_y) {
        Shader s = new FractalGalaxy((float) size_x, (float) size_y);

        float frag[] = new float[4];

        Bitmap bmp = Bitmap.createBitmap(size_x, size_y, Bitmap.Config.ARGB_8888);

        for (int i = 0; i < size_x; i++) {
            if (isCancelled()) {
                return null;
            }

            for (int j = 0; j < size_y; j++) {
                s.main(frag, (float)i + 0.5f, (float)j + 0.5f);
                bmp.setPixel(i, j, Color.rgb(color_comp(frag[0]), color_comp(frag[1]), color_comp(frag[2])));
            }
        }

        return bmp;
    }

    private static int color_comp(float val) {
        return Math.max(0, Math.min((int) (val * 256.0f), 255));
    }
}
