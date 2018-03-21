package control.view;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import view.TrayIconView;

public class PlanificateurViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    @FXML
    private Button demarrer;
    @FXML
    private Button arreter;
    @FXML
    private GridPane backgroundPane;
    @FXML
    private CheckBox lundiBox;
    @FXML
    private CheckBox mardiBox;
    @FXML
    private CheckBox mercrediBox;
    @FXML
    private CheckBox jeudiBox;
    @FXML
    private CheckBox vendrediBox;
    
    private ControlJob control;

    /*---------- CONSTRUCTEURS ----------*/
    
    public PlanificateurViewControl() throws SchedulerException
    {
        control = new ControlJob();
    }

    /*---------- METHODES PUBLIQUES ----------*/

    @FXML
    public void demarrer() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageRed);
        control.creationJobsSonar();
    }
    
    @FXML
    public void arreter() throws SchedulerException
    {
        MainScreen.changeImageTray(TrayIconView.imageBase);
        control.fermeturePlanificateur();
    }
    
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
