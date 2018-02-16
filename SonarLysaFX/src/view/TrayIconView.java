package view;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.TrayIcon.MessageType;

import javafx.application.Platform;
import javafx.stage.Stage;
import utilities.Statics;

public class TrayIconView
{
	
	/*---------- ATTRIBUTS ----------*/

	private SystemTray tray;
	private TrayIcon trayIcon;
	private Stage stage;
	
	/*---------- CONSTRUCTEURS ----------*/

	public TrayIconView(Stage stage)
	{
		// Dépendance avec l'application
		this.stage = stage;
		
		// Récupération de la barre de notifications depuis le système
		tray = SystemTray.getSystemTray();

		// Image utilisée pour l'icône
		Image image = Toolkit.getDefaultToolkit().createImage("d:\\SONAR16.jpg");

		// Initialisation de l'icone
		trayIcon = new TrayIcon(image, Statics.NOMAPPLI);
		trayIcon.setImageAutoSize(true);
		trayIcon.setToolTip("Tooltip");
		trayIcon.displayMessage(Statics.NOMAPPLI, "demo", MessageType.INFO);
		
		// Création du menu
		PopupMenu menu = new PopupMenu(Statics.NOMAPPLI);
		MenuItem item  = new MenuItem("fermer");
		item.addActionListener(e -> System.exit(0));
		MenuItem item2  = new MenuItem("ouvrir");
		item2.addActionListener(e -> removeFromTray());
		menu.add(item);
		menu.add(item2);
		trayIcon.setPopupMenu(menu);
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
	
	/**
	 * Ajoute l'icone à la barre de notifications et ferme la fenetre de l'application
	 * @throws AWTException
	 */
	public void addToTray() throws AWTException
	{
		stage.hide();
		tray.add(trayIcon);
	}
	
	/**
	 * Retire l'icoen de la barre de notifications et affiche la fenêtre de l'application
	 */
	public void removeFromTray()
	{
		tray.remove(trayIcon);
		Platform.runLater(stage::show);
		Platform.runLater(stage::toFront);
	}
	
	/*---------- METHODES PRIVEES ----------*/
	/*---------- ACCESSEURS ----------*/
}