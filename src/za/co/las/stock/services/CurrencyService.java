package za.co.las.stock.services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

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
			
			int rateIdx = output.indexOf("\"rate\": ");
			int endIdx = output.indexOf(",", rateIdx);
			
			String rate = output.substring(rateIdx + 8, endIdx);
	 
			conn.disconnect();
			return Double.parseDouble(rate);
		}
		catch (Exception e) {
			return 1.0;
		}
	}

}
