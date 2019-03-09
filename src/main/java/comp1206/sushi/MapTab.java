package comp1206.sushi;



import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

public class MapTab {
	private Tab mapTab;
	
	MapTab() {
        mapTab = new Tab();
        mapTab.setText("Map");
        mapTab.setContent(mapTab());
	}
	
	public void mapTabContent() {
		mapTab.setContent(mapTab());
	}
	
	private Pane mapTab() {
		BorderPane mainMapPanel = new BorderPane();
		return mainMapPanel;
	}
	
    public Tab getTab() {
    	return mapTab;
    }

}
