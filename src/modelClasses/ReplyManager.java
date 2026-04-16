package modelClasses;

import database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entityClasses.Post;
import entityClasses.Reply;

/*******
 * <p> Title: Reply Manager Class </p>
 * 
 * <p> Description: This Reply Manager class represents the link between ControllerReplies, Reply, and Database.</p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */

public class ReplyManager {
	// objects for holding replies and accessing database
    private List<Reply> allReplies = new ArrayList<>();
    private Database database;
    
    // constructor
    public ReplyManager(Database database) {
        this.database = database;
       // refreshFromDatabase();
    }
    /**********
	 * <p> 
	 * 
	 * Title: refreshFromDatabase Method. </p>
	 * 
	 * <p> Description: Public method gets all the replies from the database</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
    public void refreshFromDatabase() {
        allReplies = database.getAllReplies();
    }
    /**********
	 * <p> 
	 * 
	 * Title: addReply Method. </p>
	 * 
	 * <p> Description: Public method sends the reply to database to be created</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
    public boolean addReply(long parentPostId,
                            String author,
                            String content) {

        boolean ok = database.addReply(parentPostId, author, content);

        try {
	    	database.insertReply(String.valueOf(parentPostId), author, content);
	    	database.dumpReplys();
	    	}
	    catch(SQLException e) {
	    	e.printStackTrace();
	    };
        
        if (ok) {
            refreshFromDatabase();
        }
        return ok;
    }
    /**********
	 * <p> 
	 * 
	 * Title: softDeleteReply Method. </p>
	 * 
	 * <p> Description: Public method sends the reply to database to be modified</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
    public boolean softDeleteReply(long replyId) {
        boolean ok = database.softDeleteReply(replyId);
        if (ok) refreshFromDatabase();
        return ok;
    }

    /**********
	 * <p> 
	 * 
	 * Title: getRepliesforPost Method. </p>
	 * 
	 * <p> Description: Public method gets all the replies for a post to be displayed</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
    public List<Reply> getRepliesForPost(long postId) {
        return allReplies.stream()
                .filter(r -> r.getParentPostId() == postId)
                .toList();
    }

   
}
