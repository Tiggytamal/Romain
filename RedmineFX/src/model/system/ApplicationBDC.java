package model.system;

import java.io.Serializable;

import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import model.xml.ApplicationBDCXML;

public class ApplicationBDC implements Comparable<ApplicationBDC>, Serializable
{
    /* ---------- ATTIBUTES ---------- */

    private static final long serialVersionUID = 1L;
    private SimpleStringProperty nom;
    private SimpleIntegerProperty bdc;
    private SimpleFloatProperty taux;

    /* ---------- CONSTUCTORS ---------- */

	public ApplicationBDC(String nom)
    {
	    this.nom = new SimpleStringProperty(nom);
        this.bdc = new SimpleIntegerProperty(0);
        this.taux = new SimpleFloatProperty(1.3f);
    }
	
	public ApplicationBDC(ApplicationBDCXML appliXML)
    {
	    this.nom = new SimpleStringProperty(appliXML.getNom());
        this.bdc = new SimpleIntegerProperty(appliXML.getBdc());
        this.taux = new SimpleFloatProperty(appliXML.getTaux());
    }

    /* ---------- METHODS ---------- */

    @Override
    public int compareTo(ApplicationBDC o)
    {
        if (o == null)
            return 1;
        return getNom().compareTo(o.getNom());     
    }  
    
    /* ---------- ACCESS ---------- */
    
    /**
     * @param nom the nom to set
     */
    public void setNom(String nom)
    {
        this.nom.set(nom);
    }
    
    /**
     * @return the nom
     */
    public String getNom()
    {
        return nom.get();
    }


    /**
     * @param bdc the bdc to set
     */
    public void setBdc(int bdc)
    {
        this.bdc.set(bdc);
    }

    /**
     * @return the bdc
     */
    public String getBdc()
    {
        return String.valueOf(bdc.get());
    }

    /**
     * @param taux the taux to set
     */
    public void setTaux(float taux)
    {
        this.taux.set(taux);
    }
    
    /**
     * @return the taux
     */
    public String getTaux()
    {
        return String.valueOf(taux.get());
    }
}