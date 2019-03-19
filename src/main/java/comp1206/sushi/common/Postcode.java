package comp1206.sushi.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;
import org.json.*;

import comp1206.sushi.common.Postcode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Postcode extends Model {

	private String name;
	private Map<String,Double> latLong;
	private Number distance;
	private URL postcodeURL;
	private Double lat;
	private Double lon;
	private static final Double EarthRadius = 6371e3; //metres

	public Postcode(String code) {
		this.name = code;
		calculateLatLong();
		this.distance = Integer.valueOf(0);
	}

	public Postcode(String code, Restaurant restaurant) {
		this.name = code;
		calculateLatLong();
		calculateDistance(restaurant);
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Number getDistance() {
		return this.distance;
	}

	public Map<String,Double> getLatLong() throws IOException {
		return this.latLong;
	}

	/*
	 * https://www.movable-type.co.uk/scripts/latlong.html
	 */
	protected void calculateDistance(Restaurant restaurant) {
		Map<String,Double> restaurantLatLong = new HashMap<String, Double>();
		parseURL(restaurant.getLocation().getName(), restaurantLatLong);
		
		Double restaurantLat = Math.toRadians(restaurantLatLong.get("lat"));
		Double postcodeLat = Math.toRadians(latLong.get("lat"));
		Double changeInLat = Math.toRadians(latLong.get("lat")-restaurantLatLong.get("lat"));
		Double changeInLon = Math.toRadians(latLong.get("lon")-restaurantLatLong.get("lon"));
		
		Double a = (Math.sin(changeInLat/2)*Math.sin(changeInLat/2)) + 
				((Math.cos(restaurantLat)*Math.cos(restaurantLat))*(Math.sin(changeInLon/2)*Math.sin(changeInLon/2)));
		Double c = 2*Math.atan2(Math.sqrt(a),Math.sqrt(1-a));
		distance = EarthRadius*c;
				
	}

	protected void calculateLatLong() {
		try {
			
	        latLong = new HashMap<String,Double>();
			parseURL(name, latLong);
			
	    	} catch(Exception e) {
	    		e.getStackTrace();
	    	}
		this.distance = new Integer(0);
	}
	
	/*
	 * https://docs.oracle.com/javase/tutorial/networking/urls/readingURL.html
	 */
	public void parseURL(String name, Map<String,Double> latLong)  {
		try {
		StringBuilder address = new StringBuilder("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=");
        String newName = name.replaceAll("\\s+","");
        address.append(newName);
        postcodeURL = new URL(address.toString());
        BufferedReader in = new BufferedReader(new InputStreamReader(postcodeURL.openStream()));
        String inputLine = in.readLine();
        String[] stringArray = inputLine.split(":|,|}");
        String latString = stringArray[3].replaceAll("\"","");
		String lonString = stringArray[5].replaceAll("\"","");
        latLong.put("lat", Double.parseDouble(latString));
		latLong.put("lon", Double.parseDouble(lonString));
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	public URL getURL() {
		return postcodeURL;
	}
	


}
