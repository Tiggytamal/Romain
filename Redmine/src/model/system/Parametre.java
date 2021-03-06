package model.system;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table (name = "parametres")
//@formatter:off
@NamedQueries (value = {
		@NamedQuery (name = "Parametre.findAll", query = "SELECT p FROM Parametre p")
})
//@formatter:on
public class Parametre implements Serializable
{
	/* Attributes */
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column (name = "nom", nullable = false, length = 20, unique = true)
	private String nom;
	
	@Column (name = "valeur", nullable = false, length = 50)
	private String valeur;
	
	@Column (name = "type", nullable = false, length = 1)
	private String type;
	
	@Column (name = "libelle", nullable = false, length = 50)
	private String libelle;
	
	/* Constructors */
	
	public Parametre()
	{
		
	}

	/* Methods */
	
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

	public String getValeur()
	{
		return valeur;
	}

	public void setValeur(String valeur)
	{
		this.valeur = valeur;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public String getLibelle()
	{
		return libelle;
	}

	public void setLibelle(String libelle)
	{
		this.libelle = libelle;
	}
}