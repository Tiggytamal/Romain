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
    private String incident;
    private StringBuilder sbFile;

    
    /* ---------- CONSTUCTORS ---------- */
    
    public MacroControl(String cosce, BanqueXML banque, String incident)
    {
        this.cosce = cosce;
        this.banque = banque;
        this.incident = incident;
        sbFile = new StringBuilder();
    }
    
    /* ---------- METHODS ---------- */
    
    public void creerMacro(File file)
    {               
        try (BufferedWriter brFile = new BufferedWriter(new FileWriter(file)))
        {                    
            brFile.write(concatenationMacro());
        }
        catch (IOException e)
        {
            
        }
                
    }
    
    private String concatenationMacro()
    {
        mT(23,12);
        sK(banque.getCodePlaque());
        enterKey();
        wait(3, 14, 3, 15);
        enterKey();
        wait(3, 14, 3, 15);
        mT(3,15);
        wait(3, 14, 3, 15);
        sK("fad");
        enterKey();
        wait(2, 14, 2, 15);
        mT(2,15);
        wait(2, 14, 2, 15);
        sK("1");
        enterKey();
        wait(2, 14, 2, 15);
        mT(7,21);
        wait(7, 20, 7, 21);
        sK("TN5C");
        mT(6,23);
        sK(banque.getCoad());        enterKey();
        wait(10, 1, 10, 2);
        sK("<Tab><Tab><Tab>='");
        sK(banque.getCoetb());
        sK("'<Down>='");
        sK(cosce);
        sK("'\n");
        wait(11, 54 , 11, 79);
        mT(23, 3);
        wait (23, 1, 23, 3);
        sK("s");
        enterKey();
        wait (23, 1, 23, 2);
        sK("<Pf6>");
        @SuppressWarnings ("unused")
        String a =sbFile.toString();
        return sbFile.toString();
    }
    
    private void mT(int a, int b)
    {
        sbFile.append("MoveTo(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        sbFile.append(")\n");
    }
    
    private void sK(String string)
    {
        sbFile.append("SendKeys \"");
        sbFile.append(string);
        sbFile.append("\"\n");
    }
    
    private void enterKey()
    {
        sK("<Enter>");
    }

    
    private void wait(int a, int b, int c, int d)
    {
        wForA(a, b);
        wForC(c, d);
        wForU();
    }
    
    
    private void wForA(int a, int b)
    {
        sbFile.append("WaitForAttrib(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        sbFile.append(", &H08, &H3C)\n");
        
    }
    
    private void wForC(int a, int b)
    {
        sbFile.append("WaitForCursor(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        sbFile.append(")\n");
    }
    
    private void wForU()
    {
        sbFile.append("WaitForKbdUnlock()\n");
    }
    
    /* ---------- ACCESS ---------- */
    
}
