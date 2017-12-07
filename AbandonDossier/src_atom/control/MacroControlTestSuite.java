package control;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;
import model.xml.BanqueXML;
import utilities.Utilities;

public class MacroControlTestSuite extends TestCase
{
    private MacroControl control;
    private Field sbFile;
    private static final String F3 = "<Pf3>";
    
    
    public MacroControlTestSuite()
    {
        super("MacroControl");

    }
    
    @Override
    protected void setUp() throws Exception
    {
        super.setUp();
        control = new MacroControl("COSCE", new BanqueXML("nom", "oad", "coetb", "plaque"), "Incident");
        sbFile = control.getClass().getDeclaredField("sbFile");
        sbFile.setAccessible(true);
    }
    
    @Test
    public void testSK() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
    {
        Utilities.callPrivate(MacroControl.class, control, "sK", new Class[] {String.class, Boolean.class}, new Object[] {"1", true});
        Assert.assertTrue(((StringBuilder) sbFile.get(control)).toString().contains(sKprep("1", true)));
        Utilities.callPrivate(MacroControl.class, control, "sK", new Class[] {String.class, Boolean.class}, new Object[] {F3, true});
        Assert.assertTrue(((StringBuilder) sbFile.get(control)).toString().contains(sKprep(F3, true)));
        Utilities.callPrivate(MacroControl.class, control, "sK", new Class[] {String.class, Boolean.class}, new Object[] {"qsdflkjzer12348qsdfv", false});
        Assert.assertTrue(((StringBuilder) sbFile.get(control)).toString().contains(sKprep("qsdflkjzer12348qsdfv", false)));
        Utilities.callPrivate(MacroControl.class, control, "sK", new Class[] {String.class, Boolean.class}, new Object[] {F3, false});
        Assert.assertTrue(((StringBuilder) sbFile.get(control)).toString().contains(sKprep(F3, false)));
        Utilities.callPrivate(MacroControl.class, control, "sK", new Class[] {String.class, Boolean.class}, new Object[] {"", false});
        Assert.assertTrue(((StringBuilder) sbFile.get(control)).toString().contains(sKprep("", false)));
    }
    
    private String sKprep(String val, boolean bool)
    {
        String retour = "SendKeys \"" + val;
        if(bool)
        {
            return retour + "\"\nResult = WaitForKbdUnlock()\n";
        }
        return retour;
    }
    
}
