package utility;


import java.io.Serializable;
import java.util.Date;

public class Msg implements Serializable
{
	private String content;
	private char id;
	private Date date = new Date();

	public Msg(String p_content, char p_id)
	{
		this.content = p_content;
		this.id = p_id;
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
}
