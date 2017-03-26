package studio.safe.wordoftheday;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.evernote.android.job.JobManager;

import studio.safe.android.util.NetworkUtils;

/**
 * Class NetworkChangeReceiver.
 *
 * @author ansidev
 */

public class NetworkChangeReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("WOTD", "Connectivity changed");
        if (NetworkUtils.isNetworkAvailable(context)) {
            if (JobManager.instance().getAllJobsForTag(WotdJobType.WOTD_RETRY_JOB.name()).size() > 0) {
                (new WotdJobRunner(context)).sync(false);
            }
        }
    }
}
