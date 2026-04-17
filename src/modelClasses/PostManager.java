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
	 * <p> 
	 * 
	 * Title: setCurrentThread() Method. </p>
	 * 
	 * <p> Description: Public method sets the attribute current thead when changing threads</p>
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
	 * <p> 
	 * 
	 * Title: refreshFromDataBase() Method. </p>
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
	 * <p> 
	 * 
	 * Title: addPost() Method. </p>
	 * 
	 * <p> Description: Sends the post to database and then refreshes from the new database list</p>
	 * 
	 * @author Noah Cilliers
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
	

    public List<Post> getAllPosts() {
        return new ArrayList<>(allPosts);
    }
    
    /**********
	 * <p> 
	 * 
	 * Title: softDeletePost Method. </p>
	 * 
	 * <p> Description: Public method sends the post to database to be modified</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * 
	 */
    public boolean softDeletePost(long postId) {
        boolean ok = database.softDeletePost(postId);
        if (ok) refreshFromDatabase();
        return ok;
    }

    /**********
	 * <p> 
	 * 
	 * Title: searchPosts Method. </p>
	 * 
	 * <p> Description: Public method searches contents, author, and theard ID</p>
	 * 
	 * @author berto silvar
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
    
    public boolean closePost(long postId) {
    	boolean ok = database.setPostStatus(postId, "CLOSED");
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
    
    public boolean openPost(long postId) {
    	boolean ok = database.setPostStatus(postId, "OPEN");
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
    
    public boolean editPost(long postId, String newContent) {
    	boolean ok = database.editPost(postId, newContent);
    	if(ok) {
    		refreshFromDatabase();
    	}
    	return ok;
    }
}