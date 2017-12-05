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
    
    /** COSCE du sc�nario � archiver */
    private String cosce;
    /** Banque du sc�nario � archiver */
    private BanqueXML banque;
    /** Num�ro de l'incident */
    private String incident;
    /** Stringbuilder de cr�atin de la macro*/
    private StringBuilder sbFile;
    /** Sate de l'archivage*/
    private String date;

    
    /* ---------- CONSTUCTORS ---------- */
    
    public MacroControl(String cosce, BanqueXML banque, String incident)
    {
        this.cosce = cosce;
        this.banque = banque;
        
        // Traitement pour prendre que les 7 derniers chiffres de l'incident
        if (incident.length() > 7)
            this.incident = incident.substring(incident.length()-7);
        else
            this.incident = incident;
        
        sbFile = new StringBuilder();
        
        // R�cup�ration de la date du jour au bon format
        date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }
    
    /* ---------- METHODS ---------- */
    
    /**
     * Ecriture de la macro
     * 
     * @param file
     */
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
    
    /**
     * Cr�ation de la macro
     * @return
     */
    private String concatenationMacro()
    {
        varInit();
        recupCODOSB();
        recupCOEMCOPO();
        creationRequete();
        MsgBox();
        return sbFile.toString();
    }
    
    /**
     * R�cup�ration du CODOSB en TN5C
     */
    private void recupCODOSB()
    {
        sbFile.append("\n'----- R�cup�ration du CODOSB depuis le COSCE -----\n");
        loop(banque.getCodePlaque());
        mT(3,15);
        sK("fad", true);
        enterKey();
        mT(2,15);
        sK("1", true);
        enterKey();
        mT(7,21);
        sK("TN5C", true);
        mT(6,23);
        sK(banque.getCoad(), true);        
        enterKey();
        sK("<Tab><Tab><Tab>='", false);
        sbFile.append(banque.getCoetb());
        sbFile.append("'<Down>='");
        sbFile.append(cosce);
        sbFile.append("'\"\n");
        mT(23, 3);
        sK("s", true);
        enterKey();
        sK("<Pf6>", true);
        mT(9, 11);
        sbFile.append("CODOSB = GetString(09,11,15)\n");
        sK("<Pf3>", true);
        sK("<Pf3>", true);
    }
    
    /**
     * R�cup�ration du COEM et du COPO en TN5F
     */
    private void recupCOEMCOPO()
    {
        sbFile.append("\n'----- R�cup�ration du COPO et COEM du dernier agent ayant trait� le sc�nario -----\n");
        mT(7,24);
        sK("F", true);
        enterKey();
        mT(10,3);
        sK("<Tab><Tab><Tab>='", false);
        sbFile.append(banque.getCoetb());
        sbFile.append("'<Down>='");
        sbFile.append(cosce);
        sbFile.append("'\"\n");
        mT(12,3);
        sK("<Tab>1<Tab>d<Pf6>", true);
        mT(15,3);
        sK("s", true);
        mT(16,3);
        sK("s<Pf6>", true);
        mT(9,11);
        sbFile.append("COEM = GetString(09,11,7)\n");
        mT(9,20);
        sbFile.append("COPO = GetString(09,20,5)\n");
        sK("<Pf3>", true);
        sK("<Pf3>", true);
        sK("<Pf3>", true);
        sK("<Attn>", true);       
    }
    
    /**
     * Cr�ation des requ�tes sur DD01
     */
    private void creationRequete()
    {
        sbFile.append("\n'----- Cr�ation des requ�tes sur l'environnement de DEV -----\n");
        loop("1");
        sK("DEV", true);
        enterKey();
        wForU();
        sK("t", true);
        enterKey();
        wForU();
        sK("1", true);
        enterKey();
        wForU();
        enterKey();
        sbFile.append("'Requ�te TN5B\n");
        mT(9,42);
        sK("TN5B<Tab>I<Tab>1<Tab>MCE<Tab><Tab>I", false);
        sbFile.append(incident);
        nL("\"");
        enterKey();
        wForU();
        enterKey();
        wForU();
        enterKey();
        wForU();
        mT(27,22);
        sK("C�", false);
        sbFile.append(banque.getCoad());
        sbFile.append("E0");
        nL("\"");
        enterKey();
        mT(28,6);
        sK("s", true);
        enterKey();
        sK("<Pf3>", true);
        enterKey();
        wForU();
        enterKey();
        wForU();
        mT(15,11);
        sK("COETB, CODOSB, DDETDV, HEETDV, CTETDV, COEM, COPO, ZTSUP )", true);
        mT(17, 6);
        sK("i", true);
        enterKey();
        mT(17, 11);
        sK("'", false);
        sbFile.append(banque.getCoetb());
        sbFile.append("', '\" & CODOSB & \"', '");
        sbFile.append(date);
        sbFile.append("', '23.00.00',\"\n");
        mT(18,9);
        sK("'02', '\" & COEM & \"', '\" & COPO & \"', CURRENT TIMESTAMP );", true);
        enterKey();
        wForU();
        enterKey();
        sK("<Pf3>", true);
        wForU();
        sK("<Pf3>", true);
        wForU();       
        sK("1", true);
        enterKey();
        wForU();
        enterKey();
        wForU();
        sbFile.append("'Requ�te TN5F\n");
        mT(9,42);
        sK("TN5F<Tab>I<Tab>1<Tab>MCE<Tab><Tab>I", false);
        sbFile.append(incident);
        nL("\"");
        enterKey();
        wForU();
        enterKey();
        wForU();
        enterKey();
        wForU();
        mT(27,22);
        sK("C�", false);
        sbFile.append(banque.getCoad());
        sbFile.append("E0");
        nL("\"");
        enterKey();
        mT(28,6);
        sK("s", true);
        enterKey();
        sK("<Pf3>", true);
        wForU();
        enterKey();
        wForU();
        enterKey();
        wForU();
        mT(15,11);
        sK("COETB, COSCE, DDETSC, HEETSC, CTETSC, COEM, COPO, ZTSUP )", true);
        mT(17, 6);
        sK("i", true);
        enterKey();
        mT(17, 11);
        sK("'", false);
        sbFile.append(banque.getCoetb());
        sbFile.append("', '\" & COSCE & \"', '");
        sbFile.append(date);
        sbFile.append("', '23.00.00',\"\n");
        mT(18,9);
        sK("'02', '\" & COEM & \"', '\" & COPO & \"', CURRENT TIMESTAMP );", true);
        enterKey();
        wForU();
        sK("<Pf3>", true);
        wForU();
        enterKey();
        wForU();
        sK("<Pf3>", true);
        wForU();
        sK("<Pf3>", true);
        wForU();
        sK("2", true);
        enterKey();
        wForU();
        sK("m<Pf8>", true);
        mT(41,2);
        sK("d", true);
        mT(42,2);
        sK("d", true);
        enterKey();      
    }
    
    /**
     * Cr�e le message de retour � la fin de la macro
     */
    private void MsgBox()
    {
        sbFile.append("\n' ----- Cr�ation du message de fin -----\n" );
        sbFile.append("Result = MsgBox(\"Requ�tes cr��es et envoy�es en production\", vbOKOnly, \"Archivage TN5B / TN5F\")\n");
        sbFile.append("End");
    }
    
    /**
     * Change la position du curseur aux coordonn�es donn�es
     * @param a
     * @param b
     */
    private void mT(int a, int b)
    {
        sbFile.append("MoveTo(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        nL(")");
        wForC(a, b);
        wForU();
    }
    
    /**
     * Ecris le texte � l'�cran avec ou non retour � la ligne
     * 
     * @param string
     *          texte � afficher � l'�cran
     * @param chariot
     *          indique s'il y a besoin d'un retour � la ligne
     */
    private void sK(String string, boolean chariot)
    {
        sbFile.append("SendKeys \"");
        sbFile.append(string);
        if (chariot)
            nL("\"");
    }
    
    /**
     * Appuis sur Entr�e
     */
    private void enterKey()
    {
        sK("<Enter>", true);
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
     * Boucle pour retourner � l'�cran initial d'une plaque
     * @param numeroPlaque
     *              num�ro de le plaque
     */
    private void loop(String numeroPlaque)
    {
        sbFile.append("\n'-- Boucle pour retourner sur l'�cran initial de la plaque ");
        sbFile.append(numeroPlaque);        
        nL();
        sK(numeroPlaque, true);
        mT(23,12);
        enterKey();
        enterKey();
        sbFile.append("TEST = GetString(09,11,40)\n");
        sbFile.append("fin = 0\n");
        sbFile.append("Do While fin = 0\n");
        sbFile.append("If StrComp(TEST, GOODVALUE) = 0 Then fin = 1\n");
        sbFile.append("Else SendKeys \"<Pf3>\"\n");
        sbFile.append("Endif\n");
        sbFile.append("Loop\n");
        sbFile.append("'-- Fin de la boucle\n");
        nL();
    }
    
    /**
     * Initialisation des variables de la MACRO
     */
    private void varInit()
    {
        sbFile.append("'-------------------------------------\n");
        sbFile.append("'MACRO ARCHIVAGE DOSSIER EQUINOXE\n");
        sbFile.append("'BANQUE : ").append(banque.getNom()); nL();
        sbFile.append("'COSCE : ").append(cosce); nL();
        sbFile.append("'-------------------------------------\n");
        nL();
        sbFile.append("'Initialisation des variables\n");
        sbFile.append("Dim CODOSB as String '-- CODOSB du sc�nario � archiver\n");
        sbFile.append("Dim COEM as String '-- COEM du banquier ayant modifi� en dernier le sc�nario\n");
        sbFile.append("Dim COPO as String '-- COPO du banquier ayant modifi� en dernier le sc�nario\n");
        sbFile.append("Dim TEST as String '-- variable permettant de tester si on est revenu sur l'�cran initial d'une plaque\n");
        sbFile.append("Const GOODVALUE =  \"1  PDF/1    - View - Display Source Data\" '-- constante de contr�le de l'�cran initial d'une plaque.\n");
        nL();
    }
    
    /**
     * Saut de ligne avec ajout
     * @param string
     */
    private void nL(String string)
    {
        if (string != null)
            sbFile.append(string);
        sbFile.append("\n");        
    }
    
    /**
     * Saut de ligne
     */
    private void nL()
    {
        sbFile.append("\n");        
    }
    
    /* ---------- ACCESS ---------- */
    
}
