package control.view;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

public class MenuControl
{
    /* ---------- ATTIBUTS ---------- */
    
    /** Element du ménu lançant les contrôles mensuels */
    @FXML // fx:id="mensuel"
    private MenuItem mensuel;
    
    /* ---------- METHODES PUBLIQUES ---------- */
    
    @FXML
    void switchToMensuel(ActionEvent event) throws IOException 
    {
        GridPane mensuelPane = FXMLLoader.load( getClass().getResource("/view/Mensuel.fxml"));        
        BorderPane border = MainScreen.getRoot();
        border.setCenter(mensuelPane);
    }
    /* ---------- METHODES PRIVEES ---------- */
        
    /* ---------- ACCESSEURS ---------- */
    
}
