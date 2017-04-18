package model;

import java.io.Serializable;
import javax.persistence.*;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the journals database table.
 * 
 */
@Entity
@Table(name="journals")
//@formatter:off
@NamedQueries (value = {
        @NamedQuery(name="Journal.findAll", query="SELECT j FROM Journal j JOIN FETCH j.details d JOIN FETCH j.createur c")
})
//@formatter:on
public final class Journal implements Serializable
{
    /* Attributes */

    private static final long serialVersionUID = 1L;

    /** Id auto_incrémenté */
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="created_on")
	private Date dateCreation;

	@Column (name = "notes", columnDefinition = "TEXT")
	private String notes;

	/* Utilisateur ayant créé le journal */
	@ManyToOne (targetEntity = User.class, fetch = FetchType.LAZY)
	@JoinColumn(name="user_id")
	private User createur;
	
	/* List des détails composant le journal */
	@OneToMany (targetEntity = JournalDetail.class, fetch = FetchType.LAZY)
	@JoinColumn (name = "journal_id")
	private List<JournalDetail> details;

    /* Constructors */
	
	public Journal() {
	}	

    /* Methods */
    
    /* Access */

    public int getId()
    {
        return id;
    }

    public Date getDateCreation()
    {
        return dateCreation;
    }

    public String getNotes()
    {
        return notes;
    }

    public User getCreateur()
    {
        return createur;
    }

    public List<JournalDetail> getDetails()
    {
        return details;
    }
}