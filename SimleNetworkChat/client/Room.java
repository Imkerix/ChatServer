package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class Room extends JPanel
{
	private static final long serialVersionUID = 85482853777690016L;
	
	private String selected;
	private RealClient client;
	private JButton btnMoveaction;
	private JMenuBar menuBar;
	private JSeparator sep;
	private String name;
	private int width;
	@SuppressWarnings("rawtypes")
	private DefaultListModel listModel;
	@SuppressWarnings("rawtypes")
	private JList list;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Room(RealClient p_client, Object[] p_data, String p_name, int p_width, boolean p_in)
	{
		this.client = p_client;
		this.width = p_width;
		this.name = p_name;
		this.setLayout(new BorderLayout(0, 0));
		menuBar = new JMenuBar();
		this.add(menuBar, BorderLayout.NORTH);
		
			final JLabel lblRoom = new JLabel(p_name);
			menuBar.add(lblRoom);
			
			sep = new JSeparator();
			
			menuBar.add(sep);
		
		btnMoveaction = new JButton();	
		
		
		if(p_in)//// If you are in the room you only can leave.
		{
			setEntered();
		}
		else//// If you are not in the room you only can enter.
		{
			setLeaved();
		}	
		menuBar.add(btnMoveaction);
		
		
		listModel = new DefaultListModel();
		for(Object o : p_data)
		{
			listModel.addElement(o);
		}
		list = new JList<Object>(listModel);
		list.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{
				setSelected((String) list.getSelectedValue());
			}
		});
		
		//// Makes List deselect when a clicked element is clicked again
			list.setSelectionModel(new DefaultListSelectionModel(){

				private static final long serialVersionUID = -6629633884200508451L;

				@Override
	            public void setSelectionInterval(int index0, int index1) {
	                if (index0==index1) {
	                    if (isSelectedIndex(index0)) {
	                        removeSelectionInterval(index0, index0);
	                        return;
	                    }
	                }
	                super.setSelectionInterval(index0, index1);
	            }

	            @Override
	            public void addSelectionInterval(int index0, int index1) {
	                if (index0==index1) {
	                    if (isSelectedIndex(index0)) {
	                        removeSelectionInterval(index0, index0);
	                        return;
	                    }
	                super.addSelectionInterval(index0, index1);
	                }
	            }

	        });
		//// Makes List deselect when a clicked element is clicked again
		this.add(list);
		
		
		
		this.setPreferredSize(new Dimension(p_width, list.getPreferredSize().height+menuBar.getPreferredSize().height));
	}
	
	public void leaveRoom()
	{
		client.leaveRoom();
		setLeaved();
		listModel.removeElement(client.getNickname());
		this.setPreferredSize(new Dimension(width, list.getPreferredSize().height+menuBar.getPreferredSize().height));
	}
	
	@SuppressWarnings("unchecked")
	public void enterRoom()
	{
		client.enterRoom(name, null);
		setEntered();
		listModel.addElement(client.getNickname());
		this.setPreferredSize(new Dimension(width, list.getPreferredSize().height+menuBar.getPreferredSize().height));
	}
	
	public void setLeaved()
	{
		menuBar.setBackground(Color.gray);
		menuBar.setForeground(Color.gray);
		sep.setForeground	 (Color.gray);
		sep.setBackground	 (Color.gray);
		btnMoveaction.setText("Enter");
	}
	
	public void setEntered()
	{
		menuBar.setBackground(Color.cyan);
		menuBar.setForeground(Color.cyan);
		sep.setForeground	 (Color.cyan);
		sep.setBackground	 (Color.cyan);
		btnMoveaction.setText("Leave");
	}
	
	
	public RealClient getClient()
	{
		return client;
	}
	
	public String getSelected()
	{
		return selected;
	}
	
	public void setSelected(String p_selected)
	{
		selected = p_selected;
	}
	
	public JButton getBtnMoveaction()
	{
		return btnMoveaction;
	}

	public JMenuBar getMenuBar()
	{
		return menuBar;
	}

	public JSeparator getSep()
	{
		return sep;
	}

	@SuppressWarnings("rawtypes")
	public DefaultListModel getListModel()
	{
		return listModel;
	}

	@SuppressWarnings("rawtypes")
	public JList getList()
	{
		return list;
	}
}
