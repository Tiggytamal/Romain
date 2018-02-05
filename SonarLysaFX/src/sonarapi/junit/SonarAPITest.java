package sonarapi.junit;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.Test;

import sonarapi.SonarAPI;
import sonarapi.model.Composant;
import sonarapi.model.Projet;
import sonarapi.model.Vue;

public class SonarAPITest
{
	@Test
	public void testMetrics() throws InvalidFormatException, IOException
	{
		SonarAPI api = SonarAPI.getInstanceTest();
		@SuppressWarnings("unused")
		Composant composant = api.getMetriquesComposant("fr.ca.cat.cocl.ds:DS_COCL_RepriseOscare_Build:13", new String[] { "bugs", "vulnerabilities" });
	}

	@Test
	public void testGetVues() throws InvalidFormatException, IOException
	{
		SonarAPI api = SonarAPI.getInstanceTest();
		@SuppressWarnings("unused")
		List<Vue> vues = api.getVues();
	}
	
	@Test
	public void testGetComposants()
	{
		SonarAPI api = SonarAPI.getInstanceTest();
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
		SonarAPI api = SonarAPI.getInstanceTest();
		Vue vue = new Vue();
		vue.setKey("bueKey");
		vue.setName("Vue Name sdfs df");
		vue.setDescription("vue description");
		api.creerVue(vue);
	}
	
	@Test
	public void testCreerVueAsync()
	{
		SonarAPI api = SonarAPI.getInstanceTest();
		Vue vue = new Vue();
		vue.setKey("bueKey");
		vue.setName("Vue Name sdfs df");
		vue.setDescription("vue description");
		api.creerVueAsync(vue);
	}
}
