package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the journal_details database table.
 * 
 */
@Entity
@Table(name="journal_details")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="JournalDetail.findAll", query="SELECT j FROM JournalDetail j")
})
//@formatter:on
public class JournalDetail implements Serializable 
{
    /* Attributes */

	private static final long serialVersionUID = 1L;

	/** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Column(name="old_value", columnDefinition = "TEXT")
	private String ancienneValeur;

	@Column(name="prop_key", length = 30, nullable = false)
	private String propriete;

	@Column (name = "property", length = 30, nullable = false)
	private String typePropriete;

	@Column (name = "value", columnDefinition = "TEXT")
	private String nouvelleValeur;

    /* Constructors */
	public JournalDetail() 
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
     * @return the ancienneValeur
     */
    public String getAncienneValeur()
    {
        return ancienneValeur;
    }

    /**
     * @return the propriete
     */
    public String getPropriete()
    {
        return propriete;
    }

    /**
     * @return the typePropriete
     */
    public String getTypePropriete()
    {
        return typePropriete;
    }

    /**
     * @return the nouvelleValeur
     */
    public String getNouvelleValeur()
    {
        return nouvelleValeur;
    }
}