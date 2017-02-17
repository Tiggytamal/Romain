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
	/** Récupération de la date du jour */
	private final LocalDate dateDuJour = LocalDate.now();

	/** Nom de la colonne de l'objectif des incidents à atteindre */
	private final String OBJECTIF = "NbincidentsObjectif";
	/** Nom de la colonne des incidents entrants */
	private final String ENTRANTS = "NbincidentsEntrants";
	/** Nom de la colonne des incidents clos */
	private final String CLOS = "NbincidentsClos";
	/** Nom de la colonne des incidents resoled */
	private final String RESOLVED = "NbincidentsResolved";
	/** Nom de la colonne des incidents transfered */
	private final String TRANSFERED = "NbincidentsTransférés";
	/** Nom de la colonne des incidents en cours */
	private final String ENCOURS = "NbincidentsEnCours";
	/** Nom de la colonne du nombre d'incidents à traiter */
	private final String CIBLE = "Nbd'inc.àtraiterpouratteindrelacible";
	/** Nom de la colonne de l'avancement */
	private final String AVANCEMENT = "Avancement";
	/** Identifiant du calcul des incidents pending */
	private final String PENDING = "NbincidentsPending";
	/** Identifiant du calcul des problèmes resolved */
	private final String PROBSRESOLVED = "NbprobsResolved";
	/** Identifiant du calcul des problèmes en cours */
	private final String PROBSENCOURS = "NbproblèmesEnCours";
	/** Identifiant de la liste des incidents à traiter */
	private final String LISTINCS = "Listeincidents";
	/** Identifiant des la liste des incidents transférés */
	private final String LISTINCSTRANS = "ListeIncidentsTransferes";

	/** index de la colonne avec les mois de l'annèe */
	private final int INDEXCOLMOIS = 1;

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

		workbook(wbIn, wbOut);
		File newFile = new File("/ressources/test.xls");
		// Sauvegarde du premier fichier sur C
		wbIn.write(new FileOutputStream(newFile.getName()));
		wbIn.close();
		upload = new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel", "test_workbook.xls");

	}


	private void workbook(Workbook wbIn, Workbook wbOut)
	{

		/* ------ Intialisation des variables ----- */

		// Index des cellules
		int moisEnCours = 0, iEntrants = 0, iResolved = 0, iClos = 0, iTransferes = 0,
		        iEnCours = 0, iLignesNomsColonnes = 0, iCible = 0, iAvancement = 0, iObjectif = 0, compteIndex = 0;

		// Valorisation des dates
		LocalDate _1900 = LocalDate.of(1900, 1, 1);
		LocalDate _2015 = LocalDate.of(2015, 1, 1);
		long nbreJours = _2015.toEpochDay() - _1900.toEpochDay();

		// Création des feuilles de classeur
		Sheet sheetAvancementIn = wbIn.getSheet(Statics.sheetAvancement);
		Sheet sheetSM9In = wbIn.getSheet(Statics.sheetStockSM9);
		Sheet sheetAvancementOut = wbOut.getSheet(Statics.sheetAvancement);
		Sheet sheetSM9Out = wbOut.getSheet(Statics.sheetStockSM9);

		/* ------ Calcul des variables ------ */

		// Récupération des valeurs
		HashMap<String, Object> retourCalcul = calculNbreIncidents();
		int nbreDuMois = (int) retourCalcul.get(ENTRANTS);
		int nbreResolved = (int) retourCalcul.get(RESOLVED);
		int closDuMois = (int) retourCalcul.get(CLOS);
		List<Incident> incsEncours =(List<Incident>) retourCalcul.get(LISTINCS);

		// Calcul des index des lignes
		Integer[] retourIndex = recupIndexLignes(sheetAvancementIn);
		iLignesNomsColonnes = retourIndex[0];
		moisEnCours = retourIndex[1];

		// Récupération des indes des colonnes
		for (Cell cell : sheetAvancementIn.getRow(iLignesNomsColonnes))
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
		// Mise à jour des cellules
		row.getCell(iEntrants).setCellValue(nbreDuMois);
		row.getCell(iClos).setCellValue(closDuMois);
		row.getCell(iResolved).setCellValue(nbreResolved);
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
	}
	
	/**
	 * Calcul de la cellule d'affichage des incidents transférés.
	 * 
	 * @param compte
	 * @param listIncidents
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private String cellTransfere(HashMap<String, Object> map)
	{
		// Création du StringBuilder
		StringBuilder retour = new StringBuilder("Nombre d'incidents : ");
		
		//Ajout du nombre d'incident transféré
		retour.append(map.get(TRANSFERED)).append("\n");
		
		//Itération de la liste des incidents transférés
		for (Incident incident : (List<Incident>) map.get(LISTINCSTRANS))
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
	 * Permet de calculer les indes des lignes pour la mise à jour de la feuille
	 * excel
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

				if (dateTransfert.getYear() == dateDuJour.getYear() && dateTransfert.getMonth().equals(dateDuJour.getMonth()))
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
				if (estIncident && datePriseEnCharge.getYear() == dateDuJour.getYear() && datePriseEnCharge.getMonth().equals(dateDuJour.getMonth()))
				{
					totalDuMois++;
					continue;
				}

				// Incrémentation des incidents clos
				if (estIncident && statut == Statut.CLOSED && incident.getDateCloture() != null)
				{
					dateCloture = ((java.sql.Date) incident.getDateCloture()).toLocalDate();
					if (dateCloture.getYear() == dateDuJour.getYear() && dateCloture.getMonth().equals(dateDuJour.getMonth()))
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

	/**
	 * @return the file
	 */
	public UploadedFile getFile()
	{
		return file;
	}

	/**
	 * @param file
	 *            the file to set
	 */
	public void setFile(UploadedFile file)
	{
		this.file = file;
	}

	/**
	 * @return the upload
	 */
	public StreamedContent getUpload()
	{
		return upload;
	}

	/**
	 * @return the listBean
	 */
	public ListBean getListBean()
	{
		return listBean;
	}

	/**
	 * @param listBean
	 *            the listBean to set
	 */
	public void setListBean(ListBean listBean)
	{
		this.listBean = listBean;
	}
}