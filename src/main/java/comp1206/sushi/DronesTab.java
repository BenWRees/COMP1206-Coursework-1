package comp1206.sushi;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Staff;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.common.UpdateListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
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

public class DronesTab extends ServerWindow {
	private Tab dronesTab;
	private Label droneSpeedPrompt;
	private TextField droneSpeed;
	private ObservableList<Drone> modelViewLists;
	private ListView<Drone> viewPanel;
	private VBox extraInfoPanel;
	
	public DronesTab() {
        dronesTab = new Tab();
        dronesTab.setText("Drones");
        dronesTab.setContent(dronesTab());
	}
	
	public void dronesTabContent() {
		dronesTab.setContent(dronesTab());
	}
	
    private Pane dronesTab() {
        BorderPane mainDroneTab = new BorderPane();
        mainDroneTab.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        setDroneDestinations();
        droneSpeedPrompt = new Label("Set Drone Speed");
        droneSpeed = new TextField();
        fieldsBox.add(droneSpeedPrompt,0,0);
        fieldsBox.add(droneSpeed,0,1);
        entryBox.setTop(fieldsBox);

        mainDroneTab.setBottom(entryBox);
        mainDroneTab.setCenter(viewPanel);
        return mainDroneTab;
    }
    
    
    public Tab getTab() {
    	return dronesTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Drone>();
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
    			    	droneSpeed.setText("");
    			    	System.out.println("Server: " + getServer().getDrones());
    					System.out.println("ViewPanel: " + modelViewLists); //DEBUG
    				} catch (Exception e) {
    					if(droneSpeed.getText().trim().isEmpty()) {
    						popUp("Incompleted Field: make sure all Fields are complete");
    					}
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
					System.out.println("Server: " + getServer().getDrones()); //DEBUG
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
                try {
                	
					populateExtraInfoPanel();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });

        viewPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        extraInfoPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        controlPanel.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        extraInfoPanel.setPrefSize(400,300);

        
        modelViewLists = FXCollections.observableArrayList(getServer().getDrones());
        viewPanel.setItems(modelViewLists); 
        

        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }
    
	public void setDroneDestinations() {
			for(Drone currentDrone : getServer().getDrones()) {
				Random r = new Random();
				currentDrone.setDestination(getServer().getSuppliers().get(r.nextInt(getServer().getSuppliers().size())).getPostcode());
			}
	}
    
    protected VBox populateExtraInfoPanel() throws Exception {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);
    		
    		Button editButton = new Button("Edit");
    		
    		Drone droneObserved = viewPanel.getSelectionModel().getSelectedItem();
    		
    		
    		TextField droneSpeed = new TextField(getServer().getDroneSpeed(viewPanel.getSelectionModel().getSelectedItem()).toString());
    		String droneProgress = getServer().getDroneProgress(droneObserved).toString();
    		String droneName = viewPanel.getSelectionModel().getSelectedItem().getName();
    		String droneSource = getServer().getDroneSource(droneObserved).getName();
    		String droneDestination = getServer().getDroneDestination(droneObserved).getName();
    		String droneCapacity = viewPanel.getSelectionModel().getSelectedItem().getCapacity().toString();
    		String droneBattery = viewPanel.getSelectionModel().getSelectedItem().getBattery().toString();
    		String droneStatus = getServer().getDroneStatus(viewPanel.getSelectionModel().getSelectedItem()).toString();
    		
    		Label speed = new Label("Speed: ");
    		Label progress = new Label("Progress: " + droneProgress);
    		Label name = new Label("Name: " + droneName);
    		Label source = new Label("Source: " + droneSource);
    		Label destination = new Label("Destination: " + droneDestination);
    		Label capacity = new Label("Capacity: " + droneCapacity);
    		Label battery = new Label("Battery: " + droneBattery);
    		Label status = new Label("Status: " + droneStatus);
    		
    		extraInfoDesign.add(name, 0, 0);
    		extraInfoDesign.add(speed,0,1);
    		extraInfoDesign.add(droneSpeed, 1, 1);
    		extraInfoDesign.add(progress,0,2);
    		extraInfoDesign.add(source,0,3);
    		extraInfoDesign.add(destination,0,4);
    		extraInfoDesign.add(battery,0,5);
    		extraInfoDesign.add(capacity,0,6);
    		extraInfoDesign.add(status,0,7);
    		extraInfoDesign.add(editButton, 0, 8);
    		
    		/*
             * edit button action event
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
    					if(viewPanel.getSelectionModel().isEmpty()) {
       					 //TODO Auto-generated catch block
       						popUp("Must Select an Object");
       					} else {
                    	 droneObserved.setSpeed(Integer.parseInt(droneSpeed.getText()));
                    	}
    				} catch (NumberFormatException e) {
    					 //TODO Auto-generated catch block
    						popUp("Speed must be a Number");
    				}
                }
            };
            editButton.setOnAction(editButtonAction);
            //getServer().addUpdateListener((UpdateListener) editButtonAction);

    		
    		extraInfoPanel.getChildren().add(extraInfoDesign);
    		return extraInfoPanel;
    	}
    	return extraInfoPanel;
    }
    
    protected void constructObject() throws NumberFormatException{
    	if(droneSpeed.getText().trim().isEmpty()) {
    		popUp("Incompleted Field: make sure all Fields are complete");
    		} else {
    			try {
    				Number speed = Integer.parseInt(droneSpeed.getText());
    				Drone newDrone = new Drone(speed);
    				Random r = new Random();
    				newDrone.setDestination(suppliers.get(r.nextInt(suppliers.size()+1)).getPostcode());
    				modelViewLists.add(newDrone);
    				getServer().addDrone(speed);
    			} catch(NumberFormatException e) {
    				popUp("Speed must be a Number");
    			}
    		}
    }
    
    protected void removeObject() {
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			popUp("No Object Selected: Please Select an Object");
		} else {
    	Drone droneToRemove = viewPanel.getSelectionModel().getSelectedItem();
    	modelViewLists.remove(droneToRemove);
    	getServer().getDrones().clear();
    	getServer().getDrones().addAll(modelViewLists);
		}
    }
    
    protected void moveUp() {
    	Drone droneToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int droneIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(droneIndexToMove == 0) {
        	modelViewLists.remove(droneIndexToMove);
        	modelViewLists.add(modelViewLists.size(), droneToMove);
        	getServer().getDrones().remove(droneIndexToMove);
        	getServer().getDrones().add(getServer().getDrones().size(), droneToMove);
    	} else {
        	modelViewLists.remove(droneIndexToMove);
        	modelViewLists.add(droneIndexToMove-1, droneToMove);
        	getServer().getDrones().remove(droneIndexToMove);
        	getServer().getDrones().add(droneIndexToMove-1, droneToMove);
    	}
    }
    
    protected void moveDown() {
    	Drone droneToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int droneIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(droneIndexToMove == modelViewLists.size()-1) {
        	modelViewLists.remove(droneIndexToMove);
        	modelViewLists.add(0,droneToMove);
        	getServer().getDrones().remove(droneIndexToMove);
        	getServer().getDrones().add(0,droneToMove);
    	} else {
        	modelViewLists.remove(droneIndexToMove);
        	modelViewLists.add(droneIndexToMove+1, droneToMove);
        	getServer().getDrones().remove(droneIndexToMove);
        	getServer().getDrones().add(droneIndexToMove+1, droneToMove);
    	}
    }
}
    	

