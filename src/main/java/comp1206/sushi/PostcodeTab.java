package comp1206.sushi;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.common.UpdateListener;
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

public class PostcodeTab extends ServerWindow {
	private Tab postcodesTab;
	private Label postcodeEntryPrompt;
	private TextField postcodeEntry;
	private ListView<Postcode> viewPanel;
	private VBox extraInfoPanel;

	public PostcodeTab() {
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
                    	// else {
    					constructObject();
    			    	postcodeEntry.setText("");
    			    	System.out.println("Server: " + getServer().getPostcodes());
    					System.out.println("ViewPanel: " + getPostcodesList()); //DEBUG
        			//}
    				} catch (Exception e) {
    				}
               }
            };
        add.setOnAction(addButton);
       // getServer().addUpdateListener((UpdateListener) addButton);


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
			    		//System.out.println("THIS SHOULD BE WORKING");
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
        //getServer().addUpdateListener((UpdateListener) moveDownButton);


        /*
         * extra info panel button action event
         */
        viewPanel.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
            	updateUI(extraInfoPanel);
                try {
                	//System.out.println(viewPanel.getSelectionModel().getSelectedItem().getLatLong().values().toString());
					populateExtraInfoPanel();
				} catch (UnsupportedOperationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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

        //setPostcodesList(FXCollections.observableArrayList(getServer().getPostcodes()));
        viewPanel.setItems(getPostcodesList());

        topPanel.setPrefSize(300, 200);

        //extraInfoPanel = extraInfoPanel();

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }

    private VBox populateExtraInfoPanel() throws UnsupportedOperationException, Exception {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);

    		Button editButton = new Button("Edit");

    		Postcode postcodeObserved = viewPanel.getSelectionModel().getSelectedItem();

    		TextField name = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());

    		//Object[] latAndLong = viewPanel.getSelectionModel().getSelectedItem().getLatLong().values().toArray();

    		Double distance = viewPanel.getSelectionModel().getSelectedItem().getDistance().doubleValue();
    		
    		//Iterator latLongIt = postcodeObserved.getLatLong().values().iterator();
    		//ArrayList<String> latLongArray = new ArrayList<String>();
    		//while(latLongIt.hasNext()) {
    		//	latLongArray.add(latLongIt.next().toString());
    		//}
    		
    		DecimalFormat df = new DecimalFormat("#.#####");
    		
    		Label postcodeName = new Label("Name: ");
    		Label latLabel = new Label("Postcode Latitude: " + df.format(postcodeObserved.getLatLong().get("lat")));
    		Label longLabel = new Label("Postcode Longitude: " + df.format(postcodeObserved.getLatLong().get("lon")));
    		Label postcodeDistance = new Label("Distance: " + df.format(distance)+ " Metres");

    		extraInfoDesign.add(postcodeName, 0, 1);
    		extraInfoDesign.add(name, 1, 1);
    		extraInfoDesign.add(latLabel, 0, 2);
    		extraInfoDesign.add(longLabel, 0, 3);
    		extraInfoDesign.add(postcodeDistance, 0, 5);
    		extraInfoDesign.add(editButton, 0, 6);

    		/*
             * edit button action event
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	String postcodeNameNorm = name.getText().replaceAll("\\s+", "");
                    	postcodeNameNorm.toUpperCase();
            			ArrayList<String> postcodes = new ArrayList<String>();
            			for(Postcode postcodeName : getServer().getPostcodes()) {
            				String nameToEnter = postcodeName.getName().replaceAll("\\s+", "");
            				nameToEnter.toUpperCase();
            				postcodes.add(nameToEnter);
            			}
            			if(postcodes.contains(postcodeNameNorm)) {
            				popUp("Cannot Change name to a pre-Existing Postcodes");
            			} 
                    	if(serverConnectionCheck(name.getText())) {
                    		popUp("Cannot connect to Server: Please enter a proper Postcode");
                    	}	
            			if(name.getText().trim().isEmpty()) {
            				popUp("Name must have A valid postcode");
            			} else {
                    	 postcodeObserved.setName(name.getText());
            			}
                    	 //getPostcodeList().remove(postcodeObserved);
    					//need to update ListView every time button is pressed
    					//popUp("Cannot connect to Server: Please enter a proper Postcode");
                    } catch(Exception e) {
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

		/*
		* need to deconstruct 'postcodeEntry' into an array and check if each
		* elemnt of the array follows the ordering:
		* LETTER, LETTER, NUMBER, SPACE, NUMBER, LETTER, LETTER
		*/
    protected void constructObject() {
    	try {
			ArrayList<String> postcodes = new ArrayList<String>();
			for(Postcode postcodeName : getServer().getPostcodes()) {
				String nameToEnter = postcodeName.getName().replaceAll("\\s+", "");
				nameToEnter.toUpperCase();
				postcodes.add(nameToEnter);
			}
			//ArrayList<String>
			//Object[] postcodeArray = postcodeEntry.getText().split("");
			if(postcodeEntry.getText().trim().isEmpty()) {
				popUp("Incompleted Field: make sure all Fields are complete");
			}
			if(postcodes.contains(postcodeEntry.getText())) {
				popUp("Cannot Add Duplicate Postcodes");
			} else {
    		String code = postcodeEntry.getText();
    		Postcode newPostcode = new Postcode(code,getServer().getRestaurant());
    		getPostcodesList().add(newPostcode);
    		getServer().addPostcode(code);
	    	} 
    	} catch(Exception e) {
    		popUp("Cannot connect to Server: Please enter a proper Postcode");
    	}
    }
  //need to prevent removal of a postcode if the postcode is being used by a supplier
    protected void removeObject() throws UnableToDeleteException  {
    		Postcode postcodeToRemove = viewPanel.getSelectionModel().getSelectedItem();
    		getPostcodesList().remove(postcodeToRemove);
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
    
    public boolean serverConnectionCheck(String newName) {
    	try {
    	StringBuilder address = new StringBuilder("https://www.southampton.ac.uk/~ob1a12/postcode/postcode.php?postcode=");
        //String newName = postcodeEntry.getText().replaceAll("\\s+","");
        address.append(newName);
        URL postcodeURL = new URL(address.toString());
        HttpURLConnection conn = (HttpURLConnection) postcodeURL.openConnection();
        if(conn.getResponseCode() == 200) {
        	return true;
        }
        return false;
    	}catch(Exception e) {
    		e.getStackTrace();
    	}
    	return false;
    }




}
