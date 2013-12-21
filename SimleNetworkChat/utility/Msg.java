package utility;


import java.io.Serializable;
import java.util.Date;

public class Msg implements Serializable
{
	private static final long serialVersionUID = 1L;
	private String content;
	private Object object;
	private char id;
	private Date date = new Date();

	public Msg(String p_content, char p_id, Object p_object)
	{
		this.content = p_content;
		this.id = p_id;
		this.object = p_object;
	}

	public String getContent()
	{
		return content;
	}

	public char getId()
	{
		return id;
	}

	public Date getDate()
	{
		return date;
	}
	
	public Object getObject()
	{
		return object;
	}
	
}
