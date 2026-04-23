package testingAutomation;

import java.sql.SQLException;

import database.Database;
import entityClasses.User;

/*******
 * <p> Title: ThreadDeletionTesting Class </p>
 * 
 * <p> Description: This class tests the functionality of the thread deletion function of the program. </p>
 * 
 * 
 * @author Roberto zozaya
 * 
 * 
 */
public class ThreadDeletionTesting {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    
    /**
	 * 
	 * <p> Method: main(String[] args) <p>
	 * 
	 * <p> Description: main function of the Thread Deletion testing suite. In this function we will connect to the database, create a new 
	 * user, run test cases, then print output<p>
	 *
	 * @author Roberto zozaya
	 * 
	 * @param args String array of arguments to give the program
	 * 
	 */
    public static void main(String[] args) {
        Database db = new Database();

        try {
            db.connectToDatabase();

            User testUser = new User(
                    "threadTester",
                    "password123",
                    "Test",
                    "",
                    "User",
                    "Test",
                    "thread@test.com",
                    false,
                    true,
                    false
            );

            if (!db.doesUserExist(testUser.getUserName())) {
                db.register(testUser);
            }

            runThreadDeletionTests(db, testUser);

            System.out.println();
            System.out.println("=================================");
            System.out.println("Tests passed: " + testsPassed + "/" + testsRun);
            System.out.println("=================================");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.closeConnection();
        }
    }
    
    /**
	 * 
	 * <p> Method: runThreadDeletionTests(Database db, User user) <p>
	 * 
	 * <p> Description: wrapper function to run all Thread Deletion test cases<p>
	 *
	 * @author Roberto zozaya
	 * 
	 * @throws SQLException if there is an issues accessing the user posts database.
	 * 
	 * @param db Database class instance to be used in this test case
	 * 
	 * @param user User class instance to be used in this test case
	 * 
	 */
    private static void runThreadDeletionTests(Database db, User user) throws Exception {
        String threadId = "DeleteTest";
        String post1 = "First post in delete test thread";
        String post2 = "Second post in delete test thread";

        // Make sure leftover test thread does not interfere
        if (db.getThreadsList().contains(threadId)) {
            db.deleteThread(threadId);
        }

        boolean threadCreated = db.createThread(threadId);
        assertTrue("Thread should be created", threadCreated);

        assertTrue("Thread list should contain new thread",
                db.getThreadsList().contains(threadId));

        boolean postAdded1 = db.addPost(threadId, user.getUserName(), post1);
        boolean postAdded2 = db.addPost(threadId, user.getUserName(), post2);

        assertTrue("First post should be added to test thread", postAdded1);
        assertTrue("Second post should be added to test thread", postAdded2);

        assertEquals("Thread should have 2 posts before deletion",
                2, db.getPostsForThread(threadId).size());

        boolean deleted = db.deleteThread(threadId);
        assertTrue("Custom thread should be deleted", deleted);

        assertTrue("Thread list should no longer contain deleted thread",
                !db.getThreadsList().contains(threadId));

        assertEquals("Posts for deleted thread should be removed from memory",
                0, db.getPostsForThread(threadId).size());

        boolean deleteAgain = db.deleteThread(threadId);
        assertTrue("Deleting a thread that no longer exists should fail", !deleteAgain);

        boolean deleteGeneral = db.deleteThread("general");
        assertTrue("Protected thread 'general' should not be deletable", !deleteGeneral);

        boolean deleteRequests = db.deleteThread("requests");
        assertTrue("Protected thread 'requests' should not be deletable", !deleteRequests);

        boolean deleteNull = db.deleteThread(null);
        assertTrue("Deleting null thread name should fail", !deleteNull);

        boolean deleteBlank = db.deleteThread("   ");
        assertTrue("Deleting blank thread name should fail", !deleteBlank);
    }
    
    /**
   	 * 
   	 * <p> Method: assertEquals(String testName, int expected, int actual) <p>
   	 * 
   	 * <p> Description: assert function to test if the actual value equals the expected value<p>
   	 *
   	 * @author Roberto zozaya
   	 * 
   	 * @param testName name of the test case
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
    
    /**
   	 * 
   	 * <p> Method: assertTrue(String testName, boolean condition) <p>
   	 * 
   	 * <p> Description: assert function to test if the test passes the condition or not<p>
   	 *
   	 * @author Roberto zozaya
   	 * 
   	 * @param testName name of the test case
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