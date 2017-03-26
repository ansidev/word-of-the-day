package studio.safe.wordoftheday;

import android.app.Application;

import com.evernote.android.job.JobManager;

/**
 * Class WotdApp
 *
 * @author ansidev
 */

public class WotdApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        JobManager.create(this).addJobCreator(new WotdJobCreator());
        WotdJobManager.getInstance();
    }

}
