package com.example.afjp;

import java.io.InputStream;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

public class MyWidgetProvider extends AppWidgetProvider {
	SharedPreferences prefs;
	private InputStream input;
	public static String WIDGET_BUTTON = "com.example.dv106lab3.WIDGET_BUTTON";
	int[] allWidgetIds;

	@Override
	  public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

	    //Log.w(LOG, "onUpdate method called");
	    // Get all ids
	    ComponentName thisWidget = new ComponentName(context, MyWidgetProvider.class);
	    allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);

	    // Build the intent to call the service
	    Intent intent = new Intent(context.getApplicationContext(), UpdateWidgetService.class);
	    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

	    // Update the widgets via the service
	    context.startService(intent);
	  }

	@Override
	public void onReceive(Context context, Intent intent) {
		if (WIDGET_BUTTON.equals(intent.getAction())) {
		}
		super.onReceive(context, intent);
		onUpdate(context, AppWidgetManager.getInstance(context), allWidgetIds);
	}
	
}