package control;

import java.io.File;
import java.util.HashMap;

import javax.persistence.EntityManager;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import application.Main;
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
    private String user, pass, driver, jarPath, url;

    /** COntroleur */
    private ListControl listControl;

    /* ---------- CONSTUCTORS ---------- */

    private XMLControl()
    {
        jarPath = Utilities.urlToFile(Utilities.getLocation(Main.class)).getParentFile().getPath();
        System.out.println(jarPath);
        listControl = ListControl.getInstance();
        listControl.setPath(jarPath);
        param = new Parametre();
    }

    private static class XMLControlHelper
    {
    	private XMLControlHelper() {}
        private static final XMLControl INSTANCE = new XMLControl();
    }

    public static XMLControl getInstance()
    {
        return XMLControlHelper.INSTANCE;
    }

    /* ---------- METHODS ---------- */

    /**
     * Permet de créer le DAO pour les incidents à partir d'un fichier de paramétrage.
     * 
     * @return
     *         Une instance de <code>DaoIncident</code>
     * @throws FunctionalException 
     */
    public void miseAJourParam()
    {
        recupParam();
        recupDefault();
        saveParamJPA();
    }

    /**
     * Récupère les paramètre de connexion depuis le fichier XML s'il est présent.
     * 
     * @return
     *         retourne vrai si le fichier est présent et que tous les paramètres sont bons.
     * @throws FunctionalException 
     */
    private void recupParam()
    {
    	// Creation objet correstondant au fichier XML
        File file = new File(jarPath + "\\param.xml"); 
        // boolean pour vérifier si le fichier XML existe
        boolean paramOK = false; 

        if (file.exists())
        {
            try
            {
                // Création objet Parametre depuis le XML
                JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                param = (Parametre) jaxbUnmarshaller.unmarshal(file);
                listControl.setParam(param);
                paramOK = true;
            }
            catch (JAXBException e)
            {
                MainScreen.createAlert(Severity.SEVERITY_INFO, null, null);
            }
        }
        else
        {
        	System.out.println("Le fichier XML n'existe pas");
        }

        // On vérifie que le XML contient bien tous les paramètres
        if (paramOK == true && (param.getDriver() == null || param.getPass() == null || param.getUrl() == null || param.getUser() == null))
        {
        	System.out.println("Certaines valeurs du XML sont nulles");
        }
        else if (paramOK)
        {
            // Affectation des paramètres du programme
            System.out.println("utilisation des valeurs du xml");
            driver = param.getDriver();
            user = param.getUser();
            pass = param.getPass();
            url = param.getUrl();
        }
    }

    /**
     * Initialise les paramètres par default.
     */
    private void recupDefault()
    {
    	// Creation map des propriétés
        HashMap<String, String> map = new HashMap<>(); 

        // On ajoute le paramètre dans la map s'il a été renseigné
        if (pass != null)
        {
        	map.put("javax.persistence.jdbc.password", pass);
        }
        if (driver != null)
        {
        	map.put("javax.persistence.jdbc.driver", driver);
        }
        if (url != null)
        {
        	map.put("javax.persistence.jdbc.url", url);
        }
        if (user != null)
        {
        	map.put("javax.persistence.jdbc.user", user);
        }

        // Creation de la connection et on récupère les paramètres
        listControl.createFactory(map);
        EntityManager manager = listControl.getFactory().getManager().getEm();
        driver = (String) manager.getProperties().get("javax.persistence.jdbc.driver");
        url = (String) manager.getProperties().get("javax.persistence.jdbc.url");
        user = (String) manager.getProperties().get("javax.persistence.jdbc.user");
        pass = (String) manager.getProperties().get("javax.persistence.jdbc.password");
    }

    /**
     * Sauvegarde les paramètres dans un XML.
     */
    private void saveParamJPA()
    {
        param.setDriver(driver);
        param.setPass(pass);
        param.setUrl(url);
        param.setUser(user);
        saveParametre();
    }

    public void saveParametre()
    {
        try
        {
            File file = new File(jarPath + "\\param.xml");
            JAXBContext jaxbContext = JAXBContext.newInstance(Parametre.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(param, file);

        }
        catch (JAXBException e)
        {
            e.printStackTrace();
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
}
