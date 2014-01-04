package client;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JPanel;

import java.awt.BorderLayout;

import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.swing.JToggleButton;

public class RealClientGUI extends JFrame
{
	private static final long serialVersionUID = -5609940018528181823L;
	private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	private int width = (int) screenSize.getWidth() / 4;
	private int height = (int) (screenSize.getHeight() - screenSize.getHeight() / 4);
	private int roomPanelwidth = width - 30;
	private RealClient client = null;
	private RoomPanel rp;
	private JScrollPane sc;

	public RealClientGUI()
	{
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(new Dimension(width, height));
		setTitle("Client Chat");
		setResizable(false);
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
	        public void run() 
	        {
	        	if (client != null)
				{
	        		client.disconnect();
					rp.removeAll();
					rp.repaint();
				}
	        }
	    }, "Shutdown-thread"));
		
		//// menubar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnChat = new JMenu("Chat");
		menuBar.add(mnChat);
		JMenuItem mntmConnect = new JMenuItem("Connect");
		mntmConnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				JFrame frame = new JFrame("jframe");
				String ip = (String) JOptionPane.showInputDialog(frame, "Ip address", "Where is The Server ?", JOptionPane.PLAIN_MESSAGE, null, null, "127.0.0.1");
				if (ip != null)
				{
					String nickname = (String) JOptionPane.showInputDialog(frame, "Nickname", "Who do you want to be ?");

					if (nickname != null)
					{
						client = new RealClient(ip, nickname);
						rp = client.getRp(); // now the rooppanel of client is in use
						// no need to set arp preferedsize here, because it was
						// determinated in AllRoomPanel this or the other way
						// around
						sc.setViewportView(rp);
					}
				}
			}
		});
		mnChat.add(mntmConnect);
		JMenuItem mntmDisconnect = new JMenuItem("Disconnect");
		mntmDisconnect.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (client != null)
				{
					client.disconnect();
					rp.removeAll();
					rp.repaint();
				}
			}
		});
		mnChat.add(mntmDisconnect);
		//// menubar
		//// ContentPane
		getContentPane().setLayout(new BorderLayout(0, 0));
		// // ScrollPane
		sc = new JScrollPane();
		sc.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER); // no horizontal scrolling allowed.
		sc.getVerticalScrollBar().setUnitIncrement(20); // helps to scroll faster when using the mouse.
		this.getContentPane().add(sc);

		rp = new RoomPanel(roomPanelwidth); // Dummy implementation to keep space
		//// ScrollPane
		//// The part where you send messages ////
		JPanel writepanel = new JPanel();
		writepanel.setPreferredSize(new Dimension(width, 150));
		getContentPane().add(writepanel, BorderLayout.SOUTH);
		writepanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		writepanel.add(scrollPane);

		final JTextArea textArea = new JTextArea();
		textArea.setWrapStyleWord(true);
		textArea.setLineWrap(true);
		textArea.setSize(new Dimension(width, 55));
		scrollPane.setViewportView(textArea);

		JPanel marginpaneltop = new JPanel();
		writepanel.add(marginpaneltop, BorderLayout.NORTH);
		marginpaneltop.setLayout(new BorderLayout(0, 0));

		JPanel top = new JPanel();
		marginpaneltop.add(top, BorderLayout.NORTH);

		final JToggleButton tglbtnBroadcast = new JToggleButton("Broadcast");
		top.add(tglbtnBroadcast);

		final JToggleButton tglbtnRoom = new JToggleButton("Room");
		tglbtnRoom.setSelected(true);
		top.add(tglbtnRoom);

		final JToggleButton tglbtnPrivate = new JToggleButton("Private");
		top.add(tglbtnPrivate);

		JPanel bottom = new JPanel();
		marginpaneltop.add(bottom, BorderLayout.SOUTH);

		final JLabel lblWhereTo = new JLabel(tglbtnRoom.getText() + " Message");
		bottom.add(lblWhereTo);

		// Get Live into it
		final String endMessage = " Message";

		tglbtnBroadcast.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tglbtnPrivate.setSelected(false);
				tglbtnRoom.setSelected(false);
				lblWhereTo.setText(tglbtnBroadcast.getText() + endMessage);

			}
		});
		tglbtnRoom.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tglbtnPrivate.setSelected(false);
				tglbtnBroadcast.setSelected(false);
				lblWhereTo.setText(tglbtnRoom.getText() + endMessage);

			}
		});
		tglbtnPrivate.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				tglbtnBroadcast.setSelected(false);
				tglbtnRoom.setSelected(false);
				lblWhereTo.setText(tglbtnPrivate.getText() + endMessage + " to all selected users");

			}
		});
		// Get Live into it

		JPanel marginpanelbottom = new JPanel();
		writepanel.add(marginpanelbottom, BorderLayout.SOUTH);

		JButton btnNewButton = new JButton("Send");
		this.getRootPane().setDefaultButton(btnNewButton); // If nothing else is selected enter hits the send button
		btnNewButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0) // error messages do never touch again a break is to expect.
			{
				if (client != null)
				{
					if (!(textArea.getText().equals("")))
					{
						if (tglbtnRoom.isSelected())
						{
							client.roomBroadcast(textArea.getText(), null);
						}
						if (tglbtnPrivate.isSelected())
						{
							for (Room arp : rp.getRooms())
							{
								if (arp.getSelected() != null)
								{
									client.secredMessage(textArea.getText(), (String) arp.getSelected());
								} else
								{
									new Thread()
									{
										public void run()
										{
											String before = lblWhereTo.getText();
											try
											{
												this.sleep(10);
											} catch (InterruptedException e)
											{
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
											lblWhereTo.setText("No user was selected, no message was sended");

											try
											{
												this.sleep(1500);
											} catch (InterruptedException e)
											{
												// TODO Auto-generated catch block
												e.printStackTrace();
											}

											lblWhereTo.setText(before);

										}
									}.start();
								}
							}
						}
						if (tglbtnBroadcast.isSelected())
						{
							client.broadcast(textArea.getText(), null);
						}
					} else
					{
						new Thread()
						{
							public void run()
							{
								String before = lblWhereTo.getText();
								try
								{
									this.sleep(10);
								} catch (InterruptedException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								lblWhereTo.setText("Nothing to send, no message was sended");

								try
								{
									this.sleep(1500);
								} catch (InterruptedException e)
								{
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

								lblWhereTo.setText(before);

							}
						}.start();
					}
				}
				else
				{
					new Thread()
					{
						public void run()
						{
							String before = lblWhereTo.getText();
							try
							{
								this.sleep(10);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							lblWhereTo.setText("Not connected to a Server");

							try
							{
								this.sleep(1500);
							} catch (InterruptedException e)
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							lblWhereTo.setText(before);

						}
					}.start();
				}
			}
		});
		marginpanelbottom.add(btnNewButton);
		btnNewButton.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		//// The part where you send messages ////
		//// ContentPane
		setVisible(true);
	}

	public int getWidth()
	{
		return width;
	}

	public int getHeight()
	{
		return height;
	}

}