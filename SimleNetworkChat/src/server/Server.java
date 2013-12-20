package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import msg.Msg;

public class Server
{
	private ServerSocket serversocket = null;
	/** Endpoint on the Server */
	private Socket clientserver;

	ObjectOutputStream out;
	ObjectInputStream in;

	public Server(int port) throws IOException
	{
		serversocket = new ServerSocket(port);
		while (true)
		{
			clientserver = serversocket.accept();

			if (clientserver != null)
			{
				new Thread()
				{
					@Override
					public void run()
					{
						try
						{
							out = new ObjectOutputStream(clientserver.getOutputStream());
							in = new ObjectInputStream(clientserver.getInputStream());
							writeMsg("lolotrolololotrolo");
							protokol(clientserver);
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
	}

	private void protokol(Socket clientserver) throws IOException
	{
		boolean shutdown = false;
		/** Determinates if the Client wants to disconnect */

		while (shutdown == false)
		{
			Msg nextCMD = (Msg) readMsg();
			while(nextCMD == null){}
			
			if (nextCMD.getId() == 'r')
			{
				System.err.println(nextCMD.getContent());
			}
			if(nextCMD.getId() == ';')
			{
				out.writeChar('+');
				in.close();
				out.close();
				clientserver.close();
				shutdown = true;
			}
		}
	}

	public boolean writeMsg(String p_msg)
	{
		try
		{
			if (!clientserver.isConnected())
			{
				clientserver.close();
				return false;
			}
			out.writeObject((new Msg(p_msg,'r')));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
		return true;
	}

	private Msg readMsg()
	{
		try
		{
			if (!clientserver.isConnected())
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
