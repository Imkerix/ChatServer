package server;

import java.util.ArrayList;

import utility.Msg;

public class Room 
{
	private String name;
	private ArrayList<ServerClient> clientsInRoom = new ArrayList<ServerClient>();
	
	public Room(String p_name)
	{
		this.name = p_name;
	}
	
	public void roomBroadcast(ServerClient activeclient, Msg p_msg)
	{
		for (ServerClient sc : clientsInRoom)
		{
			if(sc != activeclient)  // dont ask active client for its name he knows that already ^^
			{
				sc.getSt().writeMsg(sc.getClientsocket(), sc.getOut(), p_msg);
			}	
		}
	}
	
	public void addClientToRoom(ServerClient sc)
	{
		
		for(ServerClient c : clientsInRoom) // nachricht an alle anderen im Raum, neuer typ da
		{
			c.getSt().writeMsg(c.getClientsocket(), c.getOut(), new Msg(sc.getNickname()+" entered...", name, 'i', null));
		}
		clientsInRoom.add(sc);
	}
	
	public void rmClientFromRoom(ServerClient sc)
	{
		if(clientsInRoom.contains(sc)) // gibt es den Typ
		{
			clientsInRoom.remove(sc);
			for(ServerClient c : clientsInRoom) // nachricht an alle anderen im Raum, typ weg
			{
				c.getSt().writeMsg(c.getClientsocket(), c.getOut(), new Msg(sc.getNickname()+" left...", name, 'i', null));
			}
		}
	}

	public String getName()
	{
		return name;
	}

	public ArrayList<ServerClient> getClientsInRoom()
	{
		return clientsInRoom;
	}
}
