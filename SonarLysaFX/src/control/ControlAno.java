package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import model.enums.Environnement;
import model.excel.Anomalie;
import utilities.CellHelper;
import utilities.enums.Bordure;

/**
 * Classe de getion du fichier Excel des anomalies SonarQube
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public class ControlAno extends ControlExcel
{
	/*---------- ATTRIBUTS ----------*/
	
	// Liste des indices des colonnes	
	private int colDir;
	private int colDepart;
	private int colService;
	private int colResp;
	private int colClarity;
	private int colLib;
	private int colCpi;
	private int colEdition;
	private int colLot;
	private int colEnv;
	private int colAno;
	private int colEtat;
	private int colRemarque;
	
	// Liste des noms de colonnes
	private static final String DIRECTION = "Direction";
	private static final String DEPARTEMENT = "Département";
	private static final String SERVICE = "Service";
	private static final String RESPSERVICE = "Responsable Service";
	private static final String CLARITY = "Projet Clarity";
	private static final String LIBELLE = "Libelle projet";
	private static final String CPI = "CPI  du projet";
	private static final String EDITION = "Edition";
	private static final String LOT = "Lot projet RTC";
	private static final String ENV = "Environnement";
	private static final String ANOMALIE = "Anomalie";
	private static final String ETAT = "Etat";
	private static final String REMARQUE = "Remarque";
	private static final String TRAITE = "Traité"; 
	
	// Nom de la feuillle avec les naomalies en cours
	private static final String FP = "SUIVI Qualité";
	private static final String CLOSE = "Close";
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlAno(File file) throws InvalidFormatException, IOException
	{
		super(file);
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
		
	protected Map<String, Anomalie> listAnomaliesSurLotsCrees()
	{
		// Récupération de la première feuille
		Sheet sheet = wb.getSheet(FP);
		
		Map<String, Anomalie> retour = new HashMap<>();
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) 
		{			
			Row row = sheet.getRow(i);
			
			// Création de l'anomalie
			Anomalie ano = new Anomalie();
			ano.setDirection(row.getCell(colDir).getStringCellValue());
			ano.setDepartement(row.getCell(colDepart).getStringCellValue());
			ano.setService(row.getCell(colService).getStringCellValue());
			ano.setResponsableService(row.getCell(colResp).getStringCellValue());
			ano.setProjetClarity(row.getCell(colClarity).getStringCellValue());
			ano.setLibelleProjet(row.getCell(colLib).getStringCellValue());
			ano.setCpiProjet(row.getCell(colCpi).getStringCellValue());
			ano.setEdition(row.getCell(colEdition).getStringCellValue());
			String string = row.getCell(colLot).getStringCellValue();
			ano.setLot(string);
			ano.setEnvironnement(Environnement.getEnvironnement(row.getCell(colEnv).getStringCellValue()));
			ano.setnumeroAnomalie(row.getCell(colAno).getStringCellValue());
			ano.setEtat(row.getCell(colEtat).getStringCellValue());
			ano.setRemarque(row.getCell(colRemarque).getStringCellValue());

			// Création de la clef
			if (string.startsWith("Lot "))
				string = string.substring(4);
		    retour.put(string, ano);
		}
		return retour;
	}
	
	protected List<Anomalie> createSheetError(String nomSheet, List<Anomalie> anoAcreer) throws IOException
	{	
		// Création de la feuille de calcul
		Sheet sheet = wb.getSheet(nomSheet);
		
		// Création de l'utilitaire de gestion des styles
		CellHelper helper = new CellHelper(wb);
		
		List<Anomalie> retour = new ArrayList<>();
		
		//Liste des lots existants. On itère si la feuille existe déjà, pour récupérer tous les numéros de lot déjà enregistrés.
		List<String> lotsExistants = new ArrayList<>();
		if (sheet != null)
		{
			Iterator<Row> iter = sheet.rowIterator();
			while (iter.hasNext())
			{
				Row row = iter.next();
				Cell cellLot = row.getCell(Index.LOTI.ordinal());
				Cell cellT = row.getCell(Index.TRAITEI.ordinal());
				if (cellLot.getCellTypeEnum() == CellType.STRING && cellT.getCellTypeEnum() == CellType.STRING && cellT.getStringCellValue().equals("O"))
					lotsExistants.add(row.getCell(Index.LOTI.ordinal()).getStringCellValue());
			}
			wb.removeSheetAt(wb.getSheetIndex(sheet));		
		}
		
		// Recréation de la feuille
		sheet = wb.createSheet(nomSheet);
		Cell cell;
		
		// Création du style des titres      
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle blancCentre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle jauneCentre = helper.getStyle(IndexedColors.WHITE, Bordure.VIDE, HorizontalAlignment.CENTER);
        		
		// Création des noms des colonnes
		Row titres = sheet.createRow(0);
		for (Index index : Index.values())
		{
			cell = titres.createCell(index.ordinal());
			cell.setCellStyle(styleTitre);	
			switch (index)
			{
				case LOTI:
					cell.setCellValue(LOT);
					break;
				case EDITIONI:
					cell.setCellValue(EDITION);
					break;
				case ENVI:
					cell.setCellValue(ENV);
					break;
				case TRAITEI :
					cell.setCellValue(TRAITE);
					break;
			}
		}
		
		for (Anomalie anomalie : anoAcreer)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() +1);
			if (lotsExistants.contains(anomalie.getLot()))
			{
				cell = row.createCell(Index.LOTI.ordinal());
				cell.setCellValue(anomalie.getLot());
				cell.setCellStyle(blancCentre);
				cell = row.createCell(Index.EDITIONI.ordinal());
				cell.setCellValue(anomalie.getEdition());
				cell.setCellStyle(helper.getStyle(IndexedColors.WHITE, Bordure.VIDE));
				cell = row.createCell(Index.ENVI.ordinal());
				cell.setCellValue(anomalie.getEnvironnement().toString());
				cell.setCellStyle(blancCentre);
				cell = row.createCell(Index.TRAITEI.ordinal());
				cell.setCellValue("O");
				cell.setCellStyle(blancCentre);
			}
			else
			{
				cell = row.createCell(Index.LOTI.ordinal());
				cell.setCellStyle(jauneCentre);
				cell.setCellValue(anomalie.getLot());
				cell = row.createCell(Index.EDITIONI.ordinal());
				cell.setCellStyle(helper.getStyle(IndexedColors.LIGHT_YELLOW, Bordure.VIDE));
				cell.setCellValue(anomalie.getEdition());
				cell = row.createCell(Index.ENVI.ordinal());
				cell.setCellStyle(jauneCentre);
				cell.setCellValue(anomalie.getEnvironnement().toString());
				cell = row.createCell(Index.TRAITEI.ordinal());
				cell.setCellStyle(jauneCentre);
				cell.setCellValue("N");
				retour.add(anomalie);
			}			
		}
		
		sheet.autoSizeColumn(Index.LOTI.ordinal());
		sheet.autoSizeColumn(Index.EDITIONI.ordinal());
		sheet.autoSizeColumn(Index.ENVI.ordinal());
		// Ecriture du fichier Excel
		write();
		return retour;
	}
	
	/**
	 * Permet de mettre à jour les anomalies avec une quality Gate bonne
	 * @param lotsEnErreur
	 * 				Liste de tous les lots avec un Quality Gate à "ERROR".
	 * @throws IOException 
	 */
	protected void majAnoOK(Set<String> lotsEnErreur) throws IOException
	{
		// Récupération de la feuille avec les anomalies
		Sheet sheet = wb.getSheet(FP);
		
		// Itération sur chaque ligne
		for (int i = 1; i <= sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
			
			// Si le numéro n'est pas présent dans la liste, c'est que le Quality Gate est bon
			String string = row.getCell(colLot).getStringCellValue().substring(4);
			if (!lotsEnErreur.contains(string))
			{
				majCouleurLigne(row, IndexedColors.LIGHT_GREEN);
			}
			else
			{
				majCouleurLigne(row, IndexedColors.WHITE);
			}
		}		
		
		//Ecriture du fichier
		write();
	}
	
	/**
	 * Rajoute les nouvelles anomalies à la première page du fichier Excel
	 * @param anoAajouter
	 * @throws IOException 
	 */
	protected void majNouvellesAno(List<Anomalie> anoAajouter) throws IOException
	{
		// Récupération de la feuille avec les anomalies
		Sheet sheet = wb.getSheet(FP);
		CellHelper helper = new CellHelper(wb);
		CellStyle vide = helper.getStyle(IndexedColors.RED, Bordure.VIDE);
		CellStyle videCentre = helper.getStyle(IndexedColors.RED, Bordure.VIDE, HorizontalAlignment.CENTER);
		
		for (Anomalie ano : anoAajouter)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() +1);
			Cell cell = row.createCell(colDir);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getDirection());
			cell = row.createCell(colDepart);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getDepartement());
			cell = row.createCell(colService);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getService());
			cell = row.createCell(colResp);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getResponsableService());
			cell = row.createCell(colClarity);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getProjetClarity());
			cell = row.createCell(colLib);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getLibelleProjet());
			cell = row.createCell(colCpi);
			cell.setCellStyle(vide);
			cell.setCellValue(ano.getCpiProjet());
			cell = row.createCell(colEdition);
			cell.setCellStyle(videCentre);
			cell.setCellValue(ano.getEdition());
			cell = row.createCell(colLot);
			cell.setCellStyle(videCentre);
			cell.setCellValue(ano.getLot());
			cell = row.createCell(colEnv);
			cell.setCellStyle(videCentre);
			cell.setCellValue(ano.getEnvironnement().toString());
			cell = row.createCell(colAno);
			cell.setCellStyle(videCentre);
			cell = row.createCell(colEtat);
			cell.setCellStyle(vide);
			cell = row.createCell(colRemarque);
			cell.setCellStyle(vide);
		}
		write();
	}
	
	@Override
	protected void calculIndiceColonnes()
	{
		// Récupération de la première feuille
		Sheet sheet = wb.getSheetAt(0);
		
		//Récupération des indices de colonnes
		for (Cell cell : sheet.getRow(0)) 
		{
			if (cell.getCellTypeEnum() == CellType.STRING)
			{
				switch (cell.getStringCellValue())
				{
					case DIRECTION:
					    colDir = cell.getColumnIndex();
						break;
					case DEPARTEMENT:
					    colDepart = cell.getColumnIndex();
						break;
					case SERVICE:
					    colService = cell.getColumnIndex();
						break;
					case RESPSERVICE:
					    colResp = cell.getColumnIndex();
						break;
					case CLARITY:
					    colClarity = cell.getColumnIndex();
						break;
					case LIBELLE:
					    colLib = cell.getColumnIndex();
						break;
					case CPI:
					    colCpi = cell.getColumnIndex();
						break;
					case EDITION:
					    colEdition = cell.getColumnIndex();
						break;
					case LOT:
					    colLot = cell.getColumnIndex();
						break;
					case ENV:
					    colEnv = cell.getColumnIndex();
						break;
					case ANOMALIE:
					    colAno = cell.getColumnIndex();
						break;
					case ETAT:
					    colEtat = cell.getColumnIndex();
						break;						
					case REMARQUE:
					    colRemarque = cell.getColumnIndex();
						break;						
					default:
						// Colonne sans nom reconnu
						break;
				}
			}
		}	
	}
	
	   /*---------- METHODES PRIVEES ----------*/
	
	/**
	 * Liste des numéros de colonnes des feuilles d'environnement
	 * @author ETP8137 - Grégoire mathon
	 *
	 */
	private enum Index
	{
		LOTI, EDITIONI, ENVI, TRAITEI;
	}
	/*---------- ACCESSEURS ----------*/
}