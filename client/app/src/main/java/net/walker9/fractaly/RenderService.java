package net.walker9.fractaly;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;

/**
 * Created by joel on 5/16/17.
 */

public class RenderService extends JobService {

    static void request(Context context, int id) {
        ComponentName serviceName = new ComponentName(context, RenderService.class);

        JobInfo jobInfo = new JobInfo.Builder(id, serviceName)
                .setPersisted(true)
                .setRequiresCharging(true)
                .setRequiresDeviceIdle(true)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if (result != JobScheduler.RESULT_SUCCESS) {
            Log.d("JOB_FAIL", "Job failed to schedule");
        }
    }

    private RenderTask render_task = new RenderTask(this);

    @Override
    public boolean onStartJob(JobParameters params) {
        render_task.execute(params);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        return render_task.stopJob(params);
    }
}
