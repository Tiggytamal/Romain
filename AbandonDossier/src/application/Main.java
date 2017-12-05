package application;

import javafx.application.Application;
import javafx.stage.Stage;
import utilities.enums.Severity;
import view.MainScreen;

public class Main extends Application
{
    @Override
    public void start(Stage stage)
    {
        MainScreen mainSreen = new MainScreen();
        try
        {
            mainSreen.start(stage);    
        }
        catch (Exception e)
        {
            MainScreen.createAlert(Severity.SEVERITY_ERROR, e, "Une exception est arrivée");
        }
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
