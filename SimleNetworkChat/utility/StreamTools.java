package utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class StreamTools 
{
	
	public boolean writeMsg(Socket socket, ObjectOutputStream out, Msg p_Msg)
	{
		try
		{
			if (!socket.isConnected())
			{
				socket.close();
				return false;
			}
			out.writeObject(p_Msg);
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
			Msg msg = (Msg) in.readObject();
			while(msg == null){}
			return msg;
		} catch (IOException | ClassNotFoundException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
}
