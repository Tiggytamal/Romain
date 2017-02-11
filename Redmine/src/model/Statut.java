package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


/**
 * The persistent class for the issue_statuses database table.
 * 
 */
@Entity
@Table(name="issue_statuses")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Statut.findAll", query="SELECT i FROM Statut i")
})
//@formatter:on
public final class Statut implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 30, nullable = false)
	private String nom;

	/* Constructors */
	
	public Statut() {
	}

	/* Access */
	
	public int getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
	}
}