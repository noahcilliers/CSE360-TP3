package hw3prototype;

import entityClasses.Post;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Reusable grading panel for staff and admin users.
 */
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

        if (post.getNumericGrade() == -1) {
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

        try {
            int numericGrade = Integer.parseInt(gradeField.getText().trim());
            String result = Grade.assignGrade(currentUserRole, numericGrade);

            if (result.equals("Unauthorized")) {
                resultLabel.setText("Error: Only staff/admin can assign grades.");
            } else if (result.equals("Invalid grade")) {
                resultLabel.setText("Error: Grade must be between 0 and 100.");
            } else {
                currentPost.setNumericGrade(numericGrade);
                currentPost.setLetterGrade(result);

                resultLabel.setText("Saved Grade: " + numericGrade + " | Letter Grade: " + result);
            }

        } catch (NumberFormatException ex) {
            resultLabel.setText("Error: Please enter a valid integer.");
        }
    }
}