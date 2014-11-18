package com.example.listactivity;


import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	protected String mAndroidNames [];
	private TextView textview;
	ArrayAdapter <String> adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textview = (TextView) findViewById(R.id.textView1);
		mAndroidNames = getResources().getStringArray(R.array.android_names);
		adapter= new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,mAndroidNames);
		setListAdapter(adapter);
		 
		if (mAndroidNames !=null){
			textview.setVisibility(textview.INVISIBLE);
		}
		
	}
}
