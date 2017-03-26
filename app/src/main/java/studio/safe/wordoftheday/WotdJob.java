package studio.safe.wordoftheday;

import android.support.annotation.NonNull;
import android.util.Log;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;

import org.joda.time.DateTime;
import org.joda.time.Duration;

import java.util.concurrent.TimeUnit;

/**
 * Class WotdJob.
 *
 * @author ansidev
 */

class WotdJob extends Job {

    static final String WOTD_JOB = "wotd_job";

    @Override
    @NonNull
    protected Result onRunJob(Params params) {
        Log.d(WOTD_JOB, "Job is running");
        boolean isPeriodic = params.isPeriodic();
        boolean isSuccess = new WotdJobRunner(getContext()).sync(isPeriodic);
        if (isSuccess) {
            scheduleDailyJob();
        }
        return isSuccess ? Result.SUCCESS : Result.FAILURE;
    }

    /**
     * Schedule periodic Wotd job
     *
     * @param jobType          Wotd Job type
     * @param durationInMinute duration in minute
     */
    private static void schedulePeriodicJob(WotdJobType jobType, long durationInMinute) {
        if (durationInMinute < 15) {
            durationInMinute = 15;
        }
        JobRequest jobRequest = new JobRequest.Builder(jobType.toString())
                .setPeriodic(TimeUnit.MINUTES.toMillis(durationInMinute), TimeUnit.MINUTES.toMillis(5))
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setRequirementsEnforced(true)
                .build();
        WotdJobManager.getInstance().scheduleJob(jobRequest);
        Log.d(jobRequest.getTag(), "Job will run in " + durationInMinute + " minutes");
    }

    /**
     * Schedule daily Wotd job
     */
    static void scheduleDailyJob() {
        DateTime now = DateTime.now();
        DateTime nextTime = now.plusDays(1).withTime(6, 0, 0, 0);
        Duration duration = new Duration(now, nextTime);
        schedulePeriodicJob(WotdJobType.WOTD_DAILY_JOB, duration.getStandardMinutes());
    }

    /**
     * Schedule retry job
     */
    static void scheduleRetryJob() {
        schedulePeriodicJob(WotdJobType.WOTD_RETRY_JOB, 15);
    }
}
