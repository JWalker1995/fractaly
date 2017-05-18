package net.walker9.fractaly;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.Context;
import android.os.PersistableBundle;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by joel on 5/16/17.
 */

public class RenderService extends JobService {
    private static final String TAG = "RenderService";

    static void request(Context context, int id) {
        if (id < render_tasks.size() && render_tasks.get(id) != null) {
            return;
        }

        ComponentName serviceName = new ComponentName(context, RenderService.class);

        JobInfo jobInfo = new JobInfo.Builder(id, serviceName)
                .setPersisted(true)
                .setRequiresCharging(true)
                .build();

        JobScheduler scheduler = (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
        int result = scheduler.schedule(jobInfo);
        if (result == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Scheduled job " + id);
        } else {
            Log.e(TAG, "Failed to schedule job " + id);
        }
    }

    private static ArrayList<RenderTask> render_tasks = new ArrayList<RenderTask>();

    @Override
    public boolean onStartJob(JobParameters params) {
        while (render_tasks.size() <= params.getJobId()) {
            render_tasks.add(null);
        }
        if (render_tasks.get(params.getJobId()) != null) {
            Log.e(TAG, "Trying to restart job " + params.getJobId());
            return false;
        }

        Log.d(TAG, "Starting job " + params.getJobId() + "...");

        RenderTask task = new RenderTask(this);
        render_tasks.set(params.getJobId(), task);

        task.execute(params);
        return true;
    }


    @Override
    public boolean onStopJob(JobParameters params) {
        RenderTask task = render_tasks.get(params.getJobId());
        if (task == null) {
            Log.e(TAG, "Trying to stop unstarted job " + params.getJobId());
            return false;
        }

        render_tasks.set(params.getJobId(), null);

        Log.d(TAG, "Stopping job " + params.getJobId() + "...");
        return task.stopJob(params);
    }

    public void call_finish(JobParameters params, boolean needsReschedule) {
        Log.d(TAG, "Finishing job " + params.getJobId() + " with needsReschedule=" + needsReschedule);
        jobFinished(params, needsReschedule);
    }
}
