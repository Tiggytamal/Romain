package sonarapi.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Views 
{

	private List<View> listeViews;

	@XmlElementWrapper (name = "views")
	@XmlElement (name = "views")
	public List<View> getListeViews() 
	{
		if (listeViews == null)
		{
			return new ArrayList<>();
		}
		return listeViews;
	}

	public void setListeViews(List<View> views) 
	{
		this.listeViews = views;
	}

	@Override
	public String toString() 
	{
		StringBuilder builder = new StringBuilder();
		if (listeViews != null)
		{
			for (int i = 0; i < listeViews.size(); i++) 
			{
				builder.append(listeViews.get(i).toString());
				builder.append("\n");
			}
		}
		return builder.toString();
	}
}
