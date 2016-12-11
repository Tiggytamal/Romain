package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


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

public class Tracker implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 30 , nullable = false)
	private String nom;
	
	@OneToMany (mappedBy = "tracker", targetEntity = Incident.class, fetch = FetchType.LAZY)
	private List<Incident> incidents;

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