package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.StreamTools;

public class SocketThread extends Thread 
{
	public Socket clientsocket;
	public ObjectOutputStream out;
	public ObjectInputStream in;
	StreamTools st = new StreamTools();
	
	public SocketThread(Socket p_socket)
	{
		this.clientsocket = p_socket;
		
		try {
			out = new ObjectOutputStream(clientsocket.getOutputStream());
			in = new ObjectInputStream(clientsocket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
