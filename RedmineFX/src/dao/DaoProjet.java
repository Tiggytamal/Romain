package dao;

import java.util.List;

import model.Projet;

public class DaoProjet extends DaoRedmine<Projet>
{

    @Override
    public List<Projet> readAll()
    {
        return em.createNamedQuery("Projet.findAll", Projet.class).getResultList();
    }
    
    public List<Projet> findAllPole()
    {
        return em.createNamedQuery("Projet.findAllPole", Projet.class).getResultList();
    }
    
    public List<String> findAllPoleNames()
    {
        return em.createNamedQuery("Projet.findAllPoleNames", String.class).getResultList();
    }

}
