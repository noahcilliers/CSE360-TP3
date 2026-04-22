package hw3prototype;

/**
 * Author: Octavio Terrazas
 * Handles grade assignment logic for student discussion posts.
 * This prototype supports the TP3 grading aspect by validating
 * user authorization and converting numeric grades to letter grades.
 */
public class Grade {

    /**
     * Assigns a letter grade if the user is authorized and the numeric
     * grade is within the valid range.
     *
     * @param role  the user's role in the system
     * @param grade the numeric grade value
     * @return the letter grade if valid, or an error message otherwise
     */
    public static String assignGrade(String role, int grade) {

        // Only staff members should be allowed to assign grades.
        // This protects the grading feature from unauthorized use.
        if (!role.equals("staff")) {
            return "Unauthorized";
        }

        // Grades must stay within the valid academic range.
        // Rejecting invalid input prevents incorrect data from being stored.
        if (grade < 0 || grade > 100) {
            return "Invalid grade";
        }

        // Convert the numeric grade into the correct letter grade.
        if (grade >= 90) {
            return "A";
        }
        if (grade >= 80) {
            return "B";
        }
        if (grade >= 70) {
            return "C";
        }
        if (grade >= 60) {
            return "D";
        }
        return "F";
    }
}