package control.view;

import java.io.IOException;

import control.parent.ViewControl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;

public class MenuViewControl extends ViewControl
{
    /* ---------- ATTIBUTS ---------- */
    
    /** Element du ménu lançant les contrôles mensuels */
    @FXML
    private MenuItem mensuel;
    @FXML
    private MenuItem applications;
    @FXML
    private MenuItem options;
    @FXML
    private MenuItem planificateur;
    @FXML
    private MenuItem maintenance;
    @FXML
    private Button connexion;
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    public void test()
    {

    }
    
    @FXML
    public void switchToMensuel(ActionEvent event) throws IOException 
    {
        load("/view/Mensuel.fxml");        
    }
    
    @FXML
    public void switchToApplications(ActionEvent event) throws IOException 
    {
        load("/view/Applications.fxml");        
    }
    
    @FXML
    public void switchToOptions(ActionEvent event) throws IOException 
    {
        load("/view/Options.fxml");        
    }
    
    @FXML
    public void switchToPlanificateur(ActionEvent event) throws IOException 
    {
        load("/view/Planificateur.fxml");        
    }
    
    @FXML
    public void switchToMaintenance(ActionEvent event) throws IOException 
    {
        load("/view/Maintenance.fxml");        
    }

    /* ---------- METHODES PRIVEES ---------- */
    
    /* ---------- ACCESSEURS ---------- */
    
}
