package co.grandcircus.movies.rest;

import java.io.BufferedReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import co.grandcircus.movies.model.Sun;

/**
 * Responsibility: Shows Sunrise & Sunset Time for a particular location
 */
@Service
public class SunService {
	
	private final static String GRAND_CIRCUS_LAT = "42.335993";
	private final static String GRAND_CIRCUS_LON = "-83.049806";

	public Sun getCurrentSunStatsGrandCircus() {
		return getCurrentSunStats(GRAND_CIRCUS_LAT, GRAND_CIRCUS_LON);
	}
	
	public Sun getCurrentSunStats(String lat, String lon) {
		String url = "http://api.sunrise-sunset.org/json?lat="+lat+ "&lng=" +lon;

		// Use HTTP GET with the above URL
		try (BufferedReader reader = HttpHelper.doGet(url)) { // try with resources will auto close the reader
			if (reader == null) {
				throw new RuntimeException("Not found: " + url);
			}
			
			// parse the HTTP response body to JSON
			JsonElement root = new JsonParser().parse(reader);
			// pick the "results" key from the root JSON object.
			JsonObject results = root.getAsJsonObject().get("results").getAsJsonObject();
			Sun sun = new Sun();
			
			sun.setSunrise(results.get("sunrise").getAsString());
			
			sun.setSunset(results.get("sunset").getAsString());

			return sun;
		} catch (IOException ex) {
			throw new RuntimeException("Error reading from URL: " + url, ex);
		}
	}

	/*private void setSunrise(String asString) {
		// TODO Auto-generated method stub
		
	}

	private void setSunset(String asString) {
		// TODO Auto-generated method stub
		
	}*/

}