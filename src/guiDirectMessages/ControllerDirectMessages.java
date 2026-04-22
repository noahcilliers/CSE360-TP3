package guiDirectMessages;

import entityClasses.Post;
import entityClasses.User;
import guiReplies.ViewReplies;
import guiThread.ViewThread;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;

import java.util.Optional;

/*******
 * <p> Title: ControllerThread Class </p>
 * 
 * <p> Description: This ControllerThread class represents the link between ViewThread, PostManager, and Database.</p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */
public class ControllerDirectMessages {

	private static final int MAX_POST_LENGTH = 300;
	/**********
	 * <p> 
	 * 
	 * Title: performPost() Method. </p>
	 * 
	 * <p> Description: Public method called whenever the post button is clicked, input validation included</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
	public static void performPost() {
	   if(ViewDirectMessages.requestBeingUpdated != null) {
		   Alert a = new Alert(AlertType.ERROR);
		   a.setTitle("Update In Progress");
		   a.setContentText("Finishing updating the selected request before posting a new one. ");
		   a.showAndWait();
		   return;
	   }
	    
	   String content = ViewDirectMessages.textArea_NewPost.getText().trim();
	    // input validation
	    if (content.isEmpty())
	    { 
       	 Alert a = new Alert(AlertType.ERROR);
		        a.setTitle("Post not written");
		        a.setContentText("You must write your post");
		        a.showAndWait();
       	
       	
       	return;
       }
	    
	    if (content.length() > MAX_POST_LENGTH) {
	        Alert a = new Alert(AlertType.ERROR);
	        a.setTitle("Post Too Long");
	        a.setHeaderText("Your post is too long.");
	        a.setContentText("Maximum length is " + MAX_POST_LENGTH +
	                         " characters. You are at " + content.length() + ".");
	        a.showAndWait();
	        return;
	    }
	    
	    
	    boolean ok = applicationMain.FoundationsMain.postManager.addPost(
	    	"requests",
	        ViewDirectMessages.theUser.getUserName(),
	        content
	    );

	    if (ok) {
	        ViewDirectMessages.textArea_NewPost.clear();
	        ViewDirectMessages.refreshPosts();
	    }
	}
	 /**********
		 * <p> 
		 * 
		 * Title: openReplies Method. </p>
		 * 
		 * <p> Description: Public method calls display replies with the post</p>
		 * 
		 * @author Noah Cilliers
		 * 
		 * 
		 */
	 public static void openReplies() {
		    Post p = ViewDirectMessages.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null)
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }
		    ViewReplies.displayReplies(ViewDirectMessages.theStage, ViewDirectMessages.theUser, p, "requests");
		}
	
	 /**********
		 * <p> 
		 * 
		 * Title: deletePost Method. </p>
		 * 
		 * <p> Description: Public method sends the post to database to be modified</p>
		 * 
		 * @author Noah Cilliers
		 * 
		 * 
		 */
	 public static void deletePost() {
		    Post p = ViewDirectMessages.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null) 
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }

		    if (!p.getAuthorUsername().equals(ViewDirectMessages.theUser.getUserName())) {
		        Alert a = new Alert(AlertType.ERROR);
		        a.setTitle("Delete Post");
		        a.setHeaderText("You can only delete your own posts.");
		        a.showAndWait();
		        return;
		    }
		    
		    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		    confirm.setTitle("Confirm Delete");
		    confirm.setHeaderText("Are you sure you want to delete this post?");
		    confirm.setContentText("This cannot be undone.");

		    Optional<ButtonType> result = confirm.showAndWait();

		    if (result.isPresent() && result.get() == ButtonType.OK) {
		    boolean ok = applicationMain.FoundationsMain.postManager.softDeletePost(p.getPostId());
		    	if (ok) {ViewDirectMessages.refreshPosts();}
		    }
		}
	 
	 
	 /**********
		 * <p> 
		 * 
		 * Title: goBack Method. </p>
		 * 
		 * <p> Description: Public method sends user to the home page</p>
		 * 
		 * @author Noah Cilliers
		 * 
		 * 
		 */
    public static void goBack(User theUser) {
        // send them back to their role home page (Role1Home )
    	//if staff 
    	//if(database.getRole2()){
        //guiRole2.ViewRole2Home.displayRole2Home(ViewDirectMessages.theStage, ViewDirectMessages.theUser);
        // }
        // else go to admin home
        // need to change this for threads too
     // send them back to their role home page	
    	
    	if(theUser.chosenRole.equals("Role1")) {
    		guiRole1.ViewRole1Home.displayRole1Home(ViewDirectMessages.theStage, ViewDirectMessages.theUser);
    	}
    	else if (theUser.chosenRole.equals("Role2")) {
    		guiRole2.ViewRole2Home.displayRole2Home(ViewDirectMessages.theStage, ViewDirectMessages.theUser);
    	}
    	else if (theUser.chosenRole.equals("Admin")) {
    		guiAdminHome.ViewAdminHome.displayAdminHome(ViewDirectMessages.theStage, ViewDirectMessages.theUser);
    	}
    	else if(theUser.chosenRole.equals("NULL")) {
    		System.out.println("NO ROLE CHOSEN");
    	};
    }
    
    /**
     * Title: performSearch, performs the search
     * @author Berto
     */
    public static void performSearch() {
    	ViewDirectMessages.refreshPosts();
    	
    }
    
    /**
     * Title: clearSearch, clears the search field
     * @author Berto
     */
    public static void clearSearch() {
    	ViewDirectMessages.textField_Search.clear();
    	ViewDirectMessages.refreshPosts();
    }
    
    /**
     * Title: updateRequests, updates a requests, also dose input validation
     * @author Berto
     */
    public static void updateRequest() {
        String content = ViewDirectMessages.textArea_NewPost.getText().trim();

        // If not currently updating, load selected request into the text box
        if (ViewDirectMessages.requestBeingUpdated == null) {
            Post p = ViewDirectMessages.listView_Posts.getSelectionModel().getSelectedItem();

            if (p == null) {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Request Selection");
                a.setContentText("Select a request to update.");
                a.showAndWait();
                return;
            }

            ViewDirectMessages.requestBeingUpdated = p;
            ViewDirectMessages.textArea_NewPost.setText(p.getContent());
            ViewDirectMessages.textArea_NewPost.requestFocus();
            ViewDirectMessages.button_UpdateRequest.setText("Save Update");
            return;
        }

        if (content.isEmpty()) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Update Request");
            a.setContentText("Request text cannot be empty.");
            a.showAndWait();
            return;
        }

        if (content.length() > MAX_POST_LENGTH) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Request Too Long");
            a.setHeaderText("Your updated request is too long.");
            a.setContentText("Maximum length is " + MAX_POST_LENGTH +
                    " characters. You are at " + content.length() + ".");
            a.showAndWait();
            return;
        }

        boolean ok = applicationMain.FoundationsMain.postManager.editPost(
                ViewDirectMessages.requestBeingUpdated.getPostId(),
                content
        );

        if (ok) {
            ViewDirectMessages.requestBeingUpdated = null;
            ViewDirectMessages.textArea_NewPost.clear();
            ViewDirectMessages.button_UpdateRequest.setText("Update Request");
            ViewDirectMessages.refreshPosts();
        }
    }
  
    /**
     * Title: closeRequest, closes a requests
     * @author Berto
     */
    public static void closeRequest() {
        Post p = ViewDirectMessages.listView_Posts.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Request Selection");
            a.setContentText("There is no request selected");
            a.showAndWait();
            return;
        }

        boolean ok = applicationMain.FoundationsMain.postManager.closePost(p.getPostId());
        if (ok) {
            ViewDirectMessages.refreshPosts();
        }
    }

    /**
     * Title: openRequest, sets a requests to open
     * @author Berto
     */
    public static void openRequest() {
        Post p = ViewDirectMessages.listView_Posts.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Request Selection");
            a.setContentText("There is no request selected");
            a.showAndWait();
            return;
        }

        boolean ok = applicationMain.FoundationsMain.postManager.openPost(p.getPostId());
        if (ok) {
            ViewDirectMessages.refreshPosts();
        }
    }

  
}