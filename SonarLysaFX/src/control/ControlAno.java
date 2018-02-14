package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import model.enums.Environnement;
import model.excel.Anomalie;

public class ControlAno extends ControlExcel
{
	/*---------- ATTRIBUTS ----------*/
	
	// Liste des indices des colonnes
	private int colDir;
	private int colDepart;
	private int colService;
	private int colresp;
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
	
	// Nom de la feuillle avec les naomalies en cours
	private static final String FP = "SUIVI Qualité";
	
	/*---------- CONSTRUCTEURS ----------*/
	
	protected ControlAno(File file) throws InvalidFormatException, IOException
	{
		super(file);
		calculIndiceColonnes();
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
		if (nomSheet == null)
			return;
		Sheet sheet = wb.createSheet(nomSheet);
		CellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		for (Anomalie anomalie : anoAcreer)
		{
			Row row = sheet.createRow(sheet.getLastRowNum() +1 );
			Environnement env = anomalie.getEnvironnement();
			if (env == Environnement.VMOA || env == Environnement.EDITION)
			{
				Cell cell = row.createCell(0);
				cell.setCellStyle(style);
				cell.setCellValue(anomalie.getLot());
				cell = row.createCell(1);
				cell.setCellStyle(style);
				cell.setCellValue(anomalie.getEdition());
				cell = row.createCell(2);
				cell.setCellStyle(style);
				cell.setCellValue(anomalie.getEnvironnement().toString());			
			}
			else
			{
				row.createCell(0).setCellValue(anomalie.getLot());
				row.createCell(1).setCellValue(anomalie.getEdition());
				row.createCell(2).setCellValue(anomalie.getEnvironnement().toString());
			}			
		}
		
		// Ecriture du fichier Excel
		wb.write(new FileOutputStream(file.getName()));
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
			if (lotAnos.contains(row.getCell(colLot).getStringCellValue()))
			{
	           majCouleurLigne(row, IndexedColors.LIGHT_GREEN);
			}
		}		
		
		//Ecriture fu fichier
		wb.write(new FileOutputStream(file.getName()));
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	/**
	 * initialise les numéro des colonnes du fichier Excelvenant de la PIC.
	 */
	private void calculIndiceColonnes()
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
	/*---------- ACCESSEURS ----------*/
}
