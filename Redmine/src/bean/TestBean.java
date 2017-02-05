package bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import dao.DaoIncident;
import dao.DaoProjet;
import model.Incident;
import model.Projet;

@ManagedBean (name = "test")
@RequestScoped
public class TestBean implements Serializable
{
    /* Attributes */

    private static final long serialVersionUID = 1L;

    @EJB
    private DaoIncident daoi;
    @EJB
    private DaoProjet daop;
    
    private List<String> listNomsProjets;
    
    private String nom;
    
    /* Methods */
    
    @PostConstruct
    private void postConstruct()
    {
        listNomsProjets = daop.findAllPoleNames();
    }
    
    public String chargerIncidents()
    {
        List<Incident> list = daoi.findByProject(nom);
        System.out.println(list.size());
        return "";
    }
    
    /* Access */ 

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
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @param listNomsProjets the listProjets to set
     */
    public void setListNomsProjets(List<String> listNomsProjets)
    {
        this.listNomsProjets = listNomsProjets;
    }    
}