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
        return em.createNamedQuery("incident.findAll", Incident.class).getResultList();
    }
    
    /**
     * Permet de récupérer une formation avec son intitulé
     * 
     * @param sujet
     *            l'intitulé de la formation recherchée
     * @return la formation correspondante à cette identifiant ou null si aucune a été trouvée
     */
    public List<Incident> findOne(String sujet)
    {
        return em.createNamedQuery("incident.findOne", Incident.class).setParameter("sujet", sujet).getResultList();
    }

}
