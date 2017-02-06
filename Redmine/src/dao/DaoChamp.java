package dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import model.Champ;

@Stateless
public class DaoChamp extends DaoModel<Champ> implements Serializable
{
    private static final long serialVersionUID = 1L;
    
    @Override
    public List<Champ> readAll()
    {
        return em.createNamedQuery("Champ.findAll", Champ.class).getResultList();
    }
    
    public Integer findId(String nom)
    {
        List<Integer> list = em.createNamedQuery("Champ.findId", Integer.class).setParameter("nom", nom).getResultList();
        if(list.isEmpty())
            return null;
        return list.get(0);
    }
}