package dao.general;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import utilities.interfaces.DaoGeneric;

public abstract class DaoSystem<T> extends DaoGeneric<T>
{
    
    @PersistenceContext (unitName = "Appli")
    protected EntityManager em;
}
