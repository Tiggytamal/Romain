package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlHandler;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilities.DateConvert;
import utilities.Statics;

public class MainScreen extends Application
{
	/*---------- ATTRIBUTS ----------*/
	
	/* Attibuts généraux */
	
    private Stage stage;
    private ControlHandler handler;
	
	/* Attributs FXML */
	
    @FXML
    private ComboBox<String> listeMois;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private Button charger;
    
    
	/*---------- CONSTRUCTEURS ----------*/
    
    @FXML
    public void initialize()
    {
		listeMois.getItems().addAll(Statics.JANVIER, Statics.FEVRIER, Statics.MARS, Statics.AVRIL, Statics.MAI, Statics.JUIN, Statics.JUILLET, Statics.AOUT, Statics.SEPTEMBRE, Statics.OCTOBRE, Statics.NOVEMBRE, Statics.DECEMBRE);
		listeMois.getSelectionModel().selectFirst();
		handler = new ControlHandler("ETP8137", "28H02m89,;:!");
    }
    
	/*---------- METHODES PUBLIQUES ----------*/

	@Override
	public void start(final Stage stageIn) throws Exception
	{
		final Parent root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
		final Scene scene = new Scene(root);
		stage = stageIn;
		stage.setTitle("Sonar Lysa");
		stage.setResizable(true);
		stage.setScene(scene);
		stage.show();
	}
	
    @FXML
    public void saveDebut()
    {
        DateConvert.createDate(dateDebut.getValue());
    }

    @FXML
    public void saveFin()
    {
        DateConvert.createDate(dateFin.getValue());
    }
    
    @FXML
    public void chargerExcel() throws InvalidFormatException, IOException
    {
    	FileChooser fileChooser = new FileChooser();
    	fileChooser.setTitle("FichierExcel");
    	fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Fichiers Excel (*.xls)", "*.xls", "*.xlsx"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null)
        {
        	handler.creerVueMensuelle(file);
        }
    	
    }

	/*---------- METHODES PRIVEES ----------*/

	/*---------- ACCESSEURS ----------*/

}
