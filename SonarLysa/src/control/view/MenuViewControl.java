package control.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
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
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    public void switchToMensuel(ActionEvent event) throws IOException 
    {
        GridPane mensuelPane = FXMLLoader.load( getClass().getResource("/view/Mensuel.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(mensuelPane);
    }
    
    @FXML
    public void switchToApplications(ActionEvent event) throws IOException 
    {
        GridPane mensuelPane = FXMLLoader.load( getClass().getResource("/view/Mensuel.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(mensuelPane);
    }

    /* ---------- METHODES PRIVEES ---------- */
        
    /* ---------- ACCESSEURS ---------- */
    
}
