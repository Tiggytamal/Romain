package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SecondaryTable;
import javax.persistence.SecondaryTables;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the custom_values database table.
 * 
 */
@Entity
@Table(name="custom_values")
@SecondaryTables({
	@SecondaryTable(name = "custom_fields", pkJoinColumns = @PrimaryKeyJoinColumn(name = "custom_field_id"))
})
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Valeur.findAll", query="SELECT c FROM Valeur c")
})
//@formatter:on
public final class Valeur implements Serializable
{
    /* ---------- Attributes ---------- */
    
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

    @Column (table = "custom_fields", name = "name", length = 50, nullable = false)
	private String champString;

	@ManyToOne (targetEntity = Incident.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "customized_id")
	private Incident incident;

	@Column (name = "value", columnDefinition = "TEXT")
	private String value;
	
	@Transient
	private model.enums.Champ champ;
	
    /* Constructors */

	public Valeur() 
	{
	}
	
    /* ---------- Methods ---------- */

    /* ---------- Access ---------- */
	
	/**
	 * Retourne l'énumration correspondante à la valeur en table du nom du champ
	 * @return
	 * 		L'enum
	 */
	public model.enums.Champ getChamp()
	{
		return champ;
	}
	
    /**
     * @return the id
     */
    public int getId()
    {
        return id;
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