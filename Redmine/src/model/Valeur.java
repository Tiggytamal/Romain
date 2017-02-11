package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the custom_values database table.
 * 
 */
@Entity
@Table(name="custom_values")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Valeur.findAll", query="SELECT c FROM Valeur c JOIN FETCH c.champ ch")
})
//@formatter:on
public final class Valeur implements Serializable
{
    /* Attributes */
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

    @ManyToOne (targetEntity = Champ.class, fetch = FetchType.LAZY)
	@JoinColumn (name="custom_field_id")
	private Champ champ;

	@ManyToOne (targetEntity = Incident.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "customized_id")
	private Incident incident;

	@Column (name = "value", columnDefinition = "TEXT")
	private String value;
	
    /* Constructors */
	
	public Valeur() 
	{
	}
	
    /* Methods */

    /* Access */
	
    /**
     * @return the id
     */
    public int getId()
    {
        return id;
    }

    /**
     * @return the champ
     */
    public Champ getChamp()
    {
        return champ;
    }

    /**
     * @return the incident
     */
    public Incident getIncident()
    {
        return incident;
    }

    /**
     * @return the value
     */
    public String getValue()
    {
        return value;
    }
}