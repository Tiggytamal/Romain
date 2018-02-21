package control.view;

import java.awt.AWTException;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import control.ControlXML;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import model.xml.ParametreXML;
import utilities.TechnicalException;
import view.TrayIconView;

public class MainScreen extends Application
{
	/*---------- ATTRIBUTS ----------*/

	/* Attibuts généraux */

	private static BorderPane root = new BorderPane();
	private static ParametreXML param = new ParametreXML();
	private TrayIconView trayIcon;

	/* Attributs FXML */

	/*---------- CONSTRUCTEURS ----------*/

	/*---------- METHODES PUBLIQUES ----------*/

	@Override
	public void start(final Stage stage) throws IOException, InterruptedException, JAXBException
	{
		Platform.setImplicitExit(false);
		
		// Initialisation des paramètres de l'application
	    new ControlXML().recuprerParamXML();
	    
		// Menu de l'application
		final MenuBar menu = FXMLLoader.load(getClass().getResource("/view/Menu.fxml"));
		
		// Ajout au panneau principal
		root.setTop(menu);

		// Affichage de l'interface
		final Scene scene = new Scene(root, 640, 480);
		trayIcon = new TrayIconView(stage);
		stage.setTitle("Sonar Lysa");
		stage.setResizable(true);
		stage.setScene(scene);
	    stage.iconifiedProperty().addListener(new IconifiedListener());
		stage.show();
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
	
	/**
	 * Accèes au fichier de paramètre de l'application
	 * 
	 * @return
	 */
	public static ParametreXML getParam()
	{
	    return param;
	}
	
	private class IconifiedListener implements ChangeListener<Boolean>
	{
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
        {
            if (newValue)
            {
                Platform.setImplicitExit(false);
                try
                {
                   trayIcon.addToTray();
                } catch (AWTException e)
                {
                    throw new TechnicalException("IconTray inaccessible", e.getCause());
                }
            }           
        }
	}
}