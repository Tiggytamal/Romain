package control.view;

import javafx.beans.property.SimpleStringProperty;
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
        string.addListener((ov, oldVal, newVal) -> System.out.println(ov));
        ObservableList<SimpleStringProperty> liste = FXCollections.observableArrayList();
        liste.add(string);    
        //https://stackoverflow.com/questions/26730034/java-8-observable-list-invalidation-listener-nor-change-listener-is-called-in/26734379#26734379
        //https://stackoverflow.com/questions/26838183/how-to-monitor-changes-on-objects-contained-in-an-observablelist-javafx
        //https://docs.oracle.com/javafx/2/ui_controls/list-view.htm#CEGGEDBF
    }
    

    
    /*---------- CONSTRUCTEURS ----------*/
    /*---------- METHODES PUBLIQUES ----------*/
    
    public void test()
    {
        System.out.println("test");
    }
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/
}
