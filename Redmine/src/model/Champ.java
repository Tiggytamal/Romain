package model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;


/**
 * The persistent class for the custom_fields database table.
 * 
 */
@Entity
@Table(name="custom_fields")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Champ.findAll", query="SELECT c FROM Champ c JOIN FETCH c.valeurs v")
})
//@formatter:on
public class Champ implements Serializable
{

	/* Attributes */
    
    private static final long serialVersionUID = 1L;
    
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column (name = "name", length = 50, nullable = false)
	private String nom;
	
	@OneToMany (targetEntity = Valeur.class, mappedBy = "champ", fetch = FetchType.LAZY)
	private List<Valeur> valeurs;
	
	
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