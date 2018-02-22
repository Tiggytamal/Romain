package control;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;

import model.LotSuiviPic;

import org.apache.poi.ss.usermodel.Sheet;

import sonarapi.model.Vue;
import utilities.DateConvert;

public class ControlPic extends ControlExcel
{
	/*---------- ATTRIBUTS ----------*/

	// Liste des indices des colonnes
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
	
	// Liste des noms de colonnes
	private static final String LOT = "Lot";
	private static final String LIBELLE = "Libell�";
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
	private static final String LIV = "Livraison �dition";

	/*---------- CONSTRUCTEURS ----------*/

	public ControlPic(File file) throws InvalidFormatException, IOException
	{
		super(file);
	}

	/*---------- METHODES PUBLIQUES ----------*/
	
	/**
	 * Permet de classer tous les lots Sonar du fichier dans une map. On enl�ve d'abord tout ceux qui ne sont pas pr�sents dans SonarQube.<br>
	 * Puis on les classes dans des listes, la clef de chaque liste correspond au mois et � l'ann�e de mise en production du lot.<br>
	 * Le fichier excel doit avoir un formattage sp�cifique, avec une colonne <b>Lot</b> (num�rique) et un colonne <b>livraison �dition</b> (date).<br>
	 * 
	 * @param file
	 * 			Le fichier excel envoy� par l'interface
	 * @return
	 * @throws FileNotFoundException 
	 * @throws EncryptedDocumentException 
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	protected Map<LocalDate, List<Vue>> recupLotsExcelPourMEP(Map<String, Vue> mapQube) throws IOException
	{
		// R�cup�ration de la premi�re feuille
		Sheet sheet = wb.getSheetAt(0);
		
		//Traitement Excel et contr�le avec SonarQube
		Map<LocalDate, List<Vue>> retour = creerMapVuesParMois(sheet, mapQube);
		
		// Ecriture du fichier Excel
		write();

		return retour;
	}
	
	/**
	 * Permet de remonter tous les lots depuis l'extraction de l'outils de suivi Pic
	 * 
	 * @return
	 */
	protected Map<String, LotSuiviPic> recupLotsDepuisPic()
	{
		// Map de retour
		Map<String, LotSuiviPic> retour = new HashMap<>();

		// It�ration sur toutes les feuilles du fichier Excel
		Iterator<Sheet> iter = wb.sheetIterator();
		while (iter.hasNext())
		{
			Sheet sheet = iter.next();	
			
			// It�ration sur chaque ligne pour r�cup�rer les donn�es
			for (int i = 1; i < sheet.getLastRowNum(); i++)
			{
				Row row = sheet.getRow(i);
				
				// Cr�ation de l'objet
				LotSuiviPic lot = new LotSuiviPic();
				lot.setLot(String.valueOf((int) row.getCell(colLot, MissingCellPolicy.CREATE_NULL_AS_BLANK).getNumericCellValue()));
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
		}
		return retour;
	}
	
	/*---------- METHODES PRIVEES ----------*/
	
	/**
	 * initialise les num�ro des colonnes du fichier Excelvenant de la PIC.
	 */
	@Override
	protected void calculIndiceColonnes()
	{
		// R�cup�ration de la premi�re feuille
		Sheet sheet = wb.getSheetAt(0);
		
		//R�cup�ration des indices de colonnes
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
						// Colonne sans nom reconnu
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
		
		// parcours de la feuille Excel pour r�cup�rer tous les lots et leurs dates de mise en production avec mise � jour du fichier Excel
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
	 * 			Ligne du fichier Excel � traiter
	 * @param colLot
	 * 			Indice de la colonne des num�ros de lot
	 * @param colDate
	 * 			Indice de la colonne des dates de livraison en production
	 * @param retour
	 * 			Map retournant tous les lots � rajouter dans la vue
	 * @param mapQube
	 * 			Map des vues retourn�es par SonarQube
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
	    
	    // On teste si le num�ro de lot est bien pr�sent dans Sonar.
	    if (mapQube.keySet().contains(lot))
	    {
	        // R�cup�ration de la date depuis le fichier Excel en format JDK 1.8.
	        LocalDate date = DateConvert.localDate(cellDate.getDateCellValue());
	        // Cr�ation d'une nouvelle date au 1er du mois qui servira du clef � la map.
            LocalDate clef = LocalDate.of(date.getYear(), date.getMonth(), 1);
            
	        majCouleurLigne(row, IndexedColors.LIGHT_YELLOW);
            	                    
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