package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import dao.DaoChamp;
import dao.DaoIncident;
import dao.DaoProjet;
import model.Incident;
import model.enums.Champ;
import model.enums.Statut;
import utilities.Utilities;
import utilities.interfaces.Instance;

@ManagedBean (name = "incident")
@ViewScoped
public class IncidentBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    
    //Daos
    
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;

    /** Doa de la classe Incident */
    @EJB
    private DaoIncident daoi;
    /** Dao de la classe Projet */
    @EJB
    private DaoProjet daop;
    /** Dao de la classe Champ */
    @EJB
    private DaoChamp daoc;
    
    // Attributs du Bean
    
    /** Liste des noms Pôles à afficher */
    private List<String> listNomsProjets;
    
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
    
    /* ---------- CONSTUCTORS ---------- */

    public IncidentBean()
    {
        instanciation();
    }

    /* ---------- METHODS ---------- */
    

    @Override
    public void instanciation()
    {
       listNomsProjets = new ArrayList<>();
       listIncidents = new ArrayList<>();
       listApplications = new ArrayList<>();
       applicationschoisies = new ArrayList<>();
       listincidentsTries = new ArrayList<>();
    }

    @PostConstruct
    private void postConstruct()
    {
        listNomsProjets = daop.findAllPoleNames();
    }
    /**
     * Charge les incidents depuis la base Redmine en fonction du pôle choisi
     * @return
     */
    public void chargerIncidents()
    {
        if (nomPole == null)
        {
            Utilities.updateGrowl("Vous devez choisir un pôle", FacesMessage.SEVERITY_ERROR, null);
        }

        listIncidents = new ArrayList<>();
        listIncidents = daoi.findByProject(nomPole);
        System.out.println("nombre d'incident du pôle : " + listIncidents.size());
        recuperationApplications();
               
       // triageIncident();       
        listBean.setListIncidents(listIncidents);
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
        System.out.println("taille incidents triés:" + listincidentsTries.size());
        return "";
    }

    /* ---------- ACCESS ---------- */
   
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
     * @return the listProjets
     */
    public List<String> getListNomsProjets()
    {
        return listNomsProjets;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nomPole;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom)
    {
        this.nomPole = nom;
    }

    /**
     * @param listNomsProjets
     *            the listProjets to set
     */
    public void setListNomsProjets(List<String> listNomsProjets)
    {
        this.listNomsProjets = listNomsProjets;
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

}