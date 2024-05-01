package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import main.view.HomePage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import database.Database;


public class Main extends Application {
	private Database database = Database.getInstance();
	//static -> milik class
	public static final int WIDTH = 1000;
	public static final int HEIGHT = 800;
	
	@Override
	public void start(Stage stage) throws Exception {
		new HomePage(stage);
		stage.setTitle("PT Pudding");
		stage.show();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	
	
}
