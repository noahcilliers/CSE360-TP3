package testingAutomation;

import java.util.List;

import database.Database;
import entityClasses.Reply;
import modelClasses.ReplyManager;

/**
 * TestSearchhreply, test the logic in the posts search feature.
 *
 * @author Berto Silvar
 */
public class TestSearchReply {
	
	/**
	 * 
	 * <p> Method: main(String[] args) <p>
	 * 
	 * <p> Description: main function of the Reply Search testing suite. In this function we will Insert replies into the database, 
	 * create a new ReplyManager, run test cases<p>
	 *
	 * @author Berto Silvar
	 * 
	 * @param args String array of arguments to give the program
	 * 
	 */
    public static void main(String[] args) {
        Database database = new Database();

        // test data
        database.addReply(1, "berto", "Need help with homework");
        database.addReply(1, "alex", "I finished the project");
        database.addReply(1, "berto", "Search feature works now");
        database.addReply(2, "maria", "This belongs to another post");

        ReplyManager replyManager = new ReplyManager(database);
        replyManager.refreshFromDatabase();

        testSearchRepliesByContent(replyManager);
        testSearchRepliesByAuthor(replyManager);
        testSearchRepliesIgnoresOtherPosts(replyManager);
        testSearchRepliesCaseInsensitive(replyManager);
        testSearchRepliesEmptyKeywordReturnsAllRepliesForPost(replyManager);
        testSearchRepliesNoMatches(replyManager);
    }
    
    /**
     * <p> Method: testSearchRepliesByContent(ReplyManager replyManager)<p>
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <p>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing
     *
     */
    private static void testSearchRepliesByContent(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "homework");

        if (results.size() == 1 &&
            results.get(0).getContent().equals("Need help with homework")) {
            System.out.println("PASS: testSearchRepliesByContent");
        } else {
            System.out.println("FAIL: testSearchRepliesByContent");
        }
    }
    
    /**
     * <p> Method: testSearchRepliesByAuthor(ReplyManager replyManager)<p>
     * 
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <p>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing 
     * 
     */
    private static void testSearchRepliesByAuthor(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "berto");

        if (results.size() == 2) {
            System.out.println("PASS: testSearchRepliesByAuthor");
        } else {
            System.out.println("FAIL: testSearchRepliesByAuthor");
        }
    }
    
    /**
     * <p> Method: testSearchRepliesIgnoresOtherPosts(ReplyManager replyManager) <p>
     * 
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <p>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing
     * 
     */
    private static void testSearchRepliesIgnoresOtherPosts(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "maria");

        if (results.isEmpty()) {
            System.out.println("PASS: testSearchRepliesIgnoresOtherPosts");
        } else {
            System.out.println("FAIL: testSearchRepliesIgnoresOtherPosts");
        }
    }
    
    /**
     * <p> Method: testSearchRepliesCaseInsensitive(ReplyManager replyManager) <p>
     * 
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <p>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing
     * 
     */
    private static void testSearchRepliesCaseInsensitive(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "PROJECT");

        if (results.size() == 1 &&
            results.get(0).getAuthorUsername().equals("alex")) {
            System.out.println("PASS: testSearchRepliesCaseInsensitive");
        } else {
            System.out.println("FAIL: testSearchRepliesCaseInsensitive");
        }
    }
    
    /**
     * <p> Method: testSearchRepliesEmptyKeywordReturnsAllRepliesForPost(ReplyManager replyManager) <P>
     * 
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <P>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing
     * 
     */
    private static void testSearchRepliesEmptyKeywordReturnsAllRepliesForPost(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "");

        if (results.size() == 3) {
            System.out.println("PASS: testSearchRepliesEmptyKeywordReturnsAllRepliesForPost");
        } else {
            System.out.println("FAIL: testSearchRepliesEmptyKeywordReturnsAllRepliesForPost");
        }
    }
    
    /**
     * <p> Method: testSearchRepliesNoMatches(ReplyManager replyManager) <P>
     * 
     * <p> Description: TestSearchhreplybycontent, test the logic in the posts search feature. <P>
     *
     * @author Berto Silvar
     * 
     * @param replyManager reference to the ReplyManager class instance being used for testing
     * 
     */
    private static void testSearchRepliesNoMatches(ReplyManager replyManager) {
        List<Reply> results = replyManager.searchReplies(1, "zzzzz");

        if (results.isEmpty()) {
            System.out.println("PASS: testSearchRepliesNoMatches");
        } else {
            System.out.println("FAIL: testSearchRepliesNoMatches");
        }
    }
}
