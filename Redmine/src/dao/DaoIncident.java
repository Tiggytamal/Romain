package dao;

import java.util.List;

import javax.ejb.Stateless;

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
        return em.createNamedQuery("Incident.findByProject", Incident.class).setParameter("projet", projet).getResultList();
    }

}
