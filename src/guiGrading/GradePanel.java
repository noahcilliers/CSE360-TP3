package guiGrading;

import entityClasses.Post;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;


/*******
 * <p> Title: Grade Class. </p>
 * 
 * <p> Description: Reusable grading panel for staff and admin users.
 * 
 * @author Octavio Terrazas
 ********/

public class GradePanel extends VBox {

    private final Label titleLabel;
    private final Label instructionLabel;
    private final TextField gradeField;
    private final Button assignButton;
    private final Label resultLabel;

    private Post currentPost;
    private final String currentUserRole;

    public GradePanel(String currentUserRole) {
        this.currentUserRole = currentUserRole;

        titleLabel = new Label("Grade Student Post");
        instructionLabel = new Label("Enter numeric grade (0-100):");
        gradeField = new TextField();
        gradeField.setPromptText("Enter grade here");
        assignButton = new Button("Assign Grade");
        resultLabel = new Label("No grade assigned yet.");

        setSpacing(10);
        setPadding(new Insets(15));
        getChildren().addAll(titleLabel, instructionLabel, gradeField, assignButton, resultLabel);

        assignButton.setOnAction(e -> assignGradeToCurrentPost());
    }

    public void setCurrentPost(Post post) {
        this.currentPost = post;

        if (post == null) {
            resultLabel.setText("No post selected.");
            gradeField.clear();
            return;
        }

        if (post.getLetterGrade() == null || post.getLetterGrade().isEmpty()) {
            resultLabel.setText("Current Grade: Not Assigned");
            gradeField.clear();
        } else {
            resultLabel.setText("Current Grade: " + post.getNumericGrade()
                    + " | Letter Grade: " + post.getLetterGrade());
            gradeField.setText(String.valueOf(post.getNumericGrade()));
        }
    }

    private void assignGradeToCurrentPost() {
        if (currentPost == null) {
            resultLabel.setText("Error: Select a post first.");
            return;
        }

        if (!currentUserRole.equalsIgnoreCase("staff") && !currentUserRole.equalsIgnoreCase("admin")) {
            resultLabel.setText("Error: Only staff/admin can assign grades.");
            return;
        }

        try {
            double numericGrade = Double.parseDouble(gradeField.getText().trim());

            if (numericGrade < 0 || numericGrade > 100) {
                resultLabel.setText("Error: Grade must be between 0 and 100.");
                return;
            }

            String letterGrade = Grade.getLetterGrade(numericGrade);

            currentPost.setNumericGrade(numericGrade);
            currentPost.setLetterGrade(letterGrade);

            resultLabel.setText("Saved Grade: " + numericGrade + " | Letter Grade: " + letterGrade);

        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Please enter a valid number.");
        }
    }
}