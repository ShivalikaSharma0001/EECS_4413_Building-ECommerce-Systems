package a;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//import TCPServer.Server;
//import TCPServer.Worker;

import java.net.InetAddress;

public class TCPServer 
{
	Set<InetAddress> whitelist = new HashSet<InetAddress>();
	File logFile = new File("log.txt");
	PrintStream log = new PrintStream(logFile);
	public TCPServer(int port, File file) throws Exception
	{
		ServerSocket server = new ServerSocket(port);
		log.println((new Date()).toString() + " : Server Start : " + server.getInetAddress() + " : " + server.getLocalPort());
		whitelist.add(server.getInetAddress());
		whitelist.add(InetAddress.getByName("127.0.0.1"));
		
		while (file.exists())
		{
			Socket client = server.accept();
			if (whitelist.contains(client.getInetAddress()))
			{
				log.println((new Date()).toString() + " : A connection : " + client.getInetAddress());
				Worker worker = new Worker(client, this);
				Thread t = new Thread(worker);
				t.start();
				//Worker worker = new Worker(client, this);
	     		//worker.handle();
			}
			
			else
			{
				log.println((new Date()).toString() + " : A firewall violation : " + client.getInetAddress());
			}
			
		}
		
		if(!file.exists())
		{
			log.println((new Date()).toString() + " : Server Shutdown : File missing.");
		}
		
		server.close();
	}
	
	public static void main(String[] args) throws Exception
	{
		File file = new File("running.txt");
//		File logFile = new File("log.txt");
//		PrintStream log = new PrintStream(logFile);
		TCPServer POJO = new TCPServer(4000, file);
	}	
}
