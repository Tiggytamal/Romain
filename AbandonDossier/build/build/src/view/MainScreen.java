package view;

import java.io.PrintWriter;
import java.io.StringWriter;

import control.XMLControl;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utilities.enums.Severity;

public class MainScreen extends Application
{
    /* ---------- ATTIBUTES ---------- */
    
    /* Propriétés générales */

    private Parent root;
    private FileChooser fileChooser;
    private Stage stage;
    private Scene scene;
    
    /* Controleur */
    private XMLControl xmlControl;
    
    /* Propriétés FXML */

    @FXML
    private ComboBox<String> listeBanques;
    @FXML
    private TextField codosb;
    @FXML
    private TextField cosce;
    @FXML
    private TextField coem;
    @FXML
    private TextField copo;
    

    /* ---------- CONSTUCTORS ---------- */
    
    public MainScreen()
    {
        xmlControl = XMLControl.getInstance();
    }
    
    /* ---------- METHODS ---------- */

    @Override
    public void start(Stage Primarystage) throws Exception
    {
        root = FXMLLoader.load(getClass().getResource("mainscreen.fxml"));
        scene = new Scene(root);
        stage = Primarystage;
        stage.setTitle("Création TN5B -TN5F");
        stage.setResizable(true);
        stage.setScene(scene);
        stage.show();
    }
    
    @FXML
    public void initialize()
    {
        listeBanques.getItems().addAll(xmlControl.getListeBanques().keySet());
        listeBanques.getSelectionModel().selectFirst();        
    }
    
    /**
     * Méthode permettant de créer un message d'erreur
     * 
     * @param severity
     * @param exception
     * @param detail
     */
    public static void createAlert(Severity severity, Exception exception, String detail)
    {
        // instance de l'alerte
        Alert alert;

        // Switch sur les sévérités pour récupérer le type d'alerte
        switch (severity)
        {
            case SEVERITY_ERROR :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            case SEVERITY_INFO :
                alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Information");
                alert.setHeaderText(null);
                alert.setContentText(detail);
                break;
            default :
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Une erreur est survenue");
                alert.setHeaderText("Look, an Exception Dialog");
                alert.setContentText(detail);
                break;
        }

        // Création du message d'exception si celle-ci est fournie.
        if (exception != null)
        {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            String exceptionText = sw.toString();

            Label label = new Label("Stacktrace :");

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
        }

        alert.showAndWait();
    }
    
    @FXML
    public void macro() throws Exception
    {
        String pole = listeBanques.getSelectionModel().getSelectedItem();
        
        if (!pole.equals("Choisissez une Banque"))
        {

        }
        else
            createAlert(Severity.SEVERITY_INFO, null, "Vous devez choisir un pôle");
    }
    
    /* ---------- ACCESS ---------- */

}
