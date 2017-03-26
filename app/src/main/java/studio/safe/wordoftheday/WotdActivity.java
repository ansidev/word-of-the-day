package studio.safe.wordoftheday;

import android.app.ProgressDialog;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import studio.safe.android.util.NetworkUtils;

/**
 * Class WotdActivity
 *
 * @author ansidev
 */
public class WotdActivity extends AppCompatActivity {
    static final int WOTD_NOTIFICATION_ID = 23152004;

    private NetworkChangeReceiver networkChangeReceiver;
    ProgressDialog mProgressDialog;
    TextView wotdTextView;
    TextView wotdMeaningTextView;
    String word;
    String meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        networkChangeReceiver = new NetworkChangeReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkChangeReceiver, intentFilter);
        WordOfTheDayTask wordOfTheDayTask = new WordOfTheDayTask();
        wordOfTheDayTask.execute();
    }

    @Override
    protected void onDestroy() {
        if (networkChangeReceiver != null) {
            unregisterReceiver(networkChangeReceiver);
            networkChangeReceiver = null;
        }
        super.onDestroy();
    }

    private class WordOfTheDayTask extends AsyncTask<Boolean, Boolean, Boolean> {
        private static final String PROGRESS_DIALOG_MESSAGE = "Getting today word";
        WotdJobRunner wotdJobRunner = new WotdJobRunner(WotdActivity.this);
        Wotd wordOfTheDay;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(WotdActivity.this);
            if (NetworkUtils.isNetworkAvailable(WotdActivity.this)) {
                mProgressDialog.setMessage(PROGRESS_DIALOG_MESSAGE);
            } else {
                mProgressDialog.setMessage("No internet connection. Please connect to a Wifi network or mobile network.");
            }
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        @Override
        protected Boolean doInBackground(Boolean... params) {
            return wotdJobRunner.sync(true);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            refreshView(result);
        }

        private void refreshView(Boolean isSuccess) {
            if (isSuccess) {
                wordOfTheDay = wotdJobRunner.getWordOfTheDay();
                word = wordOfTheDay.getWord();
                meaning = wordOfTheDay.getMeaning();
                wotdTextView = (TextView) findViewById(R.id.wotd);
                wotdMeaningTextView = (TextView) findViewById(R.id.wotd_meaning);

                // Display word of the day and its meaning
                wotdTextView.setText(word);
                wotdMeaningTextView.setText(meaning);
            }

            mProgressDialog.dismiss();
        }
    }
}
