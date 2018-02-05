package sonarapi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.Future;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import sonarapi.model.AjouterProjet;
import sonarapi.model.AjouterVueLocale;
import sonarapi.model.Clef;
import sonarapi.model.Composant;
import sonarapi.model.ModeleSonar;
import sonarapi.model.Parametre;
import sonarapi.model.Projet;
import sonarapi.model.Retour;
import sonarapi.model.Validation;
import sonarapi.model.Vue;
import utilities.Statics;

public class SonarAPI
{

	/*---------- ATTRIBUTS ----------*/

	private final WebTarget webTarget;
	private final String codeUser;
	private static final Logger logger = LogManager.getLogger("complet.log");

	private static final String AUTHORIZATION = "Authorization";

	/*---------- CONSTRUCTEURS ----------*/

	/**
	 * Création d'une instance de l'API pour accéder à SonarCube. La méthode est
	 * privée.
	 *
	 * @param url
	 *            Url du serveur SonarQube
	 * @param user
	 *            id de l'utilisateur
	 * @param password
	 *            mot de passe de l'utilisateur
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public SonarAPI(final String url, final String user, final String password)
	{
		webTarget = ClientBuilder.newClient().target(url);
		StringBuilder builder = new StringBuilder(user);
		builder.append(":");
		builder.append(password);
		codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
	}

	/**
	 *
	 * @param url
	 *            Url du serveur SonarQube
	 * @param user
	 *            id de l'utilisateur
	 * @param password
	 *            mot de passe de l'utilisateur
	 * @return Une instance singleton de l'api
	 * @throws IOException 
	 * @throws SecurityException 
	 */
	public static SonarAPI getInstanceTest()
	{
		return new SonarAPI(Statics.URITEST, "admin", "admin");
	}


	/*---------- METHODES PUBLIQUES GET ----------*/

	/**
	 * Permet de remonter toutes les vues déjà créées
	 * 
	 * @return
	 */
	public List<Vue> getVues()
	{
		final Response response = appelWebserviceGET("api/views/list");
		
		if (response.getStatus() == Status.OK.getStatusCode())
		{			
			logger.info("Vues retournées");
			return response.readEntity(Retour.class).getListeVues();
		}
		else
		{
			logger.error("Impossible de retourner les vues depuis Sonar = " + "api/views/list");
		}
		
		return new ArrayList<>();
	}

	/**
	 * Permet de vérifier si l'utilisateur a bien les accès à SonarQube
	 * 
	 * @return true si l'utilisateur a les accès.
	 */
	public boolean verificationUtilisateur()
	{
		final Response response = appelWebserviceGET("api/authentication/validate");
		
		if (response.getStatus() == Status.OK.getStatusCode())
		{
			logger.info("Utilisateur OK");
			return response.readEntity(Validation.class).isValid();
		}
		else
		{
			logger.info("Utilisateur KO");
		}
		
		return false;
	}
	
	/**
	 * Remonte les données métriques spécifiques à un composant
	 * 
	 * @param composantKey
	 * 					clé du composant dans la base SonarQube
	 * @param metricKeys
	 * 					clé des métriques désirées (issues, bugs, vulnerabilitie, etc..)
	 * @return
	 * 			un objet de type {@link Composant} avec toutes les informations sur celui-ci
	 */
	public Composant getMetriquesComposant(final String composantKey, String[] metricKeys)
	{		
		// 1. Création des paramètres
		Parametre paramComposant = new Parametre("componentKey", composantKey);
		
		Parametre paramMetrics = new Parametre();
		paramMetrics.setClef("metricKeys");
		StringBuilder valeur = new StringBuilder();
		for (int i = 0; i < metricKeys.length; i++)
		{
			valeur.append(metricKeys[i]);
			if (i+1 < metricKeys.length)
			{
				valeur.append(",");
			}
		}
		paramMetrics.setValeur(valeur.toString());
		
		// 2. appel du webservices
		final Response response = appelWebserviceGET("api/measures/component", paramComposant, paramMetrics);
		
		// 3. Test du retour et renvoie du composant si ok.
		if (response.getStatus() == Status.OK.getStatusCode())
		{
			Composant composant = response.readEntity(Retour.class).getComponent();
			return composant;
		}
		
		return null;
	}
	
	/**
	 * Retourne tous les composants présents dans SonarQube
	 * @return
	 */
	public List<Projet> getComposants()
	{
		Parametre param = new Parametre("search", "composant");
		final Response response = appelWebserviceGET("api/projects/index", param);
		
		if (response.getStatus() == Status.OK.getStatusCode())
		{
			logger.info("Récupération de la liste des composants OK");
			return response.readEntity(new GenericType<List<Projet>>() {});			
		}
		return new ArrayList<>();
	}

	/*---------- METHODES PUBLIQUES POST ----------*/

	/**
	 * Crée une vue dans SonarQube
	 * 
	 * @param vue
	 * @return
	 */
	public boolean creerVue(Vue vue)
	{
		Response response = appelWebservicePOST("api/views/create", vue);
		System.out.println("retour webservice creer vue : " + response.getStatus() + " " + response.getStatusInfo());
		return response.getStatus() == Status.OK.getStatusCode();
	}
	
	/**
	 * Crée une vue dans SonarQube asynchrone
	 * @param vue
	 * @return
	 */
	public Future<Response> creerVueAsync(Vue vue)
	{
		return appelWebserviceAsyncPOST("api/views/create", vue);
	}
	
	public boolean supprimerVue(Vue vue)
	{
		Response response = appelWebservicePOST("api/views/delete", new Clef(vue.getKey()));
		logger.info("retour supprimer vue : " + response.getStatus() + " " + response.getStatusInfo());
		return response.getStatus() == Status.OK.getStatusCode();
	}

	/**
	 * Ajoute des sous-vue déjà existantes à une vue donnée
	 * 
	 * @param listeViews
	 * @param parent
	 * @return
	 */
	public boolean ajouterSousVues(List<Vue> listeViews, Vue parent)
	{
		boolean ok = true;
		for (Vue view : listeViews)
		{
			AjouterVueLocale localView = new AjouterVueLocale(parent.getKey(), view.getKey());
			Response response = appelWebservicePOST("api/views/add_local_view", localView);
			logger.info("retour ajouterSousVues " + view.getName() + ": HTTP " + response.getStatus());
			if (response.getStatus() == Status.OK.getStatusCode())
			{
				ok = false;
			}
		}
		return ok;
	}
	
	/**
	 * Ajoute des sous-vue déjà existantes à une vue donnée
	 * 
	 * @param listeViews
	 * @param parent
	 * @return
	 */
	public boolean ajouterSousProjets(List<Projet> listeProjets, Vue parent)
	{
		boolean ok = true;
		for (Projet projet : listeProjets)
		{
			AjouterProjet addProjet = new AjouterProjet(parent.getKey(), projet.getKey());
			Response response = appelWebservicePOST("api/views/add_project", addProjet);
			System.out.println("retour webservice creer sousProjet " + projet.getNom() + ": " + response.getStatus() + " " + response.getStatusInfo());
			if (response.getStatus() == Status.OK.getStatusCode())
			{
				ok = false;
			}
		}
		return ok;
	}

	/**
	 * Crée un nouveau projet dans SonarQube
	 * 
	 * @param parent
	 * @param projet
	 */
	public void ajouterProjet(Vue parent, Projet projet)
	{
		// TODO
	}

	/**
	 * Force la mise à jour de toutes les vues dans SonarQube.
	 */
	public void majVues()
	{
		Response response = appelWebservicePOST("api/views/run", null);
		response.getStatus();
	}

	/*---------- METHODES PRIVEES ----------*/

	/**
	 * Appel des webservices en GET
	 * @param url
	 * 			Url du webservices
	 * @param params
	 * 			Paramètres optionnels de la requête
	 * @return
	 */
	private Response appelWebserviceGET(final String url, Parametre... params)
	{
		WebTarget requete = webTarget.path(url);
		
		if (params == null)
		{
			return requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).get();
		}
		
		for (Parametre parametre : params)
		{
			requete = requete.queryParam(parametre.getClef(), parametre.getValeur());
		}
		
		return requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser).get();
	}

	/**
	 * Appel des webservices en POST
	 * @param url
	 * 			Url du webservices
	 * @param entite
	 * 			Entite envoyée à la requete en paramètre. Utilise un objet implémentant l'interface {@link ModeleSonar}.
	 * 			Le paramètre peut être null s'il n'y a pas beaoin de paramètres.
	 * @return
	 */
	private Response appelWebservicePOST(final String url, ModeleSonar entite)
	{
		// Création ed la requête
		WebTarget requete = webTarget.path(url);
		Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);
		
		if (entite == null)
		{
			return builder.post(Entity.text(""));
		}
		return builder.post(Entity.entity(entite, MediaType.APPLICATION_JSON));
	}
	
	/**
	 * Appel des webservices en POST
	 * @param url
	 * 			Url du webservices
	 * @param entite
	 * 			Entite envoyée à la requete en paramètre. Utilise un objet implémentant l'interface {@link ModeleSonar}.
	 * 			Le paramètre peut être null s'il n'y a pas beaoin de paramètres.
	 * @return
	 */
	public Future<Response> appelWebserviceAsyncPOST(final String url, ModeleSonar entite)
	{
		// Création ed la requête
		WebTarget requete = webTarget.path(url);
		Invocation.Builder builder = requete.request(MediaType.APPLICATION_JSON).header(AUTHORIZATION, codeUser);
		
		if (entite == null)
		{
			return builder.async().post(Entity.text(""));
		}
		return builder.async().post(Entity.entity(entite, MediaType.APPLICATION_JSON));
	}
	
}