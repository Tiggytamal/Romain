package sonarapi.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class View 
{
	/* Attributs */
	
	private String key;
	private String name;
	private boolean selected;
	private String selectionMode;

	/* Accesseurs */
	
	@XmlAttribute (name = "key")
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	@XmlAttribute (name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() 
	{
		return "View [key=" + key + ", name=" + name + "]";
	}
	
	@XmlAttribute (name = "selected", required = false)
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@XmlAttribute (name = "selectionMode", required = false)
	public String getSelectionMode() {
		return selectionMode;
	}

	public void setSelectionMode(String selectionMode) {
		this.selectionMode = selectionMode;
	}	
}