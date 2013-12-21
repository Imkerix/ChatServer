package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import utility.Msg;

public class Server
{
	private ServerSocket serversocket = null; /** Endpoint on the Server */
	private List<ServerClient> connectedClients = Collections.synchronizedList(new ArrayList<ServerClient>());
	public Server(int port) throws IOException
	{
		serversocket = new ServerSocket(port);
		while (true)
		{
			ServerClient thread = new ServerClient(serversocket.accept())
				{
					@Override
					public void run()
					{
						try
						{
							protokol(this); // this is a SocketThread
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

	private void protokol(ServerClient activeclient) throws IOException
	{
		boolean shutdown = false;
		/** Determinates if the Client wants to disconnect */
		while (shutdown == false)
		{
			Msg nextCMD = activeclient.st.readMsg(activeclient.clientsocket, activeclient.in);
			if(nextCMD.getId() == 'b')		//// Broadcast message to everybody on the server
			{
				broadcast(activeclient.clientsocket, activeclient, nextCMD);
			}
			if(nextCMD.getId() == 's')		//// Message to a specific person on the server
			{
			}
			if(nextCMD.getId() == 'r')		//// Message to all people in a room
			{
			}
			if(nextCMD.getId() == '?')		//// Who is online
			{
				whoisonline(activeclient.clientsocket, activeclient);
			}
			if(nextCMD.getId() == ';')		//// Closes a Connection
			{
				shutdownThread(activeclient.clientsocket, activeclient);
				shutdown = true;
			}
		}
	}
	
	public void broadcast(Socket clientsocket, ServerClient activeclient, Msg p_msg)
	{
		for (ServerClient soth : connectedClients) // removes Thread from list of all connected clients
		{
			soth.st.writeMsg(soth.clientsocket, activeclient.out, p_msg);
		}
		System.err.println(p_msg.getContent()+" I am the Server");
	}
	
	public void shutdownThread(Socket clientsocket, ServerClient activeclient) throws IOException
	{
		clientsocket.close();
		connectedClients.remove(activeclient);
		System.out.println("is down");
	}
	
	public void whoisonline(Socket clientsocket, ServerClient activeclient) throws IOException
	{
		ArrayList<String> clientsOnline = new ArrayList<String>(); 
		for (ServerClient sc : connectedClients)
		{
			if(sc != activeclient)  // dont ask active client for its name he knows that already ^^
			{
					clientsOnline.add(sc.nickname);
			}
		}
		activeclient.st.writeMsg(activeclient.clientsocket, activeclient.out, new Msg("",'o',clientsOnline));
	}
}
