package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.xml.BanqueXML;

public class MacroControl
{
    /* ---------- ATTIBUTES ---------- */
    
    private String cosce;
    private BanqueXML banque;
    private String incident;
    private StringBuilder sbFile;
    private String date;

    
    /* ---------- CONSTUCTORS ---------- */
    
    public MacroControl(String cosce, BanqueXML banque, String incident)
    {
        this.cosce = cosce;
        this.banque = banque;
        
        if (incident.length() > 7)
            this.incident = incident.substring(incident.length()-7);
        else
            this.incident = incident;
        
        sbFile = new StringBuilder();
        
        date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
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
        varInit();
        recupCODOSB();
        recupCOEMCOPO();
        creationRequete();
        return sbFile.toString();
    }
    
    private void recupCODOSB()
    {
        mT(23,12);
        sK(banque.getCodePlaque(), false);
        enterKey();
        wait(3, 14, 3, 15);
        enterKey();
        wait(3, 14, 3, 15);
        loop();
        mT(3,15);
        wait(3, 14, 3, 15);
        sK("fad", true);
        enterKey();
        wait(2, 14, 2, 15);
        mT(2,15);
        wait(2, 14, 2, 15);
        sK("1", true);
        enterKey();
        wait(2, 14, 2, 15);
        mT(7,21);
        wait(7, 20, 7, 21);
        sK("TN5C", true);
        mT(6,23);
        sK(banque.getCoad(), true);        
        enterKey();
        wait(10, 1, 10, 2);
        sK("<Tab><Tab><Tab>='", false);
        sK(banque.getCoetb(), false);
        sK("'<Down>='", false);
        sK(cosce, false);
        sK("'", true);
        wait(11, 54 , 11, 79);
        mT(23, 3);
        wait(23, 1, 23, 3);
        sK("s", true);
        enterKey();
        wait (23, 1, 23, 2);
        sK("<Pf6>", true);
        wait(4, 14, 4, 15);
        mT(9, 11);
        wait(9, 10,94, 11);
        sbFile.append("CODOSB = GetString(09,11,15)\n");
        sK("<Pf3>", true);
        wait(11, 1, 11, 2);
        sK("<Pf3>", true);
        wait(2, 14, 2, 15);
    }
    
    private void recupCOEMCOPO()
    {
        mT(7,24);
        wait(7, 20, 7, 24);
        sK("F", true);
        enterKey();
        wait(10, 1, 10, 2);
        mT(10,3);
        wait(10, 1, 10, 3);
        sK("<Tab><Tab><Tab>='", false);
        sK(banque.getCoetb(), false);
        sK("'<Down>='", false);
        sK(cosce, false);
        sK("'", true);
        mT(12,3);
        sK("<Tab>1<Tab>d<Pf6>", true);
        wait(4, 14, 4, 15);
        mT(15,3);
        wait(15, 1, 15, 3);
        sK("s", false);
        mT(16,3);
        wait(16, 1, 16, 3);
        sK("s<Pf6>", true);
        wait(4, 14, 4, 15);
        mT(9,11);
        wait(9, 10, 9, 11);
        sbFile.append("COEM = GetString(09,11,7)\n");
        mT(9,20);
        sbFile.append("COPO = GetString(09,20,5)\n");
        sK("<Pf3>", true);
        wait(15, 1, 15,23);
        sK("<Pf3>", true);
        wait(2, 14, 2, 15);
        sK("<Pf3>", true);
        wait(2, 14, 2, 15);
        sK("<Esc>", true);       
    }
    
    private void creationRequete()
    {
        mT(23,12);
        sK("1", true);
        enterKey();
        wait(3, 14, 3, 15);
        enterKey();
        wait(3, 14, 3, 15);
        sK("DEV", true);
        enterKey();
        wait(2, 14, 2, 15);
        sK("t", true);
        enterKey();
        wait(3, 12, 3, 13);
        sK("1", true);
        enterKey();
        wait(1, 1, 1, 1);
        enterKey();
        mT(9,42);
        sK("TN5B<Tab>I<Tab>1<Tab>MCE<Tab><Tab>", false);
        sbFile.append(incident);
        nL("\"");
        enterKey();
        wait(7, 5, 7, 6);
        enterKey();
        wait(1, 1, 1, 1);
        enterKey();
        wait(27, 5, 27, 6);
        mT(27,22);
        wait(27, 21, 27, 22);
        sK("C£", false);
        sbFile.append(banque.getCoad());
        sbFile.append("E0");
        nL("\"");
        enterKey();
        mT(28,6);
        wait(28, 5, 28, 6);
        sK("s", true);
        enterKey();
        wait(27, 5, 27, 6);
        sK("<Pf3>", true);
        wait(1, 1, 1, 1);
        enterKey();
        wait(2, 5, 2, 6);
        enterKey();
        wait(4, 14, 4, 15);
        mT(15,11);
        wait(15, 8, 15, 11);
        sK("COETB, CODOSB, DDETDV, HEETDV, CTETDV, COEM, COPO, ZTSUP )", true);
        mT(17, 6);
        sK("i", true);
        enterKey();
        wait(18, 8, 18, 9);
        mT(17, 11);
        wait(17, 8, 17, 11);
        sK("'", false);
        sbFile.append(banque.getCoetb());
        sbFile.append("', '");
        nL("\"");
        sK(" CODOSB", true);
        sK("'", false);
        sbFile.append(date);
        sbFile.append("', '23.00.00',");
        nL("\"");
        mT(18,9);
    }
    
    /**
     * Change la position du curseur aux coordonnées données
     * @param a
     * @param b
     */
    private void mT(int a, int b)
    {
        sbFile.append("MoveTo(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        nL("\n");
    }
    
    /**
     * Ecris le texte à l'écran avec ou non retour à la ligne
     * 
     * @param string
     *          texte à afficher à l'écran
     * @param chariot
     *          indique s'il y a besoin d'un retour à la ligne
     */
    private void sK(String string, boolean chariot)
    {
        sbFile.append("SendKeys \"");
        sbFile.append(string);
        if (chariot)
            nL("\"");
    }
    
    /**
     * Appuis sur Entrée
     */
    private void enterKey()
    {
        sK("<Enter>", true);
    }

    /**
     * Pause pour être sûr de la position du curseur et que l'on a récupéré la main
     * @param a
     * @param b
     * @param c
     * @param d
     */
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
        nL(")");
    }
    
    private void wForU()
    {
        sbFile.append("WaitForKbdUnlock()\n");
    }
    
    /**
     * Boucle pour retourner à l'écran initial d'une plaque
     */
    private void loop()
    {
        sbFile.append("TEST = GetString(09,11,40)\n");
        sbFile.append("Do While TEST is not GOODVALUE\n");
        sK("<Pf3>", true);
        wForU();
        sbFile.append("Loop\n");
        wForU();
    }
    
    private void varInit()
    {
        sbFile.append("'-------------------------------------\n");
        sbFile.append("'MACRO ARCHIVAGE DOSSIER EQUINOXE\n");
        sbFile.append("'BANQUE : ").append(banque.getNom()); nL();
        sbFile.append("'COSCE : ").append(cosce); nL();
        sbFile.append("'-------------------------------------\n");
        nL();
        sbFile.append("'Initialisation des variables\n");
        sbFile.append("Dim CODOSB as String '-- CODOSB du scénario à archiver\n");
        sbFile.append("Dim COEM as String '-- COEM du banquier ayant modifier en dernier le scénario\n");
        sbFile.append("Dim COPO as String '-- COPO du banquier ayant modifier en dernier le scénario\n");
        sbFile.append("Dim TEST as String '-- variable permettant de tester si on est revenu sur l'écran initial d'une plaque\n");
        sbFile.append("Const GOODVALUE =  \"1  PDF/1    - View - Display Source Data\" '-- constante de contrôle de l'écran initial d'une plaque.\n");
        nL();
    }
    
    private void nL(String string)
    {
        if (string != null)
            sbFile.append(string);
        sbFile.append("\n");        
    }
    
    private void nL()
    {
        sbFile.append("\n");        
    }
    
    /* ---------- ACCESS ---------- */
    
}
