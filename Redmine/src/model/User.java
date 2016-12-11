package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the users database table.
 * 
 */
@Entity
@Table(name="users")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="User.findAll", query="SELECT u FROM User u")
})
//@formatter:on

public class User implements Serializable
{
    /* Attributes */
    
	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	/** Prénom de l'utilisateur */
	@Column (name = "firstname", length = 30, nullable = false)
	private String prenom;

	/** Nom de l'utilisateur */
	@Column (name = "lastname", length = 255, nullable = false)
	private String nom;
	
	/** Incident en cours de l'utilisateur */
	@OneToMany (mappedBy = "responsable", targetEntity = Incident.class, fetch = FetchType.LAZY)
	private List<Incident> incidentsEnCours;

	/** Incident créés par l'utilisateur */
	@OneToMany (mappedBy = "createur", targetEntity = Incident.class, fetch = FetchType.LAZY)
	private List<Incident> incidentsCrees;
	
	/** journaux créés par l'utilisateur */
	@OneToMany (mappedBy = "createur", targetEntity = Journal.class, fetch = FetchType.LAZY)
	private List<Journal> journaux;
	   
	public User() 
	{
	}

    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the prenom
     */
    public String getPrenom()
    {
        return prenom;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }

    /**
     * @return the incidentsEnCours
     */
    public List<Incident> getIncidentsEnCours()
    {
        return incidentsEnCours;
    }

    /**
     * @return the incidentsCrees
     */
    public List<Incident> getIncidentsCrees()
    {
        return incidentsCrees;
    }

    /**
     * @return the journaux
     */
    public List<Journal> getJournaux()
    {
        return journaux;
    }

}