package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.MainScreen;

public class Main extends Application
{
    @Override
    public void start(Stage stage)
    {
        MainScreen main = new MainScreen();
//        try
//        {
            try
            {
                main.start(stage);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }        
//        }
//        catch (Exception e)
//        {
//            MainScreen.createAlert(Severity.SEVERITY_ERROR, e, "Une exception est arrivée");
//        }
    }

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
