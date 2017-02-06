package bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import dao.DaoChamp;
import dao.DaoIncident;
import dao.DaoProjet;
import model.Incident;
import model.Valeur;

@ManagedBean (name = "test")
@RequestScoped
public class TestBean implements Serializable
{
    /* Attributes */

    private static final long serialVersionUID = 1L;

    /** Doa de la classe Incident */
    @EJB
    private DaoIncident       daoi;

    /** Dao de la classe Projet */
    @EJB
    private DaoProjet         daop;

    /** Dao de la classe Champ */
    @EJB
    private DaoChamp          daoc;

    /** Liste des noms Pôles à afficher */
    private List<String>      listNomsProjets;

    /** nom du pôle sélectionné */
    private String            nom;

    /** Liste des incidents */
    private List<Incident>    listIncidents;

    /* Methods */

    @PostConstruct
    private void postConstruct()
    {
        listNomsProjets = daop.findAllPoleNames();
    }

    public String chargerIncidents()
    {
        if (nom != null) {
            listIncidents = daoi.findByProject(nom);
            System.out.println(listIncidents.size());
            triageIncident();
        }

        return "";
    }

    /**
     * Permet de trier la liste des incidents en ne gardant que ceux du mois en cours
     */
    private void triageIncident()
    {
        // Récupération de la date du jour
        LocalDate date = LocalDate.now();
        DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        for (Iterator<Incident> iter = listIncidents.iterator(); iter.hasNext();) 
        {
            Incident incident = iter.next();

            LocalDate dateIncident = LocalDate.parse(incident.getMapValeurs().get("Date de prise en charge").substring(0, 10), f);
            if (dateIncident.getYear() != date.getYear() || dateIncident.getMonth() != date.getMonth()) 
                iter.remove();
        }
        System.out.println(listIncidents);
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
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom)
    {
        this.nom = nom;
    }

    /**
     * @param listNomsProjets
     *            the listProjets to set
     */
    public void setListNomsProjets(List<String> listNomsProjets)
    {
        this.listNomsProjets = listNomsProjets;
    }
}