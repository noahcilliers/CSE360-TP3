package guiReplies;

import java.util.Optional;

import entityClasses.Post;
import entityClasses.Reply;
import guiReplies.ViewReplies;
import guiThread.ViewThread;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
/*******
 * <p> Title: ControllerReplies Class </p>
 * 
 * <p> Description: This ControllerReplies class represents the link between ViewReplies, ReplyManager, and Database.</p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */
class ControllerReplies {
	private static final int MAX_POST_LENGTH = 300;
	
	/**********
	 * <p> 
	 * 
	 * Title: performReply() Method. </p>
	 * 
	 * <p> Description: Public method to perform reply and check user input when making a reply</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
	 public static void performReply() {
	        String content = ViewReplies.textArea_NewReply.getText().trim();
	        if (content.isEmpty()) 
	        { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Reply not written");
			        a.setContentText("You must write your reply");
			        a.showAndWait();
	        	
	        	
	        	return;
	        }

	        if (content.length() > MAX_POST_LENGTH) {
		        Alert a = new Alert(AlertType.ERROR);
		        a.setTitle("Reply Too Long");
		        a.setHeaderText("Your reply is too long.");
		        a.setContentText("Maximum length is " + MAX_POST_LENGTH +
		                         " characters. You are at " + content.length() + ".");
		        a.showAndWait();
		        return;
		    }
	        
	        
	        boolean ok = applicationMain.FoundationsMain.replyManager.addReply(
	            ViewReplies.thePost.getPostId(),
	            ViewReplies.theUser.getUserName(),
	            content
	        );

	        if (ok) {
	            ViewReplies.textArea_NewReply.clear();
	            ViewReplies.refreshReplies();
	            ViewThread.refreshPosts();
	        }
	    }
	 /**********
		 * <p> 
		 * 
		 * Title: deleteReply() Method. </p>
		 * 
		 * <p> Description: Public method to delete replies, checking for input validation first</p>
		 * 
		 * @author Noah Cilliers
		 * 
		 * 
		 */
	 public static void deleteReply() {

		    Reply r = ViewReplies.listView_Replies.getSelectionModel().getSelectedItem();
		    if (r == null) 
		    { 
	        	 Alert a = new Alert(AlertType.ERROR);
			        a.setTitle("Reply Selection");
			        a.setContentText("There is no reply selected");
			        a.showAndWait();
	        	
	        	return;
	        }

		    if (!r.getAuthorUsername().equals(ViewReplies.theUser.getUserName())) {
		        Alert a = new Alert(Alert.AlertType.ERROR);
		        a.setTitle("Delete Reply");
		        a.setHeaderText("You can only delete your own replies.");
		        a.showAndWait();
		        return;
		    }

		    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
		    confirm.setTitle("Confirm Delete");
		    confirm.setHeaderText("Are you sure you want to delete this reply?");
		    confirm.setContentText("This cannot be undone.");

		    Optional<ButtonType> result = confirm.showAndWait();

		    if (result.isPresent() && result.get() == ButtonType.OK) {

		        boolean ok = applicationMain.FoundationsMain.replyManager
		                .softDeleteReply(r.getReplyId());

		        if (ok) {
		            ViewReplies.refreshReplies();
		        }
		    }
		}
	 
	 
	 
}
