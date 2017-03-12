package bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

@ManagedBean (name = "hebdo")
@ViewScoped
public class HebdoBean
{

    // Bean de chargement des incidents
    @ManagedProperty (value = "#{incident}")
    private IncidentBean incidentBean;
    
    // Session Bean
    @ManagedProperty (value = "#{list}")
    private ListBean listBean;
    
    public String charger(String nomPole)
    {
        
        
        return "";
    }
    
}
