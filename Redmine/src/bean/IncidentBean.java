package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import dao.DaoIncident;
import model.Incident;
import utilities.GrowlException;
import utilities.interfaces.Instance;

@ManagedBean (name = "incident")
@SessionScoped
public class IncidentBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    //Daos

    /** Doa de la classe Incident */
    @EJB
    private DaoIncident daoi;
    
    // Attributs du Bean
    
    /* ---------- CONSTUCTORS ---------- */

    public IncidentBean()
    {
        instanciation();
    }
    
    @Override
    public void instanciation()
    {
    }

    /* ---------- METHODS ---------- */

    /**
     * Charge les incidents depuis la base Redmine en fonction du pôle choisi
     * @return
     * @throws GrowlException 
     */
    public List<Incident> chargerIncidents(String nomPole) throws GrowlException
    {
        if (nomPole == null || nomPole.isEmpty())
            throw new GrowlException(FacesMessage.SEVERITY_ERROR,"Vous devez choisir un pôle", null);

        List<Incident> listIncidents = new ArrayList<>();
        listIncidents = daoi.findByProject(nomPole);
        System.out.println("nombre d'incident du pôle : " + listIncidents.size());       
        return listIncidents;
    }

    /* ---------- ACCESS ---------- */

}