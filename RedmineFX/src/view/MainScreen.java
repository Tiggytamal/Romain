package view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import control.CommandeControl;
import control.IncidentControl;
import control.ListControl;
import control.XMLControl;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Incident;
import model.enums.Champ;
import model.system.ApplicationBDC;
import model.xml.ApplicationBDCXML;
import utilities.DateConvert;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class MainScreen extends Application
{
    /* ---------- ATTIBUTES ---------- */

    /* Propriétés générales */

    private Parent root;
    private FileChooser fileChooser;
    private Stage stage;
    private Scene scene;

    /** Liste des incidents */
    private List<Incident> listIncidents;

    /* Controleurs */

    private ListControl listControl;
    private IncidentControl incidentControl;
    private XMLControl xmlControl;
    private CommandeControl commandeControl;

    /* Propriétés FXML */

    @FXML
    private ComboBox<String> listPoles;
    @FXML
    private TableView<ApplicationBDC> listApplisBDC;
    @FXML
    private TableColumn<ApplicationBDC, String> nomCol;
    @FXML
    private TableColumn<ApplicationBDC, String> bdcCol;
    @FXML
    private TableColumn<ApplicationBDC, String> tauxCol;
    @FXML
    private Button save;
    @FXML
    private DatePicker dateDebut;
    @FXML
    private DatePicker dateFin;

    /* ---------- CONSTUCTORS ---------- */

    public MainScreen()
    {
        listControl = ListControl.getInstance();
        incidentControl = new IncidentControl();
        listIncidents = new ArrayList<>();
        xmlControl = XMLControl.getInstance();
        commandeControl = new CommandeControl();
    }

    /* ---------- METHODS ---------- */

    @Override
    public void start(final Stage stage) throws IOException
    {
        root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        scene = new Scene(root);
        this.stage = stage;
        stage.setTitle("Avancée Bon De Commande");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void initialize()
    {
        listPoles.getItems().addAll(listControl.getListNomsProjets());
        listPoles.getSelectionModel().selectFirst();
        listPoles.getSelectionModel().getSelectedItem();
        bdcCol.setCellFactory(TextFieldTableCell.forTableColumn());
        bdcCol.setOnEditCommit(new EventHandler<CellEditEvent<ApplicationBDC, String>>() {
            @Override
            public void handle(CellEditEvent<ApplicationBDC, String> t)
            {
                ((ApplicationBDC) t.getTableView().getItems().get(t.getTablePosition().getRow())).setBdc(Integer.parseInt(t.getNewValue()));
            }
        });
        tauxCol.setCellFactory(TextFieldTableCell.forTableColumn());
        tauxCol.setOnEditCommit(new EventHandler<CellEditEvent<ApplicationBDC, String>>() {
            @Override
            public void handle(CellEditEvent<ApplicationBDC, String> t)
            {
                ((ApplicationBDC) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTaux(Float.parseFloat(t.getNewValue()));
            }
        });
        listApplisBDC.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    @FXML
    public void charger() throws Exception
    {
            String list = listPoles.getSelectionModel().getSelectedItem();
            if (!list.equals("Choisissez un Pôle"))
            {
                listIncidents = incidentControl.chargerIncidents(list);
                listControl.setListIncidents(listIncidents);
            }
            else
                throw new FunctionalException(Severity.SEVERITY_ERROR, "", null);
        recuperationApplications();
    }

    /**
     * Permet de récupérer la liste des applications.
     * Va chercher dans le fichier de paramètre les données déjà enregistrées.
     * Met à jour le fichier de paramètre avec les nouvelles données.
     */
    private void recuperationApplications()
    {
        // Variables
        List<String> listAppliAjoutees = new ArrayList<>();
        HashMap<String, ApplicationBDCXML> mapXML = new HashMap<>();
        List<ApplicationBDCXML> listXML = listControl.getParam().getListAppsXML();
        listApplisBDC.getItems().clear();

        for (ApplicationBDCXML appli : listXML)
        {
            mapXML.put(appli.getNom(), appli);
        }

        // Itération sur les incidents en mémoire
        for (Incident incident : listIncidents)
        {
            // Récupération du champ correspondant à l'application de l'incident
            String appli = incident.getMapValeurs().get(Champ.APPLICATION);

            // Si l'on a pas déjà trouvé cette application
            if (appli != null && !listAppliAjoutees.contains(appli))
            {
                listAppliAjoutees.add(appli); // Ajout à la liste des applications déjà trouvées
                ApplicationBDC appliBDC;

                if (mapXML.keySet().contains(appli))
                {
                    // Traitement si l'application éxiste dans le fichier de paramètre
                    appliBDC = new ApplicationBDC(mapXML.get(appli));
                    listApplisBDC.getItems().add(appliBDC);
                }
                else
                {
                    // Traitement si l'application n'est pas dans le fichier de paramètre
                    appliBDC = new ApplicationBDC(appli);
                    listApplisBDC.getItems().add(appliBDC);
                    listXML.add(new ApplicationBDCXML(appliBDC.getNom(), Integer.parseInt(appliBDC.getBdc()), Float.parseFloat(appliBDC.getTaux())));
                }
            }
        }
        xmlControl.saveParametre();
    }

    @FXML
    public void saveExcel()
    {
        // 1. Récupération des données de la TableView pour les sauvegarder dans le fichiers paramètre
        List<ApplicationBDCXML> listXML = listControl.getParam().getListAppsXML();
        listXML.clear();
        for (ApplicationBDC appliBDC : listApplisBDC.getItems())
        {
            listXML.add(new ApplicationBDCXML(appliBDC.getNom(), Integer.parseInt(appliBDC.getBdc()), Float.parseFloat(appliBDC.getTaux())));
        }
        xmlControl.saveParametre();
        commandeControl.setApplisSelect(listApplisBDC.getSelectionModel().getSelectedItems());
        // 2. Création du fichier Excel.
        fileChooser = new FileChooser();
        fileChooser.setTitle("Fichier Excel");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers Excel (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(stage);
        if (file != null)
            commandeControl.calcul(file);
    }

    @FXML
    public void saveDebut()
    {
        commandeControl.setDateDebut(DateConvert.createDate(dateDebut.getValue()));
    }

    @FXML
    public void saveFin()
    {
        commandeControl.setDateFin(DateConvert.createDate(dateFin.getValue()));
    }
    
    @SuppressWarnings ("unused")
    private void createAlert(Severity severity)
    {
        Alert alert;
        
        switch (severity)
        {
            case SEVERITY_ERROR :
                alert = new Alert(AlertType.ERROR);
                break;
            case SEVERITY_INFO :
                alert = new Alert(AlertType.INFORMATION);
                break;
            default :
                break;
            
        }
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText("Could not find file blabla.txt!");

        Exception ex = new FileNotFoundException("Could not find file blabla.txt");

        // Create expandable Exception.
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }
}