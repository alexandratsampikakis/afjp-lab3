/**
 * VaxjoWeather.java
 * Created: May 9, 2010
 * Jonas Lundberg, LnU
 */

package com.example.afjp;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;

/**
 * This is a first prototype for a weather app. It is currently 
 * only downloading weather data for Växjö. 
 * 
 * This activity downloads weather data and constructs a WeatherReport,
 * a data structure containing weather data for a number of periods ahead.
 * 
 * The WeatherHandler is a SAX parser for the weather reports 
 * (forecast.xml) produced by www.yr.no. The handler constructs
 * a WeatherReport containing meta data for a given location
 * (e.g. city, country, last updated, next update) and a sequence 
 * of WeatherForecasts.
 * Each WeatherForecast represents a forecast (weather, rain, wind, etc)
 * for a given time period.
 * 
 * The next task is to construct a list based GUI where each row 
 * displays the weather data for a single period.
 * 
 *  
 * @author jlnmsi
 *
 */

public class VaxjoWeather extends ListActivity {
	private InputStream input;
	private WeatherReport report = null;

	List<WeatherForecast> list;
	ListView listview;
	WeatherAdapter adapter;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vaxjoweather);
        listview = (ListView) findViewById(android.R.id.list);
		
	    list = new ArrayList<WeatherForecast>();
	    list.clear();
        
        adapter = new WeatherAdapter(this, list);
        listview.setAdapter(adapter);
       
        try {
        	URL url = new URL("http://www.yr.no/sted/Sverige/Kronoberg/V%E4xj%F6/forecast.xml");
        	AsyncTask task = new WeatherRetriever().execute(url);
        } catch (IOException ioe ) {
    		ioe.printStackTrace();
    	}
        
    }
    
    private void PrintReportToConsole() {
    	if (this.report != null) {
        	/* Print location meta data */ 
        	//System.out.println(report);
        	
        	/* Print forecasts */
    		int count = 0;
    		for (WeatherForecast forecast : report) {
    			count++;
    			//System.out.println("Forecast "+count);
    			//System.out.println( forecast.toString() );
    			
    			// Adds the forecast to the list.
    			list.add(forecast);
    		}
    		adapter.notifyDataSetChanged();
    	}
    	else {
    		System.out.println("Weather report has not been loaded.");
    	}
    	
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
    		PrintReportToConsole();
    	}
    }
}