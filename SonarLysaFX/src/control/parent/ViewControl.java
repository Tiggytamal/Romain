package control.parent;

import java.io.IOException;

import control.view.MainScreen;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;

public abstract class ViewControl
{
    /*---------- ATTRIBUTS ----------*/
    
    private BorderPane border;
    
    /*---------- CONSTRUCTEURS ----------*/
    
    protected ViewControl()
    {
        border = MainScreen.getRoot();
    }
    /*---------- METHODES PUBLIQUES ----------*/
    
    /**
     * Chargement de la nouvelle page en utilisant la ressource en paramètre
     * @param ressource
     * @throws IOException
     */
    protected void load(String ressource) throws IOException
    {
        Node pane = FXMLLoader.load( getClass().getResource(ressource));
        border.setCenter(pane);       
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

}
