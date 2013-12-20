package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import server.Server;
import msg.Msg;

public class ChatClient
{
	private Socket socket;
	/** Endpoint on the client machine */
	private ObjectOutputStream out;
	/** Sends Strings and objects through the socket to the server */
	private ObjectInputStream in;
	/** Reads answerers from the Server */

	public ChatClient()
	{
		connect("127.0.0.1");
		
		Thread writeThread = new Thread()
		{
			public void run()
			{
				try
				{
					out.writeObject(new Msg("It Works!",'r'));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		writeThread.start();
		
		Thread readThread = new Thread()
		{
			public void run()
			{
					Msg nextCMD = (Msg) readMsg();
					while(nextCMD == null){}
					
					if (nextCMD.getId() == 'r')
					{
						System.err.println(nextCMD.getContent());
					}
			}
		};
		readThread.start();
	}

	public void connect(String ip)
	{
		try
		{
			socket = new Socket(ip, 12345);

			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	private Msg readMsg()
	{
		try
		{
			if (!socket.isConnected())
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
