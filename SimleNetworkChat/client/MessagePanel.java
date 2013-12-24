package client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;

import utility.Msg;

import javax.swing.ScrollPaneConstants;

public class MessagePanel extends JScrollPane
{
	private static final long serialVersionUID = 4806617628612884552L;
	private JPanel contentpanel;
	private int width;
	private ArrayList<JPanel> panels = new ArrayList<JPanel>();

	public MessagePanel(int p_width)
	{
		this.width = p_width - 30;
		setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		contentpanel = new JPanel();
		this.setViewportView(contentpanel);
		this.setDoubleBuffered(true);

	}

	public void addMessage(Msg p_msg)
	{
		JPanel msgpanel = new JPanel();
		msgpanel.setLayout(new BorderLayout(0, 0));
		JPanel headerpanel = new JPanel();
		headerpanel.setLayout(new BorderLayout(0, 0));
		JLabel lbl2 = new JLabel(p_msg.getSender() + " : " + p_msg.getDate());
		headerpanel.add(lbl2, BorderLayout.CENTER);
		msgpanel.add(headerpanel, BorderLayout.NORTH);

		JTextArea textarea = new JTextArea("\"" + p_msg.getContent() + "\"");
		textarea.setBackground(msgpanel.getBackground());
		textarea.setSize(new Dimension(width, textarea.getPreferredSize().height));
		textarea.setLineWrap(true);
		textarea.setWrapStyleWord(true);
		textarea.setEditable(false);
		msgpanel.add(textarea, BorderLayout.CENTER);
		msgpanel.add(new JSeparator(), BorderLayout.SOUTH);
		msgpanel.setPreferredSize(new Dimension(width, msgpanel.getPreferredSize().height));
		contentpanel.add(msgpanel);
		panels.add(msgpanel);
		contentpanel.setPreferredSize(mkPreferredSize());
	}

	public void addSystemMessage(Msg p_msg)
	{
		JPanel msgpanel = new JPanel();
		msgpanel.setLayout(new BorderLayout(0, 0));

		JPanel headerpanel = new JPanel();
		headerpanel.setLayout(new BorderLayout(0, 0));
		JLabel lbl2 = new JLabel(p_msg.getSender() + " : " + p_msg.getContent());
		lbl2.setForeground(Color.blue);
		headerpanel.add(lbl2, BorderLayout.CENTER);
		msgpanel.add(headerpanel, BorderLayout.NORTH);
		msgpanel.add(new JSeparator(), BorderLayout.SOUTH);
		msgpanel.setPreferredSize(new Dimension(width, 20));
		contentpanel.add(msgpanel);
		panels.add(msgpanel);
		contentpanel.setPreferredSize(mkPreferredSize());
	}

	public Dimension mkPreferredSize()
	{
		//// Determinates real height of panel to access it RealClient
		int searchedHeight = 0;
		for (Component c : contentpanel.getComponents())
		{
			searchedHeight += c.getPreferredSize().height + 5;
		}
		return new Dimension(width, searchedHeight + 5);
		//// Determinates real height of panel to access it RealClient
	}

}
