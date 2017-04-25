package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import utilities.Statics;


/**
 * The persistent class for the projects database table.
 * 
 */
@Entity
@Table(name="projects")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Projet.findAll", query="SELECT p FROM Projet p"),
        @NamedQuery(name="Projet.findAllPole", query="SELECT p FROM Projet P where p.nom LIKE 'Pôle%'"),
        @NamedQuery(name="Projet.findAllPoleNames", query="SELECT p.nom FROM Projet P where p.nom LIKE 'Pôle%'")     
})
//@formatter:on
public final class Projet extends Model implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "identifier")
	private String identifier;

	@Column (name = "name", length = 255, nullable = true)
	private String nom;

    @ManyToOne (targetEntity = Projet.class, optional = true)
    @JoinColumn (name = "parent_id")
	private Projet projetParent;

	/* Constructors */
	
	public Projet() {
	}
	
	/*Methods */
	
    @Override
    public String toString()
    {
        return nom + Statics.NL ;
    }
    
    /* Access */

    public int getId() 
    {
		return id;
	}

    public String getIdentifier() 
	{
		return identifier;
	}

	public String getNom() 
	{
		return nom;
	}

    public Projet getParent()
    {
        return projetParent;
    }
}