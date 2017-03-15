package model.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Table (name = "connexion")
@Entity
//@formatter:off
@NamedQueries (value = {
		@NamedQuery (name = "Connexion.findAll", query = "SELECT c FROM Connexion c"),
		@NamedQuery (name = "Connexion.findByNom", query = "SELECT c FROM Connexion c WHERE c.nom = :nom")
})
//@formatter:on
public class Connexion
{
	/* Attributes */

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column (name = "nom", length = 50, nullable = false, unique = true)
	private String nom;
	
	@Column (name = "pass", length = 128, nullable = false)
	private String pass;
	
	/* Constructors */
	
	public Connexion()
	{
		
	}

	/* Access */
	
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getNom()
	{
		return nom;
	}

	public void setNom(String nom)
	{
		this.nom = nom;
	}

	public String getPass()
	{
		return pass;
	}

	public void setPass(String pass)
	{
		this.pass = pass;
	}
}