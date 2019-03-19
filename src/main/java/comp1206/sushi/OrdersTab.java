package comp1206.sushi;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.common.Order;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.UpdateListener;
import comp1206.sushi.common.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class OrdersTab extends ServerWindow {
	private Tab ordersTab;
	private ObservableList<Order> modelViewLists;
	private ListView<Order> viewPanel;
	private VBox extraInfoPanel;
	
	public OrdersTab() {
        ordersTab = new Tab();
        ordersTab.setText("Orders");
        ordersTab.setContent(ordersTab());
	}
	
	public void ordersTabContent() {
		ordersTab.setContent(ordersTab());
	}
	
    private Pane ordersTab() {
        BorderPane mainOrdersPanel = new BorderPane();
        mainOrdersPanel.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        entryBox.setTop(fieldsBox);

        mainOrdersPanel.setBottom(entryBox);
        mainOrdersPanel.setCenter(viewPanel);
        return mainOrdersPanel;

    }
    
    public Tab getTab() {
    	return ordersTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Order>();
        viewPanel.setOrientation(Orientation.VERTICAL);
        HBox controlPanel = new HBox(7);
                       
        extraInfoPanel = new VBox(7);
        extraInfoPanel.setPadding(new Insets(10,5,7,15));
        viewPanel.setPadding(new Insets(10,5,5,15));
        controlPanel.setPadding(new Insets(10,0,10,15));

        add = new Button("Add");
        remove = new Button("Remove");
        moveUp = new Button("Move Up");
        moveDown = new Button("Move Down");
        controlPanel.getChildren().addAll(add,remove,moveUp,moveDown);
        
        /*
         * add button action event
         */
            EventHandler<ActionEvent> addButton = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event){
                    try {
    					constructObject();
    			    	//suppliersName.setText("");
    			    	System.out.println("Server: " + getServer().getSuppliers());
    					System.out.println("ViewPanel: " + modelViewLists); //DEBUG
    				} catch (Exception e) {
    				}
                }
            };
        add.setOnAction(addButton);
        //getServer().addUpdateListener((UpdateListener) addButton);

        
        /*
         * remove button action event
         */
        EventHandler<ActionEvent> removeButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	//need to work out how to remove selected object
                	removeObject();
					System.out.println("Server: " + getServer().getPostcodes()); //DEBUG
					System.out.println("ViewPanel: " + modelViewLists); 
					//need to update ListView every time button is pressed
				} catch (Exception e) {
					 //TODO Auto-generated catch block
						popUp("Must Select an Object");
				}
            }
        };
        remove.setOnAction(removeButton);
        //getServer().addUpdateListener((UpdateListener) removeButton);

        
        /*
         * move up button action event
         */
        EventHandler<ActionEvent> moveUpButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	//need to work out how to remove selected object
                	moveUp();
					System.out.println("Server: " + getServer().getPostcodes()); //DEBUG
					System.out.println("ViewPanel: " + modelViewLists); 
					//need to update ListView every time button is pressed
				} catch (Exception e) {
					if(viewPanel.getSelectionModel().isEmpty()) {
					 //TODO Auto-generated catch block
						popUp("Must Select an Object");
					}
				}
            }
        };
        moveUp.setOnAction(moveUpButton);
        //getServer().addUpdateListener((UpdateListener) moveUpButton);

        /*
         * move down button action event
         */
        EventHandler<ActionEvent> moveDownButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	//need to work out how to remove selected object
                	moveDown();
					System.out.println("Server: " + getServer().getPostcodes()); //DEBUG
					System.out.println("ViewPanel: " + modelViewLists); 
					//need to update ListView every time button is pressed
				} catch (Exception e) {
					if(viewPanel.getSelectionModel().isEmpty()) {
					 //TODO Auto-generated catch block
						popUp("Must Select an Object");
					}
				}
            }
        };
        moveDown.setOnAction(moveDownButton);
        //getServer().addUpdateListener((UpdateListener) moveDownButton);

        
        /*
         * extra info panel button action event
         */
        viewPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
            	updateUI(extraInfoPanel);
                populateExtraInfoPanel();
            }
        });
        

        viewPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        extraInfoPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        controlPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        extraInfoPanel.setPrefSize(400,300);

        modelViewLists = FXCollections.observableArrayList(getServer().getOrders());
        
        viewPanel.setItems(modelViewLists);
        

        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }
    
    protected VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		String name = viewPanel.getSelectionModel().getSelectedItem().getName();
    		String status = viewPanel.getSelectionModel().getSelectedItem().getStatus();
    		String  distance = viewPanel.getSelectionModel().getSelectedItem().getDistance().toString();
    		Label orderName = new Label("Name: " + name);
    		Label orderStatus = new Label("Status: " + status);
    		Label orderDistance = new Label("Distance: " + distance);
    		extraInfoPanel.getChildren().addAll(orderName,orderStatus,orderDistance);
    		return extraInfoPanel;
    	}
    	return extraInfoPanel;
    }
    
    //@Override
    protected void constructObject() {
    	Order newOrder = new Order();
    	modelViewLists.add(newOrder);
    }
    
    protected void removeObject() throws UnableToDeleteException {
    	Order orderToRemove = viewPanel.getSelectionModel().getSelectedItem();
    	modelViewLists.remove(orderToRemove);
    	int actualIndex= viewPanel.getSelectionModel().getSelectedIndex() + 1;
    	if(actualIndex == 1) {
    		actualIndex = 0;
    	}
    	Order actualOrderToRemove = getServer().getOrders().get(actualIndex);
			if(viewPanel.getSelectionModel().isEmpty()== false) {
				getServer().removeOrder(actualOrderToRemove);
			}
		if(viewPanel.getSelectionModel().isEmpty()) {
			popUp("No Object Selected: Please Select an Object");
		}
    }
    
    protected void moveUp() {
    	Order orderToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int orderIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(orderIndexToMove == 0) {
        	modelViewLists.remove(orderIndexToMove);
        	modelViewLists.add(modelViewLists.size(), orderToMove);
        	getServer().getOrders().remove(orderIndexToMove);
        	getServer().getOrders().add(getServer().getOrders().size(), orderToMove);
    	} else {
        	modelViewLists.remove(orderIndexToMove);
        	modelViewLists.add(orderIndexToMove-1, orderToMove);
        	getServer().getOrders().remove(orderIndexToMove);
        	getServer().getOrders().add(orderIndexToMove-1, orderToMove);
    	}
    }
    
    protected void moveDown() {
    	Order orderToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int orderIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(orderIndexToMove == modelViewLists.size()-1) {
    		modelViewLists.remove(orderIndexToMove);
        	modelViewLists.add(0,orderToMove);
        	getServer().getOrders().remove(orderIndexToMove);
        	getServer().getOrders().add(0,orderToMove);
    	} else {
        	modelViewLists.remove(orderIndexToMove);
        	modelViewLists.add(orderIndexToMove+1, orderToMove);
        	getServer().getOrders().remove(orderIndexToMove);
        	getServer().getOrders().add(orderIndexToMove+1, orderToMove);
    	}
    }

    
}
