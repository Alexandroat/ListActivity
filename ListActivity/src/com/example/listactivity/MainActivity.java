package com.example.listactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONObject;

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
	public final static String TAG = MainActivity.class.getSimpleName();
	private TextView textview;
	private ArrayAdapter<String> adapter;
	private GetBlogPosTask getBlogPosTask = new GetBlogPosTask();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview = (TextView) findViewById(R.id.textView1);
		// mBlogPostTitles =
		// getResources().getStringArray(R.array.android_names);

		
		 adapter = new ArrayAdapter<String>(this,
		 android.R.layout.simple_list_item_1, mBlogPostTitles);
		 setListAdapter(adapter);
		 
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
	
	class GetBlogPosTask extends AsyncTask<Object, Object, Object> {
		public final static int NUMBER_OF_POSTS = 20;
		
		final static String URL_JSON = "http://itvocationalteacher.blogspot.com/feeds/posts/default?alt=json";

		// public GetBlogPosTask()

		@Override
		protected Object doInBackground (Object... params) {
			int responseCode = -1;
			URL blogFeedUrl = null;

			try {
				blogFeedUrl = new URL(URL_JSON);

				HttpURLConnection connection = (HttpURLConnection) blogFeedUrl
						.openConnection();
				JSONObject jsonObject;
				JSONObject jsonFeed;
				JSONArray jsonAentry;
				JSONObject mBlogData;
				
				//String test = "";
				
				if (connection != null) {
					connection.connect();
				}
				responseCode = connection.getResponseCode();

				if (connection.HTTP_OK == 200) {

					InputStream inputStream = connection.getInputStream();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream));
					StringBuilder stringBuilder = new StringBuilder();
					String lines;
					String text = "";
					
					while ((lines = bufferedReader.readLine()) != null) {

						stringBuilder = stringBuilder.append(lines);
						text = stringBuilder.toString();

					}
					
					//Log.i(TAG, "Json: " + text);
					jsonObject = new JSONObject(text);
					//jsonObject.getString("version");
					jsonFeed = jsonObject.getJSONObject("feed");
					jsonAentry = jsonFeed.getJSONArray("entry");
					
					for (int i = 0; i < jsonAentry.length(); i++) {
						JSONObject jsonPost = (JSONObject) jsonAentry.get(i);
	                    JSONObject jsonTitle = (JSONObject) jsonPost.get("title");
	                    mBlogPostTitles = new String[jsonTitle.length()];
	                    //String title = Html.escapeHtml(jsonTitle.getString("$t"));
	                     mBlogPostTitles[i] = jsonTitle.getString("$t").toString();
	                   //Log.i(TAG, "Titulosss: " + test);
					}
					
					
				} else {
					Log.e(TAG, "Unable to connect : " + responseCode);
				}

				Log.i(TAG, "code : " + responseCode);

			} catch (MalformedURLException e) {

				Log.e(TAG, "exception caught : ", e);

			} catch (Exception e) {
				System.err.println("Error");
			}
			return mBlogPostTitles;
		}

	}

}


