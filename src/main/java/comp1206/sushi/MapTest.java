/*
package comp1206.sushi;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
//import com.lynden.gmapsfx.javascript.object.MapType;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;


public class MapTest implements Initializable, MapComponentInitializedListener {
    
    @FXML
    private Button button;
    
    @FXML
    private GoogleMapView mapView;
    
    private GoogleMap map;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mapView.addMapInializedListener(this);
    }    

    @Override
    public void mapInitialized() {
           LatLong joeSmithLocation = new LatLong(47.6197, -122.3231);
        LatLong joshAndersonLocation = new LatLong(47.6297, -122.3431);
        LatLong bobUnderwoodLocation = new LatLong(47.6397, -122.3031);
        LatLong tomChoiceLocation = new LatLong(47.6497, -122.3325);
        LatLong fredWilkieLocation = new LatLong(47.6597, -122.3357);
        
        
        //Set the initial properties of the map.
        MapOptions mapOptions = new MapOptions();
        
        mapOptions.center(new LatLong(47.6097, -122.3331))
                .mapType(MapType.ROADMAP)
                .overviewMapControl(false)
                .panControl(false)
                .rotateControl(false)
                .scaleControl(false)
                .streetViewControl(false)
                .zoomControl(false)
                .zoom(12);
                   
        map = mapView.createMap(mapOptions);

        //Add markers to the map
        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(joeSmithLocation);
        
        MarkerOptions markerOptions2 = new MarkerOptions();
        markerOptions2.position(joshAndersonLocation);
        
        MarkerOptions markerOptions3 = new MarkerOptions();
        markerOptions3.position(bobUnderwoodLocation);
        
        MarkerOptions markerOptions4 = new MarkerOptions();
        markerOptions4.position(tomChoiceLocation);
        
        MarkerOptions markerOptions5 = new MarkerOptions();
        markerOptions5.position(fredWilkieLocation);
        
        Marker joeSmithMarker = new Marker(markerOptions1);
        Marker joshAndersonMarker = new Marker(markerOptions2);
        Marker bobUnderwoodMarker = new Marker(markerOptions3);
        Marker tomChoiceMarker= new Marker(markerOptions4);
        Marker fredWilkieMarker = new Marker(markerOptions5);
        
        map.addMarker( joeSmithMarker );
        map.addMarker( joshAndersonMarker );
        map.addMarker( bobUnderwoodMarker );
        map.addMarker( tomChoiceMarker );
        map.addMarker( fredWilkieMarker );
        
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>Fred Wilkie</h2>"
                                + "Current Location: Safeway<br>"
                                + "ETA: 45 minutes" );

        InfoWindow fredWilkeInfoWindow = new InfoWindow(infoWindowOptions);
        fredWilkeInfoWindow.open(map, fredWilkieMarker);
    }   
}





package comp1206.sushi;


import java.net.URL;
import java.util.Locale;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import comp1206.sushi.server.ServerWindow;
import javafx.geometry.Pos;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MapTab extends ServerWindow implements MapComponentInitializedListener {
	private Tab mapTab;
	private Canvas canvas;
	private GraphicsContext gc;
    private GoogleMapView mapView;
    private GoogleMap map;
    private BorderPane mapPane;
    private ToolBar movementButtons;
    
    
    private Number RestaurantLat;
    private Number RestaurantLon;
    
	
	public MapTab() {
        mapTab = new Tab();
        mapTab.setText("Map");
        mapTab.setContent(mapTab());
        
	}
	
	public void mapTabContent() {
		mapTab.setContent(mapTab());
	}
	
	private Pane mapTab() {
		mapView = new GoogleMapView(Locale.getDefault().getLanguage(), null);
		mapView.addMapInializedListener(this);
		ToolBar movementButtons = new ToolBar();
		mapPane = new BorderPane();
		mapPane.setCenter(mapView);
		return mapPane;
	}
	
	public void mapInitialized() {		
	    //Set the initial properties of the map.
		try {
	    MapOptions mapOptions = new MapOptions();
	    LatLong restaurant = new LatLong(getServer().getRestaurant().getLocation().getLatLong().get("lat"),getServer().getRestaurant().getLocation().getLatLong().get("lon"));

	    mapOptions.center(restaurant)
	            .mapType(MapTypeIdEnum.ROADMAP)
	            .overviewMapControl(false)
	            .panControl(false)
	            .rotateControl(false)
	            .scaleControl(false)
	            .streetViewControl(false)
	            .zoomControl(false)
	            .zoom(12);

	    map = mapView.createMap(mapOptions);

	    //Add a marker to the map
	    MarkerOptions markerOptions = new MarkerOptions();

	    markerOptions.position(restaurant)
	                .visible(Boolean.TRUE)
	                .title("Restaurant");
	    			
	    

	    Marker marker = new Marker( markerOptions );
	    map.addMarker(marker);
	    
        InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
        infoWindowOptions.content("<h2>Restaurant</h2>"
                                + "Postcode: SO17 1BJ<br>");
        InfoWindow fredWilkeInfoWindow = new InfoWindow(infoWindowOptions);
        fredWilkeInfoWindow.open(map, marker);
		} catch(Exception e) {
			e.getStackTrace();
		}
	}
    public Tab getTab() {
    	return mapTab;
    }
     

}

*/