package testingAutomation;

import database.Database;
import entityClasses.Post;
import modelClasses.PostManager;

import java.util.List;

/**
 * TestRequestsWorkflow, tests the core requests workflow:
 * creating a request, closing it, and reopening it.
 *
 * @author Berto Silvar
 */
public class TestRequest {

    /**
     * main, seeds the requests thread and runs all test cases.
     *
     * @author Berto Silvar
     */
    public static void main(String[] args) {
        Database database = new Database();

        try {
            database.connectToDatabase();
        } catch (Exception e) {
            System.out.println("FATAL: Could not connect to database.");
            e.printStackTrace();
            return;
        }

        database.createThread("requests");

        PostManager postManager = new PostManager(database);
        postManager.setCurrentThread("requests");

        // seed test data
        postManager.addPost("requests", "staff1", "How do I submit Assignment 3?");
        postManager.addPost("requests", "staff2", "Close-test-request");
        postManager.addPost("requests", "staff3", "Reopen-test-request");

        postManager.refreshFromDatabase();

        testCreateRequest(database, postManager);
        testCloseRequest(database, postManager);
        testReopenRequest(database, postManager);

        database.closeConnection();
    }

    /**
     * testCreateRequest, tests that a new request is stored with status OPEN.
     *
     * @author Berto Silvar
     */
    private static void testCreateRequest(Database database, PostManager postManager) {
        List<Post> posts = database.getPostsForThread("requests");
        Post p = null;
        for (Post post : posts) {
            if (post.getAuthorUsername().equals("staff1") &&
                post.getContent().equals("How do I submit Assignment 3?")) {
                p = post;
            }
        }

        if (p != null) {
            System.out.println("PASS: Test Case 1: request appears in requests thread");
        } else {
            System.out.println("FAIL: Test Case 1: request appears in requests thread");
        }

        if (p != null && p.getStatus().equals("OPEN")) {
            System.out.println("PASS: Test Case 2: new request status is OPEN");
        } else {
            System.out.println("FAIL: Test Case 2: new request status is OPEN");
        }

        if (p != null && !p.isEdited()) {
            System.out.println("PASS: Test Case 3: new request edited flag is false");
        } else {
            System.out.println("FAIL: Test Case 3: new request edited flag is false");
        }
    }

    /**
     * testCloseRequest, tests that closePost sets the request status to CLOSED.
     *
     * @author Berto Silvar
     */
    private static void testCloseRequest(Database database, PostManager postManager) {
        List<Post> posts = database.getPostsForThread("requests");
        Post p = null;
        for (Post post : posts) {
            if (post.getAuthorUsername().equals("staff2") &&
                post.getContent().equals("Close-test-request")) {
                p = post;
            }
        }

        if (p == null) {
            System.out.println("FAIL: Test Case 4: seeded close-test post not found");
            System.out.println("FAIL: Test Case 5: status is CLOSED after closePost");
            return;
        }

        boolean closed = postManager.closePost(p.getPostId());

        if (closed) {
            System.out.println("PASS: Test Case 4: closePost returns true");
        } else {
            System.out.println("FAIL: Test Case 4: closePost returns true");
        }

        Post after = database.getPostById(p.getPostId());

        if (after != null && after.getStatus().equals("CLOSED")) {
            System.out.println("PASS: Test Case 5: status is CLOSED after closePost");
        } else {
            System.out.println("FAIL: Test Case 5: status is CLOSED after closePost");
        }
    }

    /**
     * testReopenRequest, tests that openPost on a CLOSED request sets status back to OPEN.
     *
     * @author Berto Silvar
     */
    private static void testReopenRequest(Database database, PostManager postManager) {
        List<Post> posts = database.getPostsForThread("requests");
        Post p = null;
        for (Post post : posts) {
            if (post.getAuthorUsername().equals("staff3") &&
                post.getContent().equals("Reopen-test-request")) {
                p = post;
            }
        }

        if (p == null) {
            System.out.println("FAIL: Test Case 6: seeded reopen-test post not found");
            System.out.println("FAIL: Test Case 7: status is OPEN after openPost");
            return;
        }

        // close it first so we can reopen it
        postManager.closePost(p.getPostId());

        boolean reopened = postManager.openPost(p.getPostId());

        if (reopened) {
            System.out.println("PASS: Test Case 6: openPost returns true");
        } else {
            System.out.println("FAIL: Test Case 6: openPost returns true");
        }

        Post after = database.getPostById(p.getPostId());

        if (after != null && after.getStatus().equals("OPEN")) {
            System.out.println("PASS: Test Case 7: status is OPEN after openPost");
        } else {
            System.out.println("FAIL: Test Case 7: status is OPEN after openPost");
        }
    }
}