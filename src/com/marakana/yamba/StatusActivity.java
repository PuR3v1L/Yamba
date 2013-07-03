package com.marakana.yamba;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import winterwell.jtwitter.Twitter;


public class StatusActivity extends Activity implements OnClickListener { //


	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;

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

		twitter = new Twitter("student", "password");
		twitter.setAPIRootUrl("http://yamba.marakana.com/api");
		Log.d(TAG, "Connection Object created");     // .d() - debug    .e() - error   .w() - warning     .i() - info   .wtf() - ...
	}

	// Called when button is clicked
	public void onClick(View v) {
		//noinspection ConstantConditions
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);    // status goes in doInBackground()
		//noinspection ConstantConditions
		Log.d(TAG, "onClicked");
	}

	// Asychronous Task instead of thread
	class PostToTwitter extends AsyncTask<String, Integer, String> {

		/* Called to initiate the background activity.
		The argument statuses comes from PostToTwitter().execute(status); */
		@Override
		protected String doInBackground(String... statuses) {
			try {
				Twitter.Status status = twitter.updateStatus(statuses[0]);
				return status.text;
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
