package guiRole2;

import database.Database;
import guiThread.ViewThread;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 2 Home Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page is a stub for establish future roles for the application.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerRole2Home {
	
	/*-*******************************************************************************************
	
	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	private static final int MAX_THREAD_NAME_LENGTH = 10;
	/**
	 * Default constructor is not used.
	 */
	public ControllerRole2Home() {
	}

	/**********
	 * <p> Method: addThread() </p>
	 * 
	 * @param name name of the new thread to be created.
	 * 
	 * <p> Description: This method adds a new thread to the thread combobox. </p>
	 * 
	 */
	protected static void addThread() {
		String content = ViewRole2Home.textArea_NewThread.getText().trim();
	    // input validation
	   
	    if (content.isEmpty())
	    { 
       	 Alert a = new Alert(AlertType.ERROR);
		        a.setTitle("Thread name not written");
		        a.setContentText("You must write A thread name");
		        a.showAndWait();
       	
       	
       	return;
       }
	    
	    if (content.length() > MAX_THREAD_NAME_LENGTH) {
	        Alert a = new Alert(AlertType.ERROR);
	        a.setTitle("Thread name Too Long");
	        a.setHeaderText("Your thread name is too long.");
	        a.setContentText("Maximum length is " + MAX_THREAD_NAME_LENGTH +
	                         " characters. You are at " + content.length() + ".");
	        a.showAndWait();
	        return;
	    }
		
		//guiThread.ViewThread.Thread_list.add(name);
		boolean ret = theDatabase.createThread(content);
		if(ret) {
			System.out.println("Thread: " + content + " created succesfully");
			ViewRole2Home.textArea_NewThread.clear();
		}
		else {
			System.out.println("Thread: " + content + " not created succesfully");
			 Alert a = new Alert(AlertType.ERROR);
		        a.setTitle("Thread name already in use");
		        a.setHeaderText("Your thread name a duplicate");
		        a.setContentText("Please change your thread name input from " + content + ".");
		        a.showAndWait();
		        return;
		};
	};
	
	
	/**********
	 * <p> Method: removeThread() </p>
	 * 
	 * <p> Description: This method removes a thread from the thread combobox. </p>
	 * 
	 * 
	 */
	protected static void removeThread() {
	    String selectedThread = ViewRole2Home.combo_DeleteThread
	            .getSelectionModel()
	            .getSelectedItem();

	    if (selectedThread == null || selectedThread.trim().isEmpty()) {
	        Alert a = new Alert(AlertType.ERROR);
	        a.setTitle("No Thread Selected");
	        a.setContentText("Please select a thread to delete.");
	        a.showAndWait();
	        return;
	    }

	    boolean ret = theDatabase.deleteThread(selectedThread);

	    if (ret) {
	        ViewRole2Home.combo_DeleteThread.getSelectionModel().clearSelection();
	        System.out.println("Thread: " + selectedThread + " deleted successfully");
	    } else {
	        Alert a = new Alert(AlertType.ERROR);
	        a.setTitle("Delete Thread Failed");
	        a.setHeaderText("Thread could not be deleted.");
	        a.setContentText("The thread may be protected or may not exist.");
	        a.showAndWait();
	    }
	}
	
	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}	

	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
}
