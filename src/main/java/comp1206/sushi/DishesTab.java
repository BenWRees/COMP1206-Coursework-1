package comp1206.sushi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import comp1206.sushi.ServerApplication;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Dish;
import comp1206.sushi.mock.MockServer;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.control.ListCell;

public class DishesTab extends ServerApplication {
	private Tab dishesTab;
	private Label dishNamePrompt;
	private TextField dishName;
	private Label ingredientNamePrompt;
	private ComboBox<Ingredient> ingredientName;
	private Label ingredientQuantityPrompt;
	private ComboBox<Integer> ingredientQuantity;
	private Button addIngredient;
	private Button removeIngredient;
	private Label dishPricePrompt;
	private TextField dishPrice;
	private Label restockThresholdPrompt;
	private ComboBox<Integer> restockThreshold;
	private Label restockAmountPrompt;
	private ComboBox<Integer> restockAmount;
	private ArrayList<Recipe> dishRecipe;
	private HashMap<Ingredient, Number> recipe;
	private ListView<Dish> viewPanel;
	private VBox extraInfoPanel;
	private ListView<Recipe> recipeList;
	private ArrayList<Ingredient> bannedIngredients;
	
	DishesTab() {
        dishesTab = new Tab();
        dishesTab.setText("Dishes");
        dishesTab.setContent(dishesTab());
	}
	
	public void dishesTabContent() {
        dishesTab.setContent(dishesTab());
	}
	
    private Pane dishesTab() {
        BorderPane mainDishesPanel = new BorderPane();
        mainDishesPanel.setPadding(new Insets(0,0,0,0));
        BorderPane viewPanel = this.viewPanel();
        
        ArrayList<Integer> comboBoxIntegers = new ArrayList<Integer>(); 
        comboBoxIntegers = comboBoxInteger();
        
        bannedIngredients = new ArrayList<Ingredient>();
        
        dishRecipe = new ArrayList<Recipe>();

        BorderPane entryBox = new BorderPane();
        entryBox.setPadding(new Insets(10,0,10,20));
        GridPane fieldsBox = new GridPane();

        /*
            when a pre-existed ingredient is clicked on, is selected in 'ingredientName'
         */
        ListView<Recipe> recipeList = new ListView<Recipe>();
        recipeList.setOrientation(Orientation.VERTICAL);
        recipeList.setPrefHeight(150);
        recipeList.setPadding(new Insets(5,5,5,5));
        recipeList.setCellFactory(lv -> new RecipeCell());       
        recipeList.setItems(FXCollections.observableArrayList(dishRecipe));
        
       
        ObservableList<Ingredient> ingredientsComboBox = FXCollections.observableArrayList(getServer().getIngredients());
        ArrayList<Number> ingredientQuantityList = new ArrayList<Number>();
        //Integer quantityRestockLimit = (Integer) ingredientName.getSelectionModel().getSelectedItem().getRestockThreshold();
        //for(int i=0;i <= quantityRestockLimit; i++) {
        	//ingredientQuantityList.add(i);
        //}
        //ObservableList<Number> ingredientQuantityComboBox = FXCollections.observableArrayList(ingredientQuantityList);
        ObservableList<Number> ingredientQuantityComboBox = FXCollections.observableArrayList(comboBoxIntegers);

 
        //recipeList.setItems(dishRecipe); 

        dishNamePrompt = new Label("Enter Dish Name");
        dishName = new TextField();
        ingredientNamePrompt = new Label("Enter Ingredient Name");
        dishPricePrompt = new Label("Set Dish Price");
        dishPrice = new TextField();
        restockThresholdPrompt = new Label("Enter Restock Threshold");
        restockThreshold = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
        restockThreshold.setPromptText("Dish Restock Threshold");
        restockAmountPrompt = new Label("Enter Restock Amount");
        restockAmount = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
        restockAmount.setPromptText("Dish Restock Amount");
        ingredientName = new ComboBox(ingredientsComboBox);
        ingredientName.setPromptText("Ingredient Name");
        ingredientQuantityPrompt = new Label("Enter Ingredient Quantity");
        ingredientQuantity = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
        ingredientQuantity.setPromptText("Ingredient Quantity");
        
        //when button clicked will add to HashMap 'recipe' with key(ingredientName) and value(ingredientQuantity)
        addIngredient = new Button("Add Ingredient");
        removeIngredient = new Button("Remove Ingredient");
        
        /*
         * add ingredient event listener - need to prevent an ingredient being used more than once:
         * - tell combo box to remove option of ingredient after it has been selected
         * - OR pass popUp() when object is selected again
         */
        EventHandler<ActionEvent> addIngredientListener = new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                	if(ingredientName.getSelectionModel().isEmpty() || ingredientQuantity.getSelectionModel().isEmpty()) {
                		popUp("Incompleted Field: make sure all Fields are complete");
                	} if(bannedIngredients.contains(ingredientName.getSelectionModel().getSelectedItem())) {
                		popUp("Cannot use Ingredient more than once in Recipe");
                    	dishRecipe.remove(dishRecipe.size());
                    	bannedIngredients.remove(bannedIngredients.size());
                    	recipeList.setItems(FXCollections.observableArrayList(dishRecipe));
                	} if(ingredientQuantity.getSelectionModel().getSelectedItem() >  ingredientName.getSelectionModel().getSelectedItem().getRestockAmount().intValue()) {
                		popUp("Cannot add an Ingredient that uses" + "\n" + "more than the Ingredients Restock Threshold");
                	}
                	else {
                		Ingredient ingredientToAdd = ingredientName.getSelectionModel().getSelectedItem();
                		bannedIngredients.add(ingredientToAdd);
                		Number numberToAdd = ingredientQuantity.getSelectionModel().getSelectedItem();
                		Recipe recipeToAdd = new Recipe(ingredientToAdd,numberToAdd);
                		dishRecipe.add(recipeToAdd);
                		recipeList.setItems(FXCollections.observableArrayList(dishRecipe));
                    	ingredientName.getSelectionModel().selectFirst();
                    	for(Recipe currentRecipe : dishRecipe) {
                    		System.out.println(currentRecipe.getIngredient());
                    		System.out.println(currentRecipe.getQuantity());
                    	}
                    	System.out.println(" ");                 
                	}
					//need to update ListView every time button is pressed
				} catch (Exception e) {
					System.err.println(e);
				}
            }
        };
        
       addIngredient.setOnAction(addIngredientListener);
       
       /*
        * remove ingredient event listener
        */
       EventHandler<ActionEvent> removeIngredientListener = new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
               try {
           		if(recipeList.getSelectionModel().isEmpty()) {
        			popUp("No Ingredient from the Recipe Selected:" + "\n" + "Please Select an Object from the Recipe List");
        		} else {
               	Recipe recipeToRemove = recipeList.getSelectionModel().getSelectedItem();
            	dishRecipe.remove(recipeToRemove);
            	bannedIngredients.remove(recipeToRemove);
            	recipeList.setItems(FXCollections.observableArrayList(dishRecipe));
        		}
               } catch (Exception e) {
				//need to update ListView every time button is pressed
					System.err.println(e);
				}
           }
       };
       
      removeIngredient.setOnAction(removeIngredientListener);

        fieldsBox.setVgap(5);
        fieldsBox.setHgap(7);

        fieldsBox.add(dishNamePrompt,0,0);
        fieldsBox.add(dishName,0,1);
        fieldsBox.add(ingredientNamePrompt,1,0);
        fieldsBox.add(ingredientName,1,1);
        fieldsBox.add(ingredientQuantityPrompt,2,0);
        fieldsBox.add(ingredientQuantity,2,1);
        fieldsBox.add(addIngredient,3,1);
        fieldsBox.add(removeIngredient, 3, 2);
        fieldsBox.add(dishPricePrompt,0,2);
        fieldsBox.add(dishPrice,0,3);
        fieldsBox.add(restockThresholdPrompt,1,2);
        fieldsBox.add(restockThreshold, 1, 3);
        fieldsBox.add(restockAmountPrompt, 2, 2);
        fieldsBox.add(restockAmount, 2, 3);

        entryBox.setTop(fieldsBox);
        entryBox.setBottom(recipeList);

        mainDishesPanel.setBottom(entryBox);
        mainDishesPanel.setCenter(viewPanel);
        return mainDishesPanel;
    }
    
    public Tab getTab() {
    	return dishesTab;
    }
    
    protected BorderPane viewPanel() {
        BorderPane topPanel = new BorderPane();
        viewPanel = new ListView<Dish>();
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
    			    	dishName.setText("");
    			    	dishPrice.setText("");
    			    	restockThreshold.getSelectionModel().select(null);
    			    	restockAmount.getSelectionModel().select(null);
    			    	System.out.println("Server: " + getServer().getDishes());
    					System.out.println("ViewPanel: " + getDishesList()); //DEBUG
    					//System.out.println("RECIPE: " + recipeList.getSelectionModel().toString());
    				} catch (Exception e) {
    					if(dishName.getText().trim().isEmpty() || restockThreshold.getSelectionModel().isEmpty() ||
    			    		restockAmount.getSelectionModel().isEmpty()) {
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
					System.out.println("Server: " + getServer().getDishes()); //DEBUG
					System.out.println("ViewPanel: " + getDishesList()); 
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
					System.out.println("Server: " + getServer().getDishes()); //DEBUG
					System.out.println("ViewPanel: " + getDishesList()); 
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
					System.out.println("Server: " + getServer().getDishes()); //DEBUG
					System.out.println("ViewPanel: " + getDishesList()); 
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
        
        //ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>()
        //setDishesList(FXCollections.observableArrayList(getServer().getDishes()));
        
        viewPanel.setItems(getDishesList());

        topPanel.setPrefSize(300, 200);

        topPanel.setCenter(viewPanel);
        topPanel.setBottom(controlPanel);
        topPanel.setRight(extraInfoPanel);

        return topPanel;
    }
    
    /*
     * why not use table view for recipe Editor?
     */
    protected VBox populateExtraInfoPanel() {
    	while(viewPanel.getSelectionModel().getSelectedItem() != null) {
    		GridPane extraInfoDesign = new GridPane();
    		extraInfoDesign.setVgap(7);
    		extraInfoDesign.setHgap(7);
    		
            Button addIngredient = new Button("Add Ingredient");
            Button  removeIngredient = new Button("Remove Ingredient");
            
            ArrayList<Integer> comboBoxIntegers = new ArrayList<Integer>(); 
            comboBoxIntegers = comboBoxInteger();
            ObservableList<Ingredient> ingredientsComboBox = FXCollections.observableArrayList(getServer().getIngredients());
            ArrayList<Ingredient> bannedRecipeIngredients = new ArrayList<Ingredient>();

            
    		
    		Button editButton = new Button("Edit");
    		
    		Dish dishObserved = viewPanel.getSelectionModel().getSelectedItem();
    		
    		TextField dishName = new TextField(viewPanel.getSelectionModel().getSelectedItem().getName());
    		TextField dishDescription = new TextField(viewPanel.getSelectionModel().getSelectedItem().getDescription());
    		TextField dishPrice = new TextField(viewPanel.getSelectionModel().getSelectedItem().getPrice().toString());
    		ComboBox<Number> dishRestockThreshold = new ComboBox<Number>(FXCollections.observableArrayList(comboBoxIntegers));
    		dishRestockThreshold.setPromptText(viewPanel.getSelectionModel().getSelectedItem().getRestockThreshold().toString());
    		ComboBox<Number> dishRestockAmount = new ComboBox<Number>(FXCollections.observableArrayList(comboBoxIntegers));
    		dishRestockAmount.setPromptText(viewPanel.getSelectionModel().getSelectedItem().getRestockAmount().toString());
    		
    		
    		Iterator ingredientsListIt = viewPanel.getSelectionModel().getSelectedItem().getRecipe().keySet().iterator();
    		Iterator quantityListIt = viewPanel.getSelectionModel().getSelectedItem().getRecipe().values().iterator();
    		int recipeSize = viewPanel.getSelectionModel().getSelectedItem().getRecipe().size();
    		ArrayList<Recipe> listOfRecipe = new ArrayList<Recipe>();
    		//populate list of recipe
    		while(ingredientsListIt.hasNext() && quantityListIt.hasNext()) {
    			Ingredient currentRecipeIngredient = (Ingredient) ingredientsListIt.next();
    			Number currentRecipeQuantity = (Number) quantityListIt.next();
    			Recipe newRecipe = new Recipe(currentRecipeIngredient,currentRecipeQuantity);
    			bannedRecipeIngredients.add(currentRecipeIngredient);
    			listOfRecipe.add(newRecipe);
    		}
    		 
            ListView<Recipe> recipeEditor = new ListView<Recipe>();
            recipeEditor.setOrientation(Orientation.VERTICAL);
            recipeEditor.setPrefHeight(150);
            recipeEditor.setPadding(new Insets(5,5,5,5));
            recipeEditor.setCellFactory(lv -> new RecipeCell());       
            recipeEditor.setItems(FXCollections.observableArrayList(listOfRecipe));
            
            ComboBox<Ingredient> ingredientRecipeName = new ComboBox(ingredientsComboBox);
            ingredientRecipeName.setPromptText("Ingredient Name");
            ComboBox<Number> ingredientRecipeQuantity = new ComboBox(FXCollections.observableArrayList(comboBoxIntegers));
            ingredientRecipeQuantity.setPromptText("Ingredient Quantity");
            
           
    		
    		Label name = new Label("Name: ");
    		Label description = new Label("Description: ");
    		Label price = new Label("Price: ");
    		Label recipePrompt = new Label("Recipe: ");
    		Label restockThreshold = new Label("Restock Threshold: ");
    		Label restockAmount = new Label("Restock Amount");
    		
    		extraInfoDesign.add(name, 0, 0);
    		extraInfoDesign.add(dishName, 1, 0);
    		extraInfoDesign.add(description, 0, 1);
    		extraInfoDesign.add(dishDescription, 1, 1);
    		extraInfoDesign.add(price, 0, 2);
    		extraInfoDesign.add(dishPrice, 1, 2);
    		extraInfoDesign.add(restockThreshold, 0, 3);
    		extraInfoDesign.add(dishRestockThreshold, 1, 3);
    		extraInfoDesign.add(restockAmount, 0, 4);
    		extraInfoDesign.add(dishRestockAmount, 1, 4);
    		extraInfoDesign.add(recipePrompt, 0, 5);
    		extraInfoDesign.add(recipeEditor, 1, 5);
    		extraInfoDesign.add(addIngredient, 0, 6);
    		extraInfoDesign.add(removeIngredient, 0, 7);
    		extraInfoDesign.add(ingredientRecipeName, 1, 6);
    		extraInfoDesign.add(ingredientRecipeQuantity, 1, 7);
    		extraInfoDesign.add(editButton, 0, 8);
    		
    		 EventHandler<ActionEvent> addIngredientListener = new EventHandler<ActionEvent>() {
                 @Override
                 public void handle(ActionEvent event) {
                     try {
                     	if(ingredientRecipeName.getSelectionModel().isEmpty() || ingredientRecipeQuantity.getSelectionModel().isEmpty()) {
                     		popUp("Incompleted Field: make sure all Fields are complete");
                     	} if(bannedRecipeIngredients.contains(ingredientRecipeName.getSelectionModel().getSelectedItem())) {
                     		popUp("Cannot use Ingredient more than once in Recipe");
                     		listOfRecipe.remove(listOfRecipe.size());
                     		recipeEditor.setItems(FXCollections.observableArrayList(listOfRecipe));
                     	} if(ingredientRecipeQuantity.getSelectionModel().getSelectedItem().intValue() >  ingredientRecipeName.getSelectionModel().getSelectedItem().getRestockAmount().intValue()) {
                     		popUp("Cannot add an Ingredient that uses" + "\n" + "more than the Ingredients Restock Threshold");
                     	}
                     	else {
                     		Ingredient ingredientToAdd = ingredientRecipeName.getSelectionModel().getSelectedItem();
                     		bannedIngredients.add(ingredientToAdd);
                     		Number numberToAdd = ingredientRecipeQuantity.getSelectionModel().getSelectedItem();
                     		Recipe recipeToAdd = new Recipe(ingredientToAdd,numberToAdd);
                     		bannedRecipeIngredients.add(ingredientToAdd);
                     		listOfRecipe.add(recipeToAdd);
                     		recipeEditor.setItems(FXCollections.observableArrayList(listOfRecipe));
                         	ingredientRecipeName.getSelectionModel().selectFirst();               
                     	}
     					//need to update ListView every time button is pressed
     				} catch (Exception e) {
     					System.err.println(e);
     				}
                 }
             };
             
            addIngredient.setOnAction(addIngredientListener);
            
            /*
             * remove ingredient event listener
             */
            EventHandler<ActionEvent> removeIngredientListener = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                		if(recipeEditor.getSelectionModel().isEmpty()) {
             			popUp("No Ingredient from the Recipe Selected:" + "\n" + "Please Select an Object from the Recipe List");
             		} else {
                    	Recipe recipeToRemove = recipeEditor.getSelectionModel().getSelectedItem();
                    	bannedRecipeIngredients.remove(recipeToRemove);
                 	listOfRecipe.remove(recipeToRemove);
                 	recipeEditor.setItems(FXCollections.observableArrayList(listOfRecipe));
             		}
                    } catch (Exception e) {
     				//need to update ListView every time button is pressed
     					System.err.println(e);
     				}
                }
            };
            
           removeIngredient.setOnAction(removeIngredientListener);

    		
    		/*
             * edit button action event - NEED TO ADD FUNCTIONALITY TO INGREDIENTS AND
             * INGREDIENT QUANTITY - NOT WORKING
             */
            EventHandler<ActionEvent> editButtonAction = new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                    	if(dishName.getText() == null) {
                    		dishName.setText(viewPanel.getSelectionModel().getSelectedItem().getName());
                    	}
                    	if(dishDescription.getText() == null) {
                    		dishDescription.setText(viewPanel.getSelectionModel().getSelectedItem().getDescription());
                    	}
                    	if(dishPrice.getText() == null) {
                    		dishPrice.setText(viewPanel.getSelectionModel().getSelectedItem().getPrice().toString());
                    	}
                    	if(dishRestockThreshold.getSelectionModel().isEmpty()) {
                    		dishRestockThreshold.getSelectionModel().select(viewPanel.getSelectionModel().getSelectedItem().getRestockThreshold());
                    	}
                    	if(dishRestockAmount.getSelectionModel().isEmpty()) {
                    		dishRestockAmount.getSelectionModel().select(viewPanel.getSelectionModel().getSelectedItem().getRestockAmount());
                    	}
                    
                    	try {
                    	 dishObserved.setName(dishName.getText());
                    	 dishObserved.setDescription(dishDescription.getText());
                    	 dishObserved.setPrice(Integer.parseInt(dishPrice.getText()));
                    	 dishObserved.setRestockThreshold(dishRestockThreshold.getSelectionModel().getSelectedItem());
                    	 dishObserved.setRestockAmount(dishRestockAmount.getSelectionModel().getSelectedItem());
                    	 viewPanel.getSelectionModel().getSelectedItem().getRecipe().clear();                   	 
     					HashMap<Ingredient,Number> newRecipe = new HashMap<Ingredient,Number>();;
     					for(Recipe currentRecipe : listOfRecipe) {
     						newRecipe.put(currentRecipe.getIngredient(), currentRecipe.getQuantity());
     					}
     				
     					viewPanel.getSelectionModel().getSelectedItem().setRecipe(newRecipe);
                    	
                    	 System.out.println(viewPanel.getSelectionModel().getSelectedItem().getRecipe().keySet());
                    	 System.out.println(viewPanel.getSelectionModel().getSelectedItem().getRecipe().values());
                    	} catch(NumberFormatException e) {
                    		popUp("Price must be a Number");
                    	}
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
    /*
     * need to optimise construction of the recipeList in the object so it doesn't crash
     */
    protected void constructObject() {
		if(dishName.getText().trim().isEmpty() ||
    			dishPrice.getText().trim().isEmpty() ||
    			restockThreshold.getSelectionModel().isEmpty() ||
    			restockAmount.getSelectionModel().isEmpty()) {
    				popUp("Incompleted Field: make sure all Fields are complete");
    			} else {
    				try {
    					String name = dishName.getText();
    					String description = dishName.getText();
    					Number price = Integer.parseInt(dishPrice.getText());
    					Number restockThreshold = this.restockThreshold.getSelectionModel().getSelectedItem();
    					Number restockAmount = this.restockAmount.getSelectionModel().getSelectedItem();
    					Dish newDish = new Dish(name,description,price,restockThreshold,restockAmount);
    				
    					HashMap<Ingredient,Number> newRecipe = new HashMap<Ingredient,Number>();;
    					for(Recipe currentRecipe : dishRecipe) {
    						newRecipe.put(currentRecipe.getIngredient(), currentRecipe.getQuantity());
    					}
    				
    					newDish.setRecipe(newRecipe);
    					getDishesList().add(newDish);
    					getServer().addDish(name,description,price,restockThreshold,restockAmount);
    					System.out.println("Dish Recipe: " + newDish.getRecipe().keySet().toString() + newDish.getRecipe().values().toString() );
    					System.out.println("New Recipe: " + newRecipe.keySet().toString() + newRecipe.values().toString());
    				 } catch(NumberFormatException e) {
    					 popUp("Price Must be a Number");
    				 }
    			}
    				 
    		
    }
    
    
    protected void removeObject() {
    	Dish dishToRemove = viewPanel.getSelectionModel().getSelectedItem();
    	getDishesList().remove(dishToRemove);
    	int actualIndex= viewPanel.getSelectionModel().getSelectedIndex() + 1;
    	if(actualIndex == 1) {
    		actualIndex = 0;
    	}
    	Dish actualDishToRemove = getServer().getDishes().get(actualIndex);
    	if(viewPanel.getSelectionModel().isEmpty()== false) {
			getServer().removeDish(actualDishToRemove);
		}
		if(viewPanel.getSelectionModel().getSelectedItem() == null) {
			//popUp("No Object Selected: Please Select an Object");
		}
    }
    
    protected void moveUp() {
    	Dish dishToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int dishIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	
    	if(dishIndexToMove == 0) {
    		getDishesList().remove(dishIndexToMove);
        	getDishesList().add(getDishesList().size(), dishToMove);
        	getServer().getDishes().remove(dishIndexToMove);
        	getServer().getDishes().add(getServer().getDishes().size(), dishToMove);
    	} else {
    		getDishesList().remove(dishIndexToMove);
        	getDishesList().add(dishIndexToMove-1, dishToMove);
        	getServer().getDishes().remove(dishIndexToMove);
        	getServer().getDishes().add(dishIndexToMove-1, dishToMove);
    	}
    }
    
    protected void moveDown() {
    	Dish dishToMove = viewPanel.getSelectionModel().getSelectedItem();
    	int dishIndexToMove = viewPanel.getSelectionModel().getSelectedIndex();
    	if(dishIndexToMove == getDishesList().size()-1) {
    		getDishesList().remove(dishIndexToMove);
    		getDishesList().add(0,dishToMove);
        	getServer().getDishes().remove(dishIndexToMove);
        	getServer().getDishes().add(0,dishToMove);
    	} else {
    		getDishesList().remove(dishIndexToMove);
        	getDishesList().add(dishIndexToMove+1, dishToMove);
        	getServer().getDishes().remove(dishIndexToMove);
        	getServer().getDishes().add(dishIndexToMove+1, dishToMove);
    	}
    }
    
    public HashMap<Ingredient, Number> deconstructDishRecipe() {
    	for(Recipe currentRecipe : dishRecipe) {
    		Ingredient recipeKey = currentRecipe.getIngredient();
    		Number recipeValue = currentRecipe.getQuantity();
    		
    	}
    	
    	return recipe;
    }

    
    class Recipe {
		private HashMap<Ingredient,Number> recipeCreation =  new HashMap<Ingredient,Number>();
		private Ingredient ingredientInRecipe;
		private Number ingredientQuantityInRecipe;
		
    	Recipe(Ingredient ingredientToAdd, Number ingredientQuantityToAdd) {
    		ingredientInRecipe = ingredientToAdd;
    		ingredientQuantityInRecipe = ingredientQuantityToAdd;
    		recipeCreation.put(ingredientToAdd, ingredientQuantityToAdd);
    	}
    	
    	public Ingredient getIngredient() {
    		return this.ingredientInRecipe;
    	}
    	
    	public Number getQuantity() {
    		return this.ingredientQuantityInRecipe;
    	}  	
    }
    
    
    class RecipeCell extends ListCell<Recipe> {
    	private Label ingredient;
    	private Label quantity;
    	private VBox cell;
    	
    	public RecipeCell() {
            setPrefWidth(100);

            ingredient= new Label();
            quantity = new Label();

            quantity.setWrapText(true);
            cell = new VBox(ingredient, quantity);
    	}
    	@Override
    	protected void updateItem(Recipe message, boolean empty) {
    		super.updateItem(message, empty);
    		if (message == null || empty) {
                setGraphic(null);
            } else {
                ingredient.setText(message.getIngredient().toString());
                quantity.setText(message.getQuantity().toString());
                setGraphic(cell);
            }
        }
    }
    

}
