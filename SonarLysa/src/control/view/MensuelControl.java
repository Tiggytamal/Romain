package control.view;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import utilities.DateConvert;
import utilities.Statics;

public class MensuelControl
{
    /* ---------- ATTIBUTS ---------- */
    
    
    private ControlHandler handler;

    
    @FXML
    public void initialize()
    {
        listeMois.getItems().addAll(Statics.JANVIER, Statics.FEVRIER, Statics.MARS, Statics.AVRIL, Statics.MAI, Statics.JUIN, Statics.JUILLET, Statics.AOUT, Statics.SEPTEMBRE, Statics.OCTOBRE, Statics.NOVEMBRE, Statics.DECEMBRE);
        listeMois.getSelectionModel().selectFirst();
        handler = new ControlHandler("ETP8137", "28H02m89,;:!");
    }
    
    @FXML
    private ComboBox<String> listeMois;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;
    @FXML
    private Button charger;
    @FXML
    private GridPane pane;
    
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
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
        File file = fileChooser.showOpenDialog(pane.getScene().getWindow());
        if (file != null)
        {
            handler.creerVueMensuelle(file);
        }
        
    }
}
