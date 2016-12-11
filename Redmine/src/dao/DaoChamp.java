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
}