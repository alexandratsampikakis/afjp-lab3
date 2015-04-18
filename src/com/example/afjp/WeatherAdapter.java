package com.example.afjp;

import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
 
/**
 * The custom adapter for the list in the activity VaxjoWeather.
 * @author Alexandra
 *
 */
public class WeatherAdapter extends ArrayAdapter {
	private final Context context;
	private final List<WeatherForecast> values;
	public WeatherAdapter(Context context, List<WeatherForecast> list) {
		super(context, R.layout.weather_list, list);
		this.context = context;
		this.values = list;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.weather_list, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.label);
		TextView textViewDate = (TextView) rowView.findViewById(R.id.labelDate);
		TextView textViewStopDate = (TextView) rowView.findViewById(R.id.labelStopdate);
		TextView textViewDegree = (TextView) rowView.findViewById(R.id.labelDegrees);
		TextView textViewRain = (TextView) rowView.findViewById(R.id.labelRain);
		TextView textViewWind = (TextView) rowView.findViewById(R.id.labelWind);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.logo);
		
		WeatherForecast wf = values.get(position);
		textView.setText(wf.getWeatherName());
		textViewDate.setText("From: " + wf.getStartYYMMDD() + " " + wf.getStartHHMM());
		textViewStopDate.setText("To: " + wf.getEndYYMMDD() + " " + wf.getEndHHMM());
		textViewDegree.setText(String.valueOf("Temp: " + wf.getTemp()));
		textViewRain.setText(String.valueOf("Rain: " + wf.getRain() + "mm/h"));
		textViewWind.setText(String.valueOf("Wind: " + wf.getWindSpeed() + "m/s"));		
		
		// Decides which icon to use depending on the WeatherCode we receive.
		switch (wf.getWeatherCode()) {
	        case 1:
				imageView.setImageResource(R.drawable.pic1);
				break;
		    case 2:
				imageView.setImageResource(R.drawable.pic2);
				break;
		    case 3:
				imageView.setImageResource(R.drawable.pic3);
				break;
		    case 4:
				imageView.setImageResource(R.drawable.pic4);
				break;
		    case 5:
				imageView.setImageResource(R.drawable.pic5);
				break;
		    case 6:
				imageView.setImageResource(R.drawable.pic6);
				break;
		    case 7:
				imageView.setImageResource(R.drawable.pic7);
				break;
		    case 8:
				imageView.setImageResource(R.drawable.pic8);
				break;
		    case 9:
				imageView.setImageResource(R.drawable.pic9);
				break;
		    case 10:
				imageView.setImageResource(R.drawable.pic10);
				break;
		    case 11:
				imageView.setImageResource(R.drawable.pic11);
				break;
		    case 12:
				imageView.setImageResource(R.drawable.pic12);
				break;
		    case 13:
				imageView.setImageResource(R.drawable.pic13);
				break;
		    case 14:
				imageView.setImageResource(R.drawable.pic14);
				break;
		    case 15:
				imageView.setImageResource(R.drawable.pic15);
	            break;
		}
 
		return rowView;
	}
}
