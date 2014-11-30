package com.example.listactivity;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	protected String mBlogPostTitles[];
	private TextView textview;
	ArrayAdapter<String> adapter;
	GetBlogPosTask getBlogPosTask = new GetBlogPosTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview = (TextView) findViewById(R.id.textView1);
		// mBlogPostTitles =
		// getResources().getStringArray(R.array.android_names);

		/*
		 * adapter = new ArrayAdapter<String>(this,
		 * android.R.layout.simple_list_item_1, MBlogPostTiles);
		 * setListAdapter(adapter);
		 */
		if (mBlogPostTitles != null) {
			textview.setVisibility(textview.INVISIBLE);
		}

		if (isNetworkAvailable()) {
			getBlogPosTask.execute();
		} else {
			Toast.makeText(this, R.string.no_connection_message,
					Toast.LENGTH_LONG).show();

		}

	}

	private boolean isNetworkAvailable() {
		boolean isAvailable = false;
		ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected() == true) {
			isAvailable = true;
		}
		return isAvailable;
	}

}

class GetBlogPosTask extends AsyncTask {
	public final static int NUMBER_OF_POSTS = 20;
	public static String TAG = MainActivity.class.getSimpleName();
	final static String URL_JSON = "http://itvocationalteacher.blogspot.com/feeds/posts/default?alt=json";

	// public GetBlogPosTask()

	@Override
	protected Object doInBackground(Object... params) {
		int responseCode = -1;
		URL blogFeedUrl = null;

		try {
			blogFeedUrl = new URL(URL_JSON);

			HttpURLConnection connection = (HttpURLConnection) blogFeedUrl
					.openConnection();
			if (connection != null) {
				connection.connect();
			}
			responseCode = connection.getResponseCode();

			Log.i(TAG, "code : " + responseCode);

		} catch (MalformedURLException e) {

			Log.e(TAG, "exception caught : ", e);

		} catch (Exception e) {
			System.err.println("Error");
		}
		return "Code" + responseCode;
	}

}
