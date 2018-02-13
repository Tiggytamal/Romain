package model.enums;

public enum Environnement
{
    NOUVEAU(Valeur.NOUVEAU),
    DEVTU(Valeur.DEVTU),
    TFON(Valeur.TFON),
    VMOE(Valeur.VMOE),
    VMOA(Valeur.VMOA),
    EDITION(Valeur.EDITION);
    
    private final String string;
    
    private Environnement(String string)
    {
        this.string = string;
    }
    
    @Override
    public String toString()
    {
        return string;
    }
    
    public static Environnement getEnvironnement(String envString)
    {
        switch(envString)
        {
            case Valeur.NOUVEAU :
                return NOUVEAU;
            case Valeur.DEVTU :
                return DEVTU;
            case Valeur.TFON :
                return TFON;
            case Valeur.VMOE :
                return VMOE;
            case Valeur.VMOA :
                return VMOA;
            case Valeur.EDITION :
                return EDITION;
            default :
                return null;                
        }
    }
    
    private static class Valeur
    {
        private Valeur() {}
        
        private static final String NOUVEAU = "NOUVEAU";
        private static final String DEVTU = "DEVTU";
        private static final String TFON = "TFON";
        private static final String VMOE = "VMOE";
        private static final String VMOA = "VMOA";
        private static final String EDITION = "EDITION";
    }
}
