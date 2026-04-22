package guiRole2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

/*******
 * <p> Title: ViewRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role2 Home Page. The page is a stub for some role needed for
 * the application. The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00 2025-04-20 Initial version
 */

public class ViewRole2Home {
	
	// Application window size
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	// GUI Area 1
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");

	// Separators
	protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);
	protected static Line line_Separator4 = new Line(20, 525, width - 20, 525);

	// GUI Area 3
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// New button for staff thread access
	protected static Button button_OpenThreads = new Button("Open Threads");

	// Shared references
	private static ViewRole2Home theView;
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;
	protected static Pane theRootPane;
	protected static User theUser;

	private static Scene theRole2HomeScene;
	protected static final int theRole = 3;

	public static void displayRole2Home(Stage ps, User user) {
		theStage = ps;
		theUser = user;

		if (theView == null) theView = new ViewRole2Home();

		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;

		label_UserDetails.setText("User: " + theUser.getUserName());

		theStage.setTitle("CSE 360 Foundations: Role2 Home Page");
		theStage.setScene(theRole2HomeScene);
		theStage.show();
	}

	private ViewRole2Home() {
		theRootPane = new Pane();
		theRole2HomeScene = new Scene(theRootPane, width, height);

		// GUI Area 1
		label_PageTitle.setText("Role2 Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: ");
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((_) -> {
			ControllerRole2Home.performUpdate();
		});

		// New thread button for staff
		setupButtonUI(button_OpenThreads, "Dialog", 18, 250, Pos.CENTER, 20, 350);
		button_OpenThreads.setOnAction((_) -> {
			ControllerRole2Home.openThreads();
		});

		// GUI Area 3
		setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
		button_Logout.setOnAction((_) -> {
			ControllerRole2Home.performLogout();
		});

		setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
		button_Quit.setOnAction((_) -> {
			ControllerRole2Home.performQuit();
		});

		theRootPane.getChildren().addAll(
			label_PageTitle,
			label_UserDetails,
			button_UpdateThisUser,
			line_Separator1,
			button_OpenThreads,
			line_Separator4,
			button_Logout,
			button_Quit
		);
	}

	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);
	}

	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);
	}
}