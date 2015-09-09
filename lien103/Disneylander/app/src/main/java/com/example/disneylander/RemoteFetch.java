package com.example.disneylander;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class RemoteFetch {
	//This class gets the weather from openweather map.  I learned how to do this from this tutorial: http://code.tutsplus.com/tutorials/create-a-weather-app-on-android--cms-21587
	private static final String OPEN_WEATHER_MAP_API = "http://api.openweathermap.org/data/2.5/weather?q=%s&units=metric";

	public static JSONObject getJSON(Context context, String city) {
		try {
			URL url = new URL(String.format(OPEN_WEATHER_MAP_API, city));
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.addRequestProperty("x-api-key",
					context.getString(R.string.open_weather_maps_app_id));

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));

			StringBuffer json = new StringBuffer(1024);
			String tmp = "";
			while ((tmp = reader.readLine()) != null)
				json.append(tmp).append("\n");
			reader.close();

			JSONObject data = new JSONObject(json.toString());

			return data;
		} catch (Exception e) {
			Log.e("weather", "");
			return null;
		}
	}
}