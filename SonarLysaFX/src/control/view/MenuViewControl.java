package control.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MenuViewControl
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
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    public void switchToMensuel(ActionEvent event) throws IOException 
    {
        GridPane pane = FXMLLoader.load( getClass().getResource("/view/Mensuel.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(pane);
    }
    
    @FXML
    public void switchToApplications(ActionEvent event) throws IOException 
    {
        GridPane pane = FXMLLoader.load( getClass().getResource("/view/Applications.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(pane);
    }
    
    @FXML
    public void switchToOptions(ActionEvent event) throws IOException 
    {
        SplitPane pane = FXMLLoader.load( getClass().getResource("/view/Options.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(pane);
    }
    
    @FXML
    public void switchToPlanificateur(ActionEvent event) throws IOException 
    {
        GridPane pane = FXMLLoader.load( getClass().getResource("/view/Planificateur.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(pane);
    }

    /* ---------- METHODES PRIVEES ---------- */
        
    /* ---------- ACCESSEURS ---------- */
    
}
