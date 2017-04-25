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
 * The persistent class for the trackers database table.
 * 
 */
@Entity
@Table(name="trackers")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Tracker.findAll", query="SELECT t FROM Tracker t")
})
//@formatter:on

public final class Tracker extends Model implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 30 , nullable = false)
	private String nom;

	/* Constructors */

	public Tracker() {
	}

	/* Access */
	
	public int getId() {
		return this.id;
	}

	public String getNom() {
		return this.nom;
	}
}