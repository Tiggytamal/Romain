package dao;

import java.util.HashMap;

import model.Model;
import utilities.interfaces.DaoGeneric;
/**
 * Factory de création des Dao
 * Classe singleton. Crée une instance du Manager JPA avec la map de paramètres
 * 
 * @author Tiggy Tamal
 *
 */
public class DaoFactory
{
    /* ---------- ATTIBUTES ---------- */

    /** EntityManager */
    private Manager manager;

    /** Instance de la factory */
    private static DaoFactory factory;

    /* ---------- CONSTUCTORS ---------- */

    /**
     * Constructeur privé de la Factory
     * @param map
     */
    private DaoFactory(HashMap<String, String> map)
    {
        manager = Manager.getInstance(map);
    }
    
    public static DaoFactory getFactory(HashMap<String, String> map)
    {
        if (factory == null)
            factory = new DaoFactory(map);
        return factory;            
    }
    
    /* ---------- METHODS ---------- */

    public DaoGeneric<? extends Model>  createDao(Class<? extends Model> classe)
    {        
        DaoGeneric<? extends Model> dao = null;
        switch (classe.getSimpleName())
        {
            case "Incident" :
                dao =  new DaoIncident();
                break;
            
            case "Projet" :
                dao =  new DaoProjet();
                break;                
        }
        
        dao.setEm(manager.getEm());       
        return dao;
    }
    
    /* ---------- ACCESS ---------- */
    
    /**
     * @return the manager
     */
    public Manager getManager()
    {
        return manager;
    }
    
}
