package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.StreamTools;

public class ServerClient extends Thread 
{
	private String nickname;
	private Socket clientsocket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	StreamTools st = new StreamTools();
	
	public ServerClient(Socket p_socket)
	{
		this.clientsocket = p_socket;
		
		try 
		{
			out = new ObjectOutputStream(clientsocket.getOutputStream());
			in = new ObjectInputStream(clientsocket.getInputStream());
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.nickname = st.readMsg(clientsocket, in).getContent();
	}
	
	public String getNickname()
	{
		return nickname;
	}

	public Socket getClientsocket()
	{
		return clientsocket;
	}

	public ObjectOutputStream getOut()
	{
		return out;
	}

	public ObjectInputStream getIn()
	{
		return in;
	}

	public StreamTools getSt()
	{
		return st;
	}
}
