package control.view;

import java.awt.AWTException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import view.TrayIconView;

public class MainScreen extends Application
{
	/*---------- ATTRIBUTS ----------*/

	/* Attibuts généraux */

	private static BorderPane root = new BorderPane();

	/* Attributs FXML */

	/*---------- CONSTRUCTEURS ----------*/

	/*---------- METHODES PUBLIQUES ----------*/

	@Override
	public void start(final Stage stage) throws Exception
	{
		Platform.setImplicitExit(false);
		// Menu de l'application
		final MenuBar menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
		// Ajout au panneau principal
		root.setTop(menu);

		// Affichage de l'interface
		final Scene scene = new Scene(root, 640, 480);
		TrayIconView trayIcon = new TrayIconView(stage);
		stage.setTitle("Sonar Lysa");
		stage.setResizable(true);
		stage.setScene(scene);
		stage.show();
		stage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
			{
				if (newValue)
				{
					try
					{
						trayIcon.addToTray();
					} catch (AWTException e)
					{
						e.printStackTrace();
					}
				}

			}
		});
	}

	/*---------- METHODES PRIVEES ----------*/

	/*---------- ACCESSEURS ----------*/

	/**
	 * Accès au panneau principal depuis les autres contrôleurs.
	 * 
	 * @return
	 * 
	 */
	public static BorderPane getRoot()
	{
		return root;
	}
}