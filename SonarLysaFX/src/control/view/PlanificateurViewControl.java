package control.view;

import org.quartz.SchedulerException;

import control.quartz.ControlJob;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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
        control.creationJobAnomaliesSonar();
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