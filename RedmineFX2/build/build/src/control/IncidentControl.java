package control;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import dao.DaoIncident;
import model.Incident;
import utilities.enums.Severity;
import utilities.interfaces.Instance;
import view.MainScreen;

public class IncidentControl implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    //Daos

    /** Doa de la classe Incident */
    private DaoIncident daoi;
    
    private XMLControl xmlControl;
    
    private ListControl listControl;
    
    // Attributs du Bean
    
    /* ---------- CONSTUCTORS ---------- */

    public IncidentControl()
    {
        instanciation();
    }
    
    @Override
    public void instanciation()
    {
        xmlControl = XMLControl.getInstance();
        xmlControl.miseAJourParam();
        listControl = ListControl.getInstance();
    }

    /* ---------- METHODS ---------- */

    /**
     * Charge les incidents depuis la base Redmine en fonction du pôle choisi
     * @return
     * @throws FunctionalException 
     */
    public List<Incident> chargerIncidents(String nomPole)
    {
        if (nomPole == null || nomPole.isEmpty())
            MainScreen.createAlert(Severity.SEVERITY_ERROR, null, null);

        List<Incident> listIncidents = new ArrayList<>();
        daoi = (DaoIncident) listControl.getFactory().createDao(Incident.class);
        listIncidents = daoi.findByProject(nomPole);
        System.out.println("nombre d'incident du pôle : " + listIncidents.size());       
        return listIncidents;
    }
    

    /* ---------- ACCESS ---------- */

}