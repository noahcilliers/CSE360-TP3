
package guiManageInvitations;

import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;


/**********
 * <p> 
 * 
 * Title: ViewManageInvitations </p>
 * 
 * <p> Description:
 * This class is responsible for creating and displaying the
 * "manage invitations" window. It sets up the stage, scene, and
 * controller for the invitation management UI.
 * 
 * The window displayed as a modal dialog, meaning the user
 * must close it before returning to the admin home screen.
 * 		 </p>
 */
public class ViewManageInvitations 
{

	/**
	 * creates and displays the manage invitations window
	 * 
	 */
	public static void displayManageInvitations(Stage parentStage)
	{
		//create new stage for the manage invitations window
		Stage stage = new Stage();
		
		//make the window modal (block interaction with parent window)
		stage.initModality(Modality.APPLICATION_MODAL);
		
		//set the owner so it stays on top of the admin home window
		stage.initOwner(parentStage);
		
		//controller for ui window
		ControllerManageInvitations controller = new ControllerManageInvitations(stage);
		
		//create scene using the controllers root layout
		Scene scene = new Scene(controller.getRoot(), 600, 420);
		
		// set window title to "manage invitations" and attach scene
		stage.setTitle("Manage Invitations");
		stage.setScene(scene);
		
		//show window and wait until its closed
		stage.showAndWait();
	}

}
