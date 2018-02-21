package control.view;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SplitPane;

public class OptionViewControl
{
    /*---------- ATTRIBUTS ----------*/

    @FXML
    private SplitPane splitPane;
    @FXML
    private ListView<String> options;
    
    @FXML
    public void initialize()
    {
        SimpleStringProperty string = new SimpleStringProperty("testappel");
        options.getSelectionModel().selectedItemProperty().addListener((ov, old, newval) -> test(ov));
        ObservableList<SimpleStringProperty> liste = FXCollections.observableArrayList();
        liste.add(string);    
        //https://stackoverflow.com/questions/26730034/java-8-observable-list-invalidation-listener-nor-change-listener-is-called-in/26734379#26734379
        //https://stackoverflow.com/questions/26838183/how-to-monitor-changes-on-objects-contained-in-an-observablelist-javafx
        //https://docs.oracle.com/javafx/2/ui_controls/list-view.htm#CEGGEDBF
    }
    

    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void test(ObservableValue<? extends String> ov)
    {
        if (ov.getValue().equals("Chargement fichiers"))
        	System.out.println("Chargement fichiers");
        if (ov.getValue().equals("Paramètres"))
        	System.out.println("Paramètres");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
