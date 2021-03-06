package za.co.las.stock.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CurrencyService {
	
	public double converstionRateForEUR() {
		try {
			URL url = new URL("http://rate-exchange.appspot.com/currency?from=EUR&to=ZAR");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
	 
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
	 
			String output = org.apache.commons.io.IOUtils.toString(br);

			conn.disconnect();
			return getDoubleRate(output);
		}
		catch (Exception e) {
			return 1.0;
		}
	}
	
	public double converstionRateForEURFromECB() {
		try {
			URL url = new URL("http://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/xml");
	 
			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}
	 
			BufferedReader br = new BufferedReader(new InputStreamReader(
				(conn.getInputStream())));
	 
			String output = org.apache.commons.io.IOUtils.toString(br);

			conn.disconnect();
			return getDoubleRateFromECB(output);
		}
		catch (Exception e) {
			return 1.0;
		}
	}
	
	private double getDoubleRateFromECB(String response) {
		try {
			int startIdx = response.indexOf("<Cube currency='ZAR' rate='");
			int endIdx = response.indexOf("'/>", startIdx);
			String exchanceRate = response.substring(startIdx + 27, endIdx);
		
			return Double.parseDouble(exchanceRate);
		}
		catch (Exception e) {
			e.printStackTrace();
			return 1.0;
		}
	}
	
	private double getDoubleRate(String response) {
		try {
			JSONParser parser = new JSONParser();
			Object obj = parser.parse(response);
			JSONObject jsonObj = (JSONObject)obj;
			return (Double)jsonObj.get("rate");
			//return Double.parseDouble(rate);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return 1.0;
	}
}
