package comp1206.sushi;


import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

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
	private Canvas canvas;
	private GraphicsContext gc;
    private GoogleMapView mapView;
    private GoogleMap map;
    private BorderPane mapPane;
    private ToolBar movementButtons;
    private ArrayList<Marker> markers;

    
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
	    
	    map.addMarkers(addSupplyMarkers());
	    this.refresh();
	    /*
	     * add mouse event to show InfoBox when cursor moves over a marker
	     */
	    /*
        	InfoWindowOptions infoWindowOptions = new InfoWindowOptions();
            infoWindowOptions.content("<h2>Restaurant</h2>"
                                       + "Postcode: SO17 1BJ<br>");
            InfoWindow fredWilkeInfoWindow = new InfoWindow(infoWindowOptions);
            fredWilkeInfoWindow.open(map, marker);
         */
	    

		} catch(Exception e) {
			e.getStackTrace();
		}
	}
	
	
	
	public ArrayList<Marker> addSupplyMarkers() {
		try {
		markers = new ArrayList<Marker>();
		for(Supplier currentSupplier : getServer().getSuppliers()) {
			
			MarkerOptions markerOptions = new MarkerOptions();

			LatLong currentSupplierPos = new LatLong(currentSupplier.getPostcode().getLatLong().get("lat"),currentSupplier.getPostcode().getLatLong().get("lon"));
		    markerOptions.position(currentSupplierPos)
		                .visible(Boolean.TRUE)
		                .label(currentSupplier.getName());
		    			
		    Marker marker = new Marker(markerOptions);
		    marker.setVisible(Boolean.TRUE);
		    markers.add(marker);
		    
		}
		
		for(Drone currentDrone: getServer().getDrones()) {
			
			MarkerOptions markerOptions = new MarkerOptions();

			LatLong currentSupplierPos = new LatLong(50.93772,-1.4);
		    markerOptions.position(currentSupplierPos)
		                .visible(Boolean.TRUE)
		                .label(currentDrone.getName());
		    			
		    Marker marker = new Marker(markerOptions);
		    marker.setVisible(Boolean.TRUE);
		    markers.add(marker);
		    
		}
		
		
		
		
		
		return markers;
		} catch(Exception e) {
			e.getStackTrace();
		}
		return null;
	}
	
	//public void 
	
	
    public Tab getTab() {
    	return mapTab;
    }
    
    public void refresh() {
    	Timeline timeline = new Timeline();
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000), (actionEvent) -> {
    	map.removeMarkers(markers);
    	addSupplyMarkers();
    	map.addMarkers(addSupplyMarkers());
    	 }, null, null));
    	 timeline.play();
    	 
    }
    
    
    /*
    public void startTimer() {
    	EventHandler<ActionEvent> update = new EventHandler<T>() {
            @Override
            public void handle(ActionEvent actionEvent) {
           	 mapTabContent();
            }
    	};
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0)), update, new KeyValue());
        timeline.setCycleCount(Animation.INDEFINITE);
     timeline.play();
    }
    */
     

}
