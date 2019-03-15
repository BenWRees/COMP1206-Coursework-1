package comp1206.sushi;



import javafx.geometry.Pos;
import javafx.scene.canvas.*;

import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

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
public class MapTab {
	private Tab mapTab;
	private Canvas canvas;
	private GraphicsContext gc;
	
	MapTab() {
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
		BorderPane mainMapPanel = new BorderPane();
		canvas = new Canvas(500,700);
		gc = canvas.getGraphicsContext2D();
		drawBuildings(gc);
		mainMapPanel.setCenter(canvas);		
		return mainMapPanel;
	}
	
	/*
	 * works out the position of all the buildings in the vicinity and 
	 * adds them to the canvas
	 */
	private void drawBuildings(GraphicsContext gc) {
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.CORNFLOWERBLUE);
		gc.setLineWidth(7);
		
		gc.fillRect(0, 0, 10, 10);
		gc.fillRect(0, 15, 10, 10);
		gc.fillRect(0, 30, 10, 10);
		gc.fillRect(30, 0, 10, 10);
	}
	
	/*
	 * works out the position of all the roads in the vicinity and 
	 * adds them to the canvas
	 */
	private void drawRoads(GraphicsContext gc) {
		
	}
	
	/*
	 * adds the drones to the map, all new drones start at the restaurant and animates the drones to fly directly to their destination
	 */
	private void animateDrones(GraphicsContext gc) {
		
	}
	
	/*
	 * adds the progress bars of each drone
	 * Keeps track of updates on each order with a running message list
	 */
	private Pane progressBarPane() {
		return null;
	}
	
	
    public Tab getTab() {
    	return mapTab;
    }
     

}
