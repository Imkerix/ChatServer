package utility;

import java.net.Socket;

public class SocketThread extends Thread 
{
	public Socket clientsocket;
	
	public SocketThread(Socket p_socket)
	{
		this.clientsocket = p_socket;
	}
}
