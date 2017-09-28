package control;

import java.io.File;
import java.util.HashMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import application.Main;
import model.xml.BanqueXML;
import model.xml.Parametre;
import utilities.Utilities;
import utilities.enums.Severity;
import view.MainScreen;

public class XMLControl
{
    /* ---------- ATTIBUTES ---------- */

    /** Objet contenant les paramètres du programme venant du xml. */
    private Parametre param;

    /** Paramètres du programme. */
    private String jarPath;
    
    private HashMap<String,BanqueXML> listeBanques;

    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        param = new Parametre();
        listeBanques = new HashMap<>();
        recupParam();
    }

    private static class XMLControlHelper
    {
        private static final XMLControl INSTANCE = new XMLControl();
    }

    public static XMLControl getInstance()
    {
        return XMLControlHelper.INSTANCE;
    }

    /* ---------- METHODS ---------- */


    /**
     * Récupère les paramètre de connexion depuis le fichier XML s'il est présent.
     * 
     * @return
     *         retourne vrai si le fichier est présent et que tous les paramètres sont bons.
     * @throws FunctionalException 
     */
    public void recupParam()
    {
        File file = new File(jarPath + "\\param.xml"); // Creation objet correstondant au fichier XML
        boolean paramOK = false; // boolean pour vérifier si le fichier XML existe

        if (file.exists())
        {
            try
            {
                // Création objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
                paramOK = true;
            }
            catch (JAXBException e)
            {
                MainScreen.createAlert(Severity.SEVERITY_INFO, null, null);
            }
        }
        else
            System.out.println("Le fichier XML n'existe pas");

        // On vérifie que le XML contient bien tous les paramètres
        if (paramOK == true)
        {
            // Affectation des paramètres du programme
            for (BanqueXML banque : param.getListBanqueXML())
            {
                listeBanques.put(banque.getNom(), banque);
            }
            
        }
    }

    /* ---------- ACCESS ---------- */

    /**
     * @return the jarPath
     */
    public String getJarPath()
    {
        return jarPath;
    }   

    /**
     * @return the param
     */
    public Parametre getParam()
    {
        return param;
    }

    /**
     * @return the listeBanques
     */
    public HashMap<String, BanqueXML> getListeBanques()
    {
        return listeBanques;
    }
}