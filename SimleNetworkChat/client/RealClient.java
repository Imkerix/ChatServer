package client;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

import utility.Msg;
import utility.StreamTools;

public class RealClient
{
	/** An identifing name of the Person chatting */
	public String nickname;
	/** Endpoint on the client machine */
	private Socket socket;
	/** Sends Strings and objects through the socket to the server */
	private ObjectOutputStream out;
	/** Reads answerers from the Server */
	private ObjectInputStream in;
	/** Some tools that are used on server and on client, and reuse code this way */
	private StreamTools st = new StreamTools();
	
	public RealClient(String ip, String p_nickname)
	{
		this.nickname = p_nickname;
		connect(ip);
		
		Thread readThread = new Thread()
		{
			public void run()
			{
				while(true)
				{
					Msg nextCMD = (Msg) st.readMsg(socket,in);
					if (nextCMD.getId() == 'b' || nextCMD.getId() == 's' || nextCMD.getId() == 'a')
					{
						System.out.println(nextCMD.getContent()+" I am a Client");
					}
					if (nextCMD.getId() == 'o')
					{
						@SuppressWarnings("unchecked")
						ArrayList<String> alo = (ArrayList<String>) nextCMD.getObject();
						for (String s : alo) 
						{
							System.out.println(s);
						}
					}
				}
			}
		};
		readThread.start();
	}
	
	public void write(String s, char p_id)
	{
		st.writeMsg(socket, out, new Msg(s, p_id,null));
	}

	public void connect(String ip)
	{
		try
		{
			socket = new Socket(ip, 12345);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			st.writeMsg(socket, out, new Msg(nickname, '#', null));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void disconnect()
	{
		try
		{
			st.writeMsg(socket, out, new Msg("", ';',null));
			socket.close();
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
