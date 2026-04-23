package guiGrading;

/*******
 * <p> Title: Grade Class. </p>
 * 
 * <p> Description: Converts a numeric grade into a letter grade. 
 * This method is used by the system after validation is done elsewhere.
 * @param grade the numeric grade value
 * @return the corresponding letter grade, or "Invalid" if out of range
 * 
 * @author Octavio Terrazas
 ********/

public class Grade {

    public static String getLetterGrade(double grade) {

        // Grades must stay within the valid academic range.
        // Rejecting invalid input prevents incorrect data from being stored.
        if (grade < 0 || grade > 100) {
            return "Invalid";
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