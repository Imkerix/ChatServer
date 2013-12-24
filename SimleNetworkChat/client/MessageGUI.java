package client;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Color;

public class MessageGUI extends JFrame
{
	private static final long serialVersionUID = -2294270365880823788L;
	private MessagePanel roomtabpanel;
	private MessagePanel privatetabpanel;
	private MessagePanel broadcasttabpanel;

	public MessageGUI(String nickname, int width, int height)
	{
		setTitle(nickname + " Messages");

		this.setSize(new Dimension(width, height));
		this.setResizable(false);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBackground(Color.WHITE);

		roomtabpanel = new MessagePanel(width);
		tabbedPane.addTab("Room", roomtabpanel);

		privatetabpanel = new MessagePanel(width);
		tabbedPane.addTab("Privat", privatetabpanel);

		broadcasttabpanel = new MessagePanel(width);
		tabbedPane.addTab("Broadcast", broadcasttabpanel);

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		this.setVisible(true);
	}

	public MessagePanel getRoomtabpanel()
	{
		return roomtabpanel;
	}

	public MessagePanel getPrivatetabpanel()
	{
		return privatetabpanel;
	}

	public MessagePanel getBroadcasttabpanel()
	{
		return broadcasttabpanel;
	}

}
