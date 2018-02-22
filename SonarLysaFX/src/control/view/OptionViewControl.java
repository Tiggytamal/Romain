package control.view;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlXML;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import utilities.Statics;

public class OptionViewControl
{
	/*---------- ATTRIBUTS ----------*/

	// Attributs FXML
	@FXML
	private SplitPane splitPane;
	@FXML
	private ListView<String> options;
	@FXML
	private GridPane rightSide;
	@FXML
	private Button lotsPic;
	@FXML
	private Button applis;
	@FXML
	private Button clarity;
	@FXML
	private VBox chargement;

	// Attributs de classe
	private FileChooser fc;
	private static Alert alert;

	/*---------- CONSTRUCTEURS ----------*/

	@FXML
	public void initialize()
	{
		options.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> switchPanel(ov));
		alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setContentText("Chargement");
		alert.setHeaderText(null);
	}

	/*---------- METHODES PUBLIQUES ----------*/

	public void chargerLotsPic() throws InvalidFormatException, IOException, JAXBException
	{
		fc = new FileChooser();
		fc.setTitle("Charger Lots Pic");
		fc.getExtensionFilters().add(Statics.FILTEREXCEL);
		File file = fc.showOpenDialog(splitPane.getScene().getWindow());
		if (file != null)
		{
			ControlXML control = new ControlXML();
			control.recupLotsPicDepuisExcel(file);
		}
	}

	public void chargerApplis() throws InvalidFormatException, IOException, JAXBException
	{
		fc = new FileChooser();
		fc.setTitle("Charger Applications");
		fc.getExtensionFilters().add(Statics.FILTEREXCEL);
		File file = fc.showOpenDialog(splitPane.getScene().getWindow());
		if (file != null)
		{
			ControlXML control = new ControlXML();
			control.recupListeAppsDepuisExcel(file);
		}
	}

	public void chargerClarity() throws InvalidFormatException, IOException, JAXBException
	{
		fc = new FileChooser();
		fc.setTitle("Charger Referentiel Clarity");
		fc.getExtensionFilters().add(Statics.FILTEREXCEL);
		File file = fc.showOpenDialog(splitPane.getScene().getWindow());
		if (file != null)
		{
			alert.show();
			ControlXML control = new ControlXML();
			control.recupInfosClarityDepuisExcel(file);
			alert.setContentText("Chargement effectué");
		}
	}

	/*---------- METHODES PRIVEES ----------*/

	private void switchPanel(ObservableValue<? extends String> ov)
	{
		if (ov.getValue().equals("Chargement fichiers"))
		{
			rightSide.getChildren().clear();
			rightSide.getChildren().add(chargement);
		}
		if (ov.getValue().equals("Paramètres"))
		{
			rightSide.getChildren().clear();
		}

	}

	public static void test()
	{

	}

	/*---------- ACCESSEURS ----------*/
}