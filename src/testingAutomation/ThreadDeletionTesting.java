package testingAutomation;

import database.Database;
import entityClasses.User;

public class ThreadDeletionTesting {

    private static int testsRun = 0;
    private static int testsPassed = 0;

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