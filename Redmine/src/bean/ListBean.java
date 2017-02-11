package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import model.Incident;
import utilities.interfaces.Instance;

@ManagedBean (name = "list")
@SessionScoped
public class ListBean implements Serializable, Instance
{
    /* ---------- ATTIBUTES ---------- */
    
    private static final long serialVersionUID = 1L;
    
    private List<Incident> listIncidents;

    public ListBean()
    {
        instanciation();
    }

    /* ---------- CONSTUCTORS ---------- */
    
    @Override
    public void instanciation()
    {
        listIncidents = new ArrayList<>();
    }

    /* ---------- METHODS ---------- */
    
    
    /* ---------- ACCESS ---------- */
    
    /**
     * @return the listIncidents
     */
    public List<Incident> getListIncidents()
    {
        return listIncidents;
    }

    /**
     * @param listIncidents the listIncidents to set
     */
    public void setListIncidents(List<Incident> listIncidents)
    {
        this.listIncidents = listIncidents;
    }


}
