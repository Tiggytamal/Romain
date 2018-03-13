package model;

import java.beans.Transient;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import model.enums.TypeFichier;
import utilities.Statics;

@XmlRootElement
public class FichiersXML implements XML
{
	/*---------- ATTRIBUTS ----------*/

	private List<Application> listeApplications;
	private Map<String, InfoClarity> mapClarity; 
	private Map<String, LotSuiviPic> lotsPic;
	private Map<String, Boolean> mapApplis;
	private Map<TypeFichier, String> dateMaj;
	
	public static final String NOMFICHIER = "\\fichiers.xml";

	/*---------- CONSTRUCTEURS ----------*/
	
    public FichiersXML()
	{
	    listeApplications = new ArrayList<>();
	    mapClarity = new HashMap<>();
	    lotsPic = new HashMap<>();
	    mapApplis = new HashMap<>();
	    dateMaj = new EnumMap<>(TypeFichier.class);
	}
    
    /*---------- METHODES PUBLIQUES ----------*/
    
    @Override
    public File getFile()
    {
        return new File (Statics.JARPATH + NOMFICHIER);
    }
    
    @Override
    public String controleDonnees()
    {
        StringBuilder builder = new StringBuilder("Chargement fichiers Excel :").append(Statics.NL);
        boolean manquant = false;

        // Contrôle lots Pic
        if (lotsPic.isEmpty())
        {
            builder.append("Données des lots Pic manquantes.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Lots Pics chargés. Dernière Maj : ").append(dateMaj.get(TypeFichier.LOTSPICS)).append(Statics.NL);
        }

        // Contrôle liste application
        if (listeApplications.isEmpty())
        {
            builder.append("Liste des apllications manquante.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Liste des apllications chargée. Dernière Maj : ").append(dateMaj.get(TypeFichier.APPS)).append(Statics.NL);
        }

        // Contrôle Referentiel Clarity
        if (mapClarity.isEmpty())
        {
            builder.append("Informations referentiel Clarity manquantes.").append(Statics.NL);
            manquant = true;
        }
        else
        {
            builder.append("Referentiel Clarity chargé. Dernière Maj : ").append(dateMaj.get(TypeFichier.CLARITY)).append(Statics.NL);
        }

        if (manquant)
            builder.append("Merci de recharger le(s) fichier(s) de paramétrage");

        return builder.append(Statics.NL).toString();
    }
	
	/*---------- ACCESSEURS ----------*/

	@XmlElementWrapper
	@XmlElement(name = "listeApps", required = false)
	public List<Application> getListeApplications()
	{
		return listeApplications;
	}
    
    @XmlElementWrapper
    @XmlElement(name = "mapClarity", required = false)
    public Map<String, InfoClarity> getMapClarity()
	{
		return mapClarity;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "maplotsPic", required = false)
	public Map<String, LotSuiviPic> getLotsPic()
	{
		return lotsPic;
	}
	
    @XmlElementWrapper
    @XmlElement(name = "dateMaj", required = false)
    public Map<TypeFichier, String> getDateMaj()
    {
        return dateMaj;
    }
	
	/**
	 * Permet de remonter la liste des applications sous forme d'une map (clef = nom application / valeur = etat)
	 * @return
	 */
	@Transient
	public Map<String, Boolean> getMapApplis()
	{
	    if (mapApplis.isEmpty())
	    {   
            for (Application app : listeApplications)
            {
                mapApplis.put(app.getNom(), app.isActif());
            }
	    }	    
        return mapApplis;
	}
	
	/**
	 * @param clef
	 */
	public void setDateFichier(TypeFichier fichier)
	{
		dateMaj.put(fichier, LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy",Locale.FRANCE)));
	}
}