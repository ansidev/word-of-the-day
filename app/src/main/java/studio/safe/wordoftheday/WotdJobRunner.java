package studio.safe.wordoftheday;

import android.content.Context;
import android.os.Looper;
import android.os.NetworkOnMainThreadException;
import android.os.SystemClock;
import android.support.annotation.WorkerThread;

import java.text.SimpleDateFormat;
import java.util.Locale;

import static studio.safe.wordoftheday.WotdActivity.WOTD_NOTIFICATION_ID;
import static studio.safe.wordoftheday.WotdUtils.NOTIFICATION_BODY_TEMPLATE;

/**
 * Class WotdJobRunner
 *
 * @author ansidev
 */
class WotdJobRunner {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault());

    private final Context mContext;

    private static Wotd wordOfTheDay;

    WotdJobRunner(Context context) {
        mContext = context;
    }

    Wotd getWordOfTheDay() {
        if (wordOfTheDay != null) {
            return wordOfTheDay;
        }
        return new Wotd();
    }

    /**
     * Retrieving new word
     * @param isDaily is daily job
     * @return new word of the day
     */
    @WorkerThread
    boolean sync(boolean isDaily) {
        // do something fancy

        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new NetworkOnMainThreadException();
        }

        SystemClock.sleep(1_000);
        WotdUtils wotdUtil = new WotdUtils(mContext);
        wordOfTheDay = WotdUtils.getWordOfTheDay();
        String word = wordOfTheDay.getWord();
        String meaning = wordOfTheDay.getMeaning();
        boolean isSuccess = !word.isEmpty() && !meaning.isEmpty();
        if (!isSuccess) {
            wotdUtil.createNotification(
                    WOTD_NOTIFICATION_ID,
                    R.mipmap.ic_launcher,
                    mContext.getString(R.string.app_name),
                    "Could not getting today word"
            );
            WotdJob.scheduleRetryJob();
        }
        if (isSuccess) {
            wotdUtil.createNotification(
                    WOTD_NOTIFICATION_ID,
                    R.mipmap.ic_launcher,
                    mContext.getString(R.string.app_name),
                    String.format(NOTIFICATION_BODY_TEMPLATE, word, meaning)
            );
        }
        if (isDaily) {
            WotdJob.scheduleDailyJob();
        }
        return isSuccess;
    }
}
