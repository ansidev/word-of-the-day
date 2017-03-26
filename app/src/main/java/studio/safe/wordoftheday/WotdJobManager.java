package studio.safe.wordoftheday;

import android.util.Log;

import com.evernote.android.job.JobManager;
import com.evernote.android.job.JobRequest;

import org.joda.time.DateTime;

/**
 * Class WotdJobManager
 *
 * @author ansidev
 */

class WotdJobManager {
    private static WotdJobManager instance = new WotdJobManager();
    private static long scheduleTime = DateTime.now().getMillis();

    private WotdJobManager() {
    }

    /**
     * Get WotdJobManager instance
     * @return WotdJobManager
     */
    static WotdJobManager getInstance() {
        if (instance == null) {
            instance = new WotdJobManager();
        }
        return instance;
    }

    /**
     * Schedule Wotd job
     * @param jobRequest jobRequest to schedule
     * @return scheduled job id
     */
    int scheduleJob(JobRequest jobRequest) {
        String jobTag = jobRequest.getTag();
        if (JobManager.instance().getAllJobRequestsForTag(jobTag).size() > 0) {
            JobManager.instance().cancelAllForTag(jobTag);
        }
        int jobId = jobRequest.schedule();
        Log.d(WotdJob.WOTD_JOB, String.valueOf(JobManager.instance().getAllJobRequestsForTag(jobTag).size()));
        long scheduledAt = jobRequest.getScheduledAt();
        if (scheduledAt > scheduleTime) {
            scheduleTime = scheduledAt;
            String retryJobTag = WotdJobType.WOTD_RETRY_JOB.name();
            if (WotdJobType.WOTD_DAILY_JOB.name().equals(jobTag)) {
                JobManager.instance().cancelAllForTag(retryJobTag);
            }
        }
        return jobId;
    }

}
