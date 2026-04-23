package testingAutomation;

import java.util.List;

import database.Database;
import entityClasses.Post;
import modelClasses.PostManager;

/**
 * TestSearchhPost, test the logic in the posts search feature.
 *
 * @author Berto Silvar
 */
public class TestSearchPost {
	
	/**********
     * 
	 * <p> Method: main(String[] args) </p>
	 * 
	 * <p> Description: main, insert into data base the following post.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param args array of strings to be used as arguments to the program
	 * 
	 */
    public static void main(String[] args) {
        Database database = new Database();
        
        // test data
        database.addPost("general", "berto", "Need help with homework");
        database.addPost("general", "alex", "Project is finished");
        database.addPost("cse360", "berto", "Search feature added");
        database.addPost("random", "maria", "Completely unrelated post");

        PostManager postManager = new PostManager(database);
        postManager.refreshFromDatabase();

        testSearchPostsByContent(postManager);
        testSearchPostsByAuthor(postManager);
        testSearchPostsByThreadId(postManager);
        testSearchPostsCaseInsensitive(postManager);
        testSearchPostsEmptyKeywordReturnsAll(postManager);
        testSearchPostsNoMatches(postManager);
    }
    
    /**********
     * 
	 * <p> Method: testSearchPostsByContent(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbycontent, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsByContent(PostManager postManager) {
        List<Post> results = postManager.searchPosts("homework");
        
        if (results.size() == 1 &&
            results.get(0).getContent().toLowerCase().contains("homework")) {
            System.out.println("PASS: 1 post with homework found, content");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
    
    /**********
     * 
	 * <p> Method: testSearchPostsByAuthor(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbyauthor, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsByAuthor(PostManager postManager) {
        List<Post> results = postManager.searchPosts("berto");
        
        if (results.size() == 1) {
            System.out.println("PASS: 1 post with berto found, author");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
    
    /**********
     * 
	 * <p> Method: testSearchPostsByThreadId(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbythreadid, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsByThreadId(PostManager postManager) {
        List<Post> results = postManager.searchPosts("general");

        if (results.size() == 3) {
            System.out.println("PASS: 3 post with general found, threadId");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
    
    
    /**********
     * 
	 * <p> Method: testSearchPostsCaseInsensitive(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbycaseinsensitive, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsCaseInsensitive(PostManager postManager) {
        List<Post> results = postManager.searchPosts("PROJECT");

        if (results.size() == 1 &&
            results.get(0).getAuthorUsername().equals("alex")) {
            System.out.println("PASS: 1 post with PROJECT found, case Insensitive");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
    
   
    /**********
     * 
	 * <p> Method: testSearchPostsEmptyKeywordReturnsAll(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbyemptykeywordreturnall, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsEmptyKeywordReturnsAll(PostManager postManager) {
        List<Post> results = postManager.searchPosts("");

        if (results.size() == 3) {
            System.out.println("PASS: 3 post with \"\" found, empty keyword");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
    
    /**********
     * 
	 * <p> Method: testSearchPostsNoMatches(PostManager postManager) </p>
	 * 
	 * <p> Description: TestSearchhPostbynomatches, test the logic in the posts search feature.</p>
	 * 
	 * @author Berto Silvar
	 * 
	 * @param postManager PostManager class instance to be used in this test case
	 * 
	 */
    private static void testSearchPostsNoMatches(PostManager postManager) {
        List<Post> results = postManager.searchPosts("zzzzz");

        if (results.isEmpty()) {
            System.out.println("PASS: 0 post with zzzzz found, No matches");
        } else {
            System.out.println("FAIL: logic error");
        }
    }
}