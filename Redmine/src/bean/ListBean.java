package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.StreamedContent;

import dao.DaoProjet;
import model.Incident;
import utilities.interfaces.Instance;

@ManagedBean (name = "list")
@SessionScoped
public class ListBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */
    
    private static final long serialVersionUID = 1L;
    
    /** Dao de la classe Projet */
    @EJB
    private DaoProjet daop;
    
    /** liste des incidents */
    private List<Incident> listIncidents;
    /** Liste des applications choisies */
    private List<String> applicationschoisies;
    /** Nouveau fichier à télécharger */
    private StreamedContent upload;
    /** Liste des noms de projets dans la base Redmine */
    private List<String> listNomsProjets;
    
    // Connection
    
    /** nom de connexion */
    private String connect;
    /** vérifie si la connection est bonne */
    private boolean connectok;
    
    /* ---------- CONSTUCTORS ---------- */

    public ListBean()
    {
        instanciation();
    }   
    
    @Override
    public void instanciation()
    {
        listIncidents = new ArrayList<>();
        applicationschoisies = new ArrayList<>();
    }
    
    @PostConstruct
    private void postConstruct()
    {
        listNomsProjets = daop.findAllPoleNames();
    }
    
    /* ---------- METHODS ---------- */

    
    /* ---------- ACCESS ---------- */
    

    /**
     * @return the upload
     */
    public StreamedContent getUpload()
    {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(StreamedContent upload)
    {
        this.upload = upload;
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

    /**
     * @return the listIncidents
     */
    public List<Incident> getListIncidents()
    {
        return listIncidents;
    }

    /**
     * @param listIncidents the listIncidents to set
     */
    public void setListIncidents(List<Incident> listIncidents)
    {
        this.listIncidents = listIncidents;
    }
    
    /**
     * @return the listNomsProjets
     */
    public List<String> getListNomsProjets()
    {
        return listNomsProjets;
    }
    /**
     * @return the connect
     */
    public String getConnect()
    {
        return connect;
    }

    /**
     * @param connect the connect to set
     */
    public void setConnect(String connect)
    {
        this.connect = connect;
    }

    /**
     * @return the connectok
     */
    public boolean isConnectok()
    {
        return connectok;
    }

    /**
     * @param connectok the connectok to set
     */
    public void setConnectok(boolean connectok)
    {
        this.connectok = connectok;
    }
}