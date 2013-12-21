package server;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import utility.Msg;

public class Server
{
	private ServerSocket serversocket = null; /** Endpoint on the Server */
	private ArrayList<SocketThread> connectedClients = new ArrayList<SocketThread>();
	

	public Server(int port) throws IOException
	{
		serversocket = new ServerSocket(port);
		while (true)
		{
			SocketThread thread = new SocketThread(serversocket.accept())
				{
					@Override
					public void run()
					{
						try
						{
							protokol(clientsocket,out,in,this); // this is a SocketThread
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				};
			connectedClients.add(thread);
			thread.start();
		}
	}

	private void protokol(Socket clientsocket, ObjectOutputStream out,ObjectInputStream in, SocketThread activethread) throws IOException
	{
		boolean shutdown = false;
		/** Determinates if the Client wants to disconnect */

		while (shutdown == false)
		{
			Msg nextCMD = activethread.st.readMsg(clientsocket,in);
			while(nextCMD == null){}
			
			if (nextCMD.getId() == 'r')
			{
				broadcast(clientsocket, out, activethread, nextCMD);
			}
			if(nextCMD.getId() == ';')
			{
				clientsocket.close();
				shutdown = true;
				connectedClients.remove(activethread);
				System.out.println("is down");
			}
		}
	}
	public synchronized void broadcast(Socket clientsocket, ObjectOutputStream out, SocketThread activethread, Msg p_msg)
	{
		for (SocketThread soth : connectedClients) // removes Thread from list of all connected clients
		{
			soth.st.writeMsg(soth.clientsocket, soth.out, p_msg);
		}
		
		System.err.println(p_msg.getContent()+" I am the Server");
	}
	
}
