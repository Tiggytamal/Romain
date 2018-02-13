package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;

import model.excel.LotSuiviPic;
import sonarapi.model.Vue;
import utilities.DateConvert;

public class ControlPic extends ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	/** fichier Excel transmis à l'application */
	private int colLot;
	private int colLibelle;
	private int colClarity;
	private int colCpi;
	private int colEdition;
	private int colNbCompos;
	private int colNbpaquets;
	private int colBuild;
	private int colDevtu;
	private int colTfon;
	private int colVmoe;
	private int colVmoa;
	private int colLiv;
	private static final String LOT = "Lot";
	private static final String LIBELLE = "Libellé";
	private static final String CLARITY = "Clarity";
	private static final String CPI = "CPI";
	private static final String EDITION = "Edition";
	private static final String NBCOMPOS = "Nb Composants";
	private static final String NBPAQUETS = "Nb Paquets";
	private static final String BUILD = "1er build";
	private static final String DEVTU = "DEVTU";
	private static final String TFON = "TFON";
	private static final String VMOE = "VMOE";
	private static final String VMOA = "VMOA";
	private static final String LIV = "Livraison édition";

	/*---------- CONSTRUCTEURS ----------*/

	public ControlPic(File file) throws InvalidFormatException, IOException
	{
		super(file);
		calculIndiceColonnes();
	}

	/*---------- METHODES PUBLIQUES ----------*/
	
	/**
	 * Permet de classer tous les lots Sonar du fichier dans une map. On enlève d'abord tout ceux qui ne sont pas présents dans SonarQube.<br>
	 * Puis on les classes dans des listes, la clef de chaque liste correspond au mois et à l'année de mise en production du lot.<br>
	 * Le fichier excel doit avoir un formattage spécifique, avec une colonne <b>Lot</b> (numérique) et un colonne <b>livraison édition</b> (date).<br>
	 * 
	 * @param file
	 * 			Le fichier excel envoyé par l'interface
	 * @return
	 * @throws FileNotFoundException 
	 * @throws EncryptedDocumentException 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	protected Map<LocalDate, List<Vue>> recupLotExcel(Map<String, Vue> mapQube) throws IOException
	{
		// Récupération de la première feuille
		Sheet sheet = wb.getSheetAt(0);
		
		//Traitement Excel et contrôle avec SonarQube
		Map<LocalDate, List<Vue>> retour = creerMapVuesParMois(sheet, mapQube);
		
		// Ecriture du fichier Excel
		wb.write(new FileOutputStream(file.getName()));

		return retour;
	}
	
	protected Map<String, LotSuiviPic> recupLotAnomalieExcel()
	{
		Map<String, LotSuiviPic> retour = new HashMap<>();
		// Récupération de la première feuille
		Sheet sheet = wb.getSheetAt(0);
		
		// Itération sur chaque ligne pour récupérer les données
		for (int i = 1; i < sheet.getLastRowNum(); i++)
		{
			Row row = sheet.getRow(i);
			// Création de l'objet
			LotSuiviPic lot = new LotSuiviPic();
			lot.setLot(String.valueOf(row.getCell(colLot, MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue()));
			lot.setLibelle(row.getCell(colLibelle, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
			lot.setProjetClarity(row.getCell(colClarity, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
			lot.setCpiProjet(row.getCell(colCpi, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
			lot.setEdition(row.getCell(colEdition, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
			lot.setNbreComposants((int) row.getCell(colNbCompos, MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue()); 
			lot.setNbrePaquets((int) row.getCell(colNbpaquets, MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue());
			lot.setBuild(DateConvert.localDate(row.getCell(colBuild, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));
			lot.setDevtu(DateConvert.localDate(row.getCell(colDevtu, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));
			lot.setTfon(DateConvert.localDate(row.getCell(colTfon, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));
			lot.setVmoe(DateConvert.localDate(row.getCell(colVmoe, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));
			lot.setVmoa(DateConvert.localDate(row.getCell(colVmoa, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));
			lot.setLivraison(DateConvert.localDate(row.getCell(colLiv, MissingCellPolicy.CREATE_NULL_AS_BLANK).getDateCellValue()));			
			retour.put(lot.getLot(), lot);
		}		
		return retour;
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
					case LOT:
					    colLot = cell.getColumnIndex();
						break;
					case LIBELLE:
					    colLibelle = cell.getColumnIndex();
						break;
					case CLARITY:
					    colClarity = cell.getColumnIndex();
						break;
					case CPI:
					    colCpi = cell.getColumnIndex();
						break;
					case EDITION:
					    colEdition = cell.getColumnIndex();
						break;
					case NBCOMPOS:
					    colNbCompos = cell.getColumnIndex();
						break;
					case NBPAQUETS:
					    colNbpaquets = cell.getColumnIndex();
						break;
					case BUILD:
					    colBuild = cell.getColumnIndex();
						break;
					case DEVTU:
					    colDevtu = cell.getColumnIndex();
						break;
					case TFON:
					    colTfon = cell.getColumnIndex();
						break;
					case VMOE:
					    colVmoe = cell.getColumnIndex();
						break;
					case VMOA:
					    colVmoa = cell.getColumnIndex();
						break;						
					case LIV:
					    colLiv = cell.getColumnIndex();
						break;
						
					default:
						break;
				}
			}
		}	
	}
	
	/**
	 * 
	 * @param sheet
	 * @param wb
	 * @param mapQube
	 * @return
	 */
	private Map<LocalDate, List<Vue>> creerMapVuesParMois(Sheet sheet, Map<String, Vue> mapQube)
	{
		// Initialisation de la map de retour		
		Map<LocalDate, List<Vue>> retour = new HashMap<>();
		
		// parcours de la feuille Excel pour récupérer tous les lots et leurs dates de mise en production avec mise à jour du fichier Excel
		for (int i = 1; i < sheet.getLastRowNum(); i++) 
		{
			Row row = sheet.getRow(i);
		    traitementLigne(row, colLot, colLiv, retour, mapQube);
		}
		return retour;		
	}

	
	/**
	 * Effectue le traitement de chaque ligne du fichier Excel
	 * 
	 * @param row
	 * 			Ligne du fichier Excel à traiter
	 * @param colLot
	 * 			Indice de la colonne des numéros de lot
	 * @param colDate
	 * 			Indice de la colonne des dates de livraison en production
	 * @param retour
	 * 			Map retournant tous les lots à rajouter dans la vue
	 * @param mapQube
	 * 			Map des vues retournées par SonarQube
	 * @param wb
	 * 			Workbook 
	 */
	private void traitementLigne(Row row, int colLot, int colDate, Map<LocalDate, List<Vue>> retour, Map<String, Vue> mapQube)
	{
	    Cell cellLot = row.getCell(colLot);
	    Cell cellDate = row.getCell(colDate);
	    
	    if (cellLot.getCellTypeEnum() != CellType.NUMERIC && cellDate.getCellTypeEnum() != CellType.NUMERIC)
	        return;
	    
	    String lot = String.valueOf((int)cellLot.getNumericCellValue());
	    
	    // ON teste si le numéro de lot est bien présent dans Sonar.
	    if (mapQube.keySet().contains(lot))
	    {
	        // Récupération de la date depuis le fichier Excel en format JDK 1.8.
	        LocalDate date = DateConvert.localDate(cellDate.getDateCellValue());
	        // Création d'une nouvelle date au 1er du mois qui servira du clef à la map.
            LocalDate clef = LocalDate.of(date.getYear(), date.getMonth(), 1);
            
            // Itération sur toutes les cellules de la ligne pour mettre à jour et changer la couleur de celles-ci.
            for (int j = 0; j < row.getLastCellNum(); j++)
			{               
            	Cell cell = row.getCell(j, MissingCellPolicy.CREATE_NULL_AS_BLANK);
				CellStyle style = wb.createCellStyle();
				style.cloneStyleFrom(cell.getCellStyle());					
				style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
				style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cell.setCellStyle(style);
			}
            	                    
            if (retour.keySet().contains(clef))
            {
                retour.get(clef).add(mapQube.get(lot));
            }
            else
            {
                List<Vue> liste = new ArrayList<>();
                liste.add(mapQube.get(lot));
                retour.put(clef, liste);
            }           	            
	    }   		
	}	
	/*---------- ACCESSEURS ----------*/
}