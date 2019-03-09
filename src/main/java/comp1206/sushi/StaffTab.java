package comp1206.sushi;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.common.Drone;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Staff;
import javafx.event.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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

public class StaffTab extends ServerApplication {
    private Tab staffTab; 
    private Label staffNamePrompt;
    private TextField staffName;
    private ObservableList<Staff> modelViewLists;
    private ListView<Staff> viewPanel;
    private VBox extraInfoPanel;


    
    StaffTab() {
        staffTab = new Tab();
        staffTab.setText("Staff");
        staffTab.setContent(staffTab());
    }
    
	public void staffTabContent() {
		staffTab.setContent(staffTab());
	}
    

    private Pane staffTab() {
        BorderPane mainStaffTab = new BorderPane();
        mainStaffTab.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();

        staffNamePrompt = new Label("Set Staff Name");
        staffName = new TextField();
        
        fieldsBox.add(staffNamePrompt,0,0);
        fieldsBox.add(staffName,0,1);
        entryBox.setTop(fieldsBox);

        mainStaffTab.setBottom(entryBox);
        mainStaffTab.setCenter(viewPanel);
        return mainStaffTab;
    }

    public Tab getTab() {
    	return staffTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Staff>();
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
    					staffName.setText("");
    					System.out.println("Server: " + getServer().getStaff());
    					System.out.println("ViewPanel: " + modelViewLists);//DEBUG
    				} catch (Exception e) {
    					if(staffName.getText().trim().isEmpty()) {
    						popUp("Incompleted Field: make sure all Fields are complete");
    					}
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
                	removeObject();
					System.out.println("Server: "  + getServer().getStaff()); //DEBUG
					System.out.println("ViewPanel: " + modelViewLists); 
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

        
        modelViewLists = FXCollections.observableArrayList(getServer().getStaff());
        viewPanel.setItems(modelViewLists);

        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    } 
    
    protected VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);
    		
    		Button editButton = new Button("Edit");
    		
    		Staff staffObserved = viewPanel.getSelectionModel().getSelectedItem();
    		
    		TextField name = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());
    		String fatigue = viewPanel.getSelectionModel().getSelectedItem().getFatigue().toString();
    		String status = viewPanel.getSelectionModel().getSelectedItem().getStatus();
    		Label staffName = new Label("Name: ");
    		Label staffFatigue = new Label("Fatigue: " + fatigue);
    		Label staffStatus = new Label("Status: " + status);
    		
    		extraInfoDesign.add(staffName, 0, 0);
    		extraInfoDesign.add(name, 1, 0);
    		extraInfoDesign.add(staffFatigue, 0, 1);
    		extraInfoDesign.add(staffStatus, 0, 2);
    		extraInfoDesign.add(editButton, 0, 3);
    		
    		/*
             * edit button action event
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	 staffObserved.setName(name.getText());
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
    //this is fine - NO ISSUES
    protected void constructObject() {
    	if(staffName.getText().trim().isEmpty()) {
    		popUp("Incompleted Field: make sure all Fields are complete");
    		} else {
    			String name = staffName.getText();
    			getServer().addStaff(name);
    			Staff newStaff = new Staff(name);
    			modelViewLists.add(newStaff);
    		}
    }
    //
    protected void removeObject() {
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			popUp("No Object Selected: Please Select an Object");
		}
    	Staff staffMemberToRemove = viewPanel.getSelectionModel().getSelectedItem(); 
    	modelViewLists.remove(staffMemberToRemove);
    	int actualIndex= viewPanel.getSelectionModel().getSelectedIndex() + 1;
		if(actualIndex == 1) {
			actualIndex = 0;
		}
		Staff actualStaffMemberToRemove = getServer().getStaff().get(actualIndex);
    	if(viewPanel.getSelectionModel().isEmpty()== false) {
			getServer().removeStaff(actualStaffMemberToRemove);
		}
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			popUp("No Object Selected: Please Select an Object");
		}
    }
    
    protected void moveUp() {
    	Staff staffToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int staffIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(staffIndexToMove == 0) {
        	modelViewLists.remove(staffIndexToMove);
        	modelViewLists.add(modelViewLists.size(), staffToMove);
        	getServer().getStaff().remove(staffIndexToMove);
        	getServer().getStaff().add(getServer().getStaff().size(), staffToMove);
    	} else {
        	modelViewLists.remove(staffIndexToMove);
        	modelViewLists.add(staffIndexToMove-1, staffToMove);
        	getServer().getStaff().remove(staffIndexToMove);
        	getServer().getStaff().add(staffIndexToMove-1, staffToMove);
    	}
    }
    
    protected void moveDown() {
    	Staff staffToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int staffIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(staffIndexToMove == modelViewLists.size()-1) {
        	modelViewLists.remove(staffIndexToMove);
        	modelViewLists.add(0,staffToMove);
        	getServer().getStaff().remove(staffIndexToMove);
        	getServer().getStaff().add(0,staffToMove);
    	} else {
        	modelViewLists.remove(staffIndexToMove);
        	modelViewLists.add(staffIndexToMove+1, staffToMove);
        	getServer().getStaff().remove(staffIndexToMove);
        	getServer().getStaff().add(staffIndexToMove+1, staffToMove);
    	}
    }

}
