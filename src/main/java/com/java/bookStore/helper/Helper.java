package com.java.bookStore.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class Helper {
//	Get data from API type Get
	public static String getDataTypeGet(String url) {
		
		StringBuilder responeData = new StringBuilder();
		
		try {
			URL newUrl = new URL(url);
//			HttpUrlConnection
			HttpURLConnection urlConnection = (HttpURLConnection) newUrl.openConnection();
			urlConnection.setRequestMethod("GET");
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
			
			String line = "";
			while ((line = bufferedReader.readLine()) != null) {
				responeData.append(line);
			}
			
			bufferedReader.close();
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return responeData.toString();
	}
	
//	Format Price
	public static String formatPrice(double price) {
	    DecimalFormat decimalFormat = new DecimalFormat("#,###");
	    return decimalFormat.format(price);
	}

}
