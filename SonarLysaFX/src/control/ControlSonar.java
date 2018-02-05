package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import model.xml.Application;
import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Vue;
import utilities.Statics;

public class ControlSonar
{

	/*---------- ATTRIBUTS ----------*/

	private final SonarAPI api;
	private final ControlXML controlXML;
	private static final Logger logSansApp = LogManager.getLogger("sansapp-log");
	private static final Logger loginconnue = LogManager.getLogger("inconnue-log");
	private static final Logger lognonlistee = LogManager.getLogger("nonlistee-log");

	/*---------- CONSTRUCTEURS ----------*/
	
	/**
	 * Constructeur de base pour l'api Sonar, avec le mot de passe et le l'identifiant de 
	 * @param name
	 * @param password
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws InvalidFormatException 
	 */
	public ControlSonar(String name, String password) throws InvalidFormatException, JAXBException, IOException
	{
		api = new SonarAPI(Statics.URI, name, password);
		controlXML = new ControlXML();
		controlXML.recuprerParamXML();
	}
	
	/**
	 * Instanciation sans paramètre qui permet de récuperer le contrôleur Sonar de test.
	 * @throws IOException 
	 * @throws JAXBException 
	 * @throws InvalidFormatException 
	 */
	public ControlSonar() throws InvalidFormatException, JAXBException, IOException
	{
		api = SonarAPI.getInstanceTest();
		controlXML = new ControlXML();
		controlXML.recuprerParamXML();
	}

	/*---------- METHODES PUBLIQUES ----------*/
	
	public void creerVueParApplication()
	{	
		// Création de la liste des composants par application
		Map<String, List<Projet>> mapApplication = controlerSonarQube();
				
		// Parcours de la liste pour créer chaque vue applicative avec ses composants
		for (Map.Entry<String, List<Projet>> entry : mapApplication.entrySet())
		{
			// Création de la vue principale
			Vue vue = new Vue();
			vue.setName("APPLI MASTER " + entry.getKey());
			vue.setKey("APPMASTERAPP" + entry.getKey());
			vue.setDescription("Liste des composants de l'application " + entry.getKey());
			api.supprimerVue(vue);
			api.creerVue(vue);
			api.ajouterSousProjets(entry.getValue(), vue);		
		}		
	}
	
	public Map<String, List<Projet>> controlerSonarQube()
	{
		// Récupération des composants Sonar
		Map<String, Projet> mapProjets = recupererComposantsSonar();
				
		// Création de la liste des composants par application
		return creerMapApplication(mapProjets);
	}
	
	/**
	 * Récupère tous les lots créer dans Sonar.
	 * 
	 * @return
	 */
	public Map<String, Vue> recupererLotsSonarQube()
	{
		Map<String, Vue> map = new HashMap<>();
		List<Vue> views = api.getVues();
		for (final Vue view : views)
		{
			if (view.getName().startsWith("Lot "))
			{
				map.put(view.getName().substring(4), view);
			}
		}
		return map;
	}

	/**
	 * Permet de créer une vue de tous les composants qui ont été mise en production un mois donné.
	 * 
	 * @param mois
	 * @param annee
	 * @param file
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public void creerVueSonarMensuelle(String mois, String annee, File file) throws InvalidFormatException, IOException
	{
		// Récupération des lots depuis le fichier Excel
		List<Integer> listeLot = recuperationListeLotExcel(file);
		
		// Récupératoin de tous les lots SonarQube
		Map<String, Vue> mapQube = recupererLotsSonarQube();
		
		// Comparaison des deux pour ne prendre que les lots existants déjà dans Sonar
		List<Vue> listeViewAjour = new ArrayList<>();
		for (Map.Entry<String, Vue> entry : mapQube.entrySet())
		{
			if (listeLot.contains(Integer.valueOf(entry.getKey())))
				listeViewAjour.add(entry.getValue());
		}
		
		// Création de la vue principale
		Vue vue = new Vue();
		vue.setName(new StringBuilder("MEP ").append(mois).append(Statics.SPACE).append(annee).toString());
		vue.setKey(new StringBuilder("MEPMEP").append(mois).append(annee).append("Key").toString());
		vue.setDescription(new StringBuilder("Vue des lots mis en production pendant le mois de ").append(mois).append(Statics.SPACE).append(annee).toString());
		api.creerVue(vue);
		
		// Ajout des sous-vue
		api.ajouterSousVues(listeViewAjour, vue);
	}

	/**
	 * Lance la mise à jour des vues dans SonarQube. Indispenssable après la création d'une nouvelle vue.
	 */
	public void majVues()
	{
		api.majVues();
	}

	/*---------- METHODES PRIVEES ----------*/

	/**
	 * Récupère la liste des lots envoyées en production depuis un fichier Excel 
	 * @param file
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private List<Integer> recuperationListeLotExcel(File file) throws InvalidFormatException, IOException
	{
		Workbook wb = WorkbookFactory.create(file);
		Sheet sheet = wb.getSheetAt(0);
		List<Integer> listeLot = new ArrayList<>();
		int colLot = 0;

		// Récupération de la colonne ou les numéro de lot sont affichés.
		for (Cell cell : sheet.getRow(0))
		{
			if (cell.getCellTypeEnum() == CellType.STRING && cell.getStringCellValue().equals("Lot"))
			{
				colLot = cell.getColumnIndex();
			}
		}

		// parcours de la feuille Excel pour récupérer tous les lots sauf la première
		// ligne
		for (int i = 1; i < sheet.getLastRowNum(); i++)
		{
			listeLot.add((int) sheet.getRow(i).getCell(colLot).getNumericCellValue());
		}
		wb.close();
		return listeLot;
	}
	
	/**
	 * Permet de récupérer la dernière version de chaque composants créés dans Sonar
	 * @return
	 */
	private Map<String, Projet> recupererComposantsSonar()
	{
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets = api.getComposants();
		
		// Triage ascendant de la liste par nom de projet 
		projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));
		
		// Création de la regex pour retirer les numéros de version des composants
		Pattern pattern = Pattern.compile("^\\D*");
		
		// Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine de caractères créées par la regex comme clef dans la map.
		// Les compossant étant triès par ordre alphabétique, on va écraser tous les composants qui ont un numéro de version obsolète.
		Map<String, Projet> retour = new HashMap<>();
		
		for (Projet projet : projets)
		{
			Matcher matcher = pattern.matcher(projet.getNom());
			if (matcher.find())
			{				
				retour.put(matcher.group(0), projet);
			}
		}
		return retour;		
	}
	
	/**
	 * Crée une map de toutes les apllications dans Sonar avec pour chacunes la liste des composants liés.
	 * 
	 * @param mapProjets
	 * @return
	 */
	private HashMap<String, List<Projet>> creerMapApplication(Map<String, Projet> mapProjets)
	{
		HashMap<String, List<Projet>> mapApplications = new HashMap<>();
		
		for (Projet projet : mapProjets.values())
		{
			Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] {"application"});
			if (!composant.getListeMeriques().isEmpty())
			{					
				String application = composant.getListeMeriques().get(0).getValue().trim().toUpperCase();		

				if(!testAppli(application, composant.getNom()))
				{
					continue;
				}
				
				if(mapApplications.keySet().contains(application))
				{
					mapApplications.get(application).add(projet);
				}
				else
				{
					List<Projet> liste = new ArrayList<>();
					liste.add(projet);
					mapApplications.put(application, liste);
				}			
			}
			else
			{
				logSansApp.warn(composant.getNom() + " - " + composant.getKey());
			}
		}
		return mapApplications;
	}
	
	private boolean testAppli(String application, String nom)
	{
		if(application.equals(Statics.INCONNUE))
		{
			loginconnue.warn(nom);
			return false;
		}
		
		Map<String, Application> vraiesApplis = controlXML.getMapApplis();
		
		if(vraiesApplis.keySet().contains(application))
		{
			if(vraiesApplis.get(application).isActif())
			{
				return true;
			}
			lognonlistee.warn("Application obsolète : " + application + " - composant : " + nom);
			return false;
		}
		lognonlistee.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);				
		return false;
	}
	
	/*---------- ACCESSEURS ----------*/
}