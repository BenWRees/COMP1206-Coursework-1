package comp1206.sushi.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.json.*;
import org.json.*;

import comp1206.sushi.common.Postcode;

public class Postcode extends Model {

	private String name;
	private Map<String,Double> latLong;
	private Number distance;

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
		URL theURL = new URL("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=" + this.name); 
	    HttpURLConnection con = (HttpURLConnection) theURL.openConnection();
	    con.setRequestMethod("GET");
	    InputStream iStream = theURL.openStream();
        JsonReader read = Json.createReader(iStream);
        JsonObject latLong = read.readObject();
		
		return this.latLong;
	}
	
	protected void calculateDistance(Restaurant restaurant) {
		//This function needs implementing
		Postcode destination = restaurant.getLocation();
		this.distance = Integer.valueOf(0);
	}
	
	protected void calculateLatLong() {
		//This function needs implementing
		this.latLong = new HashMap<String,Double>();
		latLong.put("lat", 0d);
		latLong.put("lon", 0d);
		this.distance = new Integer(0);
	}
	
}
