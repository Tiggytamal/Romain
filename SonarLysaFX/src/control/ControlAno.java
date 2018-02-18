package control;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;

import model.enums.Environnement;
import model.excel.Anomalie;

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
	@SuppressWarnings("unused")
	private int colDir;
	@SuppressWarnings("unused")
	private int colDepart;
	@SuppressWarnings("unused")
	private int colService;
	@SuppressWarnings("unused")
	private int colresp;
	@SuppressWarnings("unused")
	private int colClarity;
	@SuppressWarnings("unused")
	private int colLib;
	@SuppressWarnings("unused")
	private int colCpi;
	@SuppressWarnings("unused")
	private int colEdition;
	private int colLot;
	@SuppressWarnings("unused")
	private int colEnv;
	@SuppressWarnings("unused")
	private int colAno;
	@SuppressWarnings("unused")
	private int colEtat;
	@SuppressWarnings("unused")
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
	
	// Nom de la feuillle avec les naomalies en cours
	private static final String FP = "SUIVI Qualité";
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlAno(File file) throws InvalidFormatException, IOException
	{
		super(file);
	}
	
	/*---------- METHODES PUBLIQUES ----------*/
		
	protected List<String> listAnomaliesSurLotsCrees()
	{
		// Récupération de la première feuille
		Sheet sheet = wb.getSheet(FP);
		
		List<String> retour = new ArrayList<>();
		
		for (int i = 1; i <= sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
			String string = row.getCell(colLot).getStringCellValue();
			if (string.startsWith("Lot "))
				string = string.substring(4);
		    retour.add(string);
		}
		return retour;
	}
	
	protected void createSheetError(String nomSheet, List<Anomalie> anoAcreer) throws IOException
	{	
		// Création de la feuille de calcul
		Sheet sheet = wb.getSheet(nomSheet);
		if (sheet != null)
			wb.removeSheetAt(wb.getSheetIndex(sheet));		
		sheet = wb.createSheet(nomSheet);
		Cell cell;
		
		// Création des styles de cellules
		CellStyle style = wb.createCellStyle();
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        
        CellStyle styleTitre = wb.createCellStyle();
        styleTitre.cloneStyleFrom(style);
        styleTitre.setAlignment(HorizontalAlignment.CENTER);
        styleTitre.setFillForegroundColor(IndexedColors.AQUA.index);
        styleTitre.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTitre.setBorderBottom(BorderStyle.THIN);
        
        CellStyle styleJaune = wb.createCellStyle();
        styleJaune.cloneStyleFrom(style);       
		styleJaune.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
		styleJaune.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		
		CellStyle styleCentre = wb.createCellStyle();
		styleCentre.cloneStyleFrom(style);
		styleCentre.setAlignment(HorizontalAlignment.CENTER);
		
		// Création des noms des colonnes
		Row titres = sheet.createRow(0);
		for (Index index : Index.values())
		{
			cell = titres.createCell(index.ordinal());
			cell.setCellStyle(styleTitre);	
			switch (index)
			{
				case LOTE:
					cell.setCellValue(LOT);
					break;
				case EDITIONE:
					cell.setCellValue(EDITION);
					break;
				case ENVE:
					cell.setCellValue(ENV);
					break;
			}
		}
		
		for (Anomalie anomalie : anoAcreer)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() +1 );
			Environnement env = anomalie.getEnvironnement();
			if (env == Environnement.VMOA || env == Environnement.EDITION)
			{
				cell = row.createCell(Index.LOTE.ordinal());
				cell.setCellValue(anomalie.getLot());
				cell.setCellStyle(styleCentre);
				cell = row.createCell(Index.EDITIONE.ordinal());
				cell.setCellValue(anomalie.getEdition());
				cell.setCellStyle(style);
				cell = row.createCell(Index.ENVE.ordinal());
				cell.setCellValue(anomalie.getEnvironnement().toString());
				cell.setCellStyle(styleCentre);
			}
			else
			{
				cell = row.createCell(Index.LOTE.ordinal());
				cell.setCellStyle(styleCentre);
				cell.setCellValue(anomalie.getLot());
				cell = row.createCell(Index.EDITIONE.ordinal());
				cell.setCellStyle(style);
				cell.setCellValue(anomalie.getEdition());
				cell = row.createCell(Index.ENVE.ordinal());
				cell.setCellStyle(styleCentre);
				cell.setCellValue(anomalie.getEnvironnement().toString());	
			}			
		}
		
		sheet.autoSizeColumn(Index.LOTE.ordinal());
		sheet.autoSizeColumn(Index.EDITIONE.ordinal());
		sheet.autoSizeColumn(Index.ENVE.ordinal());
		// Ecriture du fichier Excel
		write();
	}
	
	/**
	 * Permet de mettre à jour les anomalies avec une quality Gate bonne
	 * @param lotAnos
	 * 				Liste de tous les lots avec un Quality Gate à "ERROR".
	 * @throws IOException 
	 */
	protected void majAnoOK(Set<String> lotAnos) throws IOException
	{
		// Récupération de la fuille avec les anomalies
		Sheet sheet = wb.getSheet(FP);
		
		// Itération sur chaque ligne
		for (int i = 1; i <= sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
			
			// Si le numéro n'est pas présent dans la liste, c'est que le Quality gate est bon
			String string = row.getCell(colLot).getStringCellValue().substring(4);
			if (!lotAnos.contains(string))
			{
	           majCouleurLigne(row, IndexedColors.LIGHT_GREEN);
			}
		}		
		
		//Ecriture fu fichier
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
					    colresp = cell.getColumnIndex();
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
		LOTE,EDITIONE,ENVE;
	}
	/*---------- ACCESSEURS ----------*/
}
