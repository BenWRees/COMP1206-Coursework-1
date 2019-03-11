package comp1206.sushi;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
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

public class PostcodeTab extends ServerApplication {
	private Tab postcodesTab;
	private Label postcodeEntryPrompt;
	private TextField postcodeEntry;
	private ListView<Postcode> viewPanel;
	private VBox extraInfoPanel;

	PostcodeTab() {
        postcodesTab = new Tab();
        postcodesTab.setText("Postcodes");
        postcodesTab.setContent(postcodeTab());
	}
	
	public void postcodesTabContent() {
		postcodesTab.setContent(postcodeTab());
	}

    /*
     * needs to populate the view with objects from the arrayList of Postcode
     * Needs to have a field to enter the Postcode address
     * Needs to calculate the lat and long of the postcode
     * adds the elements from the array list of postcode to the view pane
     */
    private Pane postcodeTab() {
        BorderPane mainPostcodePanel = new BorderPane();
        mainPostcodePanel.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        postcodeEntryPrompt = new Label("Enter Postcode");
        postcodeEntry = new TextField();
        fieldsBox.add(postcodeEntryPrompt,0,0);
        fieldsBox.add(postcodeEntry,0,1);
        entryBox.setTop(fieldsBox);
        
        //instantiatePostcodeList();

        mainPostcodePanel.setBottom(entryBox);
        mainPostcodePanel.setCenter(viewPanel);
        return mainPostcodePanel;
    }  
    
    public Tab getTab() {
    	return postcodesTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Postcode>();
        viewPanel.setOrientation(Orientation.VERTICAL);
        viewPanel.setPadding(new Insets(10,5,5,15));
        
        extraInfoPanel = new VBox(7);
        extraInfoPanel.setPadding(new Insets(10,5,7,15));
        
        
        HBox controlPanel = new HBox(7);
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
    			    	postcodeEntry.setText("");
    			    	System.out.println("Server: " + getServer().getPostcodes());
    					System.out.println("ViewPanel: " + getPostcodesList()); //DEBUG
    				} catch (Exception e) {
    				}
                }
            }; 
        add.setOnAction(addButton);
        
        /*
         * remove button action event
         */
        EventHandler<ActionEvent> removeButton = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	//need to work out how to remove selected object
			    	ArrayList<String> postcodesInSupplier = new ArrayList<String>();
			    	String objectSelected = viewPanel.getSelectionModel().getSelectedItem().toString();
			    	for(Supplier suppliersInServer : getServer().getSuppliers()) {
			    		postcodesInSupplier.add(suppliersInServer.getPostcode().toString());
			    	}
			    	if(postcodesInSupplier.contains(objectSelected)) {
			    		popUp("Cannot delete a Postcode being used by a Supplier");
			    		System.out.println("THIS SHOULD BE WORKING");
			    	} else {
                			removeObject();
			    	}
			    	System.out.println(postcodesInSupplier.contains(objectSelected));
			    	System.out.println(postcodesInSupplier.toString());
			    	
			    	/*
			    	 * newly created objects being referenced in this scope!!
			    	 * why isn't it being checked properly then??
			    	 */
					//need to update ListView every time button is pressed
				} catch (Exception e) {
					 //TODO Auto-generated catch block
						popUp("Must Select an Object");
				}
            }
        };
        remove.setOnAction(removeButton);
        
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
					System.out.println("ViewPanel: " + getPostcodesList()); 
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
					System.out.println("ViewPanel: " + getPostcodesList()); 
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
        
        //setPostcodesList(FXCollections.observableArrayList(getServer().getPostcodes()));        
        viewPanel.setItems(getPostcodesList());
        
        topPanel.setPrefSize(300, 200);
        
        //extraInfoPanel = extraInfoPanel();

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }
    
    private VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);
    		
    		Button editButton = new Button("Edit");
    		
    		Postcode postcodeObserved = viewPanel.getSelectionModel().getSelectedItem();

    		TextField name = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());
    		
    		//Object[] latAndLong = viewPanel.getSelectionModel().getSelectedItem().getLatLong().values().toArray();
    		
    		String distance = viewPanel.getSelectionModel().getSelectedItem().getDistance().toString();
    		
    		Label postcodeName = new Label("Name: ");
    		//Label latLabel = new Label("Postcode Latitude: " + postcodeObserved.getLatLong().keySet().toString());
    		//Label longLabel = new Label("Postcode Longitude: " + postcodeObserved.getLatLong().values().toString());
    		Label postcodeDistance = new Label("Distance: " + distance);
    		
    		extraInfoDesign.add(postcodeName, 0, 1);
    		extraInfoDesign.add(name, 1, 1);
    		//extraInfoDesign.add(latLabel, 0, 2);
    		//extraInfoDesign.add(longLabel, 0, 3);
    		extraInfoDesign.add(postcodeDistance, 0, 5);
    		extraInfoDesign.add(editButton, 0, 6);
    		
    		/*
             * edit button action event
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	 postcodeObserved.setName(name.getText());
                    	 //getPostcodeList().remove(postcodeObserved);
    					//need to update ListView every time button is pressed
    				} catch (Exception e) {
    					if(viewPanel.getSelectionModel().isEmpty()) {
    					 //TODO Auto-generated catch block
    						popUp("Must Select an Object");
    					}
    				}
                }
            };
            editButton.setOnAction(editButtonAction);
    		

    		
    		extraInfoPanel.getChildren().add(extraInfoDesign);
    		return extraInfoPanel;
    	}
    	return extraInfoPanel;
    }
    
    protected void constructObject() {
    	if(postcodeEntry.getText().trim().isEmpty()) {
			popUp("Incompleted Field: make sure all Fields are complete");
		} else {
    		String code = postcodeEntry.getText();
    		Postcode newPostcode = new Postcode(code);
    		getPostcodesList().add(newPostcode);
    		getServer().addPostcode(code);
    		//getPostcodesInSupplier().add(newPostcode);
	    	
		} 
    }
  //need to prevent removal of a postcode if the postcode is being used by a supplier 
    protected void removeObject() throws UnableToDeleteException  {
    	/*
    	ArrayList<Postcode> postcodesInSupplier = new ArrayList<Postcode>();
    	for(Supplier suppliersInServer : getServer().getSuppliers()) {
    		postcodesInSupplier.add(suppliersInServer.getPostcode());
    	}
    	if(postcodesInSupplier.contains(viewPanel.getSelectionModel().getSelectedItem())) {
    		popUp("Cannot delete a Postcode being used by a Supplier");
    	} else {
    	*/
    		Postcode postcodeToRemove = viewPanel.getSelectionModel().getSelectedItem();
    		getPostcodesList().remove(postcodeToRemove);
    		//getPostcodeList().remove(postcodeToRemove);
    		int actualIndex= viewPanel.getSelectionModel().getSelectedIndex() + 1;
    		if(actualIndex == 1) {
    			actualIndex = 0;
    		}
    		Postcode actualPostcodeToRemove = getServer().getPostcodes().get(actualIndex);
    		if(viewPanel.getSelectionModel().isEmpty()== false) {
    			getServer().removePostcode(actualPostcodeToRemove);
    		}
    		if(viewPanel.getSelectionModel().isEmpty()) {
    			popUp("No Object Selected: Please Select an Object");
    		}
    	//} 
    } 
    
    protected void moveUp() {
    	Postcode postcodeToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int postcodeIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(postcodeIndexToMove == 0) {
        	getPostcodesList().remove(postcodeIndexToMove);
        	getPostcodesList().add(postcodeToMove);
        	getServer().getPostcodes().remove(postcodeIndexToMove);
        	getServer().getPostcodes().add(postcodeToMove);
    	} 
    	else {
        	getPostcodesList().remove(postcodeIndexToMove);
        	getPostcodesList().add(postcodeIndexToMove-1, postcodeToMove);
        	getServer().getPostcodes().remove(postcodeIndexToMove);
        	getServer().getPostcodes().add(postcodeIndexToMove-1, postcodeToMove);
    	}
    }
    
    protected void moveDown() {
    	Postcode postcodeToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int postcodeIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(postcodeIndexToMove == getPostcodesList().size()-1) {
    		getPostcodesList().remove(postcodeIndexToMove);
    		getPostcodesList().add(0,postcodeToMove);
        	getServer().getPostcodes().remove(postcodeIndexToMove);
        	getServer().getPostcodes().add(0,postcodeToMove);
    	
    	} else {
    		getPostcodesList().remove(postcodeIndexToMove);
    		getPostcodesList().add(postcodeIndexToMove+1, postcodeToMove);
        	getServer().getPostcodes().remove(postcodeIndexToMove);
        	getServer().getPostcodes().add(postcodeIndexToMove+1, postcodeToMove);
    	}
    }
    

    

}
