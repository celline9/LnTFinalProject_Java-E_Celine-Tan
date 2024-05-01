module FinalProject {
	requires javafx.controls;
	requires javafx.base;
	requires java.sql;
	requires javafx.graphics;
	
	opens application to javafx.graphics, javafx.fxml;
	opens main.model to javafx.base;
}
