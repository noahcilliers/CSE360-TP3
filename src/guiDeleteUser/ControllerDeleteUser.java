package guiDeleteUser;

import database.Database;
import inputValidation.UserNameRecognizer;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;


/*******
 * <p> Title: ControllerDeleteUser Class. </p>
 * 
 * <p> Description: This class provides the controller actions for the Delete User Page.
 * It contains the logic required to validate user input, confirm deletion requests,
 * and invoke the appropriate database operations to remove a user account.</p>
 * 
 * 
 * @author Roberto Zozaya
 *  
 */

public class ControllerDeleteUser {

    private static Database theDatabase = applicationMain.FoundationsMain.database;
    private Stage theStage;
    private Pane root = new Pane();

    // UI controls
    private Label labelTitle = new Label("Delete User");
    private Label labelInstr = new Label("Enter username to delete (non-admin only):");
    private TextField textUserName = new TextField();

    private Button buttonDelete = new Button("Delete");
    private Button buttonClose = new Button("Close");

    public ControllerDeleteUser(Stage stage) {
        this.theStage = stage;

        setupUI();
        setupActions();
    }

    public Parent getRoot() {
        return root;
    }

    private void setupUI() {
        // Title
        labelTitle.setFont(Font.font("Arial", 24));
        labelTitle.setMinWidth(600);
        labelTitle.setAlignment(Pos.CENTER);
        labelTitle.setLayoutX(0);
        labelTitle.setLayoutY(20);

        // Instructions
        labelInstr.setFont(Font.font("Arial", 14));
        labelInstr.setLayoutX(60);
        labelInstr.setLayoutY(80);

        // Text field
        textUserName.setFont(Font.font("Arial", 14));
        textUserName.setLayoutX(60);
        textUserName.setLayoutY(110);
        textUserName.setMinWidth(300);
        textUserName.setPromptText("Username");

        // Buttons
        buttonDelete.setFont(Font.font("Dialog", 14));
        buttonDelete.setLayoutX(380);
        buttonDelete.setLayoutY(110);

        buttonClose.setFont(Font.font("Dialog", 14));
        buttonClose.setLayoutX(60);
        buttonClose.setLayoutY(170);

        root.getChildren().addAll(labelTitle, labelInstr, textUserName, buttonDelete, buttonClose);
    }

    private void setupActions() {

        buttonClose.setOnAction((_) -> {
            theStage.close();
        });

        buttonDelete.setOnAction((_) -> {
        	
            String userName = textUserName.getText().trim();
            	
            // check if username is valid
            String verifiedUsername = UserNameRecognizer.checkForValidUserName(userName);
            
            if(verifiedUsername.compareTo("") != 0) {
            	Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Delete User");
                a.setContentText(verifiedUsername);
                a.showAndWait();
                return;
            };
            	/*
            // check empty entry
            if (userName.length() == 0)
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Delete User");
                a.setContentText("Please enter a username.");
                a.showAndWait();
                return;
            }
            	*/

            // 
            if (!theDatabase.doesUserExist(userName)) 
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Delete User");
                a.setContentText("User does not exist.");
                a.showAndWait();
                return;
            }

            // 4) Prevent deleting admins
            if (theDatabase.isUserAdmin(userName))
            {
                Alert a = new Alert(Alert.AlertType.INFORMATION);
                a.setHeaderText("Delete User");
                a.setContentText("You cannot delete an admin user.");
                a.showAndWait();
                return;
            }

            // 5) Confirm deletion
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm Delete");
            confirm.setHeaderText("Delete user: " + userName + "?");
            confirm.setContentText("This action cannot be undone.");
            confirm.showAndWait();
            
            if (confirm.getResult() == javafx.scene.control.ButtonType.OK) 
            {
                theDatabase.deleteUserByUserName(userName); 

                Alert done = new Alert(Alert.AlertType.INFORMATION);
                done.setHeaderText("Delete User");
                done.setContentText("Deleted user: " + userName);
                done.showAndWait();

                textUserName.clear();
            }
        });
    }
}