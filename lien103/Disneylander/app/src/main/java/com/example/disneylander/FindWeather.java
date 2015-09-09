package com.example.disneylander;

import java.text.DecimalFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

//This is an async task to get the weather in the background

public class FindWeather extends AsyncTask<Void, Void, Void> {
	ProgressDialog progressDialog;
	private Context _ctx;
	public String disneyWeather;
	public JSONObject currentWeather;

	public TextView currentWeatherTextView;
	
	public FindWeather(Context ctx, TextView tv) {
		_ctx = ctx;
		currentWeatherTextView = tv;
	}

	@Override
	protected void onPreExecute() {

		progressDialog = ProgressDialog.show(_ctx, "Loading...", "Please wait...");
	};

	@Override
	protected Void doInBackground(Void... params) {

		try {
			currentWeather = RemoteFetch.getJSON(_ctx, "anaheim");
			int i = 0;
			int j = i+1;
			
			String tempWeather = "";
			
			JSONObject main = currentWeather.getJSONObject("main");
			tempWeather = String.format("%.2f", main.getDouble("temp"));//get current temp
			
			disneyWeather = tempWeather;
			Log.i("weather", tempWeather);
			
			

		} catch (Exception e) {
			
		}

		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		super.onPostExecute(result);
		progressDialog.dismiss();
		float celsFloat = Float.parseFloat(disneyWeather);
		float farFloat = (float) (1.8 * celsFloat + 32);//convert to farenheight
		
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(2);
		disneyWeather = df.format(farFloat);
		
	//	disneyWeather = Float.toString(farFloat);
		
		currentWeatherTextView.setText(disneyWeather + "¡");

	}
}