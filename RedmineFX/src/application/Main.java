package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.FileChooserTest;

public class Main extends Application
{

    public static void main(String[] args)
    {
        Application.launch(args);
        
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FileChooserTest test = new FileChooserTest();
        test.start(primaryStage);
        
    }

}
