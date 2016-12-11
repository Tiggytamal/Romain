package utilities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Permet de creer un convertisseru de date thread safe
 * 
 * @author Grégoire Mathon
 * @since 1.0
 */
public class ThreadDateFormat extends ThreadLocal<DateFormat>
{
	/* Attributes */
	
	private String format;
	private SimpleDateFormat sdf;
	
	/* Constructors */
	
	public ThreadDateFormat(String format)
	{
		this.format = format;
		sdf = new SimpleDateFormat(format);
	}
	
	/* Methods */
	
	@Override
	public DateFormat get()
	{
		return super.get();
	}

	@Override
	protected DateFormat initialValue()
	{
		return new SimpleDateFormat(format);
	}

	@Override
	public void remove()
	{
		super.remove();
	}

	@Override
	public void set(DateFormat value)
	{
		super.set(value);
	}
	
	/* Access */

	/**
	 * Retourne le SimpleDateFormat
	 * 
	 * @return
	 */
	public SimpleDateFormat getSdf()
	{
		return sdf;
	}

	public void setSdf(SimpleDateFormat sdf)
	{
		this.sdf = sdf;
	}
}