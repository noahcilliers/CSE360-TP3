package entityClasses;

/*******
 * <p> Title: Post Class </p>
 * 
 * <p> Description: This Post class represents a post entity in the system.  It contains the post's
 *  details such as postId, threadId, and contents. </p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */

public class Post {
	private long postId;
	private String thread;
	private String authorUsername;
	private String contents;
	private String status;
	private boolean edited;
	
	public Post(long postId, String thread, String authorUsername, String contents) {
		this(postId, thread, authorUsername, contents, "", false);
	}


public Post(long postId, String thread, String authorUsername, String contents, String status, boolean edited) 
{
	this.postId = postId;
	this.thread = thread;
	this.authorUsername = authorUsername;
	this.contents = contents;
	this.status = status;
	this.edited = edited;
}


public long getPostId() { return postId; }
public String getThreadId() { return thread; }
public String getAuthorUsername() { return authorUsername; }
public String getContent() { return contents; }
public void setContent(String s){contents = s;}
public void setAuthorUsername(String s) {authorUsername =s ;}
public String getStatus() {return status;}
public void setStatus(String status) {this.status = status;}
public boolean isEdited() {return edited;}
public void setEdited(boolean edited) {this.edited = edited;}

}