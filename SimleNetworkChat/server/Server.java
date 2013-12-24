package server;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import utility.Msg;

public class Server
{
	/** Endpoint on the Server */
	private ServerSocket serversocket = null; 
	/** A list of all clients that are connected to the server.*/
	private List<ServerClient> connectedClients = Collections.synchronizedList(new ArrayList<ServerClient>());
	private Room[] rooms = new Room[4];
	
	public Server(int port) throws IOException
	{
		rooms[0] = new Room("Spawnroom");
		rooms[1] = new Room("Redeecke");
		rooms[2] = new Room("Fanboy Streitecke");
		rooms[3] = new Room("Pause");
		
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
							connectedClients.add(this); // this is a SocketThread
							rooms[0].addClientToRoom(this); // Spawnroom // this is a SocketThread
							protokol(this); // this is a SocketThread
						} catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				};
			thread.start();
		}
	}
	
	public void protokol(ServerClient activeclient) throws IOException
	{
		
		boolean shutdown = false;
		/** Determinates if the Client wants to disconnect */
		while (!shutdown)
		{
			Msg nextCMD = activeclient.getSt().readMsg(activeclient.getClientsocket(), activeclient.getIn());
			if(nextCMD.getId() == 'b')		//// Broadcast message to everybody on the server
			{
				broadcast(activeclient, nextCMD);
			}
			if(nextCMD.getId() == 's')		//// Message to a specific person on the server
			{
				secredMessage(activeclient, nextCMD);
			}
			if(nextCMD.getId() == 'r')		//// Message to all people in a room
			{
				roomBroadcast(activeclient, nextCMD);
			}
			if(nextCMD.getId() == '+')		//// get me in room / switch room
			{
				enterRoom(activeclient, nextCMD);
			}
			if(nextCMD.getId() == '-')		//// leave room
			{
				leaveRoom(activeclient);
			}
			if(nextCMD.getId() == 'C')		//// Who is online
			{
				whoisonline(activeclient);
			}
			if(nextCMD.getId() == 'R')		//// Who is online
			{
				sendExistingRooms(activeclient);
			}
			if(nextCMD.getId() == ';')		//// Closes a Connection
			{
				shutdown = true;
				disconnect(activeclient);
			}
		}
	}
	
	/** 'b' */
	private void broadcast(ServerClient activeclient, Msg p_msg)
	{
		for (ServerClient sc : connectedClients) 
		{
			if(sc != activeclient)  // dont ask active client for its name he knows that already ^^
			{
				sc.getSt().writeMsg(sc.getClientsocket(), sc.getOut(), p_msg);
			}	
		}
		System.err.println(p_msg.getContent()+" I am the Server");
	}
	
	/** 's' */
	private void secredMessage(ServerClient activeclient, Msg nextCMD)
	{
		for (ServerClient soth : connectedClients) 
		{
			if(soth.getNickname().equals((String)nextCMD.getObject()))
			{
				soth.getSt().writeMsg(soth.getClientsocket(), soth.getOut(), new Msg((String)nextCMD.getContent() ,activeclient.getNickname(), 's', null));
			}
		}
	}
	
	/** 'r' */
	private void roomBroadcast(ServerClient activeclient, Msg p_msg)
	{
		for(Room r : rooms)
		{
			if(r.getClientsInRoom().contains(activeclient)) // In which room is the thread who wants to roombroadcast ?
			{
				r.roomBroadcast(activeclient, p_msg);
			}
		}
	}
	
	/** '+' */
	private void enterRoom(ServerClient activeclient, Msg nextCMD)
	{	
		for(Room r : rooms)
		{
			if(r.getClientsInRoom().contains(activeclient)) // which room shell be left ?
			{
				r.rmClientFromRoom(activeclient); // leave
			}
			if(r.getName().equals(nextCMD.getContent())) // which room shell be entered ?
			{
				r.addClientToRoom(activeclient); // enter
			}
		}
	}
	
	/** '-' */
	private void leaveRoom(ServerClient activeclient)
	{
		for(Room r : rooms)
		{
			if(r.getClientsInRoom().contains(activeclient)) 
			{
				r.rmClientFromRoom(activeclient); 
			}
		}
	}
	
	/** 'C' */
	private void whoisonline(ServerClient activeclient) throws IOException
	{
		ArrayList<String> clientsOnline = new ArrayList<String>(); 
		for (ServerClient sc : connectedClients)
		{
			if(sc != activeclient)  // dont ask active client for its name he knows that already ^^
			{
				clientsOnline.add(sc.getNickname());
			}
		}
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		activeclient.getSt().writeMsg(activeclient.getClientsocket(), activeclient.getOut(), new Msg("","server",'C',clientsOnline));
	}
	
	/** 'R' */
	private void sendExistingRooms(ServerClient activeclient)
	{
		HashMap<String,ArrayList<String>> existingRooms = new HashMap<String,ArrayList<String>>();
		for (Room r : rooms)
		{
			ArrayList<String> users = new ArrayList<String>();
			for(ServerClient sc : r.getClientsInRoom())
			{
				users.add(sc.getNickname());
			}
			existingRooms.put(r.getName(),users);
		}
		try
		{
			Thread.sleep(10);
		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		activeclient.getSt().writeMsg(activeclient.getClientsocket(), activeclient.getOut(), new Msg("","server",'R',existingRooms));
	}
	
	/** ';' */
	public void disconnect(ServerClient activeclient)
	{
		try {
			leaveRoom(activeclient); // The Client needs to be away completely !
			connectedClients.remove(activeclient);
			activeclient.getSt().writeMsg(activeclient.getClientsocket(), activeclient.getOut(), new Msg("", "server", ';', null));
			activeclient.getClientsocket().close();
			System.out.println("is down");
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
