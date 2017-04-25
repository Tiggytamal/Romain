package dao;

import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;



public class Manager
{
    private EntityManagerFactory emf;
    private EntityManager em;

    private static Manager instance;
    
    private Manager(HashMap<String, String> map)
    {
        emf = Persistence.createEntityManagerFactory("redmine", map);
        em = emf.createEntityManager();
    }
    
    /* ---------- METHODS ---------- */

    public static Manager getInstance(HashMap<String, String> map)
    {
        if (instance == null)
            instance = new Manager(map);
        return instance;
    }
    
    /**
     * @return the em
     */
    public EntityManager getEm()
    {
        return em;
    }
}
