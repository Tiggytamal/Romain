package model;

import java.io.File;

public interface XML
{
    /**
     * Retourne le fichier de sauvegarde de l'objet
     * @return
     */
    public File getFile();
    
    /**
     * Conbtrole les données du fichier et renvoie une chaine de caractère pour afficher les infos de controle.
     * @return
     */
    public String controleDonnees();
}
