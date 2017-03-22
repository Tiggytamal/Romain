package model.system;

import java.io.Serializable;

public class ApplicationBDC implements Comparable<ApplicationBDC>, Serializable
{
    private static final long serialVersionUID = 1L;
    private String nom;
    private int bdc;
    private float taux;

	public ApplicationBDC(String nom)
    {
        this.nom = nom;
        bdc = 0;
        taux = 1.3f;
    }   

    @Override
    public int compareTo(ApplicationBDC o)
    {
        if (o == null)
            return 1;
        return getNom().compareTo(o.getNom());     
    }  

    /**
     * @return the bdc
     */
    public int getBdc()
    {
        return bdc;
    }

    /**
     * @param bdc the bdc to set
     */
    public void setBdc(int bdc)
    {
        this.bdc = bdc;
    }

    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom;
    }
    
    public float getTaux() {
		return taux;
	}

	public void setTaux(float taux) {
		this.taux = taux;
	}
}