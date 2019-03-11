package comp1206.sushi.common;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class GeoMap {
	    private static final String GEO_CODE_SERVER = "http://api.postcodes.io/postcodes/";

	    private static String getLocation(String code) {
	        String address = buildUrl(code);

	        String content = null;

	        try
	        {
	            URL url = new URL(address);

	            InputStream stream = url.openStream();

	            try
	            {
	                int available = stream.available();

	                byte[] bytes = new byte[available];

	                stream.read(bytes);

	                content = new String(bytes);
	            }
	            finally
	            {
	                stream.close();
	            }

	            return (String) content.toString();
	        }
	        catch (IOException e)
	        {
	            throw new RuntimeException(e);
	        }
	    }
	    
	    public static String buildUrl(String code) {
	    	StringBuilder  url = new StringBuilder("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=");
	    	url.append(code);
	    	return url.toString();
	    }
	    
	    
}
