package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import model.Incident;
import model.enums.Champ;
import model.enums.Statut;
import model.enums.Tracker;
import utilities.Statics;
import utilities.Utilities;
import utilities.interfaces.Instance;

@ManagedBean(name = "excel")
@SessionScoped
public class ExcelBean implements Serializable, Instance
{

	/* ---------- ATTIBUTES ---------- */

	private static final long serialVersionUID = 1L;

	// Session Bean
	@ManagedProperty(value = "#{list}")
	private ListBean listBean;

	// Attribut pour l'import

	/** Excel envoyé à l'application */
	private UploadedFile file;
	/** Nouveau fichier à télécharger */
	private StreamedContent upload;
	/** workbook correspondant au fichier envoyé */
	private Workbook wbIn;
	/** Workbook du nouveau fichier excel */
	private Workbook wbOut;
	/** liste des incidents triés */
	private List<Incident> listIncidents;

	/** Noms des colonnes de la page Avancement */
	private final static String OBJECTIF = "NbincidentsObjectif", ENTRANTS = "NbincidentsEntrants", CLOS = "NbincidentsClos", RESOLVED = "NbincidentsResolved", 
			TRANSFERED = "NbincidentsTransférés", ENCOURS = "NbincidentsEnCours", CIBLE = "Nbd'inc.àtraiterpouratteindrelacible", AVANCEMENT = "Avancement";
	
	/** Identifiants pour les calculs des incidents */
	private final static String PENDING = "NbincidentsPending", PROBSRESOLVED = "NbprobsResolved", PROBSENCOURS = "NbproblèmesEnCours",
			LISTINCS = "Listeincidents", LISTINCSTRANS = "ListeIncidentsTransferes";	
	
	/** Noms des colonnes de la page du stock SM9 */
	private final static String SM9NUMERO = "N° d'incident", SM9TRACKER = "Tracker", SM9APP = "Application", SM9BANQUE = "Banque", 
	SM9ENVIRO = "Environnement", SM9PRIORITE = "Priorité", SM9SUJET = "Sujet", SM9ASSIGNE = "Assigné à", SM9STATUT = "Statut de l'incident", 
	SM9OUV = "Date d'ouverture", SM9PRISEENCHARGE ="Date de prise en charge", SM9RESOLUTION = "Date de résolution", SM9REOUV = "Ré-ouverture";

	/** index de la colonne avec les mois de l'annèe */
	private final static int INDEXCOLMOIS = 1;

	/* ---------- CONSTUCTORS ---------- */

	public ExcelBean()
	{
		instanciation();
	}

	/* ---------- METHODS ---------- */

	@PostConstruct
	public void postConstruct()
	{
		listIncidents = listBean.getListIncidents();
	}

	@Override
	public void instanciation()
	{
		wbIn = new HSSFWorkbook();
		wbOut = new HSSFWorkbook();
	}

	public void charger(FileUploadEvent event) throws IOException, EncryptedDocumentException, InvalidFormatException
	{
		// Récupération du fichier envoyé
		file = event.getFile();

		// Création des deux workooks
		wbIn = WorkbookFactory.create(file.getInputstream());
		wbOut = WorkbookFactory.create(file.getInputstream());

		// Traitement de la page d'avancement
		List<Incident> incidentsATraiter = avancement(wbIn, wbOut);
		
		// Traitement de la page SM9
		
		sm9(incidentsATraiter, wbIn, wbOut);
		File newFile = new File("/ressources/test.xls");
		// Sauvegarde du premier fichier sur C
		wbIn.write(new FileOutputStream(newFile.getName()));
		wbIn.close();
		upload = new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "test_workbook.xls");

	}

	/**
	 * Mise à jour de la feuille d'avancement des incidents
	 * @param wbIn
	 * @param wbOut
	 * @return
	 */
	private List<Incident> avancement (Workbook wbIn, Workbook wbOut)
	{

		/* ------ Intialisation des variables ----- */

		// Index des cellules
		int moisEnCours = 0, iEntrants = 0, iResolved = 0, iClos = 0, iTransferes = 0, iEnCours = 0, iCible = 0, iAvancement = 0, iObjectif = 0, compteIndex = 0;


		// Création des feuilles de classeur
		Sheet sheetAvancementIn = wbIn.getSheet(Statics.sheetAvancement);
		Sheet sheetAvancementOut = wbOut.getSheet(Statics.sheetAvancement);

		/* ------ Calcul des variables ------ */

		// Calcul des index des lignes
		Integer[] retourIndex = recupIndexLignes(sheetAvancementIn);
		moisEnCours = retourIndex[1];

		// Récupération des index des colonnes
		for (Cell cell : sheetAvancementIn.getRow(retourIndex[0]))
		{
			if (CellType.STRING == cell.getCellTypeEnum())
			{
				switch (cell.getStringCellValue().trim())
				{
					case OBJECTIF :
						iObjectif = cell.getColumnIndex();
						compteIndex++;
						break;
						
					case ENTRANTS:
						iEntrants = cell.getColumnIndex();
						compteIndex++;
						break;

					case CLOS:
						iClos = cell.getColumnIndex();
						compteIndex++;
						break;

					case RESOLVED:
						iResolved = cell.getColumnIndex();
						compteIndex++;
						break;

					case ENCOURS:
						iEnCours = cell.getColumnIndex();
						compteIndex++;
						break;

					case TRANSFERED:
						iTransferes = cell.getColumnIndex();
						compteIndex++;
						break;

					case CIBLE:
						iCible = cell.getColumnIndex();
						compteIndex++;
						break;

					case AVANCEMENT:
						iAvancement = cell.getColumnIndex();
						compteIndex++;
						break;
				}
			}
		}

		if (compteIndex != 8)
			Utilities.updateGrowl(FacesMessage.SEVERITY_ERROR, "Erreur dans le format du fichier Excel");

		/* ------ Mise à jour du fichier Excel ------ */

		// récupération de la ligne excel à mettre à jour
		Row row = sheetAvancementOut.getRow(moisEnCours);
		
		// Calcul du nombre d'incident
		HashMap<String, Object> retourCalcul = calculNbreIncidents();
		
		// Mise à jour des cellules
		row.getCell(iEntrants).setCellValue((int) retourCalcul.get(ENTRANTS));
		row.getCell(iClos).setCellValue((int) retourCalcul.get(CLOS));
		row.getCell(iResolved).setCellValue( (int) retourCalcul.get(RESOLVED));
		row.getCell(iTransferes).setCellValue(cellTransfere(retourCalcul));
		
		// Mise à jour de la cellule des incidents en cours
		Cell cellEncours = row.getCell(iEnCours);
		CellStyle style = wbOut.createCellStyle();
		style.setWrapText(true);
		cellEncours.setCellStyle(style);
		cellEncours.setCellValue(cellEnCours(retourCalcul));
		
		// Mise à jour de la cellule du nombre d'incidents cible
		Cell cellCible = row.getCell(iCible);
		cellCible.setCellFormula("IF(" + iClos + moisEnCours +">" + iObjectif + moisEnCours +  ";0;" + iObjectif + moisEnCours + "-" + iClos + moisEnCours + ")");
		Cell cellAvanc = row.getCell(iAvancement);
		cellAvanc.setCellFormula("IF(" + iObjectif + moisEnCours + "<>0;" + iClos + moisEnCours + "/" + iObjectif + moisEnCours + ";0");		
	
		// Renvoie la liste des incidents à traiter
		@SuppressWarnings("unchecked")
		List<Incident> retour = (List<Incident>) retourCalcul.get(LISTINCS);
		
		return retour;
	}
	
	private void sm9(List<Incident> list, Workbook wbIn, Workbook wbOut)
	{
		// Création des feuilles de classeur
		Sheet sheetSM9In = wbIn.getSheet(Statics.sheetStockSM9);
		Sheet sheetSM9Out = wbOut.getSheet(Statics.sheetStockSM9);
		
		//ints
		int iNumero = 0, itracker = 0, iApplication = 0,  iBanque = 0, iEnvironnement = 0, iPriorite = 0, iSujet = 0, iAssigne = 0,
				iStatut = 0, iDateOuv = 0, idatePrisEnCharge = 0, iDateReso = 0, iReouv = 0, iLigne1 = 0, totalIndex = 0;
		
		// Récupération des indices de la page
		for (Row row : sheetSM9In)
		{
			for (Cell cell : row)
			{
				if(cell.getCellTypeEnum() == CellType.STRING)
				{
					switch (cell.getStringCellValue())
					{
						case SM9NUMERO :
							iLigne1 = cell.getRowIndex() + 1;
							iNumero = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9TRACKER :
							itracker = cell.getColumnIndex();							
							totalIndex++;
							break;
							
						case SM9APP :
							iApplication = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9BANQUE :
							iBanque = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9ENVIRO :
							iEnvironnement = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9PRIORITE :
							iPriorite = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9SUJET :
							iSujet = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9ASSIGNE :
							iAssigne = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9STATUT :
							iStatut = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9OUV :
							iDateOuv = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9PRISEENCHARGE :
							idatePrisEnCharge = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9RESOLUTION :
							iDateReso = cell.getColumnIndex();
							totalIndex++;
							break;
							
						case SM9REOUV :
							iReouv = cell.getColumnIndex();
							totalIndex++;
							break;							
					}
				}
			}
			if (totalIndex == 13)
				break;
		}
		
	}
	
	
	/**
	 * Calcul de la cellule d'affichage des incidents transférés.
	 * 
	 * @param compte
	 * @param listIncidents
	 * @return
	 */
	private String cellTransfere(HashMap<String, Object> map)
	{
		// Création du StringBuilder
		StringBuilder retour = new StringBuilder("Nombre d'incidents : ");
		
		//Ajout du nombre d'incident transféré
		retour.append(map.get(TRANSFERED)).append("\n");
		
		//Itération de la liste des incidents transférés
		
		@SuppressWarnings("unchecked")
		List<Incident> list = (List<Incident>) map.get(LISTINCSTRANS);
		for (Incident incident : list)
		{
			// Ajout du numéro de l'incident et du groupe de transfert
			retour.append(incident.getMapValeurs().get(Champ.NUMERO)).append(" - ").append(incident.getMapValeurs().get(Champ.GRTRANSFERT)).append("\n");
		}		
		return retour.toString();
	}
	
	/**
	 * Calcul de la cellule des incidents en cours
	 * 
	 * @param map
	 * @return
	 */
	private String cellEnCours(HashMap<String, Object> map)
	{
		// Création du StringBuilder
		StringBuilder retour = new StringBuilder();
		
		// Récupération des comptes
		int enCours = (int) map.get(ENCOURS);
		int pending = (int) map.get(PENDING);
		int resolved = (int) map.get(RESOLVED);
		int total = enCours + pending + resolved;
		int probsEnCours = (int) map.get(PROBSENCOURS);
		int probsResolved = (int) map.get(PROBSRESOLVED);
		int totalProbs = probsEnCours + probsResolved;
		
		// Ajout des donnèes
		retour.append(total).append(" incidents ( ");
		retour.append(resolved).append(" resolved, ").append(enCours).append(" working, ").append(pending).append(" pending )\n");
		retour.append(totalProbs).append(" problèmes dont ").append(probsResolved).append(" resolved ");
		
		return retour.toString();
	}

	/**
	 * Permet de calculer les indes des lignes pour la mise à jour de la feuille excel
	 * 
	 * @param sheet
	 * @return
	 */
	private Integer[] recupIndexLignes(Sheet sheet)
	{
		Integer[] retour = new Integer[2];

		// Itération sur les cellules de la colonne avec les noms des mois
		for (Row row : sheet)
		{
			Cell cell = row.getCell(INDEXCOLMOIS);
			if (cell.getNumericCellValue() > 40000)
			{
				LocalDate date = LocalDate.ofEpochDay((long) cell.getNumericCellValue()).minusYears(70);

				// lorsque que l'on trouve la première ligne avec l'annèe en cours, on prend l'index + 1 pour chercher le nom des colonnes
				if (retour[0] != null && Statics.TODAY.getYear() == date.getYear())
					retour[0] = cell.getRowIndex() + 1;

				// récupération de l'incdes de la ligne à mettre jour pour ce mois.
				if (Statics.TODAY.getMonth().equals(date.getMonth()) && Statics.TODAY.getYear() == date.getYear())
					retour[1] = cell.getRowIndex();
			}
		}
		if (retour[0] == null || retour[1] == null)
		{
			Utilities.updateGrowl(FacesMessage.SEVERITY_ERROR, "Mauvaise formation du fichier Excel");
		}

		return retour;
	}

	/**
	 * Méthode de calcul du nombre d'incidents
	 * 
	 * @return 
	 * 		Liste des résultats
	 */
	private HashMap<String, Object> calculNbreIncidents()
	{
		/* ----- Variables locales ----- */
		
		// Formatteur de date
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// LocalDates
		LocalDate datePriseEnCharge = null, dateCloture = null, dateTransfert = null;
		// Strings
		String dateTransfertString = null, datePriseEnChargeString = null, da = null;
		// ints
		int totalDuMois = 0, totalResolved = 0, totalClosed = 0, totalWorkInPrg = 0, totalPending = 0, totalTransfered = 0, totalProbsResolved = 0, totalProbs = 0;
		// List<Incident>
		List<Incident> incsTransferes = new ArrayList<>(), incsATraiter = new ArrayList<>();
		// Enumérations
		Tracker tracker = null;
		Statut statut = null;
		// Booléens
		boolean estAbandonne = false, estIncident = false, estProbleme = false;
		// Objet de retour
		HashMap<String, Object> retour = new HashMap<>();

		
		/* ----- Itération sur les incidents ----- */
		
		for (Incident incident : listIncidents)
		{
			// Initialisation des variables
			datePriseEnChargeString = incident.getMapValeurs().get(Champ.DATEPRISENCHARGE);
			da = incident.getMapValeurs().get(Champ.DA);
			dateTransfertString = incident.getMapValeurs().get(Champ.DATETRANSFERT);
			tracker = incident.getTracker();
			statut = incident.getStatus();
			estAbandonne = Statics.ABANDON.equalsIgnoreCase(da);
			estIncident = tracker == Tracker.INCIDENT || tracker == Tracker.DEMANDE;
			estProbleme = tracker == Tracker.PROBLEME;
			
			// Pas de décompte si l'incident a un numéro de DA à abandon
			if (estAbandonne)
				continue;
						
			// Incrémentation des compteurs et ajout dans les listes des incidents en cours.
			switch (statut)
			{
				case RESOLVED :
					if (estIncident)
						totalResolved++;
					else if (estProbleme)
						totalProbsResolved++;					
					incsATraiter.add(incident);
					break;
					
				case NOUVEAU :
					if (estIncident)
						totalWorkInPrg++;
					else if (estProbleme)
						totalProbs++;
					incsATraiter.add(incident);
					break;
					
				case PENDING :
					if (estIncident)
						totalPending++;
					else if (estProbleme)
						totalProbs++;
					incsATraiter.add(incident);
					break;
					
				case WRKINPRG :
					if (estIncident)
						totalWorkInPrg++;
					else if (estProbleme)
						totalProbs++;
					incsATraiter.add(incident);
					break;
					
				case REFERRED :
					if (estIncident)
						totalWorkInPrg++;
					else if (estProbleme)
						totalProbs++;
					incsATraiter.add(incident);
					break;
					
				default :
					break;
			}

			// Incrémentation des incidents transférés
			if (statut == Statut.TRANSFERED && dateTransfertString != null && dateTransfertString.length() > 9)
			{
				dateTransfert = LocalDate.parse(dateTransfertString.substring(0, 10), f);

				if (dateTransfert.getYear() == Statics.TODAY.getYear() && dateTransfert.getMonth().equals(Statics.TODAY.getMonth()))
				{
					totalTransfered++;
					incsTransferes.add(incident);
				}
			}

			// Elimination des incidents qui ont une date mal formatée
			if (datePriseEnChargeString != null && datePriseEnChargeString.length() > 9)
			{
				datePriseEnCharge = LocalDate.parse(datePriseEnChargeString.substring(0, 10), f);

				// Incrémentation des incidents du mois
				if (estIncident && datePriseEnCharge.getYear() == Statics.TODAY.getYear() && datePriseEnCharge.getMonth().equals(Statics.TODAY.getMonth()))
				{
					totalDuMois++;
					continue;
				}

				// Incrémentation des incidents clos
				if (estIncident && statut == Statut.CLOSED && incident.getDateCloture() != null)
				{
					dateCloture = ((java.sql.Date) incident.getDateCloture()).toLocalDate();
					if (dateCloture.getYear() == Statics.TODAY.getYear() && dateCloture.getMonth().equals(Statics.TODAY.getMonth()))
						totalClosed++;						
				}
			}
		}

		retour.put(ENTRANTS, totalDuMois);
		retour.put(RESOLVED, totalResolved);
		retour.put(CLOS, totalClosed);
		retour.put(ENCOURS, totalWorkInPrg);
		retour.put(PENDING, totalPending);
		retour.put(TRANSFERED, totalTransfered);
		retour.put(PROBSRESOLVED, totalProbsResolved);
		retour.put(PROBSENCOURS, totalProbs);
		retour.put(LISTINCS, incsATraiter);
		retour.put(LISTINCSTRANS, incsTransferes);
		return retour;
	}

	/* ---------- ACCESS ---------- */

	public UploadedFile getFile()
	{
		return file;
	}

	public void setFile(UploadedFile file)
	{
		this.file = file;
	}

	public StreamedContent getUpload()
	{
		return upload;
	}

	public ListBean getListBean()
	{
		return listBean;
	}

	public void setListBean(ListBean listBean)
	{
		this.listBean = listBean;
	}
}