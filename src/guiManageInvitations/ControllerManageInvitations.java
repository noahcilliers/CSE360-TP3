
package guiManageInvitations;

import database.Database;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import applicationMain.FoundationsMain;


import java.util.ArrayList;

/**
 * title: ControllerManageInvitations
 * 
 * Description:
 * This class controls the manage invitations window.
 * It builds the UI, tableview + rows and connects to
 * the database. Options to delete invitations.
 * 
 * Responsible for:
 * displaying invitations codes
 * refreshing the invitations
 * delete a selected invitation
 * close window when exit button is pressed
 */
public class ControllerManageInvitations
{
	private Stage stage;
	private BorderPane root;
	private TableView<InvitationRow> table;
	
	private final Database db;
	
	/*
	 * Constructor: builds the UI and loads initial invites
	 */
	public ControllerManageInvitations(Stage stage)
	{
		// setup stage, root, and table. also connect to the database
		this.stage =stage;
		this.root = new BorderPane();
		this.table = new TableView<>();
		this.db = FoundationsMain.database;
		
		setupTable();
		setupButtons();
		loadInvitations();
	}
	
	// returns the root node so the view can place it into the scene
	public Parent getRoot()
	{
		return root;
	}
	
	/*
	 * create and configures the Tableview columns
	 * Each column maps to property in InvitationRow(code, email, role)
	 */
	private void setupTable()
	{
		//column for codes
		TableColumn<InvitationRow, String> codeCol = new TableColumn<>("Code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		codeCol.setPrefWidth(220);
		
		//column for email
		TableColumn<InvitationRow, String> emailCol = new TableColumn<>("Email");
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		emailCol.setPrefWidth(260);
		
		//column for role
		TableColumn<InvitationRow, String> roleCol = new TableColumn<>("Role");
		roleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
		roleCol.setPrefWidth(140);
		
		//add column to the table and center table
		table.getColumns().add(codeCol);
		table.getColumns().add(emailCol);
		table.getColumns().add(roleCol);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
		root.setCenter(table);
		
		//only allow one selection at a time
		table.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	}
	
	/*
	 * Creates and configures the refresh, delete, and close button
	 * also assigns event handler for each button
	 */
	private void setupButtons()
	{
		Button refreshButton = new Button("Refreash");
		Button deleteButton = new Button("Delete Selected");
		Button closeButton = new Button("Close");
		
		// reload invitations when refresh button is pressed
		refreshButton.setOnAction((_) -> loadInvitations());
		
		//delete selected invitation
		deleteButton.setOnAction((_) -> {
			InvitationRow selected = table.getSelectionModel().getSelectedItem();
			
			//if no invitation is selected throw error
			if (selected == null)
			{
				Alert a = new Alert(Alert.AlertType.INFORMATION);
				a.setHeaderText(null);
				a.setContentText("Please select an invitation to delete.");
				a.showAndWait();
				return;
			}
			
			//confirm deletion
			Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
			confirm.setHeaderText("Revoke Invitation");
			confirm.setContentText(
					"Delete invitation code:\n" + selected.getCode() +
					"\n\nEmail: " + selected.getEmail()
					);
			
			//if user cancels, do not delete
			if (confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) return;
			
			//attempt delete
			boolean ok = db.removeInvitationByCode(selected.getCode());
			if(!ok)
			{
				Alert a = new Alert(Alert.AlertType.ERROR);
				a.setHeaderText("Delete failed");
				a.setContentText("Could not delete invitation. Check console for details.");
				a.showAndWait();
				return;
			}
			
			//reload the updated list after deletion and clear selection
			loadInvitations();
			table.getSelectionModel().clearSelection();
		});
		
		
		closeButton.setOnAction((_) -> stage.close());
		
		
		HBox buttonBox = new HBox(10, refreshButton, deleteButton, closeButton);
		buttonBox.setPadding(new Insets(10));
		root.setBottom(buttonBox);
	}
	
	/*
	 * load invitations data from the database
	 * each database row is converted into a InvitationRow object
	 */
	private void loadInvitations()
	{
		//clear rows before reloading
		table.getItems().clear();
		
		if (db == null)
		{
			Alert a = new Alert(Alert.AlertType.ERROR);
			a.setHeaderText("Database not available");
			a.setContentText("The database reference is null. Make sure Foundationsmain.database is created before opening this page.");
			a.showAndWait();
			return;
		}
		
		//query database for all invitations
		ArrayList<String[]> invites = db.getAllInvitations();
		
		//convert each inviation into a invitationrow
		for (String[] row : invites)
		{
			if (row != null && row.length >= 3)
			{
				table.getItems().add(new InvitationRow(row[0], row[1], row [2]));
			}
			
		}
	}
}



















