package com.example.afjp;

import java.net.URL;
import java.util.ArrayList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class WidgetConfig extends Activity {
	ArrayList<String> list;
	ListView listview;
	ArrayList<Intent> cityList;
	ArrayAdapter<String> adapter;
	SharedPreferences prefs;
	int mAppWidgetId;
	Intent widgetIntent;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		setContentView(R.layout.activity_widget_config);
		
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
		    mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		}
		
		listview = (ListView) findViewById(R.id.listViewConfig);

		list = new ArrayList<String>();
		cityList = new ArrayList<Intent>();
		list.clear();
		
		widgetIntent = new Intent(this, MyWidgetProvider.class);
		
		Intent[] intents = {widgetIntent};
	    String[] names = {"Lule�", "�stersund", "�rnsk�ldsvik", "Stockholm"};
	    
	    for(int i=0; i<names.length; i++) {
 		   list.add(i,names[i]);
 		   cityList.add(i, intents[0]);
 	   	}

		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
		listview.setAdapter(adapter);
		
	    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
	    	
		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          final int position, long id) {
		    	  switch (position) {
			  		case 0:
			  			url="Lule�";
			  			setURL();
			  			return;
			  		case 1:
			  			url="�stersund";
			  			setURL();
			  			return;
			  		case 2:
			  			url="�rnsk�ldsvik";
			  			setURL();
			  			return;
			  		case 3:
			  			url="Stockholm";
			  			setURL();
			  			return;
			  		}
		      }
		});
	}
	
	public void setURL() {
		SharedPreferences.Editor edit = prefs.edit();
		prefs.edit().putString(mAppWidgetId+"id", url).commit();
		//Log.d("mm", ""+url);
		
		widgetIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
		setResult(RESULT_OK, widgetIntent);	
  		finish();
  		
  		Intent i = new Intent(this, UpdateWidgetService.class);
		this.startService(i);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.widget_config, menu);
		return true;
	}

}
