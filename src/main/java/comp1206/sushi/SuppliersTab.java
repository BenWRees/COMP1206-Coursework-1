package comp1206.sushi;

import java.util.ArrayList;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.common.UpdateListener;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.server.ServerWindow;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
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
import javafx.util.Duration;

public class SuppliersTab extends ServerWindow {
	private Tab suppliersTab;
	private Label suppliersNamePrompt;
	private TextField suppliersName;
	private Label suppliersPostcodePrompt;
	private ListView<Supplier> viewPanel;
	private VBox extraInfoPanel;
	private PostcodeTab postcodesTab;
	private ComboBox<Postcode> suppliersPostcode;
	private ArrayList<Supplier> suppliersInIngredient = new ArrayList<Supplier>();

	
	public SuppliersTab() {
        suppliersTab = new Tab();
        suppliersTab.setText("Suppliers");
        suppliersTab.setContent(suppliersTab());
	}
	
	public void suppliersTabContent() {
		suppliersTab.setContent(suppliersTab());
	}

    private Pane suppliersTab() {
        BorderPane mainSuppliersTab = new BorderPane();
        mainSuppliersTab.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        fieldsBox.setHgap(10);
        fieldsBox.setVgap(5);

        suppliersNamePrompt = new Label("Set Suppliers Name");
        suppliersName = new TextField();
        suppliersPostcodePrompt = new Label("Set Suppliers Postcode");
        
        
                
        //instantiateSuppliersPostcodeComboBox();
        //getSuppliersPostcodeComboBox().setPromptText("Suppliers Postcode");
        
		suppliersPostcode = new ComboBox<Postcode>(FXCollections.observableArrayList(getServer().getPostcodes()));
		suppliersPostcode.setPromptText("Suppliers Postcode");



        fieldsBox.add(suppliersNamePrompt,0,0);
        fieldsBox.add(suppliersName,0,1);
        fieldsBox.add(suppliersPostcodePrompt,1,0);
        fieldsBox.add(suppliersPostcode,1,1);
        entryBox.setTop(fieldsBox);

        mainSuppliersTab.setBottom(entryBox);
        mainSuppliersTab.setCenter(viewPanel);
        return mainSuppliersTab;
    }
    
    public Tab getTab() {
    	return suppliersTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Supplier>();
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
                    	ArrayList<String> suppliersPostcodes = new ArrayList<String>();
                    	for(Supplier supplier : getServer().getSuppliers()) {
                    		suppliersPostcodes.add(supplier.getPostcode().getName());
                    	}
                    	if(suppliersPostcodes.contains(suppliersPostcode.getSelectionModel().getSelectedItem().toString())) {
                    		popUp("Cannot add a Supplier with a Postcode already being used");
                    	} else {
    					constructObject();
    			    	suppliersName.setText("");
    			    	suppliersPostcode.getSelectionModel().select(null);
    			    	System.out.println(getServer().getSuppliers());
                    	}
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
                	ArrayList<String> suppliersInIngredient = new ArrayList<String>();
                	for(Ingredient ingredientsInServer : getServer().getIngredients()) {
                		suppliersInIngredient.add(ingredientsInServer.getSupplier().toString());
                	}
                	
                	if(suppliersInIngredient.contains(viewPanel.getSelectionModel().getSelectedItem().toString())) {
                		popUp("Cannot delete a Supplier being used by a Ingredient");
                	} else {
                		removeObject();
                	}
					System.out.println("Server: " + getServer().getSuppliers()); //DEBUG
					System.out.println("ViewPanel: " + getSuppliersList());
					System.out.println("UnableToDeleteValues: " + suppliersInIngredient.toString());
					
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
					System.out.println("Server: " + getServer().getSuppliers()); //DEBUG
					System.out.println("ViewPanel: " + getSuppliersList());
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
					System.out.println("ViewPanel: " + getSuppliersList()); 
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

        //setSuppliersList(FXCollections.observableArrayList(getServer().getSuppliers()));
        
        viewPanel.setItems(getSuppliersList());
        //viewPanel.setItems(FXCollections.observableArrayList(getServer().getSuppliers()));


        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }

    protected VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().isEmpty() == false) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);
    		
    		Button editButton = new Button("Edit");
    		
    		TextField supplierName = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());
    		Label name = new Label("Name: ");
    		
    		//This combo box repopulates appropriately
    		ComboBox<Postcode> supplierPostcode = new ComboBox<Postcode>(FXCollections.observableArrayList(getServer().getPostcodes()));
    		//throwing null point exception if left untouched
    		supplierPostcode.setPromptText(viewPanel.getSelectionModel().getSelectedItem().getPostcode().getName());
    		Label postcode = new Label("Postcode: ");
    		
    		
    		extraInfoDesign.add(name, 0, 0);
    		extraInfoDesign.add(supplierName, 1, 0);
    		extraInfoDesign.add(postcode, 0, 1);
    		extraInfoDesign.add(supplierPostcode, 1, 1);
    		extraInfoDesign.add(editButton, 0, 2);
    		
    		/*
             * edit button action event - if only textField edited then 
             */ 
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	Supplier selectedSupplier = viewPanel.getSelectionModel().getSelectedItem();	
                    	ArrayList<String> suppliersPostcodes = new ArrayList<String>();
                    	for(Supplier supplier : getServer().getSuppliers()) {
                    		suppliersPostcodes.add(supplier.getPostcode().getName());
                    	}
                    	if(suppliersPostcodes.contains(suppliersPostcode.getSelectionModel().getSelectedItem().toString())) {
                    		popUp("Cannot edit a Supplier with a Postcode already being used");
                    	}
                    	if(supplierName.getText() == null) {
                    		supplierName.setText(selectedSupplier.getName());
                    	}
                    	if(supplierPostcode.getSelectionModel().isEmpty()) {
                    		supplierPostcode.getSelectionModel().select(selectedSupplier.getPostcode());
                    	} else {
                    	selectedSupplier.setName(supplierName.getText());
                    	selectedSupplier.setPostcode(supplierPostcode.getSelectionModel().getSelectedItem()); 
                    	}
    				} catch (Exception e) {
    					if(viewPanel.getSelectionModel().isEmpty()) {
       					 //TODO Auto-generated catch block
       						popUp("Must Select an Object");
    					}
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
    
    protected void constructObject() {
		if(suppliersPostcode.getSelectionModel().isEmpty() || suppliersName.getText().trim().isEmpty()) {
			popUp("Incompleted Field: make sure all Fields are complete");
		} else {
			String name = suppliersName.getText();
			Postcode postcode = this.suppliersPostcode.getSelectionModel().getSelectedItem();
			Supplier newSupplier = new Supplier(name,postcode);
			System.out.println("Postcode: " + postcode.getName());
			getSuppliersList().add(newSupplier);
			getServer().addSupplier(name,postcode);
			//getPostcodesInSupplier().add(postcode);
		}
    }
    
    protected void removeObject()  {
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			popUp("No Object Selected: Please Select an Object");
		} else {
    		Supplier supplierToRemove = viewPanel.getSelectionModel().getSelectedItem();
    		getSuppliersList().remove(supplierToRemove);
    		getServer().getSuppliers().clear();
    		getServer().getSuppliers().addAll(getSuppliersList());
    		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
    			popUp("No Object Selected: Please Select an Object");
			}
    	}
    		
    	
    }
    
    protected void moveUp() { 
    	Supplier supplierToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int supplierIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(supplierIndexToMove == 0) {
    		getSuppliersList().remove(supplierIndexToMove);
        	getSuppliersList().add(getSuppliersList().size(), supplierToMove);
        	getServer().getSuppliers().remove(supplierIndexToMove);
        	getServer().getSuppliers().add(getServer().getSuppliers().size(), supplierToMove);
    	} else {
    		getSuppliersList().remove(supplierIndexToMove);
    		getSuppliersList().add(supplierIndexToMove-1, supplierToMove);
        	getServer().getSuppliers().remove(supplierIndexToMove);
        	getServer().getSuppliers().add(supplierIndexToMove-1, supplierToMove);
    	}
    }
    
    protected void moveDown() {
    	Supplier supplierToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int supplierIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(supplierIndexToMove == getSuppliersList().size()-1) {
    		getSuppliersList().remove(supplierIndexToMove);
    		getSuppliersList().add(0,supplierToMove);
        	getServer().getSuppliers().remove(supplierIndexToMove);
        	getServer().getSuppliers().add(0,supplierToMove);
    	} else {
    		getSuppliersList().remove(supplierIndexToMove);
    		getSuppliersList().add(supplierIndexToMove+1, supplierToMove);
        	getServer().getSuppliers().remove(supplierIndexToMove);
        	getServer().getSuppliers().add(supplierIndexToMove+1, supplierToMove);
    	}
    }
    
    public void refresh() {
    	Timeline timeline = new Timeline();
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(5000), (actionEvent) -> {
    		 Postcode currentPostcode = suppliersPostcode.getSelectionModel().getSelectedItem();
    		 suppliersPostcode = new ComboBox<Postcode>(FXCollections.observableArrayList(getServer().getPostcodes()));
    			suppliersPostcode.setPromptText("Suppliers Postcode");
    	 }, null, null));
    	 timeline.play();
    	 
    }
    
    /*
     * adds all the postcodes that are being used by a 
     */
    public void updatePostcodesArrayList() {
    	
    }
    
      
}


