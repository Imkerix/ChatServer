

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.ChatClient;
import server.Server;
 
public class Main
{

	public static void main(String[] args) throws InterruptedException
	{

		// // Server Part //////// Server Part //////// Server Part ////////
		// Server Part ////

		Thread serverThread = new Thread()
		{
			public void run()
			{
				try
				{
					Server s = new Server(12345);
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		serverThread.start();
		// // Server Part //////// Server Part //////// Server Part ////////
		// Server Part ////
		Thread.sleep(500);
		// // Client Part //////// Client Part //////// Client Part ////////
		// Client Part ////

		// // Small GUI to ask for IP //// Small GUI to ask for IP ////
		// Comment this if graphical interface is used
		final String ip = "127.0.0.1";
		// Comment this if graphical interface is used
		// JFrame frame = new JFrame("jframe");
		// Object[] options = {"127.0.0.1","10.34.12.85"};
		// final String ip = (String) JOptionPane.showInputDialog(
		// frame,
		// "Ip of the Server?",
		// "Where is The Server ???",
		// JOptionPane.PLAIN_MESSAGE,
		// null,
		// options,
		// "10.34.12.85");
		// // Small GUI to ask for IP //// Small GUI to ask for IP ////

		Thread clientThread0 = new Thread() // A Client Thread
		{
			public void run()
			{
				ChatClient c = new ChatClient();
			}
		};
		clientThread0.start();
		Thread clientThread1 = new Thread() // A Client Thread
		{
			public void run()
			{
				ChatClient c = new ChatClient();
			}
		};
		clientThread1.start();
		Thread clientThread2 = new Thread() // A Client Thread
		{
			public void run()
			{
				ChatClient c = new ChatClient();
			}
		};
		clientThread2.start();

		Thread clientThread3 = new Thread() // A Client Thread
		{
			public void run()
			{
				ChatClient c = new ChatClient();
			}
		};
		clientThread3.start();

		Thread clientThread4 = new Thread() // A Client Thread
		{
			public void run()
			{
				ChatClient c = new ChatClient();
			}
		};
		clientThread4.start();

		// // Client Part //////// Client Part //////// Client Part ////////
		// Client Part ////
	}

}
