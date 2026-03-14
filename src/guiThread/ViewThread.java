package guiThread;

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
 * <p> Title: GUIViewThreadPage Class. </p>
 * 
 * <p> Description: The Java/FX-based ViewThread Page.  This class provides the JavaFX GUI widgets
 * that enable a student/staff to perform student/staff functions.  
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayViewThread.  No other method should 
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

public class ViewThread {
	
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

	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
		

	
	
	// GUI Area 2: Compose
		protected static Label label_NewPost = new Label("Write a post:");
		protected static TextArea textArea_NewPost = new TextArea();
		protected static Button button_Post = new Button("Post");
		protected static String currentThreadId = "general";
	
		
	
	// GUI Area 3: Thread display
	protected static Label label_ThreadTitle = new Label("General Thread");
	protected static ListView<Post> listView_Posts = new ListView<>();
	
	///change thread button
	protected static ComboBox<String> combo_ThreadSelect = new ComboBox<>();

	// ViewReplies button
	protected static Button button_ViewReplies = new Button("View Replies");
	
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
	    listView_Posts.getItems().setAll(theDatabase.getPostsForThread(currentThreadId));
	}
	
	
	
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewThread theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	private static Scene theThreadScene;		// The shared Scene each invocation populates
	

	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayThread(Stage ps, User user) </p>
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
	public static void displayThread(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewThread();		// Instantiate singleton if needed
		
		
		label_UserDetails.setText("User: " + theUser.getUserName());

	   
	    refreshPosts();
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("General Thread");
		theStage.setScene(theThreadScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewThread() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayAdminHome method.</p>
	 * 
	 */
	private ViewThread() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theThreadScene = new Scene(theRootPane, width, height);
		
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Thread");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		/// thread title
		setupLabelUI(label_ThreadTitle, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 105);
		
	
		/// GUI Area 2
		 setupLabelUI(label_NewPost, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 410);
		
		 
		 setupComboBoxUI(combo_ThreadSelect, "Dialog", 16, 160, width - 180, 50);

		 combo_ThreadSelect.setItems(FXCollections.observableArrayList(
		     "general", "cse360"  
		 ));
		 combo_ThreadSelect.getSelectionModel().select(currentThreadId);

		 combo_ThreadSelect.setOnAction((_) -> {
		     String selected = combo_ThreadSelect.getSelectionModel().getSelectedItem();
		     if (selected == null) return;
		     currentThreadId = selected;
		     label_ThreadTitle.setText(selected + " Thread");
		     applicationMain.FoundationsMain.postManager
		        .setCurrentThread(currentThreadId);
		     refreshPosts();
		 });
		 
		 
		 
		/// GUI Area 3
		
		 listView_Posts.setLayoutX(20);
		 listView_Posts.setLayoutY(140);
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
			        setText(p.getAuthorUsername() + ": " + p.getContent() + "  (" + replyCount + " replies)");
			    }
			});
		
		 // Text area
		    textArea_NewPost.setLayoutX(20);
		    textArea_NewPost.setLayoutY(435);
		    textArea_NewPost.setPrefWidth(width - 200);
		    textArea_NewPost.setPrefHeight(80);
		    // post button
		    setupButtonUI(button_Post, "Dialog", 16, 150, Pos.CENTER, width - 170, 455);
		    button_Post.setOnAction((_) -> { ControllerThread.performPost(); });
		    
		    ///viewReplies button
		    setupButtonUI(button_ViewReplies, "Dialog", 16, 150, Pos.CENTER, width - 170, 500);
		    button_ViewReplies.setOnAction((_) -> { ControllerThread.openReplies(); });
		    
		    ///delete post button
		    setupButtonUI(button_DeletePost, "Dialog", 16, 150, Pos.CENTER, width - 170, 545);
		    button_DeletePost.setOnAction((_) -> { ControllerThread.deletePost(); });
		    
		// Back button
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Back.setOnAction((_) -> { ControllerThread.goBack(); });
		 
		 
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
    		button_Post, button_Back, combo_ThreadSelect,
    		label_NewPost, textArea_NewPost, listView_Posts, label_ThreadTitle, button_ViewReplies, button_DeletePost
    		);
		
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
