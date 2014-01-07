

import java.io.IOException;

import server.Server;
import client.RealClient;
import client.RealClientGUI;
 
public class Main
{
	public static void main(String[] args) throws InterruptedException
	{

		// // Server Part //////// Server Part //////// Server Part ////////
		// Server Part ////
//
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
//		Thread.sleep(500);
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

		
		
		RealClientGUI rcGUI = new RealClientGUI();
//		
//		
//		
//		Thread clientThread0 = new Thread() // A Client Thread
//		{
//			public void run()
//			{
//				RealClient c = new RealClient(ip,"nickname_0");
//				try {
//					this.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.roomBroadcast("Alle im raum ?", null);
//				try {
//					this.sleep(3000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.roomBroadcast("Da waren es noch 3", null);
////				
//			}
//		};
//		clientThread0.start();
//		Thread clientThread1 = new Thread() // A Client Thread
//		{
//			public void run()
//			{
//				RealClient c = new RealClient(ip,"nickname_1");
//				try {
//					this.sleep(4000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.leaveRoom();
//				try {
//					this.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.enterRoom("Redeecke", null);
//			}
//		};
//		clientThread1.start();
//		Thread clientThread2 = new Thread() // A Client Thread
//		{
//			public void run()
//			{
//				RealClient c = new RealClient(ip,"nickname_2");
//				try {
//					this.sleep(7000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.enterRoom("Redeecke", null);
//				try {
//					this.sleep(1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				c.roomBroadcast("nur wir zwei ^^", null);
//			}
//		};
//		clientThread2.start();
////
//		Thread clientThread3 = new Thread() // A Client Thread
//		{
//			public void run()
//			{
//				RealClient c = new RealClient(ip,"nickname_3");
////				c.updateRoomList();
//			}
//		};
//		clientThread3.start();
////
//		Thread clientThread4 = new Thread() // A Client Thread
//		{
//			public void run()
//			{
//				try
//				{
//					this.sleep(8000);
//				} catch (InterruptedException e)
//				{
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				RealClient c = new RealClient(ip,"nickname_4");
//				c.enterRoom("Redeecke", null);
//			}
//		};
//		clientThread4.start();

		// // Client Part //////// Client Part //////// Client Part ////////
		// Client Part ////
	}

}
