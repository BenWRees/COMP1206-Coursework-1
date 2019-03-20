package comp1206.sushi;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.html.HTMLDocument.Iterator;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import comp1206.sushi.server.ServerWindow;
import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.common.UpdateListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
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

public class IngredientsTab extends ServerWindow {
	private Tab ingredientsTab;
	private Label ingredientNamePrompt;
	private TextField ingredientName;
	private Label unitOfIngredientPrompt;
	private TextField unitOfIngredient;
	private Label ingredientSupplierPrompt;
	private ComboBox<Supplier> ingredientSupplier;
	private Label ingredientRestockThresholdPrompt;
	private ComboBox<Number> ingredientRestockThreshold;
	private Label ingredientRestockAmountPrompt;
	private ComboBox<Number> ingredientRestockAmount;
	private ListView<Ingredient> viewPanel;
	private VBox extraInfoPanel;
	
	public IngredientsTab() {
        ingredientsTab = new Tab();
        ingredientsTab.setText("Ingredients");
        ingredientsTab.setContent(ingredientsTab());
	}
	
	public void ingredientsTabContent() {
        ingredientsTab.setContent(ingredientsTab());
	}
	
    private Pane ingredientsTab() {
        BorderPane mainIngredientsTab = new BorderPane();
        mainIngredientsTab.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();
        
        ArrayList<Integer> comboBoxIntegers = new ArrayList<Integer>();
        comboBoxIntegers = comboBoxInteger();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();
        fieldsBox.setHgap(10);
        fieldsBox.setVgap(5);

        ingredientNamePrompt = new Label("Set Ingredient Name");
        ingredientName = new TextField();
        
        unitOfIngredientPrompt = new Label("Set Unit of Ingredient");
        unitOfIngredient = new TextField();
        
        ingredientSupplierPrompt = new Label("Set Ingredient Supplier");
        ingredientSupplier = new ComboBox(FXCollections.observableArrayList(getServer().getSuppliers()));
        ingredientSupplier.setPromptText("Ingredient Supplier");
        
        ingredientRestockThresholdPrompt = new Label("Set Ingredient Restock Threshold");
        ingredientRestockThreshold = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
        ingredientRestockThreshold.setPromptText("Ingredient Restock Threshold");
        
        ingredientRestockAmountPrompt = new Label("Set Ingredient Restock Amount");
        ingredientRestockAmount = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
        ingredientRestockAmount.setPromptText("Ingredient Restock Amount");

        fieldsBox.add(ingredientNamePrompt,0,0);
        fieldsBox.add(ingredientName,0,1);
        fieldsBox.add(unitOfIngredientPrompt,1,0);
        fieldsBox.add(unitOfIngredient,1,1);
        fieldsBox.add(ingredientSupplierPrompt,2,0);
        fieldsBox.add(ingredientSupplier,2,1);
        fieldsBox.add(ingredientRestockThresholdPrompt,0,2);
        fieldsBox.add(ingredientRestockThreshold,0,3);
        fieldsBox.add(ingredientRestockAmountPrompt,1,2);
        fieldsBox.add(ingredientRestockAmount,1,3);

        entryBox.setTop(fieldsBox);

        mainIngredientsTab.setBottom(entryBox);
        mainIngredientsTab.setCenter(viewPanel);
        return mainIngredientsTab;
    }
    
    public Tab getTab() {
    	return ingredientsTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Ingredient>();
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
    			    	ingredientName.setText("");
    			    	unitOfIngredient.setText("");
    			    	ingredientSupplier.getSelectionModel().select(null);;
    			    	ingredientRestockAmount.getSelectionModel().select(null);
    			    	ingredientRestockThreshold.getSelectionModel().select(null);;
    			    	System.out.println("Server: " + getServer().getIngredients());
    					System.out.println("ViewPanel: " + getIngredientsList()); //DEBUG
    				} catch (Exception e) {
    					if(ingredientName.getText().trim().isEmpty() ||
    			    		unitOfIngredient.getText().trim().isEmpty() ||
    			    		ingredientSupplier.getSelectionModel().isEmpty() ||
    			    		ingredientRestockThreshold.getSelectionModel().isEmpty() ||
    			    		ingredientRestockAmount.getSelectionModel().isEmpty()) {
    							popUp("Incompleted Field: make sure all Fields are complete");
    							//ISSUE - ADDING UNFINISHED ELEMENT TO ARRAY 
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
                	/*
                	 * populate an arrayList with all the ingredients in all the dishes
                	 */
                	ArrayList<Ingredient> ingredientsInDish = new ArrayList<Ingredient>();
                	ArrayList<String> ingredientsInDishString = new ArrayList<String>();
                	
                	//iterating through all the dishes in the server
                	for(Dish dishesInServer : getServer().getDishes()) {
                		ingredientsInDish.addAll(dishesInServer.getRecipe().keySet());
                		
                	}
                	for(Ingredient ingredientInIngredientsInDish : ingredientsInDish) {
                		ingredientsInDishString.add(ingredientInIngredientsInDish.getName());
                	}
            		if(ingredientsInDishString.contains(viewPanel.getSelectionModel().getSelectedItem().toString())) {
            			popUp("Cannot Delete an Ingredient being used by a Dish");
            		} else {
                	//need to work out how to remove selected object
                	removeObject();
            		}
					//need to update ListView every time button is pressed
				} 
                catch (Exception e) {
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
					System.out.println("ViewPanel: " + getIngredientsList()); 
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
					System.out.println("ViewPanel: " + getIngredientsList()); 
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

        //setIngredientsList(FXCollections.observableArrayList(getServer().getIngredients()));
        
        viewPanel.setItems(getIngredientsList());

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
    		
    		Ingredient ingredientObserved = viewPanel.getSelectionModel().getSelectedItem();
    		
    		Button editButton = new Button("Edit");
    		
    		ArrayList<Integer> comboBoxPopulation = new ArrayList<Integer>();
    		comboBoxPopulation = comboBoxInteger();
    		
    		TextField ingredientName = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());
    		TextField ingredientUnit = new TextField(viewPanel.getSelectionModel().getSelectedItem().getUnit());
    		ComboBox<Supplier> ingredientSupplier = new ComboBox<Supplier>(FXCollections.observableArrayList(getServer().getSuppliers()));
    		ingredientSupplier.setPromptText(viewPanel.getSelectionModel().getSelectedItem().getSupplier().getName());
    		ComboBox<Number> ingredientRestockThreshold = new ComboBox<Number>(FXCollections.observableArrayList(comboBoxPopulation));
    		ingredientRestockThreshold.setPromptText(getServer().getRestockThreshold(viewPanel.getSelectionModel().getSelectedItem()).toString());
    		ComboBox<Number> ingredientRestockAmount = new ComboBox<Number>(FXCollections.observableArrayList(comboBoxPopulation));
    		ingredientRestockAmount.setPromptText(getServer().getRestockAmount(viewPanel.getSelectionModel().getSelectedItem()).toString());
    		
    		Label name = new Label("Name: ");
    		Label unit = new Label("Unit: ");
    		Label supplier = new Label("Supplier: ");
    		Label restockThreshold = new Label("Restock Threshold: ");
    		Label restockAmount = new Label("Restock Amount: "); 
    		
    		extraInfoDesign.add(name, 0, 0);
    		extraInfoDesign.add(ingredientName, 1, 0);
    		extraInfoDesign.add(unit, 0, 1);
    		extraInfoDesign.add(ingredientUnit, 1, 1);
    		extraInfoDesign.add(supplier, 0, 2);
    		extraInfoDesign.add(ingredientSupplier, 1, 2);
    		extraInfoDesign.add(restockThreshold, 0, 3);
    		extraInfoDesign.add(ingredientRestockThreshold, 1, 3);
    		extraInfoDesign.add(restockAmount, 0, 4);
    		extraInfoDesign.add(ingredientRestockAmount, 1, 4);
    		extraInfoDesign.add(editButton, 0, 5);
    		
    		/*
             * edit button action event
             * NEED TO SET RESTOCK LEVELS AND SET STOCK
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	String ingredientNameNorm = ingredientName.getText().replaceAll("\\s+", "");
                    	ingredientNameNorm.toUpperCase();
                		ArrayList<String> ingredients = new ArrayList<String>();
                		for(Ingredient ingredientName : getServer().getIngredients()) {
                			String nameToEnter = ingredientName.getName().replaceAll("\\s+", "");
                			nameToEnter.toUpperCase();
                			ingredients.add(nameToEnter);
                		}
                    	if(ingredientName.getText() == null) {
                    		ingredientName.setText(viewPanel.getSelectionModel().getSelectedItem().getName());
                    	}
                    	if(ingredientUnit.getText() == null) {
                    		ingredientUnit.setText(viewPanel.getSelectionModel().getSelectedItem().getUnit());
                    	}
                    	if(ingredientSupplier.getSelectionModel().isEmpty()) {
                    		ingredientSupplier.getSelectionModel().select(viewPanel.getSelectionModel().getSelectedItem().getSupplier());
                    	}
                    	if(ingredientRestockThreshold.getSelectionModel().isEmpty()) {
                    		ingredientRestockThreshold.getSelectionModel().select(getServer().getRestockThreshold(viewPanel.getSelectionModel().getSelectedItem()));
                    	}
                    	if(ingredientRestockAmount.getSelectionModel().isEmpty()) {
                    		ingredientRestockAmount.getSelectionModel().select(getServer().getRestockAmount(viewPanel.getSelectionModel().getSelectedItem()));
                    	}
                    	if(!viewPanel.getSelectionModel().getSelectedItem().getName().equals(ingredientName.getText())) {
                    		if(ingredients.contains(ingredientNameNorm)) {
                    			popUp("Cannot add Duplicate Ingredients");
                    		}
                    	}
                    	 ingredientObserved.setName(ingredientName.getText());
                    	 ingredientObserved.setUnit(ingredientUnit.getText());
                    	 ingredientObserved.setSupplier(ingredientSupplier.getSelectionModel().getSelectedItem());
                    	 ingredientObserved.setRestockThreshold(ingredientRestockThreshold.getSelectionModel().getSelectedItem());
                    	 ingredientObserved.setRestockAmount(ingredientRestockAmount.getSelectionModel().getSelectedItem());
                    	 getSuppliersInIngredient().remove(ingredientObserved.getSupplier());
                    	 getSuppliersInIngredient().add(ingredientSupplier.getSelectionModel().getSelectedItem());
                    	 getServer().setRestockLevels(ingredientObserved, ingredientRestockThreshold.getSelectionModel().getSelectedItem(), ingredientRestockAmount.getSelectionModel().getSelectedItem());
    					//need to update ListView every time button is pressed
                    	   getServer().setStock(ingredientObserved, ingredientRestockAmount.getSelectionModel().getSelectedItem());
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
    	String ingredientNameNorm = ingredientName.getText().replaceAll("\\s+", "");
    	ingredientNameNorm.toUpperCase();
		ArrayList<String> ingredients = new ArrayList<String>();
		for(Ingredient ingredientName : getServer().getIngredients()) {
			String nameToEnter = ingredientName.getName().replaceAll("\\s+", "");
			nameToEnter.toUpperCase();
			ingredients.add(nameToEnter);
		}
		if(ingredientName.getText().trim().isEmpty() ||
    			unitOfIngredient.getText().trim().isEmpty() ||
    			ingredientSupplier.getSelectionModel().isEmpty() ||
    			ingredientRestockThreshold.getSelectionModel().isEmpty() ||
    			ingredientRestockAmount.getSelectionModel().isEmpty()) {
    				popUp("Incompleted Field: make sure all Fields are complete");
    	} 
		if(ingredients.contains(ingredientNameNorm)) {
			popUp("Cannot add Duplicate Ingredients");
		} else {
    			String name = ingredientName.getText();
    			String unit = unitOfIngredient.getText();
    			Supplier supplier = ingredientSupplier.getSelectionModel().getSelectedItem();
    			Number restockThreshold = ingredientRestockThreshold.getSelectionModel().getSelectedItem();
    			Number restockAmount = ingredientRestockAmount.getSelectionModel().getSelectedItem();
    			Ingredient newIngredient = new Ingredient(name,unit,supplier,restockThreshold,restockAmount);
    			getIngredientsList().add(newIngredient);
    			getServer().addIngredient(name,unit,supplier,restockThreshold,restockAmount);
    		}
		System.out.println(ingredients);
    }
     
    //having same issue with server and view panel, discrepancies in what is being removed
    //Should prevent ingredients being removed if they exist in a dish 
    protected void removeObject() throws UnableToDeleteException {
		
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			popUp("No Object Selected: Please Select an Object");
		} else {
    		Ingredient ingredientToRemove = viewPanel.getSelectionModel().getSelectedItem();
    		getIngredientsList().remove(ingredientToRemove);
    		getServer().getIngredients().clear();
    		getServer().getIngredients().addAll(getIngredientsList());
		}
    }
    
    protected void moveUp() {
    	Ingredient ingredientToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int ingredientIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(ingredientIndexToMove == 0) {
    		getIngredientsList().remove(ingredientIndexToMove);
        	getIngredientsList().add(getIngredientsList().size(), ingredientToMove);
        	getServer().getIngredients().remove(ingredientIndexToMove);
        	getServer().getIngredients().add(getServer().getIngredients().size(), ingredientToMove);
    	} else {
    		getIngredientsList().remove(ingredientIndexToMove);
    		getIngredientsList().add(ingredientIndexToMove-1, ingredientToMove);
        	getServer().getIngredients().remove(ingredientIndexToMove);
        	getServer().getIngredients().add(ingredientIndexToMove-1, ingredientToMove);
    	}
    }
    
    protected void moveDown() {
    	Ingredient ingredientToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int ingredientIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(ingredientIndexToMove == getIngredientsList().size()-1) {
    		getIngredientsList().remove(ingredientIndexToMove);
    		getIngredientsList().add(0,ingredientToMove);
        	getServer().getIngredients().remove(ingredientIndexToMove);
        	getServer().getIngredients().add(0,ingredientToMove);
    	} else {
    		getIngredientsList().remove(ingredientIndexToMove);
    		getIngredientsList().add(ingredientIndexToMove+1, ingredientToMove);
        	getServer().getIngredients().remove(ingredientIndexToMove);
        	getServer().getIngredients().add(ingredientIndexToMove+1, ingredientToMove);
    	}
    }
    
    public ObservableList<Ingredient> getModelViewList() {
    	return getIngredientsList();
    }
	   

}
