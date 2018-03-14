package control;

import static utilities.Statics.fichiersXML;
import static utilities.Statics.proprietesXML;
import static utilities.Statics.loginconnue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import model.Anomalie;
import model.InfoClarity;
import model.enums.Environnement;
import model.enums.TypeCol;
import model.enums.TypeParam;
import utilities.CellHelper;
import utilities.DateConvert;
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
    private int colDateCrea;
    private int colDateRel;

    // Liste des noms de colonnes
    private String direction;
    private String departement;
    private String service;
    private String respService;
    private String clarity;
    private String libelle;
    private String cpi;
    private String edition;
    private String lot;
    private String env;
    private String anomalie;
    private String etat;
    private String securite;
    private String remarque;
    private String traite;
    private String version;
    private String dateCreation;
    private String dateRelance;

    // Nom de la feuillle avec les naomalies en cours
    private static final String SQ = "SUIVI Qualité";
    private static final String AC = "Anomalies closes";
    private static final String CLOSE = "Close";
    private static final String ABANDONNEE = "Abandonnée";
    private static final String SECURITEKO = "X";
    private static final String SNAPSHOT = "SNAPSHOT";
    private static final String RELEASE = "RELEASE";
    private static final int NOMBRECOL = TypeCol.values().length;
    private String lienslots;
    private String liensAnos;

    /*---------- CONSTRUCTEURS ----------*/

    protected ControlAno(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/

    /**
     * 
     * @return
     */
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
            retour.add(creaAnodepuisExcel(row));
        }
        return retour;
    }

    /**
     * Permet de créer les feuille des anomalies par version et retourne les anomalies à créer
     * 
     * @param nomSheet
     * @param anoAcreer
     * @return
     * @throws IOException
     */
    protected List<Anomalie> createSheetError(String nomSheet, List<Anomalie> anoAcreer)
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
                    cell.setCellValue(lot);
                    break;
                case EDITIONI:
                    cell.setCellValue(edition);
                    break;
                case ENVI:
                    cell.setCellValue(env);
                    break;
                case TRAITEI:
                    cell.setCellValue(traite);
                    break;
            }
        }

        // Itération sur les anomalies à créer. Si elles sont déjà dans les anomalies traitées, on créée une ligne à l'état traitée sinon on crée une ligne à
        // l'état non traité et on ajoute celle-ci aux anomalies à créer.
        for (Anomalie ano : anoAcreer)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (lotsTraites.contains(ano.getLot()))
            {
                creerLigneVersion(row, ano, IndexedColors.WHITE, "O");
            }
            else
            {
                creerLigneVersion(row, ano, IndexedColors.LIGHT_YELLOW, "N");
                retour.add(ano);
            }
        }

        sheet.autoSizeColumn(Index.LOTI.ordinal());
        sheet.autoSizeColumn(Index.EDITIONI.ordinal());
        sheet.autoSizeColumn(Index.ENVI.ordinal());
        return retour;
    }

    /**
     * 
     * @param fichier
     * @return
     * @throws IOException
     */
    protected Sheet sauvegardeFichier(String fichier) throws IOException
    {
        // Récupération feuille principale existante
        Sheet retour = wb.getSheet(SQ);
        if (retour != null)
        {
            // Création du fichier de sauvegarde et effacement de la feuille
            wb.write(new FileOutputStream(new StringBuilder(proprietesXML.getMapParams().get(TypeParam.ABSOLUTEPATHHISTO)).append(LocalDate.now().toString()).append("-").append(fichier).toString()));
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }

        // Création des lignes de titres
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
    protected void majFeuillePrincipale(List<Anomalie> lotsEnAno, List<Anomalie> anoAajouter, Set<String> lotsEnErreurSonar, Set<String> lotsSecurite, Set<String> lotsRelease, Sheet sheet) throws IOException
    {
        // Récupération feuille et liste des anomalies closes
        Map<String, Anomalie> anoClose = new HashMap<>();
        Sheet sheetClose = saveAnomaliesCloses(anoClose);

        // Mise à jour anomalies déjà créées
        for (Anomalie ano : lotsEnAno)
        {
            Row row;
            String anoLot = ano.getLot().substring(4);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(SECURITEKO);

            // Si une anomalie est close dans RTC, on la transfert sur l'autre feuille.
            if (CLOSE.equalsIgnoreCase(ano.getEtat()) || ABANDONNEE.equals(ano.getEtat()))
            {
                row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
                creerLigneSQ(row, ano, IndexedColors.WHITE);
                continue;
            }

            // Mise en vert des anomalies avec un Quality Gate bon
            row = sheet.createRow(sheet.getLastRowNum() + 1);
            if (!lotsEnErreurSonar.contains(anoLot))
            {
                creerLigneSQ(row, ano, IndexedColors.LIGHT_GREEN);
            }
            else
            {
                if (lotsRelease.contains(anoLot))
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
        ajouterAnomaliesCloses(sheetClose, anoClose);

        autosizeColumns(sheet);
        autosizeColumns(sheetClose);
        write();
    }

    private void ajouterAnomaliesCloses(Sheet sheetClose, Map<String, Anomalie> anoClose)
    {
        Row row;
        for (Anomalie ano : anoClose.values())
        {
            row = sheetClose.createRow(sheetClose.getLastRowNum() + 1);
            creerLigneSQ(row, ano, IndexedColors.WHITE);
        }
    }

    @Override
    protected void initColonnes()
    {
        // Intialisation noms des colonnes
        Map<TypeCol, String> nomColonnes = proprietesXML.getMapColonnes();
        direction = nomColonnes.get(TypeCol.DIRECTION);
        departement = nomColonnes.get(TypeCol.DEPARTEMENT);
        service = nomColonnes.get(TypeCol.SERVICE);
        respService = nomColonnes.get(TypeCol.RESPSERVICE);
        clarity = nomColonnes.get(TypeCol.CLARITY);
        libelle = nomColonnes.get(TypeCol.LIBELLE);
        cpi = nomColonnes.get(TypeCol.CPI);
        edition = nomColonnes.get(TypeCol.EDITION);
        lot = nomColonnes.get(TypeCol.LOT);
        env = nomColonnes.get(TypeCol.ENV);
        anomalie = nomColonnes.get(TypeCol.ANOMALIE);
        etat = nomColonnes.get(TypeCol.ETAT);
        securite = nomColonnes.get(TypeCol.SECURITE);
        remarque = nomColonnes.get(TypeCol.REMARQUE);
        version = nomColonnes.get(TypeCol.VERSION);
        dateCreation = nomColonnes.get(TypeCol.DATECREATION);
        dateRelance = nomColonnes.get(TypeCol.DATERELANCE);

        Map<TypeParam, String> proprietes = proprietesXML.getMapParams();
        lienslots = proprietes.get(TypeParam.LIENSLOTS);
        liensAnos = proprietes.get(TypeParam.LIENSANOS);

    }

    @Override
    protected void calculIndiceColonnes()
    {
        // Récupération de la première feuille
        Sheet sheet = wb.getSheet(SQ);
        if (sheet == null)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier n'a pas de page Suivi Qualité");

        titres = sheet.getRow(0);
        int nbreCol = 0;

        // Récupération des indices de colonnes
        for (Cell cell : titres)
        {
            if (cell.getCellTypeEnum() != CellType.STRING)
                continue;

            if (cell.getStringCellValue().equals(direction))
            {
                colDir = cell.getColumnIndex();
                testMax(colDir);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(departement))
            {
                colDepart = cell.getColumnIndex();
                testMax(colDepart);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(service))
            {
                colService = cell.getColumnIndex();
                testMax(colService);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(respService))
            {
                colResp = cell.getColumnIndex();
                testMax(colResp);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(clarity))
            {
                colClarity = cell.getColumnIndex();
                testMax(colClarity);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(libelle))
            {
                colLib = cell.getColumnIndex();
                testMax(colLib);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(cpi))
            {
                colCpi = cell.getColumnIndex();
                testMax(colCpi);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(edition))
            {
                colEdition = cell.getColumnIndex();
                testMax(colEdition);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(lot))
            {
                colLot = cell.getColumnIndex();
                testMax(colLot);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(env))
            {
                colEnv = cell.getColumnIndex();
                testMax(colEnv);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(anomalie))
            {
                colAno = cell.getColumnIndex();
                testMax(colAno);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(etat))
            {
                colEtat = cell.getColumnIndex();
                testMax(colEtat);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(securite))
            {
                colSec = cell.getColumnIndex();
                testMax(colSec);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(remarque))
            {
                colRemarque = cell.getColumnIndex();
                testMax(colRemarque);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(version))
            {
                colVer = cell.getColumnIndex();
                testMax(colVer);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(dateCreation))
            {
                colDateCrea = cell.getColumnIndex();
                testMax(colDateCrea);
                nbreCol++;
            }
            else if (cell.getStringCellValue().equals(dateRelance))
            {
                colDateRel = cell.getColumnIndex();
                testMax(colDateRel);
                nbreCol++;
            }

        }
        if (nbreCol != NOMBRECOL)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier excel est mal configuré, vérifié les colonnes de celui-ci");
    }

    /*---------- METHODES PRIVEES ----------*/

    /**
     * 
     * @param row
     * @param ano
     * @param couleur
     */
    private void creerLigneSQ(Row row, Anomalie ano, IndexedColors couleur)
    {
        // 1. Contrôles
        if (couleur == null || row == null || ano == null)
            throw new IllegalArgumentException("Les arguments ne peuvent pas être nuls");

        // 2. Helper
        CellHelper helper = new CellHelper(wb);

        // 3. Création des styles
        CellStyle normal = helper.getStyle(couleur);
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);
        CellStyle date = wb.createCellStyle();
        date.cloneStyleFrom(centre);
        date.setDataFormat(createHelper.createDataFormat().getFormat("dd/mm/yyyy"));

        // 4. Valorisation des cellules avec les données de l'anomalie
        
        // Contrôle Clarity et mise à jour données
        controleClarity(ano);

        // Direction
        valoriserCellule(row, colDir, normal, ano.getDirection(), ano.getDirectionComment());
        
        // Département
        valoriserCellule(row, colDepart, normal, ano.getDepartement(), ano.getDepartementComment());
        
        // Service
        valoriserCellule(row, colService, normal, ano.getService(), ano.getServiceComment());
        
        // Responsable service
        valoriserCellule(row, colResp, normal, ano.getResponsableService(), ano.getResponsableServiceComment());
        
        // code projet Clarity
        valoriserCellule(row, colClarity, normal, ano.getProjetClarity(), ano.getProjetClarityComment());
        
        // libelle projet
        valoriserCellule(row, colLib, normal, ano.getLibelleProjet(), ano.getLibelleProjetComment());
        
        // Cpi du lot
        valoriserCellule(row, colCpi, normal, ano.getCpiProjet(), ano.getCpiProjetComment());
        
        // Edition
        valoriserCellule(row, colEdition, centre, ano.getEdition(), ano.getEditionComment());
        
        // Numéro du lot
        Cell cell = valoriserCellule(row, colLot, centre, ano.getLot(), ano.getLotComment());
        ajouterLiens(cell, lienslots, ano.getLot().substring(4));

        // Environnement
        cell = row.createCell(colEnv);
        cell.setCellStyle(centre);
        if (ano.getEnvironnement() != null)
            cell.setCellValue(ano.getEnvironnement().toString());

        // Numéros anomalie
        cell = row.createCell(colAno);
        cell.setCellStyle(centre);
        int numeroAno = ano.getNumeroAnomalie();
        if (numeroAno != 0)
        {
            cell.setCellValue(numeroAno);
            // Rajout de "&id=", car cela fait planter la désérialisation du fichier de paramètres
            ajouterLiens(cell, liensAnos + "&id=", String.valueOf(numeroAno));
        }
        
        // Etat anomalie
        valoriserCellule(row, colEtat, normal, ano.getEtat(), ano.getEtatComment());
        
        // Anomalie de sécurite
        valoriserCellule(row, colSec, centre, ano.getSecurite(), ano.getSecuriteComment());
        
        // Remarques
        valoriserCellule(row, colRemarque, normal, ano.getRemarque(), ano.getRemarqueComment());
        
        // Version composants
        valoriserCellule(row, colVer, centre, ano.getVersion(), ano.getVersionComment());

        // Date création
        cell = row.createCell(colDateCrea);
        if (ano.getDateCreation() != null)
            cell.setCellValue(DateConvert.convertToOldDate(ano.getDateCreation()));
        cell.setCellStyle(date);

        // Date relance
        cell = row.createCell(colDateRel);
        if (ano.getDateRelance() != null)
            cell.setCellValue(DateConvert.convertToOldDate(ano.getDateRelance()));
        cell.setCellStyle(date);
    }

    /**
     * 
     * @param row
     * @param ano
     * @param couleur
     * @param traite
     */
    private void creerLigneVersion(Row row, Anomalie ano, IndexedColors couleur, String traite)
    {
        CellStyle centre = helper.getStyle(couleur, Bordure.VIDE, HorizontalAlignment.CENTER);

        valoriserCellule(row, Index.LOTI.ordinal(), centre, ano.getLot(), null);
        valoriserCellule(row, Index.EDITIONI.ordinal(), helper.getStyle(couleur), ano.getEdition(), null);
        valoriserCellule(row, Index.ENVI.ordinal(), centre, ano.getEnvironnement().toString(), null);
        valoriserCellule(row, Index.TRAITEI.ordinal(), centre, traite, null);
    }

    /**
     * 
     * @param sheet
     */
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

    /**
     * 
     * @param cell
     * @param baseAdresse
     * @param variable
     */
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
     * @param sheet
     * @param anoAajouter
     * @param lotsSecurite
     * @param lotsRelease
     */
    private void ajouterNouvellesAnos(Sheet sheet, List<Anomalie> anoAajouter, Map<String, Anomalie> mapAnoCloses, Set<String> lotsSecurite, Set<String> lotsRelease)
    {
        for (Anomalie ano : anoAajouter)
        {
            Row row = sheet.createRow(sheet.getLastRowNum() + 1);

            String anoLot = ano.getLot().substring(4);

            // Contrôle si le lot a une erreur de sécurité pour mettre à jour la donnée.
            if (lotsSecurite.contains(anoLot))
                ano.setSecurite(SECURITEKO);

            // Ajout de la donnée de version
            if (lotsRelease.contains(anoLot))
                ano.setVersion(RELEASE);
            else
                ano.setVersion(SNAPSHOT);

            // Ajout de la date de création à la date du jour
            ano.setDateCreation(LocalDate.now());

            // Création de la ligne
            if (mapAnoCloses.keySet().contains(ano.getLot()))
            {
                Anomalie anoClose = mapAnoCloses.get(ano.getLot());
                ano.setDateCreation(anoClose.getDateCreation());
                ano.setDateRelance(anoClose.getDateRelance());
                ano.setRemarque(anoClose.getRemarque());
                ano.setNumeroAnomalie(anoClose.getNumeroAnomalie());
                ano.setEtat("A vérifier");
                creerLigneSQ(row, ano, IndexedColors.GREY_25_PERCENT);
                mapAnoCloses.remove(ano.getLot());
            }
            else
                creerLigneSQ(row, ano, IndexedColors.LIGHT_ORANGE);
        }
    }

    /**
     * Enregistre toutes les anomalies de la feuille des anomalies closes, puis retourne une feuille vide pour les traitements suivants.
     * 
     * @param anoClose
     * @return
     */
    private Sheet saveAnomaliesCloses(Map<String, Anomalie> anoClose)
    {
        // Récupération de la feuille des ano closes.
        Sheet retour = wb.getSheet(AC);
        if (retour != null)
        {
            // Itération sur les lignes sauf la première qui correspond aux titres. Récupération des informations des anomalies
            for (Iterator<Row> iter = retour.rowIterator(); iter.hasNext();)
            {
                Row row = iter.next();
                if (row.getRowNum() == 0)
                    continue;
                Anomalie ano = creaAnodepuisExcel(row);
                anoClose.put(ano.getLot(), ano);
            }
            wb.removeSheetAt(wb.getSheetIndex(retour));
        }
        retour = wb.createSheet(AC);
        creerLigneTitres(retour);
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
        Map<String, InfoClarity> map = fichiersXML.getMapClarity();
        String anoClarity = ano.getProjetClarity();
        Set<String> keyset = map.keySet();

        // Vérification si le code Clarity de l'anomalie est bien dans la map
        if (keyset.contains(anoClarity))
        {
            InfoClarity info = map.get(anoClarity);
            ano.setDepartement(info.getDepartement());
            ano.setDirection(info.getDirection());
            ano.setService(info.getService());
            return ano;
        }

        // Sinon on itère sur les clefs en supprimant les indices de lot, et on prend la première clef correspondante
        for (String key : keyset)
        {
            // On retire les deux dernières lettres pour les clefs de plus de 6 caractères finissants par 0[1-9]
            if (anoClarity.equals(key.length() > 5 && key.matches(".*0[0-9]$") ? key.substring(0, 6) : key))
            {
                InfoClarity info = map.get(key);
                ano.setDepartement(info.getDepartement());
                ano.setDirection(info.getDirection());
                ano.setService(info.getService());
                return ano;
            }
        }

        // Si on ne trouve pas, on renvoie juste l'anomalie avec le log d'erreur
        loginconnue.warn("Code Clarity inconnu : " + anoClarity + " - Lot : " + ano.getLot());
        return ano;
    }

    /**
     * 
     * @param row
     * @return
     */
    private Anomalie creaAnodepuisExcel(Row row)
    {
        Anomalie retour = new Anomalie();
        retour.setDirection(getCellStringValue(row, colDir));
        retour.setDirectionComment(getCellComment(row, colDir));
        retour.setDepartement(getCellStringValue(row, colDepart));
        retour.setDepartementComment(getCellComment(row, colDepart));
        retour.setService(getCellStringValue(row, colService));
        retour.setServiceComment(getCellComment(row, colService));
        retour.setResponsableService(getCellStringValue(row, colResp));
        retour.setResponsableServiceComment(getCellComment(row, colResp));
        retour.setProjetClarity(getCellStringValue(row, colClarity));
        retour.setProjetClarityComment(getCellComment(row, colClarity));
        retour.setLibelleProjet(getCellStringValue(row, colLib));
        retour.setLibelleProjetComment(getCellComment(row, colLib));
        retour.setCpiProjet(getCellStringValue(row, colCpi));
        retour.setCpiProjetComment(getCellComment(row, colCpi));
        retour.setEdition(getCellStringValue(row, colEdition));
        retour.setEditionComment(getCellComment(row, colEdition));
        retour.setLot(getCellStringValue(row, colLot));
        retour.setLotComment(getCellComment(row, colLot));
        retour.setEnvironnement(Environnement.getEnvironnement(getCellStringValue(row, colEnv)));
        retour.setEnvironnementComment(getCellComment(row, colEnv));
        retour.setNumeroAnomalie(getCellNumericValue(row, colAno));
        retour.setNumeroAnomalieComment(getCellComment(row, colAno));
        retour.setEtat(getCellStringValue(row, colEtat));
        retour.setEtatComment(getCellComment(row, colEtat));
        retour.setSecurite(getCellStringValue(row, colSec));
        retour.setSecuriteComment(getCellComment(row, colSec));
        retour.setRemarque(getCellStringValue(row, colRemarque));
        retour.setRemarqueComment(getCellComment(row, colRemarque));
        retour.setVersion(getCellStringValue(row, colVer));
        retour.setVersionComment(getCellComment(row, colVer));
        retour.setDateCreation(getCellDateValue(row, colDateCrea));
        retour.setDateCreationComment(getCellComment(row, colDateCrea));
        retour.setDateRelance(getCellDateValue(row, colDateRel));
        retour.setDateRelanceComment(getCellComment(row, colDateRel));
        return retour;
    }

    /*---------- ACCESSEURS ----------*/

    /**
     * Liste des numéros de colonnes des feuilles d'environnement
     * 
     * @author ETP8137 - Grégoire mathon
     *
     */
    private enum Index {
        LOTI, EDITIONI, ENVI, TRAITEI;
    }
}