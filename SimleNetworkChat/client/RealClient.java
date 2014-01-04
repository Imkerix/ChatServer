package client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

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
	private int width = (int) screenSize.getWidth() / 4;
	private int height = (int) (screenSize.getHeight() - screenSize.getHeight() / 4);
	private int roomPanelwidth = width - 30;
	private RoomPanel rp;
	

	

	public RealClient(String ip, String p_nickname)
	{
		this.nickname = p_nickname;
		rp = new RoomPanel(roomPanelwidth);
		msgGUI = new MessageGUI(nickname);
		otherClients = new ArrayList<String>();
		connect(ip);
		
		
		new Thread()
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
						// Keeps other clients up to date
						String[] clientname = nextCMD.getContent().split("\\|");
						if(clientname[0].startsWith("moved"))
						{
							for(Room r : getThis().getRp().getRooms())
							{
								if(!(clientname[1].equals("null")))	// leaving a room without entering somewhere else
								{	
									if(r.getName().equals(clientname[1])) // leaving
									{
										if(nextCMD.getSender().equals(getNickname())) // this client
										{
											r.leaveRoom();
										}
										else // not this client
										{
											r.rmGuyFromRoom(nextCMD.getSender());
										}
									}
								}	
								if(r.getName().equals(clientname[2])) // entering
								{
									if(nextCMD.getSender().equals(getNickname())) // this client
									{
										r.enterRoom();
									}
									else // not this client
									{
										r.addGuyToRoom(nextCMD.getSender());
									}
								}
							}
						}
						else
						{
							msgGUI.getRoomtabpanel().addSystemMessage(nextCMD);	// create on screen messages
						}
						// Keeps other clients up to date
						System.err.println((String)nextCMD.getObject()+"|"+nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
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
						msgGUI.getRoomtabpanel().addSystemMessage(nextCMD);
						System.err.println(nextCMD.getContent() + " " + nextCMD.getId() + " " + nickname);
						shutdown = true;
					}
				}
			}
		}.start();
		updateRoomList(); // have to ask after the reading thread is running
		rp = initRoomPanel(); 
	}

	public RoomPanel initRoomPanel()
	{
		HashMap<String, ArrayList<String>> rooms = this.getOtherRooms();
		while (rooms == null)
		{
			rooms = this.getOtherRooms();
			try
			{
				Thread.sleep(1);
			} catch (InterruptedException e)
			{
				e.printStackTrace();
			}
		}

		for (Entry<String, ArrayList<String>> pair : rooms.entrySet())
		{
			Object[] usersOfRoom = new Object[pair.getValue().size()]; // Liste mit activen useren im Raum der durch for läuft
			boolean imThere = false;

			for (int i = 0; i < pair.getValue().size(); i++)
			{
				usersOfRoom[i] = pair.getValue().get(i);
				if (pair.getValue().get(i).equals(nickname))
				{
					imThere = true;
				}
			}

			rp.addRoom(usersOfRoom, pair.getKey(), this, imThere);
		}
		return rp;
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
	
	public MessageGUI getMessageGUI()
	{
		return msgGUI;
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
	
	public RealClient getThis()
	{
		return this;
	}

	public RoomPanel getRp()
	{
		return rp;
	}
	

}
