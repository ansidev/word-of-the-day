package studio.safe.wordoftheday;

import com.evernote.android.job.Job;
import com.evernote.android.job.JobCreator;

/**
 * Class WotdJobCreator
 *
 * @author ansidev
 */

class WotdJobCreator implements JobCreator {

    @Override
    public Job create(String tag) {
        switch (tag) {
            case WotdJob.WOTD_JOB:
                return new WotdJob();
            default:
                return null;
        }
    }
}
