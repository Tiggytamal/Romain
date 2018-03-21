package control;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import control.parent.ControlExcel;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class ControlVersion extends ControlExcel
{
    /*---------- ATTRIBUTS ----------*/
    private int colVersion;
    private int colLib;

    private static final String LIBELLE = "Libellé";
    private static final String VERSION = "Numero de version";
    private static int NBRECOLONNE = 2;
    private static final String CHC2018 = "CHC2018";

    /*---------- CONSTRUCTEURS ----------*/

    protected ControlVersion(File file) throws InvalidFormatException, IOException
    {
        super(file);
    }

    /*---------- METHODES PUBLIQUES ----------*/
    /*---------- METHODES PRIVEES ----------*/
    /*---------- ACCESSEURS ----------*/

    @Override
    protected void calculIndiceColonnes()
    {
        Sheet sheet = wb.getSheetAt(0);
        Row row = sheet.getRow(0);
        int nbre = 0;

        for (int i = 0; i < row.getLastCellNum() + 1; i++)
        {
            if (LIBELLE.equals(getCellStringValue(row, i)))
            {
                colLib = i;
                nbre++;
            }
            else if (getCellStringValue(row, i).contains(VERSION))
            {
                colVersion = i;
                nbre++;
            }
            if (nbre == 2)
                break;
        }
        
        if (nbre != NBRECOLONNE)
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Le fichier excel est mal configuré (" + file.getName() + " , vérifier les colonnes de celui-ci");

    }

    @Override
    protected void initColonnes()
    {
        // Pas besoin d'initialiser les colonnes
    }

    /**
     * 
     * @return
     */
    public Map<String, String> recupVersionDepuisExcel()
    {
        Map<String, String> retour = new HashMap<>();
        Sheet sheet = wb.getSheetAt(0);
        for (int i =1; i < sheet.getLastRowNum() + 1; i++)
        {
            Row row = sheet.getRow(i);
            if (getCellStringValue(row, colLib).contains(CHC2018))
                retour.put(String.valueOf(getCellNumericValue(row, colVersion)), getCellStringValue(row, colLib));
        }
        return retour;
    }

}
