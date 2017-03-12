package bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import model.Incident;
import model.enums.Champ;
import utilities.GrowlException;
import utilities.Utilities;
import utilities.interfaces.Instance;

@ManagedBean (name = "hebdo")
@ViewScoped
public class HebdoBean implements Instance
{
    /* ---------- ATTIBUTES ---------- */
    
    // Bean de chargement des incidents
    @ManagedProperty (value = "#{incident}")
    private IncidentBean incidentBean;

    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    /** nom du pôle sélectionné */
    private String nomPole;
    /** Liste des incidents*/
    private List<Incident> listIncidents;
    /** Liste des applications selectionnées */
    private List<String> listApplications;
    /** Liste des applications choisies */
    private List<String> applicationschoisies;


    /** liste des incidents des applications selectionnées */
    private List<Incident> listincidentsTries;
    /** Liste des noms Pôles à afficher */
    private List<String> listNomsProjets;
    

    /* ---------- CONSTUCTORS ---------- */

    public HebdoBean()
    {
        instanciation();
    }
    
    @Override
    public void instanciation()
    {
        listIncidents = new ArrayList<>();   
        listNomsProjets = new ArrayList<>();
        listApplications = new ArrayList<>();
        listincidentsTries = new ArrayList<>();
        
    }
    
    @PostConstruct
    public void postConstruct()
    {
        listNomsProjets = listBean.getListNomsProjets();
    }
    
    /* ---------- METHODS ---------- */

    
    public String charger()
    {
        try
        {
            listIncidents = incidentBean.chargerIncidents(nomPole);
        }
        catch (GrowlException e)
        {
            Utilities.updateGrowl(e.getMessage(), e.getSeverity(), e.getDetail());
            return "";
        }     
        recuperationApplications();
        return "";
    }
    
    /**
     * Récupération de toutes les applications possibles des incidents du pôle choisi
     */
    private void recuperationApplications()
    {
        for (Incident incident : listIncidents)
        {
            // Récupération du champ correspondant à l'application de l'incident
            String appli = incident.getMapValeurs().get(Champ.APPLICATION);
            
            if (appli != null && !listApplications.contains(appli))
            {
                listApplications.add(appli);
            }
        }
        // Tri de la liste par ordre alphabétique des trigrammes d'application
        Collections.sort(listApplications);
    }
    
    /**
     * Permet de ne garder que les incidents des applications choisies et envoie la liste vers le Bean de Session
     * @return
     * 
     */
    public String trierParAppli()
    {
        // Itération sur la liste des incidents pour retirer tout ceux qui ne sont pas des applications choisies
        if (applicationschoisies.isEmpty())
        {
            Utilities.updateGrowl("Vous devez choisir au moins une application", FacesMessage.SEVERITY_ERROR, null);
            return "";
        }
        for (Incident incident : listIncidents)
        {
            if (applicationschoisies.contains(incident.getMapValeurs().get(Champ.APPLICATION)) )
            {
                listincidentsTries.add(incident);
            }
        }
        
        // Renvoie au Bean de session la liste des incidents des applications choisies.
        listBean.setListIncidents(listincidentsTries);
        //Transfert de la liste dans le Bean de session.
        listBean.setApplicationschoisies(listApplications);
        System.out.println("taille incidents triés:" + listincidentsTries.size());
        return "";
    }
    
    /* ---------- ACCESS ---------- */
    
    /**
     * @return the nomPole
     */
    public String getNomPole()
    {
        return nomPole;
    }

    /**
     * @param nomPole the nomPole to set
     */
    public void setNomPole(String nomPole)
    {
        this.nomPole = nomPole;
    }
    /**
     * @return the listApplications
     */
    public List<String> getListApplications()
    {
        return listApplications;
    }

    /**
     * @param listApplications the listApplications to set
     */
    public void setListApplications(List<String> listApplications)
    {
        this.listApplications = listApplications;
    }
    
    /**
     * @return the listNomsProjets
     */
    public List<String> getListNomsProjets()
    {
        return listNomsProjets;
    }

    /**
     * @param listNomsProjets the listNomsProjets to set
     */
    public void setListNomsProjets(List<String> listNomsProjets)
    {
        this.listNomsProjets = listNomsProjets;
    }
    
    
    /**
     * @return the incidentBean
     */
    public IncidentBean getIncidentBean()
    {
        return incidentBean;
    }

    /**
     * @param incidentBean the incidentBean to set
     */
    public void setIncidentBean(IncidentBean incidentBean)
    {
        this.incidentBean = incidentBean;
    }
    
    
    /**
     * @return the listBean
     */
    public ListBean getListBean()
    {
        return listBean;
    }

    /**
     * @param listBean the listBean to set
     */
    public void setListBean(ListBean listBean)
    {
        this.listBean = listBean;
    }
    /**
     * @return the applicationschoisies
     */
    public List<String> getApplicationschoisies()
    {
        return applicationschoisies;
    }

    /**
     * @param applicationschoisies the applicationschoisies to set
     */
    public void setApplicationschoisies(List<String> applicationschoisies)
    {
        this.applicationschoisies = applicationschoisies;
    }

}