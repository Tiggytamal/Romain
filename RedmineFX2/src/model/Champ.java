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
 * The persistent class for the custom_fields database table.
 * 
 */
@Entity
@Table(name="custom_fields")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Champ.findAll", query="SELECT c FROM Champ c"),
})
//@formatter:on
public final class Champ extends Model implements Serializable
{

	/* Attributes */
    
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 50, nullable = false)
	private String nom;
	
	/* Constructors	 */
	
	public Champ() 
	{
	}

	/* Access */
	
	public int getId() 
	{
		return this.id;
	}

	public String getName() 
	{
		return this.nom;
	}
}