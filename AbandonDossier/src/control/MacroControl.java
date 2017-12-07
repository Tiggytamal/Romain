package control;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.xml.BanqueXML;
import utilities.FunctionalException;
import utilities.enums.Severity;

public class MacroControl
{
    /* ---------- ATTIBUTES ---------- */
    
    /** COSCE du scénario à archiver */
    private String cosce;
    /** Banque du scénario à archiver */
    private BanqueXML banque;
    /** Numéro de l'incident */
    private String incident;
    /** Stringbuilder de créatin de la macro*/
    private StringBuilder sbFile;
    /** Sate de l'archivage*/
    private String date;
    
    /** Touche F6 */
    private static final String F3 = "<Pf3>";
    /** Touche F3 */
    private static final String F6 = "<Pf6>";
    /** Touche ESC */
    private static final String ESC = "<Attn>";
    

    
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
        
        // Récupération de la date du jour au bon format
        date = LocalDate.now().format(DateTimeFormatter.ISO_DATE);
    }
    
    /* ---------- METHODS ---------- */
    
    /**
     * Ecriture de la macro
     * 
     * @param file
     * @throws FunctionalException 
     */
    public void creerMacro(File file) throws FunctionalException
    {               
        try (BufferedWriter brFile = new BufferedWriter(new FileWriter(file)))
        {                    
            brFile.write(concatenationMacro());
        }
        catch (IOException e)
        {
            throw new FunctionalException(Severity.SEVERITY_ERROR, "Erreur à l'écriture de la macro", true);
        }                
    }
    
    /**
     * Création de la macro
     * @return
     */
    private String concatenationMacro()
    {
        varInit();
        recupCODOSB();
        recupCOEMCOPO();
        creationRequete();
        msgBox();
        return sbFile.toString();
    }
    
    /**
     * Récupération du CODOSB en TN5C
     */
    private void recupCODOSB()
    {
        sbFile.append("\n'----- Récupération du CODOSB depuis le COSCE -----\n");
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
        sK(F6, true);
        mT(9, 11);
        sbFile.append("CODOSB = GetString(09,11,15)\n");
        sK(F3, true);
        sK(F3, true);
    }
    
    /**
     * Récupération du COEM et du COPO en TN5F
     */
    private void recupCOEMCOPO()
    {
        sbFile.append("\n'----- Récupération du COPO et COEM du dernier agent ayant traité le scénario -----\n");
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
        sK(F3, true);
        sK(F3, true);
        sK(F3, true);
        sK(ESC, true);       
    }
    
    /**
     * Création des requêtes sur DD01
     */
    private void creationRequete()
    {
        sbFile.append("\n'----- Création des requêtes sur l'environnement de DEV -----\n");
        loop("1");
        sK("DEV", true);
        enterKey();
        sK("t", true);
        enterKey();
        sK("1", true);
        enterKey();
        enterKey();
        sbFile.append("'Requête TN5B\n");
        mT(9,42);
        sK("TN5B<Tab>I<Tab>1<Tab>MCE<Tab><Tab>I", false);
        sbFile.append(incident);
        nL("\"");
        enterKey();
        enterKey();
        enterKey();
        mT(27,22);
        sK("C£", false);
        sbFile.append(banque.getCoad());
        sbFile.append("E0");
        nL("\"");
        enterKey();
        mT(28,6);
        sK("s", true);
        enterKey();
        sK(F3, true);
        enterKey();
        enterKey();
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
        enterKey();
        sK(F3, true);
        sK(F3, true);      
        sK("1", true);
        enterKey();
        enterKey();
        sbFile.append("'Requête TN5F\n");
        mT(9,42);
        sK("TN5F<Tab>I<Tab>1<Tab>MCE<Tab><Tab>I", false);
        sbFile.append(incident);
        nL("\"");
        enterKey();
        enterKey();
        enterKey();
        mT(27,22);
        sK("C£", false);
        sbFile.append(banque.getCoad());
        sbFile.append("E0");
        nL("\"");
        enterKey();
        mT(28,6);
        sK("s", true);
        enterKey();
        sK(F3, true);
        enterKey();
        enterKey();
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
        sK(F3, true);
        enterKey();
        sK(F3, true);
        sK(F3, true);
        sK("2", true);
        enterKey();
        sK("m<Pf8>", true);
        sK("d", true);
        mT(42,2);
        sK("d", true);
        enterKey();      
    }
    
    /**
     * Crée le message de retour à la fin de la macro
     */
    private void msgBox()
    {
        sbFile.append("\n' ----- Création du message de fin -----\n" );
        sbFile.append("Result = MsgBox(\"Requêtes créées et envoyées en production\", vbOKOnly, \"Archivage TN5B / TN5F\")\n");
        sbFile.append("End");
    }
    
    /**
     * Change la position du curseur aux coordonnées données
     * @param a
     * @param b
     */
    private void mT(int a, int b)
    {
        sbFile.append("Result = MoveTo(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        nL(")");
        wForC(a, b);
        wForU();
    }
    
    /**
     * Ecris le texte à l'écran avec ou non retour à la ligne
     * 
     * @param string
     *          texte à afficher à l'écran
     * @param chariot
     *          indique s'il y a besoin d'un retour à la ligne
     */
    private void sK(String string, Boolean chariot)
    {
        sbFile.append("SendKeys \"");
        sbFile.append(string);
        if (chariot)
        {
            nL("\"");
            wForU();
        }
    }
    
    /**
     * Appuis sur Entrée
     */
    private void enterKey()
    {
        sK("<Enter>", true);
    }
    
    private void wForC(int a, int b)
    {
        sbFile.append("Result = WaitForCursor(");
        sbFile.append(a);
        sbFile.append(", ");
        sbFile.append(b);
        nL(")");
    }
    
    private void wForU()
    {
        sbFile.append("Result = WaitForKbdUnlock()\n");
    }
    
    /**
     * Boucle pour retourner à l'écran initial d'une plaque
     * @param numeroPlaque
     *              numéro de le plaque
     */
    private void loop(String numeroPlaque)
    {
        mT(23,12);
        sK(numeroPlaque, true);
        enterKey();
        enterKey();
        sbFile.append("\n'-- Boucle pour retourner sur l'écran initial de la plaque ");
        sbFile.append(numeroPlaque);        
        nL();
        sbFile.append("TEST = GetString(09,11,40)\n");
        sbFile.append("Do While StrComp(TEST, GOODVALUE) <> 0\n");
        sK(F3, true);
        sbFile.append("TEST = GetString(09,11,40)\n");
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
        sbFile.append("Dim CODOSB as String '-- CODOSB du scénario à archiver\n");
        sbFile.append("Dim COEM as String '-- COEM du banquier ayant modifié en dernier le scénario\n");
        sbFile.append("Dim COPO as String '-- COPO du banquier ayant modifié en dernier le scénario\n");
        sbFile.append("Dim TEST as String '-- variable permettant de tester si on est revenu sur l'écran initial d'une plaque\n");
        sbFile.append("Const GOODVALUE =  \"1  PDF/1    - View - Display Source Data\" '-- constante de contrôle de l'écran initial d'une plaque.\n");
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
