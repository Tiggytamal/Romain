package utilities;

import java.util.Locale;

import javax.faces.bean.ManagedBean;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;


@ManagedBean (name ="i18n")
public class I18nBean
{
	/*Attributes*/
	
	private Locale locale;
	private String lang;

	/*Constructors*/
	
	public I18nBean()
	{
	}

	/*Methods*/
	
	public String langChoice()
	{
		// Change la localisation
		int index = lang.indexOf("_");
		if (index > 0)
		{
			String langChoice = lang.substring(0, index);
			String country = lang.substring(index + 1);
			locale = new Locale(langChoice, country);
		}
		else
			locale = new Locale(lang);
		
		// Change la vue dynamiquement
		UIViewRoot viewroot = FacesContext.getCurrentInstance().getViewRoot();
		viewroot.setLocale(locale);
		
		// Change la nouvelle localisation par default de l'application
		FacesContext.getCurrentInstance().getApplication().setDefaultLocale(locale);
		return "";		
	}

	/*Access*/
	
	public Locale getLocale()
	{
		return locale;
	}

	public void setLocale(Locale locale)
	{
		this.locale = locale;
	}

	public String getLang()
	{
		return lang;
	}

	public void setLang(String lang)
	{
		this.lang = lang;
	}
}
