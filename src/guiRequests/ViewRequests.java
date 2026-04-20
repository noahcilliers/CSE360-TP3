package guiRequests;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiUserUpdate.ViewUserUpdate;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ListCell;
import entityClasses.Post;
import modelClasses.PostManager;

/*******
 * <p> Title: GUIViewRequestsPage Class. </p>
 * 
 * <p> Description: The Java/FX-based ViewThread Page.  This class provides the JavaFX GUI widgets
 * that enable a student/staff to perform student/staff functions.  
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayViewRequests.  No other method should 
 * attempt to instantiate this class as that is controlled by displayViewThread.  It ensure that
 * only one instance of class is instantiated and that one is properly configured for each use.  
 * 
 * Please note that this implementation is not appropriate for concurrent systems with multiple
 * users. This Baeldung article provides insight into the issues: 
 *           https://www.baeldung.com/java-singleton</p>
 * 
 * <p> Copyright: Noah Cilliers © 2025 </p>
 * 
 * @author Noah Cilliers
 * 
 * @version 1.00		2026-02-20 Initial version
 *  
 */

public class ViewRequests {
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	
	// These are the widget attributes for the GUI. There are 5 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
	
	//Search area berto
	protected static Label label_Search = new Label("Search posts:");
	protected static TextField textField_Search = new TextField();
	protected static Button button_Search = new Button("Search");
	protected static Button button_ClearSearch = new Button("Clear");
	
	//requests buttons for edit, close, open
	protected static Button button_OpenRequest = new Button("ReOpen Request");
	protected static Button button_CloseRequest = new Button("Close Request");
	protected static Button button_UpdateRequest = new Button("Update Request");
	protected static Post requestBeingUpdated = null;
	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
		

	
	
	// GUI Area 2: Compose
		protected static Label label_NewPost = new Label("Write a post:");
		protected static TextArea textArea_NewPost = new TextArea();
		protected static Button button_Post = new Button("Post");
		protected static String currentThreadId = "requests";
	
		
	
	// GUI Area 3: Thread display
	protected static Label label_ThreadTitle = new Label("Requests");
	protected static ListView<Post> listView_Posts = new ListView<>();
	
	///change thread button
	protected static ComboBox<String> combo_ThreadSelect = new ComboBox<>();

	// ViewReplies button
	protected static Button button_ViewRequest = new Button("View Request Thread");
	
	// delete post button
	protected static Button button_DeletePost = new Button("Delete Post");
	
	// GUI Area 5: This is last of the GUI areas.  It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	// Navigation
	protected static Button button_Back = new Button("Back");

	// This is the end of the GUI objects for the page.
	
	
	//helper methods
	public static void refreshPosts() {
	    //listView_Posts.getItems().setAll(theDatabase.getPostsForThread(currentThreadId));
		 applicationMain.FoundationsMain.postManager
	        .setCurrentThread("requests");
	    thePostManager.refreshFromDatabase();
	    listView_Posts.getItems().setAll(thePostManager.getAllPosts());
	    
	    //Berto added these to refreash search
	    String searchText = textField_Search.getText() == null ? "" : textField_Search.getText().trim();
	    if(searchText.isEmpty()) 
	    {
	    	listView_Posts.getItems().setAll(thePostManager.getAllPosts());
	    }else
	    {
	    	listView_Posts.getItems().setAll(thePostManager.searchPosts(searchText));
	    }
	}
	
	

	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewRequests theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	private static PostManager thePostManager = applicationMain.FoundationsMain.postManager;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	private static Scene theThreadScene;		// The shared Scene each invocation populates
	

	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayRequests(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayRequests(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewRequests();		// Instantiate singleton if needed
		
		
		label_UserDetails.setText("User: " + theUser.getUserName());

	   
	    refreshPosts();
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("Requests Thread");
		theStage.setScene(theThreadScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewRequests() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayAdminHome method.</p>
	 * 
	 */
	private ViewRequests() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theThreadScene = new Scene(theRootPane, width, height);
		
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Requests");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		/// thread title
		setupLabelUI(label_ThreadTitle, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 135);
		
	
		/// GUI Area 2
		 setupLabelUI(label_NewPost, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 410);
		
		 applicationMain.FoundationsMain.postManager
	        .setCurrentThread("requests");
		// setupComboBoxUI(combo_ThreadSelect, "Dialog", 16, 160, width - 180, 50);

		 //combo_ThreadSelect.setItems(FXCollections.observableArrayList(
		   //  "requests" 
		 //));
		 //combo_ThreadSelect.getSelectionModel().select(currentThreadId);

		 //combo_ThreadSelect.setOnAction((_) -> {
		   //  String selected = combo_ThreadSelect.getSelectionModel().getSelectedItem();
		     //if (selected == null) return;
		    // currentThreadId = selected;
		     //label_ThreadTitle.setText(selected + " Thread");
		     //applicationMain.FoundationsMain.postManager
		       // .setCurrentThread(currentThreadId);
		     //refreshPosts();
		// });
		 
		 //search setup - berto
		 setupLabelUI(label_Search, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 105);
		 setupTextUI(textField_Search, "Dialog", 16, 250, Pos.BASELINE_LEFT, 130, 105, true);
		 setupButtonUI(button_Search, "Dialog", 14, 100, Pos.CENTER, 400,100);
		 button_Search.setOnAction((_) ->{ ControllerRequests.performSearch();});
		 setupButtonUI(button_ClearSearch, "Dialog", 14, 100, Pos.CENTER, 510, 100);
		 button_ClearSearch.setOnAction((_) -> { ControllerRequests.clearSearch(); });
		 textField_Search.setOnAction((_) -> { ControllerRequests.performSearch(); });
		 
		/// GUI Area 3
		
		 listView_Posts.setLayoutX(20);
		 listView_Posts.setLayoutY(170);
		 listView_Posts.setPrefWidth(width - 40);
		 listView_Posts.setPrefHeight(260);
		 
		 
		 listView_Posts.setCellFactory(lv -> new javafx.scene.control.ListCell<entityClasses.Post>() {
			    @Override
			    protected void updateItem(entityClasses.Post p, boolean empty) {
			        super.updateItem(p, empty);
			        if (empty || p == null) {
			            setText("");
			            return;
			        }

			        int replyCount = applicationMain.FoundationsMain.database.getReplyCountForPost(p.getPostId());
			        int readReplyCount = applicationMain.FoundationsMain.database.getReadReplyCountForPost(p.getPostId(), theUser);
			        int totalNewReplies = replyCount - readReplyCount;
			        String unreadPost = "";
			        if(readReplyCount == 0) unreadPost = " (New Post)";
			        String text = "";
			        if ("my-posts".equals(currentThreadId)) {
			            text += "[" + p.getThreadId() + "] ";
			        }
			       
			        String editedText = p.isEdited() ? " [Edited]" : "";
			        text += "[" + p.getStatus() + "] " + p.getAuthorUsername() + ": " + p.getContent()
			                + editedText + unreadPost + "  (" + replyCount + " replies)" + " (" + totalNewReplies + " New Replies)";
			        
			        setText(text);
			    }
			});
		
		 // Text area
		    textArea_NewPost.setLayoutX(20);
		    textArea_NewPost.setLayoutY(435);
		    textArea_NewPost.setPrefWidth(width - 200);
		    textArea_NewPost.setPrefHeight(80);
		    // post button
		    setupButtonUI(button_Post, "Dialog", 18, 150, Pos.CENTER, width - 170, 440);
		    button_Post.setOnAction((_) -> { ControllerRequests.performPost(); });
		    
		    ///viewReplies button
		    setupButtonUI(button_ViewRequest, "Dialog", 18, 150, Pos.CENTER, width - 450, 560);
		    button_ViewRequest.setOnAction((_) -> { ControllerRequests.openReplies(); });
		    
		    ///delete post button
		    setupButtonUI(button_DeletePost, "Dialog", 18, 150, Pos.CENTER, width - 170, 560);
		    button_DeletePost.setOnAction((_) -> { ControllerRequests.deletePost(); });
		    
		// Back button
		setupButtonUI(button_Back, "Dialog", 18, 150, Pos.CENTER, 20, 560);
		button_Back.setOnAction((_) -> { ControllerRequests.goBack(); });
		 
		setupButtonUI(button_OpenRequest, "Dialog", 18, 150, Pos.CENTER, width - 620, 560);
		button_OpenRequest.setOnAction((_) -> { ControllerRequests.openRequest(); });

		setupButtonUI(button_CloseRequest, "Dialog", 18, 150, Pos.CENTER, width - 170, 520);
		button_CloseRequest.setOnAction((_) -> { ControllerRequests.closeRequest(); });

		setupButtonUI(button_UpdateRequest, "Dialog", 18, 150, Pos.CENTER, width - 170, 480);
		button_UpdateRequest.setOnAction((_) -> { ControllerRequests.updateRequest(); });
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
    		button_Post, button_Back, combo_ThreadSelect,
    		label_NewPost, textArea_NewPost, listView_Posts, label_ThreadTitle, button_ViewRequest, button_DeletePost
    		, label_Search, textField_Search, button_Search, button_ClearSearch, button_CloseRequest, button_UpdateRequest, button_OpenRequest); // added these for search - berto
		
		// With theRootPane set up with the common widgets, it is up to displayAdminHome to show
		// that Pane to the user after the dynamic elements of the widgets have been updated.
	}

	/*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
	*/

	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	

	
	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}
