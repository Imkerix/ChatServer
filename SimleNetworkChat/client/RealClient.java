package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

import utility.Msg;
import utility.StreamTools;

public class RealClient
{
	/** An identifing name of the Person chatting */
	private String nickname;
	/** Endpoint on the client machine */
	private Socket socket;
	/** Sends Strings and objects through the socket to the server */
	private ObjectOutputStream out;
	/** Reads answerers from the Server */
	private ObjectInputStream in;
	/**
	 * Some tools that are used on server and on client, and reuse code this way
	 */
	private StreamTools st = new StreamTools();
	private boolean shutdown;
	private ArrayList<String> otherClients;
	private HashMap<String, ArrayList<String>> otherRooms;
	private MessageGUI msgGUI;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = (int) screenSize.getWidth() / 3;
	private int height = (int) (screenSize.getHeight() - screenSize.getHeight() / 3);

	public RealClient(String ip, String p_nickname)
	{
		this.nickname = p_nickname;
		msgGUI = new MessageGUI(nickname, width, height);
		connect(ip);

		Thread readThread = new Thread()
		{

			@SuppressWarnings("unchecked")
			public void run()
			{
				while (!shutdown)
				{
					Msg nextCMD = (Msg) st.readMsg(socket, in);
					if (nextCMD.getId() == 'r')
					{
						msgGUI.getRoomtabpanel().addMessage(nextCMD);
						System.out.println(nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
					}
					if (nextCMD.getId() == 'b')
					{
						msgGUI.getBroadcasttabpanel().addMessage(nextCMD);
						System.out.println(nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
					}
					if (nextCMD.getId() == 's')
					{
						msgGUI.getPrivatetabpanel().addMessage(nextCMD);
						System.out.println(nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
					}
					if (nextCMD.getId() == 'i')
					{
						msgGUI.getRoomtabpanel().addSystemMessage(nextCMD);
						System.err.println(nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
					}
					if (nextCMD.getId() == 'C')
					{
						otherClients = (ArrayList<String>) nextCMD.getObject();
					}
					if (nextCMD.getId() == 'R')
					{
						otherRooms = (HashMap<String, ArrayList<String>>) nextCMD.getObject();
					}
					if (nextCMD.getId() == ';')
					{
						shutdown = true;
					}
				}
			}
		};
		readThread.start();
	}

	public void broadcast(String s, Object o)
	{
		st.writeMsg(socket, out, new Msg(s, nickname, 'b', o));
	}

	public void roomBroadcast(String s, Object o)
	{
		st.writeMsg(socket, out, new Msg(s, nickname, 'r', o));
	}

	public void leaveRoom()
	{
		st.writeMsg(socket, out, new Msg("", nickname, '-', null));
	}

	public void enterRoom(String s, Object o)
	{
		st.writeMsg(socket, out, new Msg(s, nickname, '+', o));
	}

	public void secredMessage(String s, String o)
	{
		st.writeMsg(socket, out, new Msg(s, nickname, 's', o));
	}

	public void updateUserList()
	{
		st.writeMsg(socket, out, new Msg("", nickname, 'C', null));
	}

	public ArrayList<String> getOtherClients()
	{
		return otherClients;
	}

	public void updateRoomList()
	{
		st.writeMsg(socket, out, new Msg("", nickname, 'R', null));
	}

	public HashMap<String, ArrayList<String>> getOtherRooms()
	{
		return otherRooms;
	}

	public String getNickname()
	{
		return nickname;
	}

	public void connect(String ip)
	{
		try
		{
			socket = new Socket(ip, 12345);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			st.writeMsg(socket, out, new Msg(nickname, nickname, '#', null));
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void disconnect()
	{
		st.writeMsg(socket, out, new Msg("", nickname, ';', null));
	}

}
