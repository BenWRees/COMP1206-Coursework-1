package comp1206.sushi;


import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import com.lynden.gmapsfx.GoogleMapView;
import com.lynden.gmapsfx.MapComponentInitializedListener;
import com.lynden.gmapsfx.javascript.event.UIEventHandler;
import com.lynden.gmapsfx.javascript.event.UIEventType;
import com.lynden.gmapsfx.javascript.object.Animation;
import com.lynden.gmapsfx.javascript.object.GoogleMap;
import com.lynden.gmapsfx.javascript.object.InfoWindow;
import com.lynden.gmapsfx.javascript.object.InfoWindowOptions;
import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.MapOptions;
import com.lynden.gmapsfx.javascript.object.MapTypeIdEnum;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.server.ServerWindow;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.*;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.util.Duration;

/*
 * need to:
 * Populate the map with buildings and roads
 * Populate the map with drones 
 * add progress Bars for all the drones
 * add a running list message for all the updates to the orders
 * mouse drag event in mapTab that allows for moving through the map
 * 	need a repaint event to repaint the map 
 * 
 */
public class MapTab extends ServerWindow implements MapComponentInitializedListener {
	private Tab mapTab;
    private GoogleMapView mapView;
    private GoogleMap map;
    private BorderPane mapPane;
    private ArrayList<Marker> markers;
    private ArrayList<Marker> supplierMarkers;
    private LatLong currentSupplierPos;

    
    private Number RestaurantLat;
    private Number RestaurantLon;
    private DroneFlying drones;
    
	
	public MapTab() {
        mapTab = new Tab();
        mapTab.setText("Map");
        mapTab.setContent(mapTab());
        
	}
	
	public void mapTabContent() {
		mapTab.setContent(mapTab());
	}
	
	/*
	 * populates the panel that contains the map
	 */
	private Pane mapTab() {
		mapView = new GoogleMapView(Locale.getDefault().getLanguage(), null);
		mapView.addMapInializedListener(this);
		ToolBar movementButtons = new ToolBar();
		mapPane = new BorderPane();
		mapPane.setCenter(mapView);
		startTimer();
		return mapPane;
	}
	
	/*
	 * Creates and populates Map
	 */
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
	            .zoom(15);

	    map = mapView.createMap(mapOptions);

	    markers = new ArrayList<Marker>();
	    //Add a marker to the map
	    MarkerOptions markerOptions = new MarkerOptions();

	    markerOptions.position(restaurant)
	                .visible(Boolean.TRUE)
	                .title("Restaurant")
	                .label("Restaurant");
	    			
	    

	    Marker marker = new Marker(markerOptions);
	    marker.setTitle("Restaurant");
	    marker.setVisible(true);
	    map.addMarker(marker);
	    
	    //markers.addAll(drones.getDroneMarkers());
	    //markers.addAll(addSupplyMarkers());
	   addSupplyMarkers();
	    //DroneFlying droneFlying = new DroneFlying();
	    this.refresh();
	    
	    

		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	
	public ArrayList<Marker> addSupplyMarkers() throws IOException {
		
		supplierMarkers = new ArrayList<Marker>();
		for(Supplier currentSupplier : getServer().getSuppliers()) {
			
			MarkerOptions markerOptions = new MarkerOptions();

			currentSupplierPos = new LatLong(currentSupplier.getPostcode().getLatLong().get("lat"),currentSupplier.getPostcode().getLatLong().get("lon"));
		    markerOptions.position(currentSupplierPos)
		                .visible(Boolean.TRUE)
		                .label(currentSupplier.getName().toUpperCase());
		    			
		    Marker marker = new Marker(markerOptions);
		    marker.setVisible(Boolean.TRUE);
		    map.addMarker(marker);
		    supplierMarkers.add(marker);
		    
		}
		
		return supplierMarkers;
	}	
	
    public Tab getTab() {
    	return mapTab;
    }
    
    public void refresh() {
    	Timeline timeline = new Timeline();
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	timeline.playFrom(Duration.millis(1000));
    	 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000), (actionEvent) -> {
    	map.removeMarkers(supplierMarkers);
    	//supplierMarkers.clear();
    	try {
    		//markers.addAll(addSupplyMarkers());
			addSupplyMarkers();
		} catch (Exception e) {
			
			e.printStackTrace();
		}
    	 }, null, null));
    	 timeline.play();
    	 
    }
    
    public GoogleMap getMap() {
    	return map;
    }
    

}
