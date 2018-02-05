package sonarapi.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Classe de wrapping des retour JSON.
 * tous les attibuts sont optionnels,pour permettre le retour de n'importe quel type d'objet.
 * @author ETP8137 - Grégoire Mathon
 *
 */
@XmlRootElement
public class Retour implements ModeleSonar
{
	private Composant component;
	private List<Vue> listeVues;

	@XmlAttribute (name = "component", required = false)
	public Composant getComponent()
	{
		return component;
	}
	
	@XmlAttribute (name = "views", required = false)
	public List<Vue> getListeVues()
	{
		return listeVues;
	}	
}