package control.view;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlSonar;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utilities.Statics;

public class MaintenanceViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    private GridPane backgroundPane;
    @FXML
    private Button creer;
    
    private ControlSonar handler;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    @FXML
    public void initialize()
    {
        handler = new ControlSonar(Statics.info.getPseudo(), Statics.info.getMotDePasse());
    }
    
    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
    
    public void creerVue() throws InvalidFormatException, IOException
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("FichierExcel");
        fileChooser.getExtensionFilters().add(Statics.FILTEREXCEL);
        File file = fileChooser.showOpenDialog(backgroundPane.getScene().getWindow());
        if (file != null)
        {
            handler.creerVueCDM(file);
        }      
    }
}