package junit.control;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Base64;

import javax.xml.bind.JAXBException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import control.ControlSonar;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ControlSonarTest
{
	private ControlSonar handler;

	@BeforeAll
	public void init() throws InvalidFormatException, JAXBException, IOException
	{
//		handler = new ControlSonar();
		handler = new ControlSonar("ETP8137", "28H02m89,;:!");
	}

	@Test
	public void recupererLotsSonarQube() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		Method[] methods = ControlSonar.class.getDeclaredMethods();
		Method call = null;
		for (Method method : methods)
		{
			if (method.getName().equals("recupererLotsSonarQube"))
			{
				call = method;
				call.setAccessible(true);
				break;
			}
		}		
		call.invoke(handler);
	}

	@Test
	public void testCreerVutetroduction() throws InvalidFormatException, IOException
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
	public void testRecupererComposantsSonar() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException
	{
		Method[] methods = ControlSonar.class.getDeclaredMethods();
		for (Method method : methods)
		{
			if (method.getName().equals("recupererComposantsSonar"))
			{
				method.setAccessible(true);
				method.invoke(handler);
				break;
			}
		}
	}
	
	@Test
	public void testCreerVueParApplication()
	{
		handler.creerVueParApplication();
	}
	
	@Test
	public void testControlerSonar()
	{
		handler.controlerSonarQube();
	}
	
	@Test
	public void testcreerVuesQGErreur()
	{
		handler.creerVuesQGErreur();
	}
}