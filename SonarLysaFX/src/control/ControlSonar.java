package control;

import static control.view.MainScreen.param;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import junit.control.ControlSonarTest;
import model.Anomalie;
import model.LotSuiviPic;
import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Status;
import sonarapi.model.Vue;
import utilities.DateConvert;
import utilities.Statics;
import utilities.Utilities;

public class ControlSonar
{

	/*---------- ATTRIBUTS ----------*/

	private final SonarAPI api;
	private static final String FICHIERANOMALIES = "d:\\Suivi_Quality_Gate.xlsx";

	/*---------- CONSTRUCTEURS ----------*/

	/**
	 * Constructeur de base pour l'api Sonar, avec le mot de passe et le l'identifiant
	 * 
	 * @param name
	 * @param password
	 */
	public ControlSonar(String name, String password)
	{
		api = new SonarAPI(Statics.URI, name, password);
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
			Vue vue = creerVue("APPMASTERAPP" + entry.getKey(), "APPLI MASTER " + entry.getKey(), "Liste des composants de l'application " + entry.getKey(), false);
			api.ajouterSousProjets(entry.getValue(), vue);
		}
	}

	/**
	 * Remonte les composants par application de SonarQube et créée les logs sur les defaults.
	 * 
	 * @return map des composants SonarQube par application
	 */
	public Map<String, List<Projet>> controlerSonarQube()
	{
		// Récupération des composants Sonar
		Map<String, Projet> mapProjets = recupererComposantsSonar();
		return creerMapApplication(mapProjets);
	}

	public void creerVueProduction(File file) throws InvalidFormatException, IOException
	{
		ControlPic excel = new ControlPic(file);
		Map<LocalDate, List<Vue>> mapLot = excel.recupLotsExcelPourMEP(recupererLotsSonarQube());
		excel.close();
		if (mapLot.size() == 1)
		{
			creerVueMensuelle(mapLot);
		}
		else if (mapLot.size() == 3)
		{
			creerVueTrimestrielle(mapLot);
		}
	}


	@SuppressWarnings("unchecked")
	public void creerVuesQGErreur() throws InvalidFormatException, IOException
	{
		// 1. Récupération des données depuis les fichiers Excel.

		// Fichier des lots édition
		Map<String, LotSuiviPic> lotsPIC = param.getLotsPic();

		// 2. Récupération des lots Sonar en erreur.
		Map<String, Set<String>> mapLots;
		
		List<String> LotsSecurite = new ArrayList<>();
		
		if (ControlSonarTest.deser)
		{
			mapLots = Utilities.deserialisation("d:\\lotsSonar.ser", HashMap.class);
		}
		else
		{
			mapLots = lotSonarQGError(new String[] {"13", "14"}, LotsSecurite);
			Utilities.serialisation("d:\\lotsSecurite.ser", LotsSecurite);
			Utilities.serialisation("d:\\lotsSonar.ser", mapLots);
		}
		
		// 3. Supression des lots déjà créés et création des feuille Excel avec les nouvelles erreurs
		majFichierAnomalies(lotsPIC, mapLots, LotsSecurite, new File(FICHIERANOMALIES));

		// 4. Création des vues
		for (Map.Entry<String, Set<String>> entry : mapLots.entrySet())
		{
			// Création de la vue et envoie vers SonarQube
			Vue vueParent = creerVue("LotsErreurKey" + entry.getKey(), "Lots en erreur - Edition " + entry.getKey(), "Vue regroupant tous les lots avec des composants en erreur", true);

			for (String lot : entry.getValue())
			{
				Vue vue = new Vue();
				vue.setKey("view_lot_" + lot);
				vue.setName("Lot " + lot);
				// Ajout des sous-vue
				api.ajouterSousVue(vue, vueParent);
			}
		}
	}

	public void creerVuesDatastage()
	{
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets = api.getComposants();
		Vue vue = creerVue("DSDataStageListeKey", "Liste Composants Datastage", "Vue regroupant tous les composants Datastage", true);
		for (Projet projet : projets)
		{
			if (projet.getNom().startsWith("Composant DS_"))
				api.ajouterProjet(projet, vue);
		}
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
	 * Récupère tous les lots créés dans Sonar.
	 * 
	 * @return
	 */
	private Map<String, Vue> recupererLotsSonarQube()
	{
		Map<String, Vue> map = new HashMap<>();
		List<Vue> views = api.getVues();
		for (Vue view : views)
		{
			if (view.getName().startsWith("Lot "))
			{
				map.put(view.getName().substring(4), view);
			}
		}
		return map;
	}

	/**
	 * Permet de récupérer la dernière version de chaque composants créés dans Sonar
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Projet> recupererComposantsSonar()
	{
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets;

		if (ControlSonarTest.deser)
		{
			projets = Utilities.deserialisation("d:\\composants.ser", List.class);
		}
		else
		{
			projets = api.getComposants();
			Utilities.serialisation("d:\\composants.ser", projets);
		}

		// Triage ascendant de la liste par nom de projet
		projets.sort((o1, o2) -> o1.getNom().compareTo(o2.getNom()));

		// Création de la regex pour retirer les numéros de version des composants
		Pattern pattern = Pattern.compile("^\\D*");

		// Création de la map de retour et parcours de la liste des projets pour remplir celle-ci. On utilise la chaine
		// de caractères créées par la regex comme clef dans la map.
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
	 * Permet de récupérer les composants de Sonar triés par version
	 * 
	 * @return
	 */
	private Map<String, List<Projet>> recupererComposantsSonarVersion(String[] versions)
	{
		// Appel du webservice pour remonter tous les composants
		List<Projet> projets = api.getComposants();

		// Création de la map de retour en utilisant les versions données
		Map<String, List<Projet>> retour = new HashMap<>();

		for (String version : versions)
		{
			retour.put(version, new ArrayList<>());
		}

		for (Projet projet : projets)
		{
			for (String version : versions)
			{
				if (projet.getNom().endsWith(version))
				{
					retour.get(version).add(projet);
				}
			}
		}
		return retour;
	}

	/**
	 * Récupère tous les composants Sonar des versions choisies avec une qualityGate en erreur.<br>
	 * la clef de la map correspond à la version, et la valeur, à la liste des lots en erreur de cette version.
	 * 
	 * @param versions
	 * @return
	 */
	private Map<String, Set<String>> lotSonarQGError(String[] versions, List<String> lotSecurite)
	{
		// Récupération des composants Sonar selon les version demandées
		Map<String, List<Projet>> mapProjets = recupererComposantsSonarVersion(versions);

		// Création de la map de retour
		HashMap<String, Set<String>> retour = new HashMap<>();

		// Itération sur les composants pour remplir la map de retour avec les lot en erreur par version
		for (Map.Entry<String, List<Projet>> entry : mapProjets.entrySet())
		{
			retour.put(entry.getKey(), new TreeSet<>());
			
			// Iteration sur la liste des projets
			for (Projet projet : entry.getValue())
			{
				// Récupération du composant
				Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] {"lot", "alert_status"});

				// Récupération depuis la map des métriques du numéro de lot et du status de la Quality Gate
				Map<String, String> metriques = composant.getMapMetriques();
				String lot = metriques.get("lot");
				String alert = metriques.get("alert_status");

				// Si le lot a un Quality Gate en Erreur, on le rajoute à la liste et on contrôle aussi les erreurs de sécurité. 
				// S'il y en a on le rajoute aussi à la liste des lots avec des problèmesde sécurité.
				if (alert != null && Status.getStatus(alert) == Status.ERROR && lot != null && !lot.isEmpty())
				{
					retour.get(entry.getKey()).add(lot);
					int securite = api.getSecuriteComposant(projet.getKey());
					if (securite > 0)
						lotSecurite.add(lot);						
				}
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
		// Initialisation de la map
		HashMap<String, List<Projet>> mapApplications = new HashMap<>();

		// Itération sur la liste des projets
		for (Projet projet : mapProjets.values())
		{
			// Récupération du code application
			Composant composant = api.getMetriquesComposant(projet.getKey(), new String[] { "application" });

			// Test si la liste est vide, cela veut dire que le projet n'a pas de code application.
			if (!composant.getMetriques().isEmpty())
			{
				String application = composant.getMetriques().get(0).getValue().trim().toUpperCase();

				// Si l'application n'est pas dans la PIC, on continue au projet suivant.
				if (!testAppli(application, projet.getNom()))
				{
					continue;
				}

				// Mise à jour de la map de retour avec en clef, le code application et en valeur : la liste des projets liés.
				if (mapApplications.keySet().contains(application))
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
				Statics.logSansApp.warn("Application non renseignée - Composant : " + projet.getNom());
			}
		}
		return mapApplications;
	}

	/**
	 * Vérifie qu'une application d'un composant Sonar est présente dans la liste des applications de la PIC.
	 * 
	 * @param application
	 *            Application enregistrée pour le composant dans Sonar.
	 * @param nom
	 *            Nom du composant Sonar.
	 */
	private boolean testAppli(String application, String nom)
	{
		if (application.equals(Statics.INCONNUE))
		{
		    Statics.loginconnue.warn("Application : INCONNUE - Composant : " + nom);
			return false;
		}

		Map<String, Boolean> vraiesApplis = param.getMapApplis();

		if (vraiesApplis.keySet().contains(application))
		{
			if (vraiesApplis.get(application))
			{
				return true;
			}
			Statics.lognonlistee.warn("Application obsolète : " + application + " - composant : " + nom);
			return false;
		}
		Statics.lognonlistee.warn("Application n'existant pas dans le référenciel : " + application + " - composant : " + nom);
		return false;
	}

	/**
	 * Crée la vue Sonar pour une recherche trimetrielle des composants mis en production .
	 * 
	 * @param mapLot
	 */
	private void creerVueTrimestrielle(Map<LocalDate, List<Vue>> mapLot)
	{
		// Création des variables. Transfert de la HashMap dans une TreeMap pour trier les dates.
		List<Vue> lotsTotal = new ArrayList<>();
		Map<LocalDate, List<Vue>> treeLot = new TreeMap<>(mapLot);
		Iterator<Entry<LocalDate, List<Vue>>> iter = treeLot.entrySet().iterator();
		StringBuilder builderNom = new StringBuilder();
		StringBuilder builderDate = new StringBuilder();
		List<String> dates = new ArrayList<>();

		// Itération sur la map pour regrouper tous les lots dans la même liste.
		// Crée le nom du fichier sous la forme TEMP MMM-MMM-MMM yyyy(-yyyy)
		while (iter.hasNext())
		{
			Entry<LocalDate, List<Vue>> entry = iter.next();
			
			// Regroupe tous les lots dans la même liste.
			lotsTotal.addAll(entry.getValue());
			LocalDate clef = entry.getKey();

			builderNom.append(DateConvert.dateFrancais(clef, "MMM"));
			if (iter.hasNext())
			{
				builderNom.append("-");
			}

			String date = DateConvert.dateFrancais(clef, "yyyy");
			if (!dates.contains(date))
			{
				dates.add(date);
				builderDate.append(date);
				if (iter.hasNext())
				{
					builderDate.append("-");
				}
			}
		}

		if (builderDate.charAt(builderDate.length() - 1) == '-')
		{
			builderDate.deleteCharAt(builderDate.length() - 1);
		}

		String nom = builderNom.toString();
		String date = builderDate.toString();

		// Création de la vue et envoie vers SonarQube
		Vue vue = creerVue(new StringBuilder("MEPMEP").append(nom).append(date).toString().replace("é", "e").replace("û", "u"),
		        new StringBuilder("TEP ").append(nom).append(Statics.SPACE).append(date).toString(),
		        new StringBuilder("Vue des lots mis en production pendant les mois de ").append(nom).append(Statics.SPACE).append(date).toString(), true);

		// Ajout des sous-vue
		api.ajouterSousVues(lotsTotal, vue);
	}

	/**
	 * Crée la vue Sonar pour une recherche mensuelle des composants mis en production .
	 * 
	 * @param mapLot
	 */
	private void creerVueMensuelle(final Map<LocalDate, List<Vue>> mapLot)
	{
		Iterator<Entry<LocalDate, List<Vue>>> iter = mapLot.entrySet().iterator();
		Entry<LocalDate, List<Vue>> entry = iter.next();
		String nomVue = new StringBuilder("MEP ").append(DateConvert.dateFrancais(entry.getKey(), "MMM yyyy")).toString();

		// Création de la vue principale
		Vue vue = creerVue(new StringBuilder("MEP").append(DateConvert.dateFrancais(entry.getKey(), "MMMyyyy")).append("Key").toString(), nomVue,
		        new StringBuilder("Vue des lots mis en production pendant le mois de ").append(entry.getKey()).toString(), true);

		// Ajout des sous-vue
		api.ajouterSousVues(entry.getValue(), vue);
	}

	/**
	 * Crée une vue dans Sonar avec suppression ou non de la vue précédente.
	 * 
	 * @param key
	 * @param name
	 * @param description
	 * @param suppression
	 * @return
	 */
	private Vue creerVue(String key, String name, String description, boolean suppression)
	{
		// Contrôle
		if (key == null || key.isEmpty() || name == null || name.isEmpty())
		{
			throw new IllegalArgumentException("Le nom et la clef de la vue sont obligatoires");
		}

		// Création de la vue
		Vue vue = new Vue();
		vue.setKey(key);
		vue.setName(name);

		// Ajout de la description si elle est valorisée
		if (description != null)
		{
			vue.setDescription(description);
		}

		// Suppresison de la vue précedente
		if (suppression)
		{
			api.supprimerVue(vue);
		}

		// Appel de l'API Sonar
		api.creerVue(vue);

		return vue;
	}

	/**
	 * Permet de mettre à jour le fichier des anomalies Sonar, en allant chercher les nouvelles dans Sonar et en vérifiant celles qui ne sont plus d'actualité.
	 * 
	 * @param lotsPIC
	 *            Fichier excel d'extraction de la PIC de tous les lots.
	 * @param mapLots
	 *            map des lots Sonar avec une quality Gate en erreur
	 * @param lotsSecurite 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	private void majFichierAnomalies(Map<String, LotSuiviPic> lotsPIC, Map<String, Set<String>> mapLots, List<String> lotsSecurite, File file) throws InvalidFormatException, IOException
	{
		// Controleur
		ControlAno controlAno = new ControlAno(file);

		// Lecture du fichier pour remonter les anomalies en cours.
		List<Anomalie> listeLotenAno = controlAno.listAnomaliesSurLotsCrees();
		
		// Création de la liste des lots
		List<String> numeroslots = new ArrayList<>();
		for (Anomalie ano : listeLotenAno)
		{
			String string = ano.getLot();
			if (string.startsWith("Lot "))
				string = string.substring(4);
			numeroslots.add(string);
		}
		
		// Liste des anomalies à ajouter après traitement
		List<Anomalie> anoAajouter = new ArrayList<>();
		
		// Iteration sur les lots du fichier des anomalies en cours pour resortir celles qui n'ont plus une Quality Gate bloquante.
		Set<String> lotsEnErreur = new TreeSet<>();
		for (Set<String> value : mapLots.values())
		{
			lotsEnErreur.addAll(value);
		}

		// Itération sur les lots en erreurs venant de Sonar pour chaque version de composants (13, 14, ...)
		for (Entry<String, Set<String>> entry : mapLots.entrySet())
		{
			List<Anomalie> anoACreer = new ArrayList<>();
			Iterator<String> iter = entry.getValue().iterator();

			while (iter.hasNext())
			{
				String numeroLot = iter.next();
				// Si le lot est déjà dans la liste des anomalies, on le retire de la liste.
				if (numeroslots.contains(numeroLot))
				{
					iter.remove();
				}
				else
				{
					// Sinon on va chercher les informations de ce lot dans le fichier des lots de la PIC. Si on ne le trouve pas, il faudra mettre à jour ce fichier
					LotSuiviPic lot = lotsPIC.get(numeroLot);
					if (lot == null)
					{
						Statics.lognonlistee.warn("Mettre à jour le fichier Pic - Lots : " + numeroLot + " non listé");
						continue;
					}
					Anomalie ano = new Anomalie(lot);
					anoACreer.add(ano);
				}
			}

			// Mise à jour de la feuille des anomalies pour chaque version de composants
			anoAajouter.addAll(controlAno.createSheetError(Utilities.transcoEdition(entry.getKey()), anoACreer));
		}
		
		// Mis à jour de la feuille principale
		controlAno.majNouvellesAno(listeLotenAno, anoAajouter, lotsEnErreur, lotsSecurite);
		
		controlAno.close();
	}

	/*---------- ACCESSEURS ----------*/
}