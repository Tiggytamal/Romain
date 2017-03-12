package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.model.StreamedContent;

import model.Incident;
import utilities.interfaces.Instance;

@ManagedBean (name = "list")
@SessionScoped
public class ListBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */
    
    private static final long serialVersionUID = 1L;
    
    /** liste des incidents */
    private List<Incident> listIncidents;
    /** Liste des applications choisies */
    private List<String> applicationschoisies;
    /** Nouveau fichier à télécharger */
    private StreamedContent upload;
    
    /* ---------- CONSTUCTORS ---------- */

    /* ---------- METHODS ---------- */
   
    @Override
    public void instanciation()
    {
        listIncidents = new ArrayList<>();
        applicationschoisies = new ArrayList<>();
    }
    
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

    public ListBean()
    {
        instanciation();
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
}