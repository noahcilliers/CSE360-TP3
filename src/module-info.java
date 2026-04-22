module FoundationsF25 {
	requires javafx.controls;
	requires java.sql;
	opens applicationMain to javafx.graphics, javafx.fxml;
	opens guiManageInvitations to javafx.base;
	
	exports hw3prototype;
}
