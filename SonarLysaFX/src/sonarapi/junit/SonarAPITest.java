package sonarapi.junit;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Vue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SonarAPITest
{
    private SonarAPI api;
    
    @BeforeAll
    public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
    {
        api = SonarAPI.getInstanceTest();
//        api = new SonarAPI(Statics.URI, "ETP8137", "28H02m89,;:!");
    }
    
	@Test
	public void testMetrics() throws InvalidFormatException, IOException
	{
		@SuppressWarnings("unused")
		Composant composant = api.getMetriquesComposant("fr.ca.cat.cocl.ds:DS_COCL_RepriseOscare_Build:13", new String[] { "bugs", "vulnerabilities" });
	}

	@Test
	public void testGetVues() throws InvalidFormatException, IOException
	{
		List<Vue> vues = api.getVues();
		assertTrue(vues != null && !vues.isEmpty());		
	}
	
	@Test
	public void testGetComposants()
	{
		List<Projet> projets = api.getComposants();
		projets.sort((o1, o2) -> o2.getNom().compareTo(o1.getNom()));
		Pattern pattern = Pattern.compile("^\\D*");
		int i = 0;
		for (Projet projet : projets)
		{			
			System.out.println("nom = " + projet.getNom());
			Matcher matcher = pattern.matcher(projet.getNom());
			if (matcher.find())
				System.out.println("matcher = " + matcher.group(0));
			i++;
			if (i == 50)
				break;
		}
	}
	
	@Test
	public void testCreerVue()
	{
		Vue vue = new Vue();
		vue.setKey("APPLI_Master_5MPR");
		vue.setName("APPLI_Master_5MPR");
		vue.setDescription("vue description");
		api.creerVue(vue);
	}
	
	@Test
	public void testCreerVueAsync()
	{
		Vue vue = new Vue();
		vue.setKey("bueKey");
		vue.setName("Vue Name sdfs df");
		vue.setDescription("vue description");
		api.creerVueAsync(vue);
	}
}
