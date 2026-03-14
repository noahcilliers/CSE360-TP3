package guiReplies;

import java.util.ArrayList;
import java.util.List;
import entityClasses.Post;
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
import guiThread.ControllerThread;
import guiThread.ViewThread;
import guiUserUpdate.ViewUserUpdate;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

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

public class ViewReplies {
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	
	// These are the widget attributes for the GUI. There are 5 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	
	protected static Label label_PageTitle = new Label("Replies");
	protected static Label label_UserDetails = new Label();
	protected static Label label_OriginalPost = new Label();
	

	// This is a separator and it is used to partition the GUI for various tasks
	private static Line line_Separator1 = new Line(20, 95, width-20, 95);
		

	protected static ListView<entityClasses.Reply> listView_Replies = new ListView<>();
	
	// GUI Area 2: Compose
		protected static Label label_NewReply = new Label("Write a reply:");
		protected static TextArea textArea_NewReply = new TextArea();
		protected static Button button_Reply = new Button("Reply");

	
		
	
	// GUI Area 3: Thread display
	protected static Label label_ThreadTitle = new Label("Reply Thread");
	//protected static ListView<String> listView_Posts = new ListView<>();

	// delete post button
	protected static Button button_DeletePost = new Button("Delete Reply");
	
	// GUI Area 5: This is last of the GUI areas.  It is used for quitting the application, logging
	// out, and on other pages a return is provided so the user can return to a previous page when
	// the actions on that page are complete.  Be advised that in most cases in this code, the 
	// return is to a fixed page as opposed to the actual page that invoked the pages.
	// Navigation
	protected static Button button_Back = new Button("Back");
	// This is the end of the GUI objects for the page.
	
	   protected static void refreshReplies() {
	        listView_Replies.getItems().clear();
	        // replace with ReplyManager if you have it:
	        listView_Replies.getItems().addAll(theDatabase.getRepliesForPost(thePost.getPostId()));
	    }

	
	
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewReplies theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	public static Post thePost;				// the Post which is being replied to
	protected static Stage theStage;			// The Stage that JavaFX has established for us
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
	protected static User theUser;				// The current logged in User

	private static Scene theRepliesScene;		// The shared Scene each invocation populates
	

	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayReplies(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the  page to be displayed.
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
	public static void displayReplies(Stage ps, User user, Post p) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		thePost = p;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewReplies();		// Instantiate singleton if needed
		
		
		label_UserDetails.setText("User: " + theUser.getUserName());
		label_OriginalPost.setText("Post: " + thePost.getAuthorUsername() + ": " + thePost.getContent());
	   
	    refreshReplies();
				
		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("Replies");
		theStage.setScene(theRepliesScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewReplies() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayAdminHome method.</p>
	 * 
	 */
	private ViewReplies() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theRepliesScene = new Scene(theRootPane, width, height);
		
	
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Replies");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		/// thread title
		setupLabelUI(label_ThreadTitle, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 105);
		
	
		/// GUI Area 2
		 setupLabelUI(label_NewReply, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 410);
		
		/// GUI Area 3
		
		 listView_Replies.setLayoutX(20);
		 listView_Replies.setLayoutY(140);
		 listView_Replies.setPrefWidth(width - 40);
		 listView_Replies.setPrefHeight(260);
		 
		 listView_Replies.setCellFactory(lv -> new javafx.scene.control.ListCell<entityClasses.Reply>() {
			    @Override
			    protected void updateItem(entityClasses.Reply r, boolean empty) {
			        super.updateItem(r, empty);
			        setText(empty || r == null ? "" : r.getAuthorUsername() + ": " + r.getContent());
			    }
			});
		
		 // Text area
		    textArea_NewReply.setLayoutX(20);
		    textArea_NewReply.setLayoutY(435);
		    textArea_NewReply.setPrefWidth(width - 200);
		    textArea_NewReply.setPrefHeight(80);

		    setupButtonUI(button_Reply, "Dialog", 16, 150, Pos.CENTER, width - 170, 455);
		    button_Reply.setOnAction((_) -> { ControllerReplies.performReply(); });
		    
		    ///delete post button
		    setupButtonUI(button_DeletePost, "Dialog", 16, 150, Pos.CENTER, width - 170, 500);
		    button_DeletePost.setOnAction((_) -> { ControllerReplies.deleteReply(); });
		    
		// Back button
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Back.setOnAction((_) -> ViewThread.displayThread(theStage, theUser));
		 
		 
		// Place all of the widget items into the Root Pane's list of children
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1, label_OriginalPost,
    		button_Reply, button_Back,
    		label_NewReply, textArea_NewReply, listView_Replies, label_ThreadTitle, button_DeletePost
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
	 * @param f		The size of the font to be  used
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
