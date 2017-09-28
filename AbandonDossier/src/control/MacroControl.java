package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.xml.BanqueXML;

public class MacroControl
{
    /* ---------- ATTIBUTES ---------- */
    
    private String cosce;
    private BanqueXML banque;

    
    /* ---------- CONSTUCTORS ---------- */
    
    public MacroControl(String cosce, BanqueXML banque)
    {
        this.cosce = cosce;
        this.banque = banque;
    }
    
    /* ---------- METHODS ---------- */
    
    public void creerMacro(File file)
    {
        try (BufferedWriter brFile = new BufferedWriter(new FileWriter(file)))
        {
            StringBuilder sbFile = new StringBuilder();
            
            sbFile.append("");
            
            brFile.write(sbFile.toString());
        }
        catch (IOException e)
        {
            
        }
                
    }
    
    /* ---------- ACCESS ---------- */
    
}
