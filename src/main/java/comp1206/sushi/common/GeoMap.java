package comp1206.sushi.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
* class should take the url code and check
*/

public class GeoMap {
	    private static final String GEO_CODE_SERVER = "http://api.postcodes.io/postcodes/";
	    private String[] stringArray;
	    private String inputLine;
	    private URL postcodeURL;
	    private BufferedReader in;
	    private StringBuilder address;
	    private Double lat;
	    private Double lon;
	    private HashMap<String,Double> latLong;
	    
	    public GeoMap(String name) {
	    	try {
	    	address = new StringBuilder("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=");
	        String newName = name.replaceAll("\\s+","");
	        latLong = new HashMap<String,Double>();
	        address.append(newName);
	        postcodeURL = new URL(address.toString());
	        in = new BufferedReader(new InputStreamReader(postcodeURL.openStream()));

	        inputLine = in.readLine();
	        String latString = stringArray[3].replaceAll("\"","");
    		String lonString = stringArray[5].replaceAll("\"","");
	        stringArray = inputLine.split(":|,|}");
	        lat = Double.parseDouble(latString);
	        lon = Double.parseDouble(lonString);
	    	} catch(Exception e) {
	    		e.getStackTrace();
	    	}
	    }
	    public void addLatAndLongToHashMap(Map<String, Double> hashmapToHaveElementsAddedToIt) {
	    	hashmapToHaveElementsAddedToIt.put("lat", lat);
	    	hashmapToHaveElementsAddedToIt.put("lon", lon);
	    }
	    
	    
	    public Double getLat() {
	    	return lat;
	    }
	    
	    public Double getLong() {
	    	return lon;
	    }
}
