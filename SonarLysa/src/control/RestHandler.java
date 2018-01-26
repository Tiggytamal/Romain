package control;

import java.util.HashMap;
import java.util.Map;

import sonarapi.SonarAPI;
import sonarapi.model.View;
import sonarapi.model.Views;
import utilities.Statics;

public class RestHandler 
{
	private SonarAPI api;
	
	public RestHandler()
	{
		String name = "ETP8137";
        String password = "28H02m89,;:!";
		api = SonarAPI.getInstance(Statics.URI, name, password);		
	}
	
	
	/**
	 * @param arg
	 */
	public static void main (String... arg) 
	{
		RestHandler handler = new RestHandler();
        Views views = handler.api.getViews();
        System.out.println(views.getListeViews().size());
        System.out.println(handler.api.verificationUtilisateur());       
	}
	
	public Map<String, View> recupererLots()
	{
		Map<String, View> map = new HashMap<>();
		Views views = api.getViews();
		for (View view : views.getListeViews()) 
		{
			if (view.getName().startsWith("Lot "))
				map.put(view.getName(), view);
		}
		return map;
	}
	
	
}
