package dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;

import dao.general.DaoSystem;
import model.system.Connexion;


@Stateless
public class DaoConnexion extends DaoSystem<Connexion> implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see utilities.interfaces.DaoGeneric#readAll()
	 */
	@Override
	public List<Connexion> readAll()
	{
		return em.createNamedQuery("Connexion.findAll", Connexion.class).getResultList();
	}
	
	/**
	 * Permet de trouver un compte de connexion avec le nom du compte
	 * 
	 * @param nom
	 * le nom du compte
	 * @return
	 * le compt de connexion
	 */
	public Connexion findByNom(String nom)
	{
		List<Connexion> list = em.createNamedQuery("Connexion.findByNom", Connexion.class).setParameter("nom", nom).getResultList();
		if (list.isEmpty())
			return null;
		return list.get(0);
	}

}
