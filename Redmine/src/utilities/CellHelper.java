package utilities;

import java.util.HashMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import model.enums.Statut;
import utilities.enums.Side;
import utilities.enums.StyleEnum;

public class CellHelper
{
    /* ---------- ATTIBUTES ---------- */
    Workbook wb;
    HashMap<StyleEnum, CellStyle> map;

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
     * @param statut
     *          {@link model.enums.Statut}
     * @param side
     *          {@link utilities.enums.Side}
     * @return
     */
    public CellStyle getStyle(Statut statut, Side side)
    {
        if (statut == null)
            return map.get(StyleEnum.VIDE);

        if (statut == Statut.RESOLVED)
        {
            return getStyleGris(side);
        }
        else if (statut == Statut.NOUVEAU || statut == Statut.REFERRED)
        {
            return getStyleBlanc(side);
        }
        else if (statut == Statut.PENDING)
        {
            return getStyleJaune(side);
        }
        else if (statut == Statut.WRKINPRG)
        {
            return getStyleVert(side);
        }
        return null;
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

    private CellStyle createStyle(Statut statut, Side bord)
    {
        // Initialisation du style
        CellStyle style = wb.createCellStyle();

        // Renvoie un style vide sans statut d'incident
        if (statut == null)
            return style;

        // Alignement centré plus ligne fine en bordure
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setWrapText(true);

        // Switch sur le statut de l'incident pour choisir la couleur du fond
        switch (statut)
        {
            case RESOLVED :
                style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                break;
            case NOUVEAU :
                style.setFillForegroundColor(IndexedColors.WHITE.index);
                break;
            case PENDING :
                style.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
                break;
            case WRKINPRG :
                style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.index);
                break;
            case REFERRED :
                style.setFillForegroundColor(IndexedColors.WHITE.index);
                break;
            default :
                style.setFillForegroundColor(IndexedColors.WHITE.index);
                break;
        }
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        // On retourne le syle directement si aucun bord n'est envoyé
        if (bord == null)
            return style;

        // Switch sur le placement de la cellule, rajout d'un bordure plus épaisse au bord du tableau

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
        }

        return style;
    }

    /**
     * Génération de tous les différents styles possibles
     */
    private void createAllStyle()
    {
        map = new HashMap<>();
        // Style blancs
        map.put(StyleEnum.BLANC, createStyle(Statut.NOUVEAU, null));
        map.put(StyleEnum.BLANCDROITE, createStyle(Statut.NOUVEAU, Side.DROITE));
        map.put(StyleEnum.BLANCGAUCHE, createStyle(Statut.NOUVEAU, Side.GAUCHE));
        map.put(StyleEnum.BLANCHAUT, createStyle(Statut.NOUVEAU, Side.HAUT ));
        map.put(StyleEnum.BLANCHAUTDROITE, createStyle(Statut.NOUVEAU, Side.HAUTDROITE ));
        map.put(StyleEnum.BLANCHAUTGAUCHE, createStyle(Statut.NOUVEAU, Side.HAUTGAUCHE ));
        map.put(StyleEnum.BLANCBAS, createStyle(Statut.NOUVEAU, Side.BAS ));
        map.put(StyleEnum.BLANCBAS, createStyle(Statut.NOUVEAU, Side.BAS ));
        map.put(StyleEnum.BLANCBAS, createStyle(Statut.NOUVEAU, Side.BAS ));
        // Styles jaunes
        map.put(StyleEnum.JAUNE, createStyle(Statut.PENDING, null));
        map.put(StyleEnum.JAUNEDROITE, createStyle(Statut.PENDING, Side.DROITE));
        map.put(StyleEnum.JAUNEGAUCHE, createStyle(Statut.PENDING, Side.GAUCHE));
        map.put(StyleEnum.JAUNEHAUT, createStyle(Statut.PENDING, Side.HAUT ));
        map.put(StyleEnum.JAUNEHAUTDROITE, createStyle(Statut.PENDING, Side.HAUTDROITE ));
        map.put(StyleEnum.JAUNEHAUTGAUCHE, createStyle(Statut.PENDING, Side.HAUTGAUCHE ));
        map.put(StyleEnum.JAUNEBAS, createStyle(Statut.PENDING, Side.BAS ));
        map.put(StyleEnum.JAUNEBASDROITE, createStyle(Statut.PENDING, Side.BASDROITE ));
        map.put(StyleEnum.JAUNEBASGAUCHE, createStyle(Statut.PENDING, Side.BASGAUCHE ));
        // Styles gris
        map.put(StyleEnum.GRIS, createStyle(Statut.RESOLVED, null));
        map.put(StyleEnum.GRISDROITE, createStyle(Statut.RESOLVED, Side.DROITE));
        map.put(StyleEnum.GRISGAUCHE, createStyle(Statut.RESOLVED, Side.GAUCHE));
        map.put(StyleEnum.GRISHAUT, createStyle(Statut.RESOLVED, Side.HAUT ));
        map.put(StyleEnum.GRISHAUTDROITE, createStyle(Statut.RESOLVED, Side.HAUTDROITE ));
        map.put(StyleEnum.GRISHAUTGAUCHE, createStyle(Statut.RESOLVED, Side.HAUTGAUCHE ));
        map.put(StyleEnum.GRISBAS, createStyle(Statut.RESOLVED, Side.BAS ));
        map.put(StyleEnum.GRISBASDROITE, createStyle(Statut.RESOLVED, Side.BASDROITE ));
        map.put(StyleEnum.GRISBASGAUCHE, createStyle(Statut.RESOLVED, Side.BASGAUCHE ));
        // Styles verts
        map.put(StyleEnum.VERT, createStyle(Statut.WRKINPRG, null));
        map.put(StyleEnum.VERTDROITE, createStyle(Statut.WRKINPRG, Side.DROITE));
        map.put(StyleEnum.VERTGAUCHE, createStyle(Statut.WRKINPRG, Side.GAUCHE));
        map.put(StyleEnum.VERTHAUT, createStyle(Statut.WRKINPRG, Side.HAUT ));
        map.put(StyleEnum.VERTHAUTDROITE, createStyle(Statut.WRKINPRG, Side.HAUTDROITE ));
        map.put(StyleEnum.VERTHAUTGAUCHE, createStyle(Statut.WRKINPRG, Side.HAUTGAUCHE ));
        map.put(StyleEnum.VERTBAS, createStyle(Statut.WRKINPRG, Side.BAS ));
        map.put(StyleEnum.VERTBASDROITE, createStyle(Statut.WRKINPRG, Side.BASDROITE ));
        map.put(StyleEnum.VERTBASGAUCHE, createStyle(Statut.WRKINPRG, Side.BASGAUCHE ));
        // Style vide
        map.put(StyleEnum.VIDE, createStyle(null, null));
    }
    
    /**
     * Méthode privée retournant un style de fond gris
     * 
     * @param side
     * @return
     */
    private CellStyle getStyleGris(Side side)
    {
        if (side == null)
            return map.get(StyleEnum.GRIS);       
        switch (side)
        {
            case BAS :
                return map.get(StyleEnum.GRISBAS);
            case BASDROITE :
                return map.get(StyleEnum.GRISBASDROITE);
            case BASGAUCHE :
                return map.get(StyleEnum.GRISBASGAUCHE);
            case DROITE :
                return map.get(StyleEnum.GRISDROITE);
            case GAUCHE :
                return map.get(StyleEnum.GRISGAUCHE);
            case HAUT :
                return map.get(StyleEnum.GRISHAUT);
            case HAUTDROITE :
                return map.get(StyleEnum.GRISHAUTDROITE);
            case HAUTGAUCHE :
                return map.get(StyleEnum.GRISHAUTGAUCHE);
            default :
                throw new IllegalArgumentException("StyleEnum inconnu et non prévu : " + side);
           
        }

    }
    
    /**
     * Méthode privée retournant un style de fond blanc
     * 
     * @param side
     * @return
     */
    private CellStyle getStyleBlanc(Side side)
    {
        if (side == null)
            return map.get(StyleEnum.BLANC);       
        switch (side)
        {
            case BAS :
                return map.get(StyleEnum.BLANCBAS);
            case BASDROITE :
                return map.get(StyleEnum.BLANCBASDROITE);
            case BASGAUCHE :
                return map.get(StyleEnum.BLANCBASGAUCHE);
            case DROITE :
                return map.get(StyleEnum.BLANCDROITE);
            case GAUCHE :
                return map.get(StyleEnum.BLANCGAUCHE);
            case HAUT :
                return map.get(StyleEnum.BLANCHAUT);
            case HAUTDROITE :
                return map.get(StyleEnum.BLANCHAUTDROITE);
            case HAUTGAUCHE :
                return map.get(StyleEnum.BLANCHAUTGAUCHE);
            default :
                throw new IllegalArgumentException("StyleEnum inconnu et non prévu : " + side);
           
        }

    }
    
    /**
     * Méthode privée retournant un style de fond jaune
     * 
     * @param side
     * @return
     */
    private CellStyle getStyleJaune(Side side)
    {
        if (side == null)
            return map.get(StyleEnum.JAUNE);       
        switch (side)
        {
            case BAS :
                return map.get(StyleEnum.JAUNEBAS);
            case BASDROITE :
                return map.get(StyleEnum.JAUNEBASDROITE);
            case BASGAUCHE :
                return map.get(StyleEnum.JAUNEBASGAUCHE);
            case DROITE :
                return map.get(StyleEnum.JAUNEDROITE);
            case GAUCHE :
                return map.get(StyleEnum.JAUNEGAUCHE);
            case HAUT :
                return map.get(StyleEnum.JAUNEHAUT);
            case HAUTDROITE :
                return map.get(StyleEnum.JAUNEHAUTDROITE);
            case HAUTGAUCHE :
                return map.get(StyleEnum.JAUNEHAUTGAUCHE);
            default :
                throw new IllegalArgumentException("StyleEnum inconnu et non prévu : " + side);
           
        }

    }
    
    /**
     * Méthode privée retournant un fond de style vert
     * 
     * @param side
     * @return
     */
    private CellStyle getStyleVert(Side side)
    {
        if (side == null)
            return map.get(StyleEnum.VERT);       
        switch (side)
        {
            case BAS :
                return map.get(StyleEnum.VERTBAS);
            case BASDROITE :
                return map.get(StyleEnum.VERTBASDROITE);
            case BASGAUCHE :
                return map.get(StyleEnum.VERTBASGAUCHE);
            case DROITE :
                return map.get(StyleEnum.VERTDROITE);
            case GAUCHE :
                return map.get(StyleEnum.VERTGAUCHE);
            case HAUT :
                return map.get(StyleEnum.VERTHAUT);
            case HAUTDROITE :
                return map.get(StyleEnum.VERTHAUTDROITE);
            case HAUTGAUCHE :
                return map.get(StyleEnum.VERTHAUTGAUCHE);
            default :
                throw new IllegalArgumentException("StyleEnum inconnu et non prévu : " + side);
           
        }

    }


    /* ---------- ACCESS ---------- */

}
