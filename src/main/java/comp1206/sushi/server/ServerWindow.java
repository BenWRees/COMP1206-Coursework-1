package comp1206.sushi.server;

import java.util.ArrayList;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import comp1206.sushi.common.Dish;
import comp1206.sushi.common.Ingredient;
import comp1206.sushi.common.Model;
import comp1206.sushi.common.Postcode;
import comp1206.sushi.common.Supplier;
import comp1206.sushi.mock.MockServer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import comp1206.sushi.DishesTab;
import comp1206.sushi.DronesTab;
import comp1206.sushi.IngredientsTab;
import comp1206.sushi.MapTab;
import comp1206.sushi.OrdersTab;
import comp1206.sushi.PostcodeTab;
import comp1206.sushi.StaffTab;
import comp1206.sushi.SuppliersTab;
import comp1206.sushi.UsersTab;
import comp1206.sushi.common.*;
import comp1206.sushi.server.ServerInterface.UnableToDeleteException;

/**
 * Provides the Sushi Server user interface
 *
 */
public class ServerWindow extends Application implements UpdateListener {

	private static final long serialVersionUID = -4661566573959270000L;
	//private ServerInterface server;
	
	 private StackPane root;
	    private Scene mainScene;
	    private TabPane serverWindow;
	    private PostcodeTab postcodesTab;
	    private StaffTab staffTab;
	    private DronesTab dronesTab;
	    private SuppliersTab suppliersTab;
	    private IngredientsTab ingredientsTab;
	    private DishesTab dishesTab;
	    private OrdersTab ordersTab;
	    private UsersTab usersTab;
	    private MapTab mapTab;
	    private MenuBar menuBar;
		private ObservableList<Postcode> postcodes = FXCollections.observableArrayList(getServer().getPostcodes());
		private ArrayList<Postcode> postcodesList;
		protected ObservableList<Supplier> suppliers = FXCollections.observableArrayList(getServer().getSuppliers());
		private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList(getServer().getIngredients());
		private ObservableList<Dish> dishes = FXCollections.observableArrayList(getServer().getDishes());
		private ComboBox<Postcode> suppliersPostcode;
	    protected Button add;
	    protected Button remove;
	    protected Button moveUp;
	    protected Button moveDown;
	    
	    /*
		 * adds all the Ingredients Suppliers to an array list
		 */
		private ArrayList<Supplier> suppliersInIngredient = new ArrayList<Supplier>();

	    
	    
	    
	    private static MockServer server = new MockServer();
	    
	    public static void main(String[] argv) {
	    	launch(argv);
	    }
	    @Override
	    public void start(Stage primaryStage) {

	        menuBar = menuBar();
	        root = new StackPane(menuBar);
	        serverWindow = new TabPane();
	        serverWindow.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS.UNAVAILABLE);
	        

	        postcodesTab = new PostcodeTab();
	        dronesTab = new DronesTab();
	        staffTab = new StaffTab();
	        suppliersTab = new SuppliersTab();
	        ingredientsTab = new IngredientsTab();
	        dishesTab = new DishesTab();
	        ordersTab = new OrdersTab();
	        usersTab = new UsersTab();
	        mapTab = new MapTab();
	        
	        //instantiateSuppliersPostcodeComboBox();

	        Tab postcodes = postcodesTab.getTab();
	        Tab staff = staffTab.getTab();
	        Tab drone = dronesTab.getTab();
	        Tab supplier = suppliersTab.getTab();
	        Tab ingredient = ingredientsTab.getTab();
	        Tab dish = dishesTab.getTab();
	        Tab order = ordersTab.getTab();
	        Tab user = usersTab.getTab();
	        Tab map = mapTab.getTab();
	        
	        
	    	//CALL UPDATE ON ALL THE ComboBoxs
	        serverWindow.setOnMouseClicked(new EventHandler<MouseEvent>() {
	        	//switch (serverWindow.getSelectionModel().getSelectedItem())
	            @Override
	            public void handle(MouseEvent event) {;
	            if(serverWindow.getSelectionModel().getSelectedItem() == postcodes) {
	            	postcodesTab.postcodesTabContent();
	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == staff) {
	            	staffTab.staffTabContent();

	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == drone) {
	            	dronesTab.dronesTabContent();

	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == supplier) {
	            	suppliersTab.suppliersTabContent();
	            	
	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == ingredient) {
	            	ingredientsTab.ingredientsTabContent();

	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == dish) {
	            	dishesTab.dishesTabContent();

	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == order) {
	            	ordersTab.ordersTabContent();

	            }
	            
	            if(serverWindow.getSelectionModel().getSelectedItem() == user) {
	            	usersTab.usersTabContent();
	            }
	            /*
	            if(serverWindow.getSelectionModel().getSelectedItem() == map) {
	            	startTimer();
	            }
				*/
	            }
	        });
	        
	        
	        serverWindow.getSelectionModel().select(0);
	        serverWindow.getTabs().addAll(postcodes, staff, drone, supplier, ingredient, dish, order, user, map);

	        root.getChildren().add(serverWindow);
	        startTimer();

	        mainScene = new Scene(root, 1000, 1200);

	        primaryStage.setTitle("Server Window");
	        primaryStage.setScene(mainScene);
	        primaryStage.show();
	    }


	    private MenuBar menuBar() {
	        MenuBar menuBar = new MenuBar();
	        menuBar.setUseSystemMenuBar(true);

	        /*
	            File contains, all the Tabs, Preferences, new window
	         */
	        Menu menuFile = new Menu("File");
	        /*
	            Edit contains, copy, paste, delete
	         */
	        Menu menuEdit = new Menu("Edit");
	        /*
	            View contains, full screen, hide, force reload
	         */
	        Menu menuView = new Menu("View");
	        /*
	            Map contains, Map popup, Map preferences
	         */
	        Menu menuMap = new Menu("Map");

	        menuBar.getMenus().addAll(menuFile,menuEdit,menuView, menuMap);

	        return menuBar;
	    }
	    
	    
	    public MockServer getServer() {
	    	return server;
	    }
	    
	    public void popUp(String textInPopup) {
	    	final Stage popup = new Stage();
	        VBox popUpBox = new VBox(20);
	        Label text = new Label(textInPopup);
	        Button ok = new Button("Okay");
	        EventHandler<ActionEvent> okayButtonListener = new EventHandler<ActionEvent>() {
	            @Override
	            public void handle(ActionEvent event){
	            	popup.close();
	            }
	        };
	        ok.setOnAction(okayButtonListener);
	        popUpBox.getChildren().addAll(text,ok);
	        popUpBox.setAlignment(Pos.CENTER);
	        text.setFont(new Font("Helvetica",15));
	        Scene popUpScene = new Scene(popUpBox, 500, 300);
	        popup.setScene(popUpScene);
	        popup.show();
	    }
	    
	    public ArrayList<Integer> comboBoxInteger() {
	    	ArrayList<Integer> numbers = new ArrayList<Integer>();
	    	for(Integer i=0; i<101 ; i++) {
	    		numbers.add(i);
	    	}
	    	return numbers;
	    }
	    
	    public ObservableList<Postcode> getPostcodesList() {
	    	return postcodes;
	    }
	    
	    
	    public ObservableList<Supplier> getSuppliersList() {
	    	return suppliers;
	    }
	    
	    
	    public ObservableList<Ingredient> getIngredientsList() {
	    	return ingredients;
	    }
	    
	    
	    public ObservableList<Dish> getDishesList() {
	    	return dishes;
	    }
	    /*
	    public ComboBox<Postcode> getSuppliersPostcodeComboBox() {
	    	return suppliersPostcode;
	    }
	    */
	    public void instantiateSuppliersPostcodeComboBox() {
	    	//instantiatePostcodeList();
	    	suppliersPostcode = new ComboBox(FXCollections.observableArrayList(getServer().getPostcodes())); 
	    }
	    
	    
	    public ArrayList<Postcode> getPostcodeList() {
	    	return postcodesList;
	    }
	    
	    public ArrayList<Supplier> getSuppliersInIngredient() {
	    	return suppliersInIngredient;
	    }
	    
	    /*
	    public void instantiatePostcodeList() {
	    	postcodesList = new ArrayList<Postcode>();
	    	postcodesList.addAll(getServer().getPostcodes());
	    }
	    */
	   /* 
	    public void instantiateSuppliersInIngredients() {
	    	suppliersInIngredients = new ArrayList<Supplier>();
	    	suppliersInIngredients = suppliers.toArray()
	    }
	    */
	    
	    protected void updateUI(Pane panel) {
	    	panel.getChildren().clear();
	    }
	    
	
	/**
	 * Start the timer which updates the user interface based on the given interval to update all panels
	 */
	public void startTimer() {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);     
        int timeInterval = 5;
        
       scheduler.scheduleAtFixedRate(() -> refreshAll(), 0, timeInterval, TimeUnit.SECONDS);
	}
	
	/**
	 * Refresh all parts of the server application based on receiving new data, calling the server afresh
	 */
	public void refreshAll() {
    	Timeline timeline = new Timeline();
    	timeline.setCycleCount(Timeline.INDEFINITE);
    	 timeline.getKeyFrames().add(new KeyFrame(Duration.millis(10000), (actionEvent) -> {
    		 suppliersTab.suppliersTabContent();
    		 ingredientsTab.ingredientsTabContent();
    		 dishesTab.dishesTabContent();
    		 
    	 }, null, null));
    	 timeline.play();
	}
	
	@Override
	/**
	 * Respond to the model being updated by refreshing all data displays
	 */
	public void updated(UpdateEvent updateEvent) {
		refreshAll();
	}
	
}
