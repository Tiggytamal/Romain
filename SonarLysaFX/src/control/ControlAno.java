package control;

import static control.view.MainScreen.param;
import static utilities.Statics.loginconnue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Row.MissingCellPolicy;
import org.apache.poi.ss.usermodel.Sheet;

import model.Anomalie;
import model.InfoClarity;
import model.enums.Environnement;
import model.enums.TypeParam;
import utilities.CellHelper;
import utilities.FunctionalException;
import utilities.enums.Bordure;
import utilities.enums.Severity;

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
    private int colSec;
    private int colRemarque;
    private int colVer;

    // Liste des noms de colonnes
    private static final String DIRECTION = "Direction";
    private static final String DEPARTEMENT = "Département";
    private static final String SERVICE = "Service";
    private static final String RESPSERVICE = "Responsable Service";
    private static final String CLARITY = "Projet Clarity";
    private static final String LIBELLE = "Libelle projet";
    private static final String CPI = "CPI du lot";
    private static final String EDITION = "Edition";
    private static final String LOT = "Lot projet RTC";
    private static final String ENV = "Etat du lot";
    private static final String ANOMALIE = "Anomalie";
    private static final String ETAT = "Etat de l'anomalie";
    private static final String SECURITE = "Sécurité";
    private static final String REMARQUE = "Remarque";
    private static final String TRAITE = "Traité";
    private static final String VERSION = "Version";

    // Nom de la feuillle avec les naomalies en cours
    private static final String SQ = "SUIVI Qualité";
    private static final String AC = "Anomalies closes";
    private static final String CLOSE = "Close";
    private static final String ABANDONNEE = "Abandonnée";
    private static final String LIENSLOTS = "http://ttp10-snar.ca-technologies.fr/governance?id=view_lot_";
    private static final String LIENSANO = "https://ttp10-jazz.ca-technologies.credit-agricole.fr/ccm/web/projects/Support%20NICE#action=com.ibm.team.workitem.viewWorkItem&id=";
    private static final String SECURITEKO = "X";
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";

    /*---------- CONSTRUCTEURS ----------*/

    protected ControlAno(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    protected List<Anomalie> listAnomaliesSurLotsCrees()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);

        // Liste de retour
        List<Anomalie> retour = new ArrayList<>();

        // Itération sur chaque ligne pour créer les anomalies
        for (int i = 1; i <= sheet.getLastRowNum(); i++)
        {
            Row row = sheet.getRow(i);

            // Création de l'anomalie
            Anomalie ano = new Anomalie();
            ano.setDirection(getCellValue(row, colDir));
            ano.setDepartement(getCellValue(row, colDepart));
            ano.setService(getCellValue(row, colService));
            ano.setResponsableService(getCellValue(row, colResp));
            ano.setProjetClarity(getCellValue(row, colClarity));
            ano.setLibelleProjet(getCellValue(row, colLib));
            ano.setCpiProjet(getCellValue(row, colCpi));
            ano.setEdition(getCellValue(row, colEdition));
            ano.setLot(getCellValue(row, colLot));
            ano.setEnvironnement(Environnement.getEnvironnement(getCellValue(row, colEnv)));

            // Numéro anomalie
            Cell cellAno = row.getCell(colAno, MissingCellPolicy.CREATE_NULL_AS_BLANK);
            ano.setNumeroAnomalie((int) cellAno.getNumericCellValue());
            // Si le liens n'est pas nul on le sauvegarde
            if (cellAno.getHyperlink() != null)
                ano.setLiensAno(cellAno.getHyperlink().getAddress());

            ano.setEtat(getCellValue(row, colEtat));
            ano.setSecurite(getCellValue(row, colSec));
            ano.setRemarque(getCellValue(row, colRemarque));
            ano.setVersion(getCellValue(row, colVer));
            retour.add(ano);
        }
        return retour;
    }

    /**
     * Permet de créer les feuille des anomalies par version et retourne les anomalies à créer
     * @param nomSheet
     * @param anoAcreer
     * @return
     * @throws IOException
     */
    protected List<Anomalie> createSheetError(String nomSheet, List<Anomalie> anoAcreer) throws IOException
    {
        // Création de la feuille de calcul
        Sheet sheet = wb.getSheet(nomSheet);

        List<Anomalie> retour = new ArrayList<>();

        // Liste des lots existants. On itère si la feuille existe déjà, pour récupérer tous les numéros de lot déjà enregistrés.
        List<String> lotsTraites = new ArrayList<>();
        if (sheet != null)
        {
            Iterator<Row> iter = sheet.rowIterator();
            while (iter.hasNext())
            {
                Row row = iter.next();
                Cell cellLot = row.getCell(Index.LOTI.ordinal());
                Cell cellT = row.getCell(Index.TRAITEI.ordinal());
                if (cellLot.getCellTypeEnum() == CellType.STRING && cellT.getCellTypeEnum() == CellType.STRING && cellT.getStringCellValue().equals("O"))
                    lotsTraites.add(row.getCell(Index.LOTI.ordinal()).getStringCellValue());
            }
            wb.removeSheetAt(wb.getSheetIndex(sheet));
        }

        // Recréation de la feuille
        sheet = wb.createSheet(nomSheet);
        Cell cell;

        // Création du style des titres
        CellStyle styleTitre = helper.getStyle(IndexedColors.AQUA, Bordure.VIDE, HorizontalAlignment.CENTER);

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
                case TRAITEI:
                    cell.setCellValue(TRAITE);
                    break;
            }
        }

        // Itération sur les anomalies à créer. Si elles sont déjà dans les anomalies traitées, on créée une ligne à l'état traitée sinon on crée une ligne à
        // l'état non traité et on ajoute celle-ci aux anomalies à créer.
        for (Anomalie anomalie : anoAcreer)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (lotsTraites.contains(anomalie.getLot()))
            {
                creerLigneVersion(row, anomalie, IndexedColors.WHITE, "O");
            }
            else
            {
                creerLigneVersion(row, anomalie, IndexedColors.LIGHT_YELLOW, "N");
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

    protected Sheet sauvegardeFichier(String fichier) throws IOException
    {
        // Récupération feuille principale existante
        Sheet retour = wb.getSheet(SQ);
        if (retour != null)
        {
            // Création du fichier de sauvegarde et effacement de la feuille
            wb.write(new FileOutputStream(new StringBuilder(param.getMapParams().get(TypeParam.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString()));
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }
        retour = wb.createSheet(SQ);
        creerLigneTitres(retour);
        return retour;       
    }
    
    /**
     * Gestion de la feuille principale des anomalies. Maj des anciennes plus création des nouvelles
     * 
     * @param anoAajouter
     * @param lotsSecurite
     * @param anoAajouter2
     * @param lotsEnErreur
     * @throws IOException
     * @throws InvalidFormatException
     * @throws EncryptedDocumentException
     */
    protected void majFeuillePrinciale(List<Anomalie> lotsEnAno, List<Anomalie> anoAajouter, Set<String> lotsEnErreurSonar, Set<String> lotsSecurite, Set<String> lotsRelease, Sheet sheet) throws IOException
    {
        // Récupération feuille  et liste des anomalies closes
        List<String> anoClose = new ArrayList<>();
        Sheet sheetClose = gestionAnomaliesCLoses(anoClose);

        // Mise à jour anomalies déjà créées
        for (Anomalie ano : lotsEnAno)
        {
            Row row;
            String lot = ano.getLot().substring(4);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(lot))
                ano.setSecurite(SECURITEKO);

            // Si une anomalie est close dans RTC, on la transfert sur l'autre feuille.
            if (CLOSE.equals(ano.getEtat()) || ABANDONNEE.equals(ano.getEtat()))
            {
                row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.WHITE);
                continue;
            }

            // Mise en vert des anomalies avec un Quality Gate bon
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (!lotsEnErreurSonar.contains(lot))
            {
                creerLigneSQ(row, ano, IndexedColors.LIGHT_GREEN);
            }
            else
            {
                if (lotsRelease.contains(lot))
                {
                    ano.setVersion(RELEASE);
                    creerLigneSQ(row, ano, IndexedColors.LIGHT_YELLOW);
                }
                else
                {
                    ano.setVersion(SNAPSHOT);
                    creerLigneSQ(row, ano, IndexedColors.WHITE);
                }
            }
        }

        // Ajout des nouvelles anomalies
        ajouterNouvellesAnos(sheet, anoAajouter, anoClose, lotsSecurite, lotsRelease);

        autosizeColumns(sheet);
        autosizeColumns(sheetClose);
        write();
    }

    @Override
    protected void calculIndiceColonnes()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier n'a pas de page Suivi Qualité");

        titres = sheet.getRow(0);

        // Récupération des indices de colonnes
        for (Cell cell : titres)
        {
            if (cell.getCellTypeEnum() == CellType.STRING)
            {
                switch (cell.getStringCellValue())
                {
                    case DIRECTION:
                        colDir = cell.getColumnIndex();
                        testMax(colDir);
                        break;
                    case DEPARTEMENT:
                        colDepart = cell.getColumnIndex();
                        testMax(colDepart);
                        break;
                    case SERVICE:
                        colService = cell.getColumnIndex();
                        testMax(colDir);
                        break;
                    case RESPSERVICE:
                        colResp = cell.getColumnIndex();
                        testMax(colService);
                        break;
                    case CLARITY:
                        colClarity = cell.getColumnIndex();
                        testMax(colClarity);
                        break;
                    case LIBELLE:
                        colLib = cell.getColumnIndex();
                        testMax(colLib);
                        break;
                    case CPI:
                        colCpi = cell.getColumnIndex();
                        testMax(colCpi);
                        break;
                    case EDITION:
                        colEdition = cell.getColumnIndex();
                        testMax(colEdition);
                        break;
                    case LOT:
                        colLot = cell.getColumnIndex();
                        testMax(colLot);
                        break;
                    case ENV:
                        colEnv = cell.getColumnIndex();
                        testMax(colEnv);
                        break;
                    case ANOMALIE:
                        colAno = cell.getColumnIndex();
                        testMax(colAno);
                        break;
                    case ETAT:
                        colEtat = cell.getColumnIndex();
                        testMax(colEtat);
                        break;
                    case SECURITE:
                        colSec = cell.getColumnIndex();
                        testMax(colSec);
                        break;
                    case REMARQUE:
                        colRemarque = cell.getColumnIndex();
                        testMax(colRemarque);
                        break;
                    case VERSION:
                        colVer = cell.getColumnIndex();
                        testMax(colVer);
                        break;
                    default:
                        // Colonne sans nom reconnu
                        break;
                }
            }
        }
    }

    /*---------- METHODES PRIVEES ----------*/

    private void creerLigneSQ(Row row, Anomalie ano, IndexedColors couleur)
    {
        // Contrôles
        if (couleur == null || row == null || ano == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls");

        // Variables
        CellHelper helper = new CellHelper(wb);
        CellStyle normal = null;
        CellStyle centre = null;

        // Switch sur la couleur pour créer les styles
        normal = helper.getStyle(couleur);
        centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        // Contrôle Clarity et mise à jour données
        controleClarity(ano);

        // Alimentation avec les données de l'anomalie
        Cell cell = row.createCell(colDir);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getDirection());
        cell = row.createCell(colDepart);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getDepartement());
        cell = row.createCell(colService);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getService());
        cell = row.createCell(colResp);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getResponsableService());
        cell = row.createCell(colClarity);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getProjetClarity());
        cell = row.createCell(colLib);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getLibelleProjet());
        cell = row.createCell(colCpi);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getCpiProjet());
        cell = row.createCell(colEdition);
        cell.setCellStyle(centre);
        cell.setCellValue(ano.getEdition());
        cell = row.createCell(colLot);
        cell.setCellStyle(centre);

        // Gestion des numéros de lots
        cell.setCellValue(ano.getLot());
        ajouterLiens(cell, LIENSLOTS, ano.getLot().substring(4));

        // Gestion de la variable d'environnement
        cell = row.createCell(colEnv);
        cell.setCellStyle(centre);
        if (ano.getEnvironnement() != null)
            cell.setCellValue(ano.getEnvironnement().toString());

        // Gestion des numéros d'anomalies
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnomalie();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);
            ajouterLiens(cell, LIENSANO, String.valueOf(numeroAno));
        }
        cell = row.createCell(colEtat);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getEtat());
        cell = row.createCell(colSec);
        cell.setCellStyle(centre);
        cell.setCellValue(ano.getSecurite());
        cell = row.createCell(colRemarque);
        cell.setCellStyle(normal);
        cell.setCellValue(ano.getRemarque());
        cell = row.createCell(colVer);
        cell.setCellStyle(centre);
        cell.setCellValue(ano.getVersion());
    }

    private void creerLigneVersion(Row row, Anomalie ano, IndexedColors couleur, String traite)
    {
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        Cell cell = row.createCell(Index.LOTI.ordinal());
        cell.setCellValue(ano.getLot());
        cell.setCellStyle(centre);
        cell = row.createCell(Index.EDITIONI.ordinal());
        cell.setCellValue(ano.getEdition());
        cell.setCellStyle(helper.getStyle(couleur));
        cell = row.createCell(Index.ENVI.ordinal());
        cell.setCellValue(ano.getEnvironnement().toString());
        cell.setCellStyle(centre);
        cell = row.createCell(Index.TRAITEI.ordinal());
        cell.setCellValue(traite);
        cell.setCellStyle(centre);
    }

    private void creerLigneTitres(Sheet sheet)
    {
        // Création de la ligne de titres
        Row titresNew = sheet.createRow(0);

        // On itère sur la ligne de titres
        for (int i = 0; i < titres.getLastCellNum(); i++)
        {
            Cell newCell = titresNew.createCell(i);
            Cell oldCell = titres.getCell(i);

            // On continue si la cellule du titre est nulle
            if (oldCell == null)
            {
                continue;
            }

            // Copie du style des cellules
            CellStyle newCellStyle = wb.createCellStyle();
            newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
            newCell.setCellStyle(newCellStyle);

            // Copie des titres
            newCell.setCellValue(oldCell.getStringCellValue());
            switch (oldCell.getCellTypeEnum())
            {
                case BLANK:
                    newCell.setCellValue(oldCell.getStringCellValue());
                    break;
                case BOOLEAN:
                    newCell.setCellValue(oldCell.getBooleanCellValue());
                    break;
                case ERROR:
                    newCell.setCellErrorValue(oldCell.getErrorCellValue());
                    break;
                case FORMULA:
                    newCell.setCellFormula(oldCell.getCellFormula());
                    break;
                case NUMERIC:
                    newCell.setCellValue(oldCell.getNumericCellValue());
                    break;
                case STRING:
                    newCell.setCellValue(oldCell.getRichStringCellValue());
                    break;
                case _NONE:
                    break;
            }
        }
    }

    private void ajouterLiens(Cell cell, String baseAdresse, String variable)
    {
        if (cell == null || baseAdresse == null || baseAdresse.isEmpty())
            throw new IllegalArgumentException("La cellule ou l'adresse ne peuvent être nulles");
        Hyperlink link = createHelper.createHyperlink(HyperlinkType.URL);
        Font font = wb.createFont();
        font.setUnderline(Font.U_SINGLE);
        font.setColor(IndexedColors.BLUE.index);
        cell.getCellStyle().setFont(font);
        link.setAddress(baseAdresse + variable);
        cell.setHyperlink(link);
    }

    /**
     * 
     * @param row
     * @param cellIndex
     * @return
     */
    private String getCellValue(Row row, int cellIndex)
    {
        return row.getCell(cellIndex, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue();
    }

    /**
     * 
     * @param sheet
     * @param anoAajouter
     * @param lotsSecurite
     * @param lotsRelease
     */
    private void ajouterNouvellesAnos(Sheet sheet, List<Anomalie> anoAajouter, List<String> anoClose, Set<String> lotsSecurite, Set<String> lotsRelease)
    {
        for (Anomalie ano : anoAajouter)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            String lot = ano.getLot().substring(4);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(lot))
                ano.setSecurite(SECURITEKO);

            // Ajout de la donnée de version
            if (lotsRelease.contains(lot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Création de la ligne
            if (anoClose.contains(lot))
                creerLigneSQ(row, ano, IndexedColors.GREY_25_PERCENT);
            else
                creerLigneSQ(row, ano, IndexedColors.LIGHT_ORANGE);
        }
    }

    /**
     * Retourne la feuille des anomalies closes en remplissant la liste de celles-ci.
     * 
     * @param anoClose
     * @return
     */
    private Sheet gestionAnomaliesCLoses(List<String> anoClose)
    {
        Sheet retour = wb.getSheet(AC);
        if (retour == null)
        {
            retour = wb.createSheet(AC);
            creerLigneTitres(retour);
        }
        
        for (Iterator<Row> iter = retour.rowIterator(); iter.hasNext();)
        {
            Row row = iter.next();
            anoClose.add(row.getCell(colLot, MissingCellPolicy.CREATE_NULL_AS_BLANK).getStringCellValue());
        }
        return retour;
    }
    
    /**
     * Contrôle si le code clarity de l'anomalie est bien dans le fichier Excel et renseigne les informations depuis celui-ci
     * 
     * @param ano
     */
    private Anomalie controleClarity(Anomalie ano)
    {
        // Récupération infox Clarity depuis fichier Excel
        Map<String, InfoClarity> map = param.getMapClarity();
        String clarity = ano.getProjetClarity();
        Set<String> keyset = map.keySet();

        // Vérification si le code Clarity de l'anomalie est bine dans la map
        if (keyset.contains(clarity))
        {
            InfoClarity info = map.get(clarity);
            ano.setDepartement(info.getDepartement());
            ano.setDirection(info.getDirection());
            ano.setService(info.getDirection());
            return ano;
        }

        // Sinon on itère sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (String key : keyset)
        {
            // ON retire les deux dernières lettres pour les clefs de puis de 6 caractères.
            if (clarity.equals(key.length() < 6 ? key :  key.substring(0, 6)))
            {
                InfoClarity info = map.get(key);
                ano.setDepartement(info.getDepartement());
                ano.setDirection(info.getDirection());
                ano.setService(info.getDirection());
                return ano;
            }
        }

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        loginconnue.warn("Code Clarity inconnu : " + clarity + " - Lot : " + ano.getLot());
        return ano;
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Liste des numéros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Grégoire mathon
     *
     */
    private enum Index 
    {
        LOTI, EDITIONI, ENVI, TRAITEI;
    }
}