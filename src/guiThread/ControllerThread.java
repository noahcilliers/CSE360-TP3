package guiThread;

import entityClasses.Post;
import guiReplies.ViewReplies;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
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
public class ControllerThread {

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
	    String content = ViewThread.textArea_NewPost.getText().trim();
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
	    	ViewThread.currentThreadId,
	        ViewThread.theUser.getUserName(),
	        content
	    );

	    if (ok) {
	        ViewThread.textArea_NewPost.clear();
	        ViewThread.refreshPosts();
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
		    Post p = ViewThread.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null)
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }
		    ViewReplies.displayReplies(ViewThread.theStage, ViewThread.theUser, p);
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
		    Post p = ViewThread.listView_Posts.getSelectionModel().getSelectedItem();
		    if (p == null) 
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Post Selection");
			        a.setContentText("There is no post selected");
			        a.showAndWait();
	        	
	        	return;
	        }

		    if (!p.getAuthorUsername().equals(ViewThread.theUser.getUserName())) {
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
		    	if (ok) {ViewThread.refreshPosts();}
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

        guiRole1.ViewRole1Home.displayRole1Home(ViewThread.theStage, ViewThread.theUser);
    }

  

  
}