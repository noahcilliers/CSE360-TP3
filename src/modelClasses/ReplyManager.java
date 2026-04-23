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
     * 
	 * <p> Method: refreshFromDatabase() </p>
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
     * 
	 * <p> Method: addReply(long parentPostId, String author, String content) </p>
	 * 
	 * <p> Description: Public method sends the reply to database to be created</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param parentPostId number to identify what post this reply is being added to
	 * 
	 * @param author userName of the person who is making the reply
	 * 
	 * @param content text content of the reply to be added
	 * 
	 * @return ok boolean to see if the reply was added successfully
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
     * 
	 * <p> Method: softDeleteReply(long replyId) </p>
	 * 
	 * <p> Description: Public method sends the reply to database to be modified</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param replyId number used to identify the reply to be deleted
	 * 
	 * @return ok boolean to see if the reply is deleted successfully
	 * 
	 * 
	 */
    public boolean softDeleteReply(long replyId) {
        boolean ok = database.softDeleteReply(replyId);
        if (ok) refreshFromDatabase();
        return ok;
    }

    /**********
     * 
	 * <p> Method: getRepliesForPost(long postId) </p>
	 * 
	 * <p> Description: Public method gets all the replies for a post to be displayed</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param postId number to identify the post to get all the replies for
	 * 
	 * @return replyList data structure that stores replies for a given post
	 * 
	 * 
	 */
    public List<Reply> getRepliesForPost(long postId) {
        return allReplies.stream()
                .filter(r -> r.getParentPostId() == postId)
                .toList();
    }

    /**********
	 *
	 * <p> Method: searchReplies(long postID,String keyword) </p>
	 * 
	 * <p> Description: Public method searches contents, author, and theard ID</p>
	 * 
	 * @author berto silvar
	 * 
	 * @return results data structure that stores replies for a given post that matches the given keyword
	 * 
	 */
    public List<Reply> searchReplies(long postID,String keyword){
    	if(keyword == null || keyword.trim().isEmpty())
    	{
    		return getRepliesForPost(postID);
    	}
    	
    	String lowerKeyword = keyword.toLowerCase();
    	List<Reply> results = new ArrayList<>();
    	
    	for (Reply r : allReplies)
    	{
    		if(r.getParentPostId() == postID) {
    			if(r.getContent().toLowerCase().contains(lowerKeyword) ||
    					r.getAuthorUsername().toLowerCase().contains(lowerKeyword)) {
    				results.add(r);
    			}
    		}
    	}
    	
    	return results;
    }
   
}
