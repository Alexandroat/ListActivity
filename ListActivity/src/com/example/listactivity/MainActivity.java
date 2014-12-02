package com.example.listactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends ListActivity {
	protected String mBlogPostTitles[];
	public final static String TAG = MainActivity.class.getSimpleName();
	private TextView textview;
	// private ArrayAdapter<String> adapter;
	private GetBlogPosTask getBlogPosTask = new GetBlogPosTask();
	public final static int NUMBER_OF_POSTS = 20;
	private JSONObject mBlogData;
	final static String URL_JSON = "http://itvocationalteacher.blogspot.com/feeds/posts/default?alt=json";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview = (TextView) findViewById(R.id.textView1);

		if (isNetworkAvailable()) {
			getBlogPosTask.execute();
		} else {
			Toast.makeText(this, R.string.no_connection_message,
					Toast.LENGTH_LONG).show();

		}
		if (mBlogPostTitles != null) {
			textview.setVisibility(textview.INVISIBLE);
		}
	}

	public void updateList() {
		if (mBlogData != null) {
			// TODO : Manejar errores
			try {
				// JSONObject jsonObject = new JSONObject(text);
				// jsonObject.getString("version");
				JSONObject jsonFeed = mBlogData.getJSONObject("feed");
				JSONArray jsonAentry = jsonFeed.getJSONArray("entry");
				mBlogPostTitles = new String[jsonAentry.length()];

				for (int i = 0; i < jsonAentry.length(); i++) {
					JSONObject jsonPost = (JSONObject) jsonAentry.get(i);
					JSONObject jsonTitle = (JSONObject) jsonPost.get("title");

					mBlogPostTitles[i] = Html.fromHtml(
							jsonTitle.getString("$t")).toString();
				
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							this,
							android.R.layout.simple_expandable_list_item_1,
							mBlogPostTitles);
					setListAdapter(adapter);
					findViewById(R.id.textView1).setVisibility(View.INVISIBLE);
				}

			} catch (JSONException e) {
				Log.e(TAG, "exception caught:", e);
			}
		} else {
			// Log.e(TAG, mBlogData.toString());
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

	class GetBlogPosTask extends AsyncTask<Object, Void, JSONObject> {

		// public GetBlogPosTask()

		@Override
		protected JSONObject doInBackground(Object[] params) {
			int responseCode = -1;
			URL blogFeedUrl = null;
			JSONObject jsonResponse = null;

			try {
				blogFeedUrl = new URL(URL_JSON);

				HttpURLConnection connection = (HttpURLConnection) blogFeedUrl
						.openConnection();

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
						jsonResponse = new JSONObject(text);

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
			return jsonResponse;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			mBlogData = result;
			updateList();
		}

	}

}
