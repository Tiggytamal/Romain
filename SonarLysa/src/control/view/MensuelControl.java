package control.view;

import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
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
        selectPane.getChildren().clear();
        backgroundPane.getChildren().remove(creer);
        
    }
    
    /* Attributs FXML */
    
    @FXML
    private ComboBox<String> listeMois;
    @FXML
    private HBox listeMoisHBox;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private HBox dateDebutHBox;
    @FXML
    private DatePicker dateFin;
    @FXML
    private HBox dateFinHBox;
    @FXML
    private Button charger;
    @FXML
    private Button creer;
    @FXML
    private GridPane backgroundPane;
    @FXML
    private RadioButton radioMois;
    @FXML
    private RadioButton radioDate;
    @FXML
    private RadioButton radioActuel;
    @FXML
    private ToggleGroup toggleGroup;
    @FXML
    private GridPane selectPane;


    
    
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
        File file = fileChooser.showOpenDialog(backgroundPane.getScene().getWindow());
        if (file != null)
        {
            handler.creerVueMensuelle(file);
        }      
    }
    
    @FXML
    public void afficherParMois()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(listeMois);
        backgroundPane.getChildren().add(creer);
    }
    
    @FXML
    public void afficherParDate()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(dateDebutHBox);
        selectPane.getChildren().add(dateFinHBox);
        backgroundPane.getChildren().add(creer);

    }
    
    @FXML
    public void afficherParMoisEnCours()
    {
        selectPane.getChildren().clear();
        backgroundPane.getChildren().add(creer);
    }
    
    @FXML
    public void creerVue()
    {

    }
}
