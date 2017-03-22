package dao;


import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class DaoRedmine<T>
{
	private EntityManagerFactory emf;
	protected EntityManager em;

	public DaoRedmine()
	{
		emf = Persistence.createEntityManagerFactory("RedmineFX");
		em = emf.createEntityManager();
	}

	public abstract List<T> readAll();
}