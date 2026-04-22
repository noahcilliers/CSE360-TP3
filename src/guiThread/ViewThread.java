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
	
	// Search area
	protected static Label label_Search = new Label("Search posts:");
	protected static TextField textField_Search = new TextField();
	protected static Button button_Search = new Button("Search");
	protected static Button button_ClearSearch = new Button("Clear");
	
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

	// change thread button
	protected static ComboBox<String> combo_ThreadSelect = new ComboBox<>();

	// ViewReplies button
	protected static Button button_ViewReplies = new Button("View Replies");
	
	// delete post button
	protected static Button button_DeletePost = new Button("Delete Post");
	
	//Grading Controls
	protected static Label label_GradeTitle = new Label("Grade Selected Post:");
	protected static TextField textField_Grade = new TextField();
	protected static Button button_AssignGrade = new Button("Assign Grade");
	protected static Label label_GradeResult = new Label("No grade assigned yet.");
	

	// Navigation
	protected static Button button_Back = new Button("Back");

	// This is the end of the GUI objects for the page.
	
	
	// helper methods
	public static void refreshPosts() {
	    thePostManager.refreshFromDatabase();
	    listView_Posts.getItems().setAll(thePostManager.getAllPosts());
	    
	    String searchText = textField_Search.getText() == null ? "" : textField_Search.getText().trim();
	    if (searchText.isEmpty()) {
	    	listView_Posts.getItems().setAll(thePostManager.getAllPosts());
	    } else {
	    	listView_Posts.getItems().setAll(thePostManager.searchPosts(searchText));
	    }
	}

	/**
	 * Determines whether the current user should be allowed to see grading controls.
	 * 
	 * Adjust this method if your User class stores roles differently.
	 */
	private static boolean canGradeCurrentUser() {
		if (theUser == null) return false;
		
		return applicationMain.FoundationsMain.activeHomePage == 1
				|| applicationMain.FoundationsMain.activeHomePage == 3;

	}

	/**
	 * Updates the grade display for the currently selected post.
	 */
	private static void updateGradeDisplay(Post p) {
		if (p == null) {
			label_GradeResult.setText("No grade assigned yet.");
			textField_Grade.clear();
			return;
		}

		if (p.getNumericGrade() == -1) {
			label_GradeResult.setText("Current Grade: Not Assigned");
			textField_Grade.clear();
		} else {
			label_GradeResult.setText("Current Grade: " + p.getNumericGrade() + " | Letter Grade: " + p.getLetterGrade());
			textField_Grade.setText(String.valueOf(p.getNumericGrade()));
		}
	}
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewThread theView;

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	private static PostManager thePostManager = applicationMain.FoundationsMain.postManager;
	
	protected static Stage theStage;
	private static Pane theRootPane;
	protected static User theUser;

	private static Scene theThreadScene;
	

	/*-*******************************************************************************************

	Constructors
	
	*/

	/**********
	 * <p> Method: displayThread(Stage ps, User user) </p>
	 */
	public static void displayThread(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewThread();
		
		label_UserDetails.setText("User: " + theUser.getUserName());

	    refreshPosts();

	    // Only staff/admin should see grading controls
	    boolean allowGrading = canGradeCurrentUser();
	    label_GradeTitle.setVisible(allowGrading);
	    textField_Grade.setVisible(allowGrading);
	    button_AssignGrade.setVisible(allowGrading);
	    label_GradeResult.setVisible(allowGrading);

	    if (!allowGrading) {
	    	textField_Grade.clear();
	    	label_GradeResult.setText("");
	    } else {
	    	Post selected = listView_Posts.getSelectionModel().getSelectedItem();
	    	updateGradeDisplay(selected);
	    }
				
		// Set the title for the window, display the page
		theStage.setTitle("General Thread");
		theStage.setScene(theThreadScene);
		theStage.show();
	}
	
	/**********
	 * <p> Method: ViewThread() </p>
	 */
	private ViewThread() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theThreadScene = new Scene(theRootPane, width, height);
		
		// GUI Area 1
		label_PageTitle.setText("Thread");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: ");
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		// Thread title
		setupLabelUI(label_ThreadTitle, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 135);
		
		// GUI Area 2
		setupLabelUI(label_NewPost, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 410);
		
		setupComboBoxUI(combo_ThreadSelect, "Dialog", 16, 160, width - 180, 50);

		combo_ThreadSelect.setItems(FXCollections.observableArrayList(
		    "general", "cse360", "my-posts"  
		));
		combo_ThreadSelect.getSelectionModel().select(currentThreadId);

		combo_ThreadSelect.setOnAction((_) -> {
		    String selected = combo_ThreadSelect.getSelectionModel().getSelectedItem();
		    if (selected == null) return;
		    currentThreadId = selected;
		    label_ThreadTitle.setText(selected + " Thread");
		    applicationMain.FoundationsMain.postManager.setCurrentThread(currentThreadId);
		    refreshPosts();
		});
		 
		// Search setup
		setupLabelUI(label_Search, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 105);
		setupTextUI(textField_Search, "Dialog", 16, 250, Pos.BASELINE_LEFT, 130, 105, true);
		setupButtonUI(button_Search, "Dialog", 14, 100, Pos.CENTER, 400, 100);
		button_Search.setOnAction((_) -> { ControllerThread.performSearch(); });
		setupButtonUI(button_ClearSearch, "Dialog", 14, 100, Pos.CENTER, 510, 100);
		button_ClearSearch.setOnAction((_) -> { ControllerThread.clearSearch(); });
		textField_Search.setOnAction((_) -> { ControllerThread.performSearch(); });
		 
		// GUI Area 3
		listView_Posts.setLayoutX(20);
		listView_Posts.setLayoutY(170);
		listView_Posts.setPrefWidth(width - 40);
		listView_Posts.setPrefHeight(180);
		 
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
		        if (readReplyCount == 0) unreadPost = " (New Post)";
		        String text = "";
		        if ("my-posts".equals(currentThreadId)) {
		            text += "[" + p.getThreadId() + "] ";
		        }

		        text += p.getAuthorUsername() + ": " + p.getContent() + unreadPost + "  (" + replyCount + " replies)" + " (" + totalNewReplies + " New Replies)";

		        // Show grade info to staff/admin for any graded post
		        if (canGradeCurrentUser() && p.getNumericGrade() != -1) {
		            text += " | Grade: " + p.getNumericGrade() + " (" + p.getLetterGrade() + ")";
		        }

		        // Show grade info to a Role1 student only for their own graded post
		        else if (!canGradeCurrentUser()
		                && p.getNumericGrade() != -1
		                && p.getAuthorUsername().equals(theUser.getUserName())) {
		            text += " | Your Grade: " + p.getNumericGrade() + " (" + p.getLetterGrade() + ")";
		        }

		        setText(text);
		    }
		});

		// When a post is selected, update grade display
		listView_Posts.getSelectionModel().selectedItemProperty().addListener((obs, oldPost, newPost) -> {
			if (canGradeCurrentUser()) {
				updateGradeDisplay(newPost);
			}
		});
		
		// Grade area
		setupLabelUI(label_GradeTitle, "Arial", 16, 250, Pos.BASELINE_LEFT, 20, 355);

		setupTextUI(textField_Grade, "Dialog", 16, 160, Pos.BASELINE_LEFT, 20, 385, true);
		textField_Grade.setPromptText("Enter grade (0-100)");

		setupButtonUI(button_AssignGrade, "Dialog", 16, 150, Pos.CENTER, 200, 385);
		button_AssignGrade.setOnAction((_) -> { ControllerThread.performGrade(); });

		setupLabelUI(label_GradeResult, "Arial", 15, width - 40, Pos.BASELINE_LEFT, 20, 420);

		// Write-a-post area
		setupLabelUI(label_NewPost, "Arial", 16, width, Pos.BASELINE_LEFT, 20, 455);

		// Text area
		textArea_NewPost.setLayoutX(20);
		textArea_NewPost.setLayoutY(480);
		textArea_NewPost.setPrefWidth(width - 200);
		textArea_NewPost.setPrefHeight(70);

		// Post button
		setupButtonUI(button_Post, "Dialog", 16, 150, Pos.CENTER, width - 170, 480);
		button_Post.setOnAction((_) -> { ControllerThread.performPost(); });

		// ViewReplies button
		setupButtonUI(button_ViewReplies, "Dialog", 16, 150, Pos.CENTER, width - 170, 520);
		button_ViewReplies.setOnAction((_) -> { ControllerThread.openReplies(); });

		// Delete post button
		setupButtonUI(button_DeletePost, "Dialog", 16, 150, Pos.CENTER, width - 170, 560);
		button_DeletePost.setOnAction((_) -> { ControllerThread.deletePost(); });

		// Back button
		setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 20, 555);
		button_Back.setOnAction((_) -> { ControllerThread.goBack(); });
		 
		// Place all widget items into the root pane
		theRootPane.getChildren().addAll(
			label_PageTitle, label_UserDetails, line_Separator1,
			button_Post, button_Back, combo_ThreadSelect,
			label_NewPost, textArea_NewPost, listView_Posts, label_ThreadTitle,
			button_ViewReplies, button_DeletePost,
			label_Search, textField_Search, button_Search, button_ClearSearch,
			label_GradeTitle, textField_Grade, button_AssignGrade, label_GradeResult
		);
	}

	/*-*******************************************************************************************

	Helper methods used to minimize the number of lines of code needed above
	
	*/

	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	

	private void setupComboBoxUI(ComboBox<String> c, String ff, double f, double w, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}