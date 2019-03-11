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
		return this.latLong;
	}

	protected void calculateDistance(Restaurant restaurant) {
		//This function needs implementing
		Postcode destination = restaurant.getLocation();
		this.distance = Integer.valueOf(0);
	}

	protected void calculateLatLong() {
		//This function needs implementing
		/*
		URL theURL = new URL("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode="+this.name);
	  	HttpURLConnection con = (HttpURLConnection) theURL.openConnection();
	  	con.setRequestMethod("GET");
	  	InputStream iStream = theURL.openStream();
  		JsonReader read = Json.createReader(iStream);
    	JsonObject latLong = read.readObject();
		 */	
		
		this.latLong = new HashMap<String,Double>();
		latLong.put("lat", 0d);
		latLong.put("lon", 0d);
		/*
		JSONObject json = readJsonFromUrl();
		Double latitude = latLong.get("lat");
		latLong.put("lat", (Double) json.get("lat"));
		latLong.put("lon", (Double) json.get("lon"));
		Map<String, Object> latLongMap = json.toMap();
		System.out.println(latLongMap.toString());
		*/
		this.distance = new Integer(0);
	}
	/*
	public JSONObject readJsonFromUrl() throws IOException {
		InputStream is = new URL("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode="+this.name).openStream();
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,Charset.forName("UTF-8")));
			String  jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} finally {
			is.close();
		}
	}
	
	private String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while((cp =  rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}
	*/

}
