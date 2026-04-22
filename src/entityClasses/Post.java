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
	
	private int numericGrade = -1;
	private String letterGrade = "Not Assigned";
	



public Post(long postId, String thread, String authorUsername, String contents) 
{
	this.postId = postId;
	this.thread = thread;
	this.authorUsername = authorUsername;
	this.contents = contents;
}


public long getPostId() { return postId; }
public String getThreadId() { return thread; }
public String getAuthorUsername() { return authorUsername; }
public String getContent() { return contents; }
public void setContent(String s){contents = s;}
public void setAuthorUsername(String s) {authorUsername =s ;}

public int getNumericGrade() {
    return numericGrade;
}

public void setNumericGrade(int numericGrade) {
    this.numericGrade = numericGrade;
}

public String getLetterGrade() {
    return letterGrade;
}

public void setLetterGrade(String letterGrade) {
    this.letterGrade = letterGrade;
}

}