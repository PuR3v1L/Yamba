package com.marakana.yamba;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;


public class StatusActivity extends BaseActivity implements OnClickListener, TextWatcher {

    private static final String TAG = "StatusActivity";
    SharedPreferences prefs;
    EditText editText;
    Button updateButton;
    Twitter twitter;
    TextView textCount;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);

        // Find views
        editText = (EditText) findViewById(R.id.editText);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(this);

        textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        editText.addTextChangedListener(this);  // TextWatcher call is attached to the editText

//		twitter = new Twitter("student", "password");
//		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
//		Log.d(TAG, "Connection Object created");     // .d() - debug    .e() - error   .w() - warning     .i() - info   .wtf() - ...
    }

    // Called when button is clicked
    public void onClick(View v) {
        //noinspection ConstantConditions
        Button buttonClicked = (Button) v; // Identify the button which was pressed
        Toast.makeText(StatusActivity.this, buttonClicked.getText(), Toast.LENGTH_SHORT).show(); // Toast it's text
        String status = editText.getText().toString();

        // Initialise the object and post to twitter.
        try {
            //getTwitter().setStatus(status) // Won't work due to UI block
            twitter = ((YambaApplication) getApplication()).getTwitter();   // Initialise twitter object
            new PostToTwitter().execute(status);    // status goes in doInBackground()
        } catch (TwitterException e) {
            Log.d(TAG, "Twitter setStatus failed: " + e);
        }

		/*// Old code - Without shared preferences
        new PostToTwitter().execute(status);    // status goes in doInBackground()*/

        //no inspection Constant Conditions
        Log.d(TAG, "onClicked");
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // nothing to do here
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        // nothing to do here
    }

    /* Called after the text in editText has changed. statusText has the text that
     has been written this far. */
    @Override
    public void afterTextChanged(Editable statusText) {
        int count = 140 - statusText.length();
        textCount.setText(Integer.toString(count));
        textCount.setTextColor(Color.GREEN);
        if (count < 0)
            textCount.setTextColor(Color.RED);
        else if (count < 15)
            textCount.setTextColor(Color.YELLOW);
    }


    // Called When User changes the preferences

    // Asychronous Task instead of thread
    class PostToTwitter extends AsyncTask<String, Integer, String> {

        // Called to initiate the background activity.
        // The argument statuses comes from PostToTwitter().execute(status); *//*
        @Override
        protected String doInBackground(String... statuses) {
            try {
                try {
                    Twitter.Status status = twitter.updateStatus(statuses[0]);
                    return status.text;
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                    e.printStackTrace();
                    return "Failed to Post";
                }
                // Twitter.Status status = twitter.updateStatus(statuses[0]);
                // return status.text;
            } catch (Exception e) {
                Log.e(TAG, e.toString());
                e.printStackTrace();
                return "Failed to Post";
            }
        }

        // Called when there's a status to be updated
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            /* No job to do here. If this example were instead a file download, for instance,
            this could report the percentage of completion or amount of data downloaded thus far. */

        }

        /* Called once the background activity has completed.
        The argument result is the whatever the doInBackground returns */
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
        }


    }
}
