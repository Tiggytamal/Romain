package utilities;

import java.util.EnumMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import utilities.enums.Bordure;
import utilities.enums.Couleur;

/**
 * Classe de getion des styles de cellule Excel.
 * 
 * @author ETP137 - Grégoire Mathon
 *
 */
public class CellHelper
{
    /* ---------- ATTIBUTES ---------- */
    private Workbook wb;
    private EnumMap<Couleur, EnumMap<Bordure, CellStyle>> map;

    /* ---------- CONSTUCTORS ---------- */

    public CellHelper(Workbook wb)
    {
        this.wb = wb;
        createAllStyle();
    }

    /* ---------- METHODS ---------- */
    
    /**
     * Retourne le style de cellule voulu selon le statut de l'incident et son placement dans le tableau
     * 
     * @param bordure
     *          {@link model.enums.Statut}
     * @param side
     *          {@link utilities.utilities.enums.Side}
     * @return
     * @throws IllegalAccessException 
     */
    public CellStyle getStyle(Couleur couleur, Bordure bordure)
    {
        if (couleur == null || bordure == null)
            throw new IllegalArgumentException("La couleur ou la bordure ne peuvent être nulles");

        return map.get(couleur).get(bordure);
    }
    
    public Cell setFontColor (Cell cell, IndexedColors color)
    {
        CellStyle style = wb.createCellStyle();
        style.cloneStyleFrom(cell.getCellStyle());
        Font font = wb.createFont();
        font.setColor(color.index);
        font.setFontName("Comic Sans MS");
        font.setFontHeightInPoints((short) 12);
        style.setFont(font);
        cell.setCellStyle(style);
        return cell;
    }
    
    public Cell recentrage (Cell cell)
    {
        CellStyle style = cell.getCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return cell;
    }
    
    /* ---------- PRIVATE METHODS ---------- */

    private CellStyle createStyle(Couleur couleur, Bordure bord)
    {
        // Initialisation du style
        CellStyle style = wb.createCellStyle();

        // Renvoie un style vide sans statut d'incident
        if (couleur == null || bord == null)
            throw new IllegalArgumentException("La couleur ou la bordure ne peuvent être nulles");

        // Alignement centré plus ligne fine en bordure
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setWrapText(true);

        // Switch pour sélectionner la couleur de fond
        switch (couleur)
        {
            case GRIS :
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                break;
            case BLANC :
                style.setFillForegroundColor(IndexedColors.WHITE.index);
                break;
            case JAUNE :
                style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
                break;
            case VERT :
                style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
                break;
            case ROUGE :
                style.setFillForegroundColor(IndexedColors.RED.index);
        }
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // Switch sur le placement de la cellule, rajout d'une bordure plus épaisse au bord du tableau
        switch (bord)
        {
            case BAS :
                style.setBorderBottom(BorderStyle.THICK);
                break;
                
            case DROITE :
                style.setBorderRight(BorderStyle.THICK);
                break;
                
            case GAUCHE :
                style.setBorderLeft(BorderStyle.THICK);
                break;
                
            case HAUT :
                style.setBorderTop(BorderStyle.THICK);
                break;
                
            case BASDROITE :
                style.setBorderRight(BorderStyle.THICK);
                style.setBorderBottom(BorderStyle.THICK);
                break;
                
            case BASGAUCHE :
                style.setBorderLeft(BorderStyle.THICK);
                style.setBorderBottom(BorderStyle.THICK);
                break;
                
            case HAUTDROITE :
                style.setBorderTop(BorderStyle.THICK);
                style.setBorderRight(BorderStyle.THICK);
                break;
                
            case HAUTGAUCHE :
                style.setBorderTop(BorderStyle.THICK);
                style.setBorderLeft(BorderStyle.THICK);
                break;
            case VIDE :
                break;
        }

        return style;
    }

    /**
     * Génération de tous les différents styles possibles
     */
    private void createAllStyle()
    {
        map = new EnumMap<>(Couleur.class);
        
        // Style blancs
        EnumMap<Bordure, CellStyle> bordure = new EnumMap<>(Bordure.class);
        bordure.put(Bordure.VIDE, createStyle(Couleur.BLANC, Bordure.VIDE));
        bordure.put(Bordure.DROITE, createStyle(Couleur.BLANC, Bordure.DROITE));
        bordure.put(Bordure.GAUCHE, createStyle(Couleur.BLANC, Bordure.GAUCHE));
        bordure.put(Bordure.HAUT, createStyle(Couleur.BLANC, Bordure.HAUT));
        bordure.put(Bordure.HAUTDROITE, createStyle(Couleur.BLANC, Bordure.HAUTDROITE));
        bordure.put(Bordure.HAUTGAUCHE, createStyle(Couleur.BLANC, Bordure.HAUTGAUCHE));
        bordure.put(Bordure.BAS, createStyle(Couleur.BLANC, Bordure.BAS));
        bordure.put(Bordure.BASDROITE, createStyle(Couleur.BLANC, Bordure.BASDROITE));
        bordure.put(Bordure.BASGAUCHE, createStyle(Couleur.BLANC, Bordure.BASGAUCHE));
        map.put(Couleur.BLANC, bordure);
        // Styles jaunes
        bordure = new EnumMap<>(Bordure.class);
        bordure.put(Bordure.VIDE, createStyle(Couleur.JAUNE, Bordure.VIDE));
        bordure.put(Bordure.DROITE, createStyle(Couleur.JAUNE, Bordure.DROITE));
        bordure.put(Bordure.GAUCHE, createStyle(Couleur.JAUNE, Bordure.GAUCHE));
        bordure.put(Bordure.HAUT, createStyle(Couleur.JAUNE, Bordure.HAUT));
        bordure.put(Bordure.HAUTDROITE, createStyle(Couleur.JAUNE, Bordure.HAUTDROITE));
        bordure.put(Bordure.HAUTGAUCHE, createStyle(Couleur.JAUNE, Bordure.HAUTGAUCHE));
        bordure.put(Bordure.BAS, createStyle(Couleur.JAUNE, Bordure.BAS));
        bordure.put(Bordure.BASDROITE, createStyle(Couleur.JAUNE, Bordure.BASDROITE));
        bordure.put(Bordure.BASGAUCHE, createStyle(Couleur.JAUNE, Bordure.BASGAUCHE));
        map.put(Couleur.JAUNE, bordure);
        // Styles gris
        bordure = new EnumMap<>(Bordure.class);
        bordure.put(Bordure.VIDE, createStyle(Couleur.GRIS, Bordure.VIDE));
        bordure.put(Bordure.DROITE, createStyle(Couleur.GRIS, Bordure.DROITE));
        bordure.put(Bordure.GAUCHE, createStyle(Couleur.GRIS, Bordure.GAUCHE));
        bordure.put(Bordure.HAUT, createStyle(Couleur.GRIS, Bordure.HAUT));
        bordure.put(Bordure.HAUTDROITE, createStyle(Couleur.GRIS, Bordure.HAUTDROITE));
        bordure.put(Bordure.HAUTGAUCHE, createStyle(Couleur.GRIS, Bordure.HAUTGAUCHE));
        bordure.put(Bordure.BAS, createStyle(Couleur.GRIS, Bordure.BAS));
        bordure.put(Bordure.BASDROITE, createStyle(Couleur.GRIS, Bordure.BASDROITE));
        bordure.put(Bordure.BASGAUCHE, createStyle(Couleur.GRIS, Bordure.BASGAUCHE));
        map.put(Couleur.GRIS, bordure);
        // Styles verts
        bordure = new EnumMap<>(Bordure.class);
        bordure.put(Bordure.VIDE, createStyle(Couleur.VERT, Bordure.VIDE));
        bordure.put(Bordure.DROITE, createStyle(Couleur.VERT, Bordure.DROITE));
        bordure.put(Bordure.GAUCHE, createStyle(Couleur.VERT, Bordure.GAUCHE));
        bordure.put(Bordure.HAUT, createStyle(Couleur.VERT, Bordure.HAUT));
        bordure.put(Bordure.HAUTDROITE, createStyle(Couleur.VERT, Bordure.HAUTDROITE));
        bordure.put(Bordure.HAUTGAUCHE, createStyle(Couleur.VERT, Bordure.HAUTGAUCHE));
        bordure.put(Bordure.BAS, createStyle(Couleur.VERT, Bordure.BAS));
        bordure.put(Bordure.BASDROITE, createStyle(Couleur.VERT, Bordure.BASDROITE));
        bordure.put(Bordure.BASGAUCHE, createStyle(Couleur.VERT, Bordure.BASGAUCHE));
        map.put(Couleur.VERT, bordure);
    }

    /* ---------- ACCESS ---------- */

}
