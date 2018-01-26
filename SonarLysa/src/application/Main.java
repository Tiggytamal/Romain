package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.MainScreen;

public class Main extends Application
{
	public static void main(final String[] args)
	{
		Application.launch(args);
	}

	@Override
	public void start(final Stage stage) throws Exception
	{
		Thread.currentThread().setUncaughtExceptionHandler((t, e) -> {
			//TODO
		});
		MainScreen main = new MainScreen();
		main.start(stage);
	}
}
