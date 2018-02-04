package control.view;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import control.ControlAPI;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import utilities.DateConvert;

public class MensuelViewControl
{
    /* ---------- ATTIBUTS ---------- */
    
    
    private ControlAPI handler;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    
    @FXML
    public void initialize()
    {
        handler = new ControlAPI("ETP8137", "28H02m89,;:!");
        selectPane.getChildren().clear();
        backgroundPane.getChildren().remove(creer);       
    }
    
    /* Attributs FXML */
    
    @FXML
    private HBox listeMoisHBox;
    @FXML
    private DatePicker dateDebutPicker;
    @FXML
    private HBox dateDebutHBox;
    @FXML
    private DatePicker dateFinPicker;
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
    private VBox selectPane;


    
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    public void saveDebut()
    {
        dateDebut = dateDebutPicker.getValue();
    }

    @FXML
    public void saveFin()
    {
        dateFin = dateFinPicker.getValue();
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
        selectPane.getChildren().add(charger);
    }
    
    @FXML
    public void afficherParDate()
    {
        selectPane.getChildren().clear();
        selectPane.getChildren().add(dateDebutHBox);
        selectPane.getChildren().add(dateFinHBox);
        selectPane.getChildren().add(creer);

    }
    
    @FXML
    public void creerVue()
    {
        System.out.println(dateDebut);
        System.out.println(dateFin);
    }
}
