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

	public void addRoom(Object[] p_data, String p_name, RealClient p_client, boolean p_in)
	{
		Room room = new Room(p_client, p_data, p_name, roomPanelWidth, p_in);
		room.setPreferredSize(new Dimension(roomPanelWidth, room.getList().getPreferredSize().height + room.getMenuBar().getPreferredSize().height));
		roompanels.add(room);
		this.add(room);
		mkPreferredSize();

	}

//	public void rmRoom(int componentIndexToDelete) // could be usefull some day
//	{
//		this.remove(componentIndexToDelete);
//		mkPreferredSize();
//	}

	public ArrayList<Room> getRooms()
	{
		return roompanels;
	}

	public void mkPreferredSize()
	{
		//// Determinates real height of panel to access it RealClient
		int searchedHeight = 0;
		for (Room rp : roompanels)
		{
			searchedHeight += rp.getPreferredSize().height + 5; // +5 is the hgap beetween two RoomPanels
		}
		this.setPreferredSize(new Dimension(this.getPreferredSize().width, searchedHeight + 5));	// the last +5 are the hgap at the beginning nnot counted above
		//// Determinates real height of panel to access it RealClient
	}
	
	public Room getActiveRoom(String nickname)
	{
		for(Room r : roompanels)
		{
			if(r.getClient().getNickname().equals(nickname))
			{
				return r;
			}
		}
		return null;
	}

}
