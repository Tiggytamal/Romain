package control.view;

import static control.view.MainScreen.param;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlXML;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.StageStyle;
import model.ParametreXML.TypeParam;
import utilities.FunctionalException;
import utilities.Statics;
import utilities.enums.Severity;

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
	private VBox chargementPane;
	@FXML
	private VBox optionsPane;
	@FXML
	private HBox versionsPane;
	@FXML
	private ListView<String> listeVersions;
	@FXML
	private Button supprimer;
	@FXML
	private TextField newVersionField;
	@FXML
	private Button ajouter;
	@FXML
	private TextField pathField;
	@FXML
	private TextField suiviField;
	@FXML
	private TextField datastageField;
	@FXML
	private TextField filtreField;

	// Attributs de classe
	private FileChooser fc;
	private Alert alert;
	private String versionsParam;
	private static final int ROW_HEIGHT = 24;

	/*---------- CONSTRUCTEURS ----------*/

	@FXML
	public void initialize()
	{
		// Ajout listener changement de fenêtre d'options
		options.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> switchPanel(ov));
		rightSide.getChildren().clear();
		
		// Initialisation aletre
		alert = new Alert(AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setContentText("Chargement");
		alert.setHeaderText(null);
		
		// Initialition liste des versions affichée
		versionsParam = param.getMapParams().get(TypeParam.VERSIONS);
		if (versionsParam == null)
			versionsParam = "";
		if (!versionsParam.isEmpty())
			listeVersions.getItems().addAll(versionsParam.split("-"));
		
		// Mise à jour automatique de la liste des versions
		listeVersions.getSelectionModel().selectFirst();
		listeVersions.setPrefHeight((double) listeVersions.getItems().size() * ROW_HEIGHT + 2);
		listeVersions.getItems().addListener((ListChangeListener.Change<? extends String> c) ->listeVersions.setPrefHeight((double) listeVersions.getItems().size() * ROW_HEIGHT + 2));
		
		// Intialisation des TextField depuis le fichier de paramètre
		String path = param.getMapParams().get(TypeParam.ABSOLUTEPATH);
		if (path != null && !path.isEmpty())
			pathField.setText(path);
		String suivi = param.getMapParams().get(TypeParam.NOMFICHIER);
		if (suivi != null && !suivi.isEmpty())
			suiviField.setText(suivi);
		String datastage = param.getMapParams().get(TypeParam.NOMFICHIERDATASTAGE);
		if (datastage != null && !datastage.isEmpty())
			datastageField.setText(datastage);
		String filtre = param.getMapParams().get(TypeParam.FILTREDATASTAGE);
		if (filtre != null && !filtre.isEmpty())
			filtreField.setText(filtre);		
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
			alert.show();
			ControlXML control = new ControlXML();
			control.recupLotsPicDepuisExcel(file);
			alert.setContentText("Chargement lots Pic effectué");
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
			alert.show();
			ControlXML control = new ControlXML();
			control.recupListeAppsDepuisExcel(file);
			alert.setContentText("Chargement Applications effectué");
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
			alert.setContentText("Chargement Clarity effectué");
		}
	}

	/**
	 * Supprime la version selectionnée de la liste
	 * 
	 * @throws JAXBException
	 */
	public void suppVersion() throws JAXBException
	{
		// Suppression de la version de la liste affichée
		int index = listeVersions.getSelectionModel().getSelectedIndex();
		ObservableList<String> liste = listeVersions.getItems();
		if (index != -1)
		{
			liste.remove(index);
			if (!liste.isEmpty())
				listeVersions.getSelectionModel().select(index - 1);
		}

		// Suppresion de la version du fichier paramètre
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < liste.size(); i++)
		{
			builder.append(liste.get(i));
			if (i < liste.size() - 1)
				builder.append("-");
		}
		param.getMapParams().put(TypeParam.VERSIONS, builder.toString());
		new ControlXML().saveParam();
	}

	/**
	 * Ajoute une nouvelle version à la liste et au fichier de paramètre
	 * 
	 * @throws JAXBException
	 */
	public void ajouterVersion() throws JAXBException
	{
		String version = newVersionField.getText();
		// On contrôle la bonne structure du nom de la version et on ne crée pas de doublon
		if (version.matches("^E[0-9][0-9]") && !versionsParam.contains(version))
		{
			versionsParam += "-" + version;
			param.getMapParams().put(TypeParam.VERSIONS, versionsParam);
			listeVersions.getItems().add(version);
		}
		else
		{
			throw new FunctionalException(Severity.SEVERITY_ERROR, "LA version doit être de la forme ^E[0-9][0-9]");
		}
		new ControlXML().saveParam();
	}

	/*---------- METHODES PRIVEES ----------*/

	private void switchPanel(ObservableValue<? extends String> ov)
	{
		if (ov.getValue().equals("Chargement fichiers"))
		{
			rightSide.getChildren().clear();
			rightSide.getChildren().add(chargementPane);
		}
		if (ov.getValue().equals("Paramètres"))
		{
			rightSide.getChildren().clear();
			rightSide.getChildren().add(optionsPane);
		}
	}

	/*---------- ACCESSEURS ----------*/
}