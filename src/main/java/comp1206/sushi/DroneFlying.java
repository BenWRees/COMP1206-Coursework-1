package comp1206.sushi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.lynden.gmapsfx.javascript.object.LatLong;
import com.lynden.gmapsfx.javascript.object.Marker;
import com.lynden.gmapsfx.javascript.object.MarkerOptions;

import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Supplier;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/*
 * need to set destination for drone
 * 
 * set progress of drone
 * 
 */
public class DroneFlying extends MapTab {
	private ArrayList<Marker> droneMarkers;
	private LatLong currentDronePos;
	
	/*
	 * creates all the drone markers for the map and sets them to the restaurant
	 */
	public DroneFlying() throws Exception {
			droneMarkers = new ArrayList<Marker>();
			//setDroneDestinations();
			for(Drone currentDrone : getServer().getDrones()) {
				MarkerOptions markerOptions = new MarkerOptions();
				currentDronePos = new LatLong(getServer().getDroneSource(currentDrone).getLatLong().get("lat"), getServer().getDroneSource(currentDrone).getLatLong().get("lon"));
			    
				markerOptions.position(currentDronePos)
			                .visible(Boolean.TRUE)
			                .label(currentDrone.getName().toLowerCase());
			    			
			    Marker marker = new Marker(markerOptions);
			    marker.setVisible(Boolean.TRUE);
			    
			    getMap().addMarker(marker);
			}
			
	}
	
	public ArrayList<Marker> updateDroneMarkers() {
		try {
			for(Drone currentDrone : getServer().getDrones()) {
			
				LatLong currentBearing = new LatLong(currentDrone.getDestination().getLatLong().get("lat"),currentDrone.getDestination().getLatLong().get("lon"));
				LatLong currentLatLong = currentDronePos.getDestinationPoint(currentDronePos.getBearing(currentBearing), Double.parseDouble(currentDrone.getDestination().getDistance().toString()));
			
				MarkerOptions currentDroneMarker = new MarkerOptions();
			
				currentDroneMarker.position(currentLatLong)
            						.visible(Boolean.TRUE)
            						.label(currentDrone.getName().toLowerCase());
			
				Marker marker = new Marker(currentDroneMarker);
				marker.setVisible(Boolean.TRUE);
				Double speed = Double.valueOf(currentDrone.getSpeed().toString());
				Double distance = Double.valueOf(currentDrone.getDestination().getDistance().toString());
				Double pastProgress = Double.valueOf(currentDrone.getProgress().toString());
				currentDrone.setProgress(pastProgress+ (100*(speed/distance)));
				
				droneMarkers.add(marker);
		}
		
		}catch(Exception e) {
			e.getStackTrace();
		}
		return droneMarkers;
	}
	
	/*
	 * for each drone pick a random supplier
	 */
	public void setDestination() {
		Iterator<Drone> dronesIt = getServer().getDrones().iterator();
		Iterator<Supplier> suppliersIt = getServer().getSuppliers().iterator();
		while(suppliersIt.hasNext() && dronesIt.hasNext()) {
			dronesIt.next().setDestination(suppliersIt.next().getPostcode());
		}
	}
	
	/*
	 * forces all the drones to move to their destination 
	 */
	public void moveDrones() {
		Timeline timeline = new Timeline();
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(1000), (actionEvent) -> {

    		droneMarkers.clear();
    		updateDroneMarkers();
			getMap().addMarkers(droneMarkers);
    	 }, null, null));
    	 timeline.play();
	}
	
	public ArrayList<Marker> getDroneMarkers() {
		return droneMarkers;
	}

}
