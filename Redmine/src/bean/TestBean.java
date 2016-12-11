package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import dao.DaoIncident;
import model.Incident;

@ManagedBean (name = "test")
@RequestScoped
public class TestBean implements Serializable
{

    private static final long serialVersionUID = 1L;

    @EJB
    private DaoIncident daoi;

    public String test()
    {
        long a =System.currentTimeMillis();
        List<Incident> list = daoi.findOne("Pôle T-ICR");
        long b =System.currentTimeMillis();
        System.out.println((b-a)/1000);
        System.out.println(list.size());
        a =System.currentTimeMillis();
        list = new ArrayList<>(28000);
        list = daoi.readAll();
        b =System.currentTimeMillis();
        System.out.println((b-a)/1000);
        System.out.println(list.size());

        return "";
    }
}
