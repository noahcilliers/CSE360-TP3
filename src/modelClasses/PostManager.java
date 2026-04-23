package modelClasses;
import database.Database;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import entityClasses.Post;


/*******
 * <p> Title: Post Manager Class </p>
 * 
 * <p> Description: This Post Manager class represents the link between ControllerThread, Post, and Database.</p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */
public class PostManager {
	// variables
	 private Database database;
	 private List<Post> allPosts = new ArrayList<>();
	 private String currentThreadId = "general";
	
	//constuctor 
	public PostManager(Database database)
	{
		this.database = database;
        refreshFromDatabase();
	}
	//methods
	
	/**********
	 * 
	 * <p> Method: setCurrentThread(String threadId) </p>
	 * 
	 * <p> Description: Public method sets the attribute current thead when changing threads</p>
	 * 
	 * @param threadId name of the thread to set 
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
	public void setCurrentThread(String threadId) {
	    this.currentThreadId = threadId;
	    refreshFromDatabase();
	}
   
	/**********
	 * 
	 * <p> Method: refreshFromDatabase() </p>
	 * 
	 * <p> Description: Public method refreshes the list of posts form database</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
	public void refreshFromDatabase() {
		if(currentThreadId.equals("my-posts")) 
		{
			allPosts = database.getPostsFromUser(database.getCurrentUsername());
		}
		else {
		allPosts = database.getPostsForThread(currentThreadId);
		}
    }
	
	/**********
	 * 
	 * <p> Method:  addPost(String threadId, String authorUsername, String content) </p>
	 * 
	 * <p> Description: Sends the post to database and then refreshes from the new database list</p>
	 * 
	 * @param threadId name of the thread to add the post to
	 * 
	 * @param authorUsername username of the author of the post to add
	 * 
	 * @param content text content of the post to add
	 * 
	 * @author Noah Cilliers
	 * 
	 * @return ok boolean to see if the post was added successfully
	 * 
	 * 
	 */
	public boolean addPost(String threadId, String authorUsername, String content) {

	    if (content == null || content.trim().isEmpty()) {
	        return false;
	    }

	    // First save to database
	    boolean ok = database.addPost(threadId, authorUsername, content);
	    if(ok) {
	    	Post newestPost = database.getPostById(getLatestPostId());
	    	if (newestPost != null) {
	    		try {
	    	    	database.insertPost(newestPost.getPostId(),
	    	    			newestPost.getThreadId(),
	    	    			newestPost.getAuthorUsername(),
	    	    			newestPost.getContent(),
	    	    			newestPost.getStatus(),
	    	    			newestPost.isEdited());
	    	    	database.dumpPosts();
	    	    	}
	    	    catch(SQLException e) {
	    	    	e.printStackTrace();
	    	    };	
	    	}
	    }
	    
	    if (ok) {
	        // Reload posts from database 
	        refreshFromDatabase();
	    }

	    return ok;
	}
	
	
	/**********
	 * 
	 * <p> Method: getAllPosts() </p>
	 * 
	 * <p> Description: Public method gets the runtime data structure storing the system's posts </p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 * @return allPosts data structure that stores all posts
	 * 
	 */
	
    public List<Post> getAllPosts() {
        return new ArrayList<>(allPosts);
    }
    
    /**********
     * 
     * 
	 * <p> Method: softDeletePost(long postId) </p>
	 * 
	 * <p> Description: Public method sends the post to database to be modified</p>
	 * 
	 * @param postId number used to identity the post to delete
	 * 
	 * @author Noah Cilliers
	 * 
	 * @return ok boolean to see if the post was deleted successfully
	 * 
	 */
    public boolean softDeletePost(long postId) {
        boolean ok = database.softDeletePost(postId);
        if (ok) refreshFromDatabase();
        return ok;
    }

    /**********
     * 
	 * <p> Method: searchPosts(String keyword) </p>
	 * 
	 * <p> Description: Public method searches contents, author, and theard ID</p>
	 * 
	 * @param keyword String input given by the user to compare to post contents for the purposes of searching the list of posts
	 * 
	 * @author berto silvar
	 * 
	 * @return results list data structure storing all posts that match give keyword
	 * 
	 * 
	 */
    public List<Post> searchPosts(String keyword){
    	if(keyword == null || keyword.trim().isEmpty())
    	{
    		return getAllPosts();
    	}
    	
    	String lowerKeyword = keyword.toLowerCase();
    	List<Post> results = new ArrayList<>();
    	
    	for (Post p : allPosts)
    	{
    		if (p.getContent().toLowerCase().contains(lowerKeyword) || 
    				p.getAuthorUsername().toLowerCase().contains(lowerKeyword) ||
    				p.getThreadId().toLowerCase().contains(lowerKeyword) ||
    				p.getStatus().toLowerCase().contains(lowerKeyword))
    		{
    			results.add(p);
    		}
    	}
    	
    	return results;
    }
    
    /**
     * <p> Method: getLatestPostId() <p>
     * 
     * <p> Description: getLatestPostId, gets the latest postId <p>
     * 
     * @author Berto
     * 
     * @return maxId The latests post made in the system's postId number
     * 
     */
    private long getLatestPostId() {
    	long maxId = -1;
    	for (Post p : database.getPostsForThread(currentThreadId)) {
    		if (p.getPostId() > maxId) {
    			maxId = p.getPostId();
    		}
    	}
    	
    	if(maxId == -1) {
    		for(Post p : allPosts) {
    			if(p.getPostId() > maxId) {
    				maxId = p.getPostId();
    			}
    		}
    	}
    	return maxId;
    }
    
    /**
     * 
     * <p> Method: closePost(long postId) <p>
     * 
     * <p> Description: closePost, sets a requests to closed <p>
     * 
     * @author Berto
     * 
     * @param postId number used to identify post to close
     * 
     * @return ok boolean to see if the post closed successfully
     * 
     */
    public boolean closePost(long postId) {
    	boolean ok = database.setPostStatus(postId, "CLOSED");
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
    
    
    /**
     * <p> Method: openPost(long postId) <p>
     * 
     * <p> Description: openPost, sets a post to open <p>
     * 
     * @author Berto
     * 
     * @param postId number used to identify post to open
     * 
     * @return ok boolean to see if the post opened successfully
     *
     */
    public boolean openPost(long postId) {
    	boolean ok = database.setPostStatus(postId, "OPEN");
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
    
    /**
     * <p> Method: editPost(long postId, String newContent) <p>
     * 
     * <p> Description: editPost, updates a post with the new body <p>
     * 
     * @author Berto
     * 
     * @param postId number used to identify post to edit
     * 
     * @param newContent new text of post
     * 
     * @return ok boolean to see if the post edited successfully
     */
    public boolean editPost(long postId, String newContent) {
    	boolean ok = database.editPost(postId, newContent);
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
}