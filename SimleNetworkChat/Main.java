

import java.io.IOException;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import client.RealClient;
import server.Server;
import utility.Msg;
 
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
//		 JFrame frame = new JFrame("jframe");
//		 Object[] options = {"127.0.0.1","192.168.2.101"};
//		 final String ip = (String) JOptionPane.showInputDialog(
//		 frame,
//		 "Ip of the Server?",
//		 "Where is The Server ???",
//		 JOptionPane.PLAIN_MESSAGE,
//		 null,
//		 options,
//		 "10.34.12.85");
		// // Small GUI to ask for IP //// Small GUI to ask for IP ////

		Thread clientThread0 = new Thread() // A Client Thread
		{
			public void run()
			{
				RealClient c = new RealClient(ip,"nickname_0");
				c.write(" client0",'b');
			}
		};
		clientThread0.start();
		Thread clientThread1 = new Thread() // A Client Thread
		{
			public void run()
			{
				RealClient c = new RealClient(ip,"nickname_1");
				c.write(" client1",'b');
			}
		};
		clientThread1.start();
		Thread clientThread2 = new Thread() // A Client Thread
		{
			public void run()
			{
				RealClient c = new RealClient(ip,"nickname_2");
				c.write(" client2",'b');
			}
		};
		clientThread2.start();

		Thread clientThread3 = new Thread() // A Client Thread
		{
			public void run()
			{
				RealClient c = new RealClient(ip,"nickname_3");
				c.write(" client3",'b');
			}
		};
		clientThread3.start();

		Thread clientThread4 = new Thread() // A Client Thread
		{
			public void run()
			{
				RealClient c = new RealClient(ip,"nickname_4");
				try {
					this.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				c.write(" client4",'?');
				
			}
		};
		clientThread4.start();

		// // Client Part //////// Client Part //////// Client Part ////////
		// Client Part ////
	}

}
