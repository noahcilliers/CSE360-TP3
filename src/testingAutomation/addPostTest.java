package testingAutomation;
import database.Database;
import java.util.ArrayList;
import java.util.List;
import entityClasses.Post;
import modelClasses.PostManager;





/*******
 * <p> Title: AddPostTestingAutomatioin Class </p>
 * 
 * <p> Description: This class tests the functionality for add post with different contents within the post.
 *  It prints to the console if the code passes each test.
 *  </p>
 * 
 * <p> Copyright: Noah Cilliers © 2026 </p>
 * 
 * @author Noah Cillers
 * 
 * 
 */



public class addPostTest {

	
	/**********
     * 
	 * <p> Method: performTestCase(int testCase, Post p, boolean expectedPass) </p>
	 * 
	 * <p> Description: Public method to perform a test case on a post instance</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param testCase number to identify this test case against others during testing
	 * 
	 * @param p Post class instance
	 * 
	 * @param expectedPass boolean that tracks weather this test is expected to fail or succeed
	 * 
	 * 
	 */
	public static void performTestCase(int testCase, Post p, boolean expectedPass) {
	    Database database = new Database();

	    try {
	        database.connectToDatabase();
	    } catch (Exception e) {
	        System.out.println("Test Case " + testCase + ": FAILED - Could not connect to database.");
	        e.printStackTrace();
	        return;
	    }

	    PostManager postManager = new PostManager(database);

	    boolean result = postManager.addPost(p.getThreadId(), p.getAuthorUsername(), p.getContent());

	    List<Post> posts = postManager.getAllPosts();
	    boolean foundInDatabase = false;

	    if (p.getContent() != null) {
	        String trimmedContent = p.getContent().trim();
	        for (Post post : posts) {
	            if (post.getContent().equals(trimmedContent)
	                    && post.getAuthorUsername().equals(p.getAuthorUsername())
	                    && post.getThreadId().equals(p.getThreadId())) {
	                foundInDatabase = true;
	                break;
	            }
	        }
	    }

	    if (expectedPass) {
	        if (result && foundInDatabase) {
	            System.out.println("Test Case " + testCase + ": PASSED - Post was successfully added to the database.");
	        } else {
	            System.out.println("Test Case " + testCase + ": FAILED - Expected post to be added, but it was not found in the database.");
	        }
	    } else {
	        if (!result && !foundInDatabase) {
	            System.out.println("Test Case " + testCase + ": PASSED - Post was correctly rejected.");
	        } else {
	            System.out.println("Test Case " + testCase + ": FAILED - Expected post to be rejected, but it was added to the database.");
	        }
	    }
	}

	
	/**********
     * 
	 * <p> Method: main(String[] args) </p>
	 * 
	 * <p> Description: main method of the Post adding testing suite </p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param args array of strings to be used as arguments to the program
	 * 
	 * 
	 */
	public static void main(String[] args) {
		// Test Case 1: Valid post with normal content
		Post p1 = new Post(0, "general", "testUser", "Hello, this is a test post!");
		Post p2 = new Post(0, "general", "testUser", "");
		Post p3 = new Post(0, "general", "testUser", null);
		Post p4 = new Post(0, "general", "testUser", "   ");
		Post p5 = new Post(0, "announcements", "admin", "Important announcement!");
		
		performTestCase(1, p1, true);

		// Test Case 2: Post with empty content (should fail)
		performTestCase(2, p2, false);

		// Test Case 3: Post with null content (should fail)
		performTestCase(3, p3, false);

		// Test Case 4: Post with whitespace-only content (should fail)
		performTestCase(4, p4, false);
		
	}
}

