package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.MainScreen;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception
    {
        MainScreen mainSreen = new MainScreen();
        mainSreen.start(stage);    
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
