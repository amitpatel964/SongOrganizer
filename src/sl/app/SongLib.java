package sl.app;
import java.io.File;
import java.net.URL;

import javafx.application.Application;
import javafx.event.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

// Amit Patel and Hideyo Sakamoto

public class SongLib extends Application implements EventHandler<ActionEvent>{
	
	Button button;

	@Override
	public void start(Stage primaryStage) throws Exception {
		//xmlns="http://javafx.com/javafx" xmlns:fx="http://javafx.com/fxml"
		//xmlns="http://javafx.com/javafx/16" xmlns:fx="http://javafx.com/fxml/1"
		
		Parent root = FXMLLoader.load(getClass().getResource("/sl/view/fx.fxml"));
		
		primaryStage.setTitle("Songlib");
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
	}

	public static void main(String[] args) {
		launch(args);
	}

	
	public void handle(ActionEvent event) {
		
	}
	
}