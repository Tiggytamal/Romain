package dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;

import model.Incident;

@Stateless
public class DaoIncident extends DaoModel<Incident>
{

    @Override
    public List<Incident> readAll()
    {
        return em.createNamedQuery("Incident.findAll", Incident.class).getResultList();
    }
    
    /**
     * Permet de récupérer une formation avec son intitulé
     * 
     * @param projet
     *            l'intitulé de la formation recherchée
     * @return la formation correspondante à cette identifiant ou null si aucune a été trouvée
     */
    public List<Incident> findByProject(String projet)
    {
        em.clear();
        TypedQuery<Incident> query = em.createNamedQuery("Incident.findByProject", Incident.class).setParameter("projet", projet);
        System.out.println(query.unwrap(EJBQueryImpl.class).getDatabaseQuery().getSQLString());
        long a = System.currentTimeMillis();
        List<Incident> list = query.getResultList();
        long b = System.currentTimeMillis();
        System.out.println("Temps de la requête: " + (b-a)/1000);
        return list;
    }

}
