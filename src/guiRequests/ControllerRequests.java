package guiRequests;

import entityClasses.Post;
import guiReplies.ViewReplies;
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
public class ControllerRequests {

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
	   if(ViewRequests.requestBeingUpdated != null) {
		   Alert a = new Alert(AlertType.ERROR);
		   a.setTitle("Update In Progress");
		   a.setContentText("Finishing updating the selected request before posting a new one. ");
		   a.showAndWait();
		   return;
	   }
	    
	   String content = ViewRequests.textArea_NewPost.getText().trim();
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
	        ViewRequests.theUser.getUserName(),
	        content
	    );

	    if (ok) {
	        ViewRequests.textArea_NewPost.clear();
	        ViewRequests.refreshPosts();
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
		    Post p = ViewRequests.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null)
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }
		    ViewReplies.displayReplies(ViewRequests.theStage, ViewRequests.theUser, p, "requests");
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
		    Post p = ViewRequests.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null) 
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }

		    if (!p.getAuthorUsername().equals(ViewRequests.theUser.getUserName())) {
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
		    	if (ok) {ViewRequests.refreshPosts();}
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
    public static void goBack() {
        // send them back to their role home page (Role1Home )
    	//if staff 
    	//if(database.getRole2()){
        guiRole2.ViewRole2Home.displayRole2Home(ViewRequests.theStage, ViewRequests.theUser);
        // }
        // else go to admin home
        // need to change this for threads too
    }
    
    public static void performSearch() {
    	ViewRequests.refreshPosts();
    	
    }
    
    public static void clearSearch() {
    	ViewRequests.textField_Search.clear();
    	ViewRequests.refreshPosts();
    }

    public static void updateRequest() {
        String content = ViewRequests.textArea_NewPost.getText().trim();

        // If not currently updating, load selected request into the text box
        if (ViewRequests.requestBeingUpdated == null) {
            Post p = ViewRequests.listView_Posts.getSelectionModel().getSelectedItem();

            if (p == null) {
                Alert a = new Alert(AlertType.ERROR);
                a.setTitle("Request Selection");
                a.setContentText("Select a request to update.");
                a.showAndWait();
                return;
            }

            ViewRequests.requestBeingUpdated = p;
            ViewRequests.textArea_NewPost.setText(p.getContent());
            ViewRequests.textArea_NewPost.requestFocus();
            ViewRequests.button_UpdateRequest.setText("Save Update");
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
                ViewRequests.requestBeingUpdated.getPostId(),
                content
        );

        if (ok) {
            ViewRequests.requestBeingUpdated = null;
            ViewRequests.textArea_NewPost.clear();
            ViewRequests.button_UpdateRequest.setText("Update Request");
            ViewRequests.refreshPosts();
        }
    }
  
    public static void closeRequest() {
        Post p = ViewRequests.listView_Posts.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Request Selection");
            a.setContentText("There is no request selected");
            a.showAndWait();
            return;
        }

        boolean ok = applicationMain.FoundationsMain.postManager.closePost(p.getPostId());
        if (ok) {
            ViewRequests.refreshPosts();
        }
    }

    public static void openRequest() {
        Post p = ViewRequests.listView_Posts.getSelectionModel().getSelectedItem();
        if (p == null) {
            Alert a = new Alert(AlertType.ERROR);
            a.setTitle("Request Selection");
            a.setContentText("There is no request selected");
            a.showAndWait();
            return;
        }

        boolean ok = applicationMain.FoundationsMain.postManager.openPost(p.getPostId());
        if (ok) {
            ViewRequests.refreshPosts();
        }
    }

  
}