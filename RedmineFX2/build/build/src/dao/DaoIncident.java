package dao;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.eclipse.persistence.internal.jpa.EJBQueryImpl;

import model.Incident;
import utilities.interfaces.DaoGeneric;

public class DaoIncident extends DaoGeneric<Incident>
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
        Date oneYear = Date.valueOf(LocalDate.now().minusYears(1));
        TypedQuery<Incident> query = em.createNamedQuery("Incident.findByProject", Incident.class).setParameter("projet", projet).setParameter("oneYear", oneYear, TemporalType.DATE);
        System.out.println(query.unwrap(EJBQueryImpl.class).getDatabaseQuery().getSQLString());
        long a = System.currentTimeMillis();
        List<Incident> list = query.getResultList();
        long b = System.currentTimeMillis();
        System.out.println("Temps de la requête: " + (b-a)/1000);
        return list;
    }
}
