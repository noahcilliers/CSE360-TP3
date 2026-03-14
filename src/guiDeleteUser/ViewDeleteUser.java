package guiDeleteUser;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewDeleteUser Class. </p>
 * 
 * <p> Description: The ViewDeleteUser page is used to delete a user when specified
 * with input validation to prevent a user with the admin role being deleted. </p>
 * 
 * 
 * @author Roberto Zozaya
 * 
 *  
 */



public class ViewDeleteUser {

	/*****
	 * <p> Method: displayDeleteUser</p>
	 * 
	 * <p> Description: This constructor is used to establish user entity objects. </p>
	 * 
	 * @param parentStage stage for delete user
	 * 
	 * 
	 */
    public static void displayDeleteUser(Stage parentStage) {

        // Create a new stage (window)
        Stage stage = new Stage();

        // Make it modal (blocks clicking the parent window)
        stage.initModality(Modality.APPLICATION_MODAL);

        // Set the owner so it stays on top of Admin Home
        stage.initOwner(parentStage);

        // Controller builds the UI
        ControllerDeleteUser controller = new ControllerDeleteUser(stage);

        // Scene uses the controller's root pane
        Scene scene = new Scene(controller.getRoot(), 600, 300);

        stage.setTitle("Delete User");
        stage.setScene(scene);

        // Show and wait until closed
        stage.showAndWait();
    }
}