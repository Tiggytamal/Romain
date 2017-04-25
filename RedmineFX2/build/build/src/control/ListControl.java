package control;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dao.DaoFactory;
import model.Incident;
import model.xml.Parametre;
import utilities.interfaces.Instance;
/**
 * Classe singleton permettant de sauvegarder les informations de session
 * 
 * @author Tiggy Tamal
 * @since 1.0
 */
public class ListControl implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */
    
    private static final long serialVersionUID = 1L;
    
    /** Factory pour les Daos*/
    private DaoFactory factory;


    /** liste des incidents */
    private List<Incident> listIncidents;
    /** Liste des applications choisies */
    private List<String> applicationschoisies;
    /** Nouveau fichier à télécharger */
    private File upload;

    /** Fichier paramètre du programme */
    private Parametre param;
    /** Chemin du repertoire du fichier jar */
    private String path;
    
    
    /* ---------- CONSTUCTORS ---------- */

    private ListControl()
    {
        instanciation();
    }   
    
    @Override
    public void instanciation()
    {
        listIncidents = new ArrayList<>();
        applicationschoisies = new ArrayList<>();
        param = new Parametre();
    }
    
    private static class ListControlHelper
    {
        private static final ListControl INSTANCE = new ListControl();
    }
    
    /* ---------- METHODS ---------- */

    public static ListControl getInstance()
    {
        return ListControlHelper.INSTANCE;        
    }
    
    public void createFactory (HashMap<String, String> map)
    {
        factory = DaoFactory.getFactory(map);
    }
    
    /* ---------- ACCESS ---------- */
    

    /**
     * @return the upload
     */
    public File getUpload()
    {
        return upload;
    }

    /**
     * @param upload the upload to set
     */
    public void setUpload(File upload)
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
     * @return the param
     */
    public Parametre getParam()
    {
        return param;
    }

    /**
     * @param param the param to set
     */
    public void setParam(Parametre param)
    {
        this.param = param;
    }
    /**
     * @return the path
     */
    public String getPath()
    {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * @return the factory
     */
    public DaoFactory getFactory()
    {
        return factory;
    }
}