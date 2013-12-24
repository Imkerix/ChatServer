package client;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

public class RoomPanel extends JPanel
{
	private static final long serialVersionUID = 170913076776578711L;
	private ArrayList<Room> roompanels = new ArrayList<Room>();
	private int roomPanelWidth;
	
	public RoomPanel(int p_roomPanelwidth)
	{
		this.roomPanelWidth = p_roomPanelwidth;
	}
	
	public Room mkRoom(Object[] p_data,final String p_name, final int p_width, final RealClient p_client,boolean p_in)
	{
		final Room room = new Room(p_client,p_data, p_name, p_width, p_in);
			room.getBtnMoveaction().addActionListener(new ActionListener() 
			{
				public void actionPerformed(ActionEvent arg0) 
				{
					if(room.getBtnMoveaction().getText().equals("Enter"))
					{
						
						for(Room rm : roompanels)
						{
							if(rm.getClient().getNickname().equals(p_client.getNickname())) // Hat dein Client den selben namen wie meiner
							{
								rm.leaveRoom();
							}
						}
						room.enterRoom();
					}
					else
					{
						room.leaveRoom();
					}
				}
			});
			room.setPreferredSize(new Dimension(p_width, room.getList().getPreferredSize().height+room.getMenuBar().getPreferredSize().height));
			
		return room;
	}
	
	public void addRoomPanel(Object[] p_data,String p_name,RealClient p_client,boolean p_in)
	{
			Room room = mkRoom(p_data, p_name, roomPanelWidth, p_client, p_in);
			roompanels.add(room);
			this.add(room);
			mkPreferredSize(); 
	}
	
	public void rmRoomPanel(int componentIndexToDelete)
	{
			this.remove(componentIndexToDelete);
			mkPreferredSize();
	}
	
	public ArrayList<Room> getRooms()
	{
		return roompanels;
	}
	
	public void mkPreferredSize()
	{
		//// Determinates real height of panel to access it RealClient
			int searchedHeight = 0;
			for(Room rp : roompanels)
			{
				searchedHeight += rp.getPreferredSize().height+5; // +5 is the hgap beetween two RoomPanels
			}
			this.setPreferredSize(new Dimension(this.getPreferredSize().width, searchedHeight+5));	// the last +5 are the hgap at the beginning nnot counted above
		//// Determinates real height of panel to access it RealClient
	}

}
