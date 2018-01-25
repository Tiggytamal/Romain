package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import view.MainScreen;

public class Main extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);       
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        MainScreen main = new MainScreen();
        main.start(stage);
        Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {//TODO
        	});
    }
}
