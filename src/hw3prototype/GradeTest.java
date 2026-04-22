package hw3prototype;

/**
 * Test class for the Grade prototype.
 * This class uses simple output checks to demonstrate
 * a test-driven development approach for the grading feature.
 */
public class GradeTest {

    /**
     * Runs prototype test cases for the grading system.
     *
     * @param args command-line arguments
     */
    public static void main(String[] args) {

        System.out.println("Running Grade tests...");
        System.out.println();

        testCase("Valid staff grade 95", Grade.assignGrade("staff", 95), "A");
        testCase("Valid staff grade 80", Grade.assignGrade("staff", 80), "B");
        testCase("Valid lower boundary 0", Grade.assignGrade("staff", 0), "F");
        testCase("Valid upper boundary 100", Grade.assignGrade("staff", 100), "A");
        testCase("Invalid low grade -1", Grade.assignGrade("staff", -1), "Invalid grade");
        testCase("Invalid high grade 101", Grade.assignGrade("staff", 101), "Invalid grade");
        testCase("Unauthorized student", Grade.assignGrade("student", 95), "Unauthorized");
    }

    /**
     * Compares actual and expected results for one test case.
     *
     * @param testName the name of the test
     * @param actual the actual result
     * @param expected the expected result
     */
    public static void testCase(String testName, String actual, String expected) {
        System.out.print(testName + ": ");

        if (actual.equals(expected)) {
            System.out.println("PASS");
        } else {
            System.out.println("FAIL");
            System.out.println("  Expected: " + expected);
            System.out.println("  Actual:   " + actual);
        }
    }
}