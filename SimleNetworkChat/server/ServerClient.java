package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.StreamTools;

public class ServerClient extends Thread 
{
	public String nickname;
	public Socket clientsocket;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	
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
}
