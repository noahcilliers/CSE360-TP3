package guiGrading;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


/*******
 * <p> Title: Grade Class. </p>
 * 
 * <p> Description: GUI for assigning grades.
 * 
 * @author Octavio Terrazas
 ********/

public class GradeGUI extends Application {

    @Override
    public void start(Stage primaryStage) {

        Label titleLabel = new Label("Staff Grade Assignment");
        Label roleLabel = new Label("Role: staff");
        Label instructionLabel = new Label("Enter numeric grade (0-100):");

        TextField gradeField = new TextField();
        gradeField.setPromptText("Enter grade here");

        Button assignButton = new Button("Assign Grade");

        Label resultLabel = new Label("Result will appear here.");

        assignButton.setOnAction(e -> {
            String currentUserRole = "staff";

            try {
                double numericGrade = Double.parseDouble(gradeField.getText().trim());

                if (!currentUserRole.equalsIgnoreCase("staff")) {
                    resultLabel.setText("Error: Only staff can assign grades.");
                    return;
                }

                if (numericGrade < 0 || numericGrade > 100) {
                    resultLabel.setText("Error: Grade must be between 0 and 100.");
                    return;
                }

                String letterGrade = Grade.getLetterGrade(numericGrade);

                resultLabel.setText("Numeric Grade: " + numericGrade + " | Letter Grade: " + letterGrade);

            } catch (NumberFormatException ex) {
                resultLabel.setText("Error: Please enter a valid number.");
            }
        });

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