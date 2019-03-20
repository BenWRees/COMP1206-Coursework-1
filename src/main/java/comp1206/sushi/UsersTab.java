package comp1206.sushi;

import java.util.ArrayList;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.common.User;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.UpdateListener;
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
import javafx.scene.control.ComboBox;
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
import javafx.scene.control.PasswordField;

public class UsersTab extends ServerWindow {
	private Tab usersTab;
	private ObservableList<User> modelViewLists;
	private ListView<User> viewPanel;
	private Label userNamePrompt;
	private TextField userName;
	private Label userPasswordPrompt;
	private PasswordField userPassword;
	private Label userAddressPrompt;
	private TextField userAddress;
	private Label userPostcodePrompt;
	private ComboBox<Postcode> userPostcode;
	private VBox extraInfoPanel;

	
	public UsersTab() {
        usersTab = new Tab();
        usersTab.setText("Users");
        usersTab.setContent(usersTab());
	}
	
	public void usersTabContent() {
		usersTab.setContent(usersTab());
	}
	
    private Pane usersTab() {
        BorderPane mainUsersPanel = new BorderPane();
        mainUsersPanel.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = viewPanel();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        fieldsBox.setVgap(7);
        fieldsBox.setHgap(7);
        fieldsBox.setPadding(new Insets(5,5,5,5));
        
        userNamePrompt = new Label("Enter Name");
        userName = new TextField();
        userPasswordPrompt = new Label("Enter Password");
        userPassword = new PasswordField();
        userAddressPrompt = new Label("Enter Address");
        userAddress = new TextField();
        userPostcodePrompt = new Label("Choose Postcode");
        userPostcode = new ComboBox(FXCollections.observableArrayList(getServer().getPostcodes()));
        userPostcode.setPromptText("Postcode");
        
        entryBox.setTop(fieldsBox);
        fieldsBox.add(userNamePrompt, 0, 0);
        fieldsBox.add(userName, 0, 1);
        fieldsBox.add(userPasswordPrompt, 1, 0);
        fieldsBox.add(userPassword, 1, 1);
        fieldsBox.add(userAddressPrompt, 2, 0);
        fieldsBox.add(userAddress, 2, 1);
        fieldsBox.add(userPostcodePrompt, 3, 0);
        fieldsBox.add(userPostcode, 3, 1);

        

        mainUsersPanel.setBottom(entryBox);
        mainUsersPanel.setCenter(viewPanel);
        return mainUsersPanel;

    }
    

    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<User>();
        viewPanel.setOrientation(Orientation.VERTICAL);
        HBox controlPanel = new HBox(7);
               
        extraInfoPanel = new VBox(7);
        extraInfoPanel.setPadding(new Insets(10,5,7,15));
        viewPanel.setPadding(new Insets(10,5,5,15));
        controlPanel.setPadding(new Insets(10,0,10,15));
        
        modelViewLists = FXCollections.observableArrayList(getServer().getUsers());
        
        viewPanel.setItems(modelViewLists);

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
    					//if(suppliersName.getText().trim().isEmpty() ||
    							//suppliersPostcode.getSelectionModel().getSelectedItem() == null) {
    						popUp("Incompleted Field: make sure all Fields are complete");
    					//}
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
                	//need to work out how to remove selected object else {
                		removeObject();
					
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
//        getServer().addUpdateListener((UpdateListener) moveDownButton);

        
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
        
        viewPanel.setItems(modelViewLists);        

        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }
    
    protected VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		String userName = viewPanel.getSelectionModel().getSelectedItem().getName(); 
    		String userDistance = viewPanel.getSelectionModel().getSelectedItem().getDistance().toString();
    		String userPostcode = viewPanel.getSelectionModel().getSelectedItem().getPostcode().getName();
    		Label name = new Label("Name: " + userName);
    		Label distance = new Label("Distance: " + userDistance);
    		Label postcode = new Label("Postcode: "+ userPostcode);
    		extraInfoPanel.getChildren().addAll(name,distance,postcode);
    		return extraInfoPanel;
    	}
    	return extraInfoPanel;
    }
    
    public Tab getTab() {
    	return usersTab;
    }
    
    protected void constructObject()  {
    	if(userName.getText().trim().isEmpty() || userPassword.getText().trim().isEmpty()
    		|| userAddress.getText().trim().isEmpty() || userPostcode.getSelectionModel().isEmpty()) {
    		popUp("Incompleted Field: make sure all Fields are complete");
    	} else {
    		String username = userName.getText();
    		String password = userPassword.getText();
    		String address = userAddress.getText();
    		Postcode postcode = userPostcode.getSelectionModel().getSelectedItem();
    		User newUser = new User(username,password,address,postcode);
    		modelViewLists.add(newUser);
    	}
    	
    }
    
  //need to prevent removal of a postcode if the postcode is being used by a supplier
    protected void removeObject() throws UnableToDeleteException  {
		if(viewPanel.getSelectionModel().isEmpty()) {
			popUp("No Object Selected: Please Select an Object");
		} else {
    		User userToRemove = viewPanel.getSelectionModel().getSelectedItem();
    		modelViewLists.remove(userToRemove);
    		getServer().getUsers().clear();
    		getServer().getUsers().addAll(modelViewLists);
    	}
    }
    
    protected void moveUp() {
    	User userToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int userIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(userIndexToMove == 0) {
        	modelViewLists.remove(userIndexToMove);
        	modelViewLists.add(modelViewLists.size(), userToMove);
        	getServer().getUsers().remove(userIndexToMove);
        	getServer().getUsers().add(getServer().getUsers().size(), userToMove);
    	} else {
        	modelViewLists.remove(userIndexToMove);
        	modelViewLists.add(userIndexToMove-1, userToMove);
        	getServer().getUsers().remove(userIndexToMove);
        	getServer().getUsers().add(userIndexToMove-1, userToMove);
    	}
    }
    
    protected void moveDown() {
    	User userToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int userIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(userIndexToMove == modelViewLists.size()-1) {
        	modelViewLists.remove(userIndexToMove);
        	modelViewLists.add(0,userToMove);
        	getServer().getUsers().remove(userIndexToMove);
        	getServer().getUsers().add(0,userToMove);
    	} else {
        	modelViewLists.remove(userIndexToMove);
        	modelViewLists.add(userIndexToMove+1, userToMove);
        	getServer().getUsers().remove(userIndexToMove);
        	getServer().getUsers().add(userIndexToMove+1, userToMove);
    	}
    }
}
