package studio.safe.wordoftheday;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by ansidev on Mar 18, 2017.
 */

class WotdUtils {

    final static String EMPTY = "";
    private final static String WOTD_ROOT_PATH = "dictionary.cambridge.org";
    private final static String WOTD_SCHEME = "http";
    private final static Uri.Builder wotdUriBuilder = new Uri.Builder()
            .scheme(WOTD_SCHEME).authority(WOTD_ROOT_PATH)
            .appendPath("dictionary")
            .appendPath("english");

    private final static String WOTD_ERROR = "WOTD_ERROR";
    private final static String WOTD_URL = wotdUriBuilder.build().toString();
    private final static String WOTD_HTML_CSS_CLASS = ".wotd-hw";
    private final static String WOTD_LINK_CSS_CLASS = ".with-icons__content > a";

    private final Context context;

    final static String NOTIFICATION_BODY_TEMPLATE = "%s: %s";

    WotdUtils(Context context) {
        this.context = context;
    }

    /**
     * Get today English word from Cambridge
     *
     * @return an English word
     */
    static Wotd getWordOfTheDay() {
        Wotd wordOfTheDay = new Wotd();
        try {
            Connection connection = Jsoup.connect(WOTD_URL);
            Document doc = connection.get();
            Elements wotdElement = doc.select(WOTD_HTML_CSS_CLASS);

            String word = wotdElement.text();
            String meaning = wotdElement.next().text();
            String url = doc.select(WOTD_LINK_CSS_CLASS).attr("href");

            wordOfTheDay.setWord(word);
            wordOfTheDay.setMeaning(meaning);
            wordOfTheDay.setUrl(url);
        } catch (IOException e) {
            Log.d(WOTD_ERROR, "Cannot get word of the day");
            e.printStackTrace();
        }
        return wordOfTheDay;
    }

    /**
     * Create a notification
     *
     * @param notificationId Notification ID
     * @param iconResourceId Icon resource ID
     * @param title          Notification title
     * @param body           notification body
     */
    void createNotification(int notificationId, int iconResourceId, String title, String body) {
        Intent resultIntent;
        if (context instanceof WotdActivity) {
            resultIntent = ((WotdActivity) context).getIntent();
        } else {
            resultIntent = new Intent(context, WotdActivity.class);
        }

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder
                .setAutoCancel(true)
                .setSmallIcon(iconResourceId)
                .setContentTitle(title)
                .setContentText(body)
                .setContentIntent(resultPendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        // notificationId allows you to update the notification later on.
        notificationManager.notify(notificationId, notificationBuilder.build());
    }


}