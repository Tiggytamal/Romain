package utilities.interfaces;

import java.util.List;

/**
 * Classe abtraite générique de tous les Dao.
 * Contient une méthode pour lire tous les objets de la table correspondant au model en paramètre
 * 
 * @author Grégoire Mathon
 * @since 1.0
 *
 * @param <T>
 * 	la classe de modèle correspondante
 */
public abstract class DaoGeneric<T>
{
	/**
	 * Permet d'extraire tous les objets correspondant à la classe de modele en paramètre
	 * 
	 * @return
	 * une liste des objets
	 */
	public abstract List<T> readAll();
}
