package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import utility.Msg;
import utility.SocketThread;
import utility.StreamTools;

public class Server
{
	private ServerSocket serversocket = null; /** Endpoint on the Server */
	StreamTools st = new StreamTools();

	public Server(int port) throws IOException
	{
		serversocket = new ServerSocket(port);
		while (true)
		{
			Socket clientserver = serversocket.accept();
				new SocketThread(clientserver)
				{
					@Override
					public void run()
					{
						try
						{
							ObjectOutputStream out = new ObjectOutputStream(clientsocket.getOutputStream());
							ObjectInputStream in = new ObjectInputStream(clientsocket.getInputStream());
							st.writeMsg(clientsocket,out,"lolotrolololotrolo");
							protokol(clientsocket,out,in);
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}.start();
		}
	}

	private void protokol(Socket clientsocket, ObjectOutputStream out,ObjectInputStream in) throws IOException
	{
		boolean shutdown = false;
		/** Determinates if the Client wants to disconnect */

		while (shutdown == false)
		{
			Msg nextCMD = st.readMsg(clientsocket,in);
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
				clientsocket.close();
				shutdown = true;
			}
		}
	}
    
	
	
}
