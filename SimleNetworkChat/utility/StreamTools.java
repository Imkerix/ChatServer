package utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StreamTools 
{
	
	public boolean writeMsg(Socket socket, ObjectOutputStream out, String p_msg)
	{
		try
		{
			if (!socket.isConnected())
			{
				socket.close();
				return false;
			}
			out.writeObject((new Msg(p_msg,'r')));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	public Msg readMsg(Socket socket, ObjectInputStream in)
	{
		try
		{
			if (!socket.isConnected())
			{
				return null;
			}
			return (Msg) in.readObject();
		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
}
