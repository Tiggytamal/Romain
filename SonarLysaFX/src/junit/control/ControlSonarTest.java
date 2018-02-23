package junit.control;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import control.ControlSonar;
import control.ControlXML;
import junit.TestUtils;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlSonarTest
{
	private ControlSonar handler;
	public static boolean deser;

	@BeforeAll
	public void init() throws InvalidFormatException, JAXBException, IOException, InterruptedException
	{
		// handler = new ControlSonar();
		handler = new ControlSonar("ETP8137", "28H02m89,;:!");
		deser = false;
		new ControlXML().recuprerParamXML();
	}

	@Test
	public void recupererLotsSonarQube() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		TestUtils.callPrivate("recupererLotsSonarQube", handler, null);
	}

	@Test
	public void testCreerVueProduction() throws InvalidFormatException, IOException
	{
		handler.creerVueProduction(new File("d:\\Classeur1.xlsx"));
	}

	@Test
	public void majVues() throws InvalidFormatException, IOException
	{
		handler.majVues();
	}

	@Test
	public void créationHeader() throws InvalidFormatException, IOException
	{
		String codeUser;
		StringBuilder builder = new StringBuilder("ETP8137");
		builder.append(":");
		builder.append("28H02m89,;:!");
		codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());

		builder = new StringBuilder("admin");
		builder.append(":");
		builder.append("admin");
		codeUser = Base64.getEncoder().encodeToString(builder.toString().getBytes());
		codeUser.length();
	}

	@Test
	public void testRecupererComposantsSonar() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException
	{
		TestUtils.callPrivate("recupererComposantsSonar", handler, null);
	}

	@Test
	public void testCreerVueParApplication()
	{
		handler.creerVueParApplication();
	}


	@Test
	public void testcreerVuesQGErreur() throws InvalidFormatException, IOException, JAXBException
	{
		handler.creerVuesQGErreur();
	}
	
	@Test
	public void testCreerVuesDatastage()
	{
		handler.creerVuesDatastage();
	}
}