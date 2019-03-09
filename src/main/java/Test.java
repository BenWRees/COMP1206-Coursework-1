import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class Test {
	public static void main(String[] argv) throws IOException {
		URL theURL = new URL("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=SO17 1TJ"); 
	    HttpURLConnection con = (HttpURLConnection) theURL.openConnection();
	    //con.setRequestMethod("GET");
	    InputStream iStream = theURL.openStream();
        JsonReader read = Json.createReader(iStream);
        JsonObject latLong = read.readObject();
        System.out.println(latLong.toString());
	}

}
