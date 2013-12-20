package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import utility.Msg;
import utility.StreamTools;

public class ChatClient
{
	private Socket socket;
	/** Endpoint on the client machine */
	private ObjectOutputStream out;
	/** Sends Strings and objects through the socket to the server */
	private ObjectInputStream in;
	/** Reads answerers from the Server */
	private StreamTools st = new StreamTools();

	public ChatClient()
	{
		connect("127.0.0.1");
		
		Thread writeThread = new Thread()
		{
			public void run()
			{
				st.writeMsg(socket, out, "It Works!");
			}
		};
		writeThread.start();
		
		Thread readThread = new Thread()
		{
			public void run()
			{
					Msg nextCMD = (Msg) st.readMsg(socket,in);
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
	
	public void disconnect()
	{
		try
		{
			out.close();
			in.close();
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
