package bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
	private final String CIBLE = "Nb d'inc.àtraiterpouratteindrelacible";
	/** Nom de la colonne de l'avancement */
	private final String AVANCEMENT = "Avancement";

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
		upload = new DefaultStreamedContent(new FileInputStream(newFile.getName()), "application/vnd.ms-excel",
		        "test_workbook.xls");

	}

	private void workbook(Workbook wbIn, Workbook wbOut)
	{

		/* ------ Intialisation des variables ----- */

		// Index des cellules
		Integer moisEnCours = null, iEntrants = null, iResolved = null, iClos = null, iTransferes = null,
		        iEnCours = null, iLignesNomsColonnes = null, iCible = null, iAvancement = null;
		int compteIndex = 0;

		// Valorisation des dates
		LocalDate _1900 = LocalDate.of(1900, 1, 1);
		LocalDate _2015 = LocalDate.of(2015, 1, 1);
		long nbreJours = _2015.toEpochDay() - _1900.toEpochDay();

		// Création des feuilles de classeur
		Sheet sheetAvancement = wbIn.getSheet(Statics.sheetAvancement);
		Sheet sheetSM9 = wbIn.getSheet(Statics.sheetStockSM9);

		/* ------ Calcul des variables ------ */

		// Calcul du nombre d'incidents
		Object[] retourCalcul = calculNbreIncidents();
		int nbreDuMois = (int) retourCalcul[0];
		int nbreResolved = (int) retourCalcul[1];
		int closDuMois = (int) retourCalcul[2];

		// Calcul des index des lignes
		Integer[] retourIndex = recupIndexLignes(sheetAvancement);
		iLignesNomsColonnes = retourIndex[0];
		moisEnCours = retourIndex[1];

		// Printing
		System.out.println(nbreDuMois + " - " + nbreResolved + " - " + closDuMois);
		System.out.println(nbreJours);

		for (Cell cell : sheetAvancement.getRow(iLignesNomsColonnes))
		{
			if (CellType.STRING.equals(cell.getCellTypeEnum()))
			{
				switch (cell.getStringCellValue().trim())
				{
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

		if (compteIndex != 7)
			Utilities.updateGrowl(FacesMessage.SEVERITY_ERROR, "Erreur dans le format du fichier Excel");

		/* ------ Mise à jour du fichier Excel ------ */

		sheetAvancement.getRow(moisEnCours).getCell(iEntrants).setCellValue(nbreDuMois);
		sheetAvancement.getRow(moisEnCours).getCell(iClos).setCellValue(nbreResolved);
		sheetAvancement.getRow(moisEnCours).getCell(iResolved).setCellValue(nbreResolved);

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

				// lorsque que l'on trouve la première ligne avec l'annèe en
				// cours, on prend l'index + 1 pour chercher le nom des colonnes
				if (retour[0] != null && Statics.TODAY.getYear() == date.getYear())
					retour[0] = cell.getRowIndex() + 1;

				// récupération de l'incdes de la ligne à mettre jour pour ce
				// mois.
				if (Statics.TODAY.getMonth().equals(date.getMonth()) && Statics.TODAY.getYear() == date.getYear())
					retour[1] = cell.getRow().getRowNum();
			}
		}
		if (retour[0] == null || retour[1] == null)
		{
			Utilities.updateGrowl(FacesMessage.SEVERITY_ERROR, "Mauvaise formation du fichier Excel");
		}

		return retour;
	}

	/**
	 * Méthode de calcul du nombre d'incidents arrivés le mois en cours
	 * 
	 * @return le nombre d'incidents
	 */
	private Object[] calculNbreIncidents()
	{
		/* ----- Variables locales ----- */
		
		// Formatteur de date
		DateTimeFormatter f = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		// LocalDate
		LocalDate dateIncident = null, dateCloture = null, dateTransfert = null;
		// String
		String dateTransfertString = null, dateIncidentString = null, da = null;
		// int
		int totalDuMois = 0, totalResolved = 0, totalClosed = 0, totalWorkInPrg = 0, totalOpen = 0, totalPending = 0, totalTransfered = 0,
				totalProbsResolved = 0;
		// List<Incident>
		List<Incident> incsTransferes = new ArrayList<>(), incsATraiter = new ArrayList<>();
		Tracker tracker = null;
		Object[] retour = new Object[7];

		// Itération sur les incidents
		for (Incident incident : listIncidents)
		{
			dateIncidentString = incident.getMapValeurs().get(Champ.DATEPRISENCHARGE);
			da = incident.getMapValeurs().get(Champ.DA);
			dateTransfertString = incident.getMapValeurs().get(Champ.DATETRANSFERT);
			tracker = incident.getTracker();
			
			switch (incident.getStatus())
			{
				case RESOLVED :
					if (tracker == Tracker.INCIDENT || tracker == Tracker.DEMANDE)
						totalResolved++;
					if (tracker == Tracker.PROBLEME)
						totalProbsResolved++;					
					incsATraiter.add(incident);
					break;
					
				case NOUVEAU :
					
					break;
					
				case PENDING :
					
					break;
					
				case WRKINPRG :
					
					break;
				default:
					break;
					
			}
			// Incrémentation des incidents resolved
			if (Statut.RESOLVED.getString().equals(status))
			{
				totalResolved++;
				continue;
			}

			// incrémentation des incidents nouveaux
			if (Statut.NOUVEAU.getString().equals(status))
			{
				totalWorkInPrg++;
				continue;
			}
			
			// Incrémentation des incidents pending
			if (Statut.PENDING.getString().equals(status))
			{
				totalPending++;
				continue;
			}
			
			// Incrémentation des incidents work in prg
			if (Statut.WRKINPRG.getString().equals(status))
			{
				totalWorkInPrg++;
				continue;
			}

			// Incrémentation des incidents transférés
			if (dateTransfertString != null && dateTransfertString.length() > 9)
			{
				dateTransfert = LocalDate.parse(dateTransfertString.substring(0, 10), f);

				if (dateTransfert.getYear() == dateDuJour.getYear() && dateTransfert.getMonth().equals(dateDuJour.getMonth()))
				{
					totalTransfered++;
					continue;
				}
			}

			// Elimination des incidents qui ont une date mal formatée ou un n° de DA à abandon.
			if (dateIncidentString != null && dateIncidentString.length() > 9 && !Statics.ABANDON.equalsIgnoreCase(da))
			{
				dateIncident = LocalDate.parse(dateIncidentString.substring(0, 10), f);

				// On ne garde que les incidents arrivés le mois en cours
				if (dateIncident.getYear() == dateDuJour.getYear() && dateIncident.getMonth().equals(dateDuJour.getMonth()))
				{
					totalDuMois++;
					continue;
				}

				// Incrémentation des incidents clos
				if (incident.getDateCloture() != null)
				{
					dateCloture = ((java.sql.Date) incident.getDateCloture()).toLocalDate();
					if (Statut.CLOSED.getString().equals(status) && dateCloture.getYear() == dateDuJour.getYear() && dateCloture.getMonth().equals(dateDuJour.getMonth()))
					{
						totalClosed++;
						continue;
					}
				}
			}
		}

		retour[0] = totalDuMois;
		retour[1] = totalResolved;
		retour[2] = totalClosed;
		retour[3] = totalWorkInPrg;
		retour[4] = totalOpen;
		retour[5] = totalPending;
		retour[6] = totalTransfered;
		retour[7] = incsTransferes;
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