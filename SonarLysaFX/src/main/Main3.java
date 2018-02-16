package main;

import java.awt.AWTException;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionListener;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 *
 * @author nathan
 */
public class Main3 extends Application
{
	private Parent root;
	private TrayIcon trayIcon;
	private boolean firstTime;
	//
	// static SynchroneThread st = new SynchroneThread("Synchronisation");

	@Override
	public void start(final Stage stage)
	{
		Platform.setImplicitExit(false);

		StackPane layout = new StackPane(createContent());
		layout.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);");
		layout.setPrefSize(300, 200);

		// this dummy app just hides itself when the app screen is clicked. A real app might have some interactive UI and a separate icon which hides the app window.
		layout.setOnMouseClicked(event -> stage.hide());
		Scene scene = new Scene(layout, 300, 250, Color.WHITE);
		firstTime = true;
		createTrayIcon(stage);
		stage.setScene(scene);
		stage.show();

	}

	public void createTrayIcon(final Stage stage)
	{
		if (SystemTray.isSupported())
		{
			// get the SystemTray instance
			SystemTray tray = SystemTray.getSystemTray();
			// load an image
			java.awt.Image image = Toolkit.getDefaultToolkit().createImage("d:\\SONAR16.jpg");

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent t)
				{
					hide(stage);
				}
			});

			final ActionListener closeListener = new ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					System.exit(0);
				}
			};

			ActionListener showListener = new ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Platform.runLater(new Runnable() {
						@Override
						public void run()
						{
							stage.show();
						}
					});
				}
			};

			ActionListener hideListener = new ActionListener() {
				@Override
				public void actionPerformed(java.awt.event.ActionEvent e)
				{
					Platform.runLater(new Runnable() {
						@Override
						public void run()
						{
							stage.hide();
						}
					});
				}
			};
			// create a popup menu
			PopupMenu popup = new PopupMenu();

			java.awt.MenuItem showItem = new java.awt.MenuItem("Show");
			showItem.addActionListener(showListener);
			popup.add(showItem);

			java.awt.MenuItem hideItem = new java.awt.MenuItem("Hide");
			hideItem.addActionListener(hideListener);
			popup.add(hideItem);

			java.awt.MenuItem closeItem = new java.awt.MenuItem("Close");
			closeItem.addActionListener(closeListener);
			popup.add(closeItem);
			/// ... add other items
			// construct a TrayIcon
			trayIcon = new TrayIcon(image, "GED Desktop", popup);
			// set the TrayIcon properties
			trayIcon.addActionListener(showListener);
			// add the tray image
			try
			{
				tray.add(trayIcon);
			} catch (AWTException e)
			{
				System.out.println(e);
			}
		}
	}

	public void showProgramIsMinimizedMsg()
	{
		if (firstTime)
		{
			trayIcon.displayMessage("Some message.", "Some other message.", TrayIcon.MessageType.INFO);
			firstTime = false;
		}
	}

	private void hide(final Stage stage)
	{
		Platform.runLater(new Runnable() {
			@Override
			public void run()
			{
				if (SystemTray.isSupported())
				{
					stage.hide();
					// showProgramIsMinimizedMsg();
				}
				else
				{
					System.exit(0);
				}
			}
		});
	}

	public static void main(String[] args)
	{
		launch(args);
	}
	
	private Node createContent()
	{
		Label hello = new Label("hello, world");
		hello.setStyle("-fx-font-size: 40px; -fx-text-fill: forestgreen;");
		Label instructions = new Label("(click to hide)");
		instructions.setStyle("-fx-font-size: 12px; -fx-text-fill: orange;");

		VBox content = new VBox(10, hello, instructions);
		content.setAlignment(Pos.CENTER);

		return content;
	}

}