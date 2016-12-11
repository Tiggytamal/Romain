package dao;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import utilities.interfaces.DaoGeneric;

public abstract class DaoModel<T> extends DaoGeneric<T>
{
    
    @PersistenceContext (unitName = "Pointpole")
    protected EntityManager em;
}
