package hw3prototype;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * GUI for assigning grades.
 */
public class GradeGUI extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Labels
        Label titleLabel = new Label("Staff Grade Assignment");
        Label roleLabel = new Label("Role: staff"); // shows current role
        Label instructionLabel = new Label("Enter numeric grade (0-100):");

        // Input field
        TextField gradeField = new TextField();
        gradeField.setPromptText("Enter grade here");

        // Button
        Button assignButton = new Button("Assign Grade");

        // Output label
        Label resultLabel = new Label("Result will appear here.");

        // Button action
        assignButton.setOnAction(e -> {
            String currentUserRole = "staff"; // simulate logged-in user

            try {
                int numericGrade = Integer.parseInt(gradeField.getText().trim());

                String result = Grade.assignGrade(currentUserRole, numericGrade);

                // Handle results
                if (result.equals("Unauthorized")) {
                    resultLabel.setText("Error: Only staff can assign grades.");
                } else if (result.equals("Invalid grade")) {
                    resultLabel.setText("Error: Grade must be between 0 and 100.");
                } else {
                    resultLabel.setText("Numeric Grade: " + numericGrade + " | Letter Grade: " + result);
                }

            } catch (NumberFormatException ex) {
                resultLabel.setText("Error: Please enter a valid integer.");
            }
        });

        // Layout
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));
        root.getChildren().addAll(
                titleLabel,
                roleLabel,
                instructionLabel,
                gradeField,
                assignButton,
                resultLabel
        );

        Scene scene = new Scene(root, 400, 230);

        primaryStage.setTitle("Grade Assignment");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}