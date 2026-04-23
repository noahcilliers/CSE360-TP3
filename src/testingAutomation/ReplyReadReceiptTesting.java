package testingAutomation;

import database.Database;
import entityClasses.Post;
import entityClasses.User;

public class ReplyReadReceiptTesting {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    /**********
     * 
	 * <p> Method: main(String[] args) </p>
	 * 
	 * <p> Description: main method of the read replies receipts testing suite </p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param args array of strings to be used as arguments to the program
	 * 
	 * 
	 */
    public static void main(String[] args) {
        Database db = new Database();

        try {
            db.connectToDatabase();
         

            User testUser = new User(
                    "receiptTester",
                    "password123",
                    "Test",
                    "",
                    "User",
                    "Test",
                    "receipt@test.com",
                    false,
                    true,
                    false
            );

            if (!db.doesUserExist(testUser.getUserName())) {
                db.register(testUser);
            }

            runReplyReadReceiptTests(db, testUser);

            System.out.println();
            System.out.println("=================================");
            System.out.println("Tests passed: " + testsPassed + "/" + testsRun);
            System.out.println("=================================");

        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        } 
        finally 
        {
            db.closeConnection();
        }
    }

    /**********
     * 
	 * <p> Method: runReplyReadReceiptTests(Database db, User user) </p>
	 * 
	 * <p> Description: Public method to perform a test case on a post instance</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param db Database class instance to be used in testing
	 * 
	 * @param user User class instance to be used in testing
	 * 
	 * 
	 * 
	 */
    private static void runReplyReadReceiptTests(Database db, User user) throws Exception
    {
        String threadId = "TestThread";
        String postText = "Automated test post";

        boolean postAdded = db.addPost(threadId, user.getUserName(), postText);
        assertTrue("Post should be added", postAdded);

        Post createdPost = db.getPostsForThread(threadId)
                .get(db.getPostsForThread(threadId).size() - 1);

        long postId = createdPost.getPostId();

        boolean reply1 = db.addReply(postId, "otherUser1", "First reply");
        boolean reply2 = db.addReply(postId, "otherUser2", "Second reply");

        assertTrue("First reply should be added", reply1);
        assertTrue("Second reply should be added", reply2);

        assertEquals("Reply count should start at 2",
                2, db.getReplyCountForPost(postId));

        assertEquals("Initially read reply count should be 0",
                0, db.getReadReplyCountForPost(postId, user));

        db.markAllRepliesAsRead(postId, user);

        assertEquals("After marking as read, read reply count should be 2",
                2, db.getReadReplyCountForPost(postId, user));

        db.markAllRepliesAsRead(postId, user);

        assertEquals("Duplicate mark should not increase read count",
                2, db.getReadReplyCountForPost(postId, user));

        boolean reply3 = db.addReply(postId, "otherUser3", "Third reply");
        assertTrue("Third reply should be added", reply3);

        assertEquals("Reply count should now be 3",
                3, db.getReplyCountForPost(postId));

        assertEquals("Read reply count should still be 2 before re-reading",
                2, db.getReadReplyCountForPost(postId, user));

        int unreadCount = db.getReplyCountForPost(postId) - db.getReadReplyCountForPost(postId, user);
        assertEquals("Unread count should be 1 after one new reply",
                1, unreadCount);

        db.markAllRepliesAsRead(postId, user);

        assertEquals("After re-marking, all 3 replies should be read",
                3, db.getReadReplyCountForPost(postId, user));
    }
    
    /**********
     * 
	 * <p> Method: assertEquals(String testName, int expected, int actual) </p>
	 * 
	 * <p> Description: Public method to perform a test case on a post instance</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param testName Name of the test being run for the purpose of identifying it against other tests being run
	 * 
	 * @param expected expected value of the test case
	 * 
	 * @param actual actual value of the test case
	 * 
	 */
    private static void assertEquals(String testName, int expected, int actual) {
        testsRun++;
        if (expected == actual) {
            testsPassed++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName
                    + " | expected: " + expected
                    + ", actual: " + actual);
        }
    }

    
    /**********
     * 
	 * <p> Method: assertTrue(String testName, boolean condition) </p>
	 * 
	 * <p> Description: Public method to perform a test case on a post instance</p>
	 * 
	 * @author Noah Cilliers
	 * 
	 * @param testName Name of the test being run for the purpose of identifying it against other tests being run
	 * 
	 * @param condition expected boolean value of the test case
	 * 
	 * 
	 */
    private static void assertTrue(String testName, boolean condition) {
        testsRun++;
        if (condition) {
            testsPassed++;
            System.out.println("[PASS] " + testName);
        } else {
            System.out.println("[FAIL] " + testName);
        }
    }
}
