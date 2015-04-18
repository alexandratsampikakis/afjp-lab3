package com.example.afjp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ListView;
import android.widget.RemoteViews;

public class UpdateWidgetService extends Service {
	private static final String LOG = "com.example.dv106lab3";
	
	SharedPreferences prefs;
	private InputStream input;
	private WeatherReport report = null;
	List<WeatherForecast> list;
	ListView listview;
	WeatherAdapter adapter;
	RemoteViews remoteViews;
	int weatherCode;
	String weatherName;
	int weatherTemp;
	String weatherCity;
	String id;

	public static String WIDGET_BUTTON = "com.example.dv106lab3.WIDGET_BUTTON";
	AppWidgetManager appWidgetManager;
	int[] appWidgetIds;
	int[] allWidgetIds2;
	int wid;

	@Override
	public void onStart(Intent intent, int startId) {
		Log.i(LOG, "Called");
		appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());		
		appWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
		list = new ArrayList<WeatherForecast>();
		list.clear();

		adapter = new WeatherAdapter(this, list);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);

		// Get all ids
		ComponentName thisWidget = new ComponentName(this, MyWidgetProvider.class);

		allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);
		
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int widgetId : allWidgetIds) {
			
			try {
				id = prefs.getString(widgetId+"id", "mm");				
				if(id.equals("Lule�")) {
					URL url = new URL("http://www.yr.no/place/Sweden/Norrbotten/Lule%C3%A5/forecast.xml");
					AsyncTask task = new WeatherRetriever().execute(url);
				} else if(id.equals("�stersund")) {
					URL url = new URL("http://www.yr.no/place/Sweden/J%C3%A4mtland/%C3%96stersund/forecast.xml");
					AsyncTask task = new WeatherRetriever().execute(url);
				} else if(id.equals("Stockholm")) {
					URL url = new URL("http://www.yr.no/sted/Sverige/Stockholm/Stockholm/forecast.xml");
					AsyncTask task = new WeatherRetriever().execute(url);
				} else if(id.equals("�rnsk�ldsvik")) {
					URL url = new URL("http://www.yr.no/sted/Sverige/V%C3%A4sternorrland/%C3%96rnsk%C3%B6ldsvik/forecast.xml");
					AsyncTask task = new WeatherRetriever().execute(url);
				}
				
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}

			// The update button.
			Intent buttonIntent = new Intent(WIDGET_BUTTON);
			PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			// RemoteViews remoteViews;
			remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_layout);

			weatherCode = prefs.getInt(widgetId+"wcode", 0);
			weatherName = prefs.getString(widgetId+"wname", "Weather");
			weatherTemp = prefs.getInt(widgetId+"wtemp", 9999);
			weatherCity = id;
			wid = widgetId;
			PrintReportToConsole(widgetId);

			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			remoteViews.setOnClickPendingIntent(R.id.buttonUpdate, pendingIntent);
			
			// When clicking the widget.
			Intent widgetIdi = new Intent(this.getApplicationContext(), VaxjoWeather.class);
			widgetIdi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			widgetIdi.setAction("com.example.dv106lab3");
			
			PendingIntent widgetIdpi = PendingIntent.getBroadcast(this, widgetId, widgetIdi, PendingIntent.FLAG_UPDATE_CURRENT);
			remoteViews.setOnClickPendingIntent(R.id.layoutWidget, widgetIdpi);
			
			appWidgetManager.updateAppWidget(widgetId, remoteViews);
		}
		stopSelf();
		super.onStart(intent, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	private class WeatherRetriever extends AsyncTask<URL, Void, WeatherReport> {
    	protected WeatherReport doInBackground(URL... urls) {
    		try {
    			return WeatherHandler.getWeatherReport(urls[0]);
    		} catch (Exception e) {
    			throw new RuntimeException(e);
    		} 
    	}

    	protected void onProgressUpdate(Void... progress) {

    	}

    	protected void onPostExecute(WeatherReport result) {
    		report = result;
    		PrintReportToConsole(wid);
    		setTextToWidget(wid);
    	}
    }
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		SharedPreferences.Editor edit = prefs.edit();
		prefs.edit().remove(id);
		super.onDestroy();
	}
	
	private void PrintReportToConsole(int widgetId) {
    	if (this.report != null) {
        	/* Print location meta data */ 
        	System.out.println(report);
        	
        	/* Print forecasts */
    		int count = 0;
    		for (WeatherForecast forecast : report) {
    			count++;
    			System.out.println("Forecast "+count);
    			System.out.println( forecast.toString() );
    			
    			// Adds the forecast to the list.
    			
    			list.add(forecast);
    			if(count == 1) {
    				weatherName = forecast.getWeatherName().toString();
    				weatherCode = forecast.getWeatherCode();
    				weatherTemp = forecast.getTemp();
    				//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: "+weatherName);
    				//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: kod: "+weatherCode);
    				//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: temp : "+weatherTemp);
    				
    				SharedPreferences.Editor edit = prefs.edit();
    				prefs.edit().putInt(widgetId+"wcode", weatherCode).commit();
    				prefs.edit().putString(widgetId+"wname", weatherName).commit();
    				prefs.edit().putInt(widgetId+"wtemp", weatherTemp).commit();
    			}
    		}
    		adapter.notifyDataSetChanged();
    		
    	}
    	else {
    		System.out.println("Weather report has not been loaded.");
    	}	
    	setTextToWidget(widgetId);
    }
	
	public void setTextToWidget(int widgetId) {
		int number = (new Random().nextInt(100));
		Log.d("Listan", "Size: "+list.size());
		
		weatherName = prefs.getString(widgetId+"wname", weatherName);
		weatherCode = prefs.getInt(widgetId+"wcode", weatherCode);
		weatherTemp = prefs.getInt(widgetId+"wtemp", weatherTemp);
		
		//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: wname : "+weatherName);
		//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: temp 3: "+weatherTemp);
		
		remoteViews.setTextViewText(R.id.update, String.valueOf(number));
		remoteViews.setTextViewText(R.id.label, weatherName);
		remoteViews.setTextViewText(R.id.labelDegrees, "Temp: " + weatherTemp);
		remoteViews.setTextViewText(R.id.lableCity, weatherCity);
		
		//Log.d("Listan", "ssssssssssssssssssssssssssssssssssssssssssss: kod 2 : "+weatherCode);
		switch (weatherCode) {
	        case 1:
	        	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic1);
				break;
		    case 2:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic2);
				break;
		    case 3:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic3);
				break;
		    case 4:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic4);
				break;
		    case 5:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic5);
				break;
		    case 6:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic6);
				break;
		    case 7:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic7);
				break;
		    case 8:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic8);
				break;
		    case 9:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic9);
				break;
		    case 10:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic10);
				break;
		    case 11:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic11);
				break;
		    case 12:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic12);
				break;
		    case 13:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic13);
				break;
		    case 14:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic14);
				break;
		    case 15:
		    	remoteViews.setImageViewResource(R.id.logo, R.drawable.pic15);
	            break;
		}
	}
	
	

}