package view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScreen extends Application
{

	
	@Override
	public void start(Stage stage) throws Exception 
	{
        Parent root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        Scene scene = new Scene(root);
        stage.setTitle("Sonar Lysa");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();		
	}
	
	

}
