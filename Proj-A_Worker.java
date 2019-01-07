package a;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import com.google.gson.Gson;

import projA.Course;
import projA.Util;

public class Worker implements Runnable 
{
	private Socket client;
	TCPServer server;
	
	public Worker(Socket client, TCPServer server) 
	{
		super();
		this.client = client;
		this.server = server;
	}

	public void handle() throws IOException, JAXBException 
    {
    	BufferedReader reader =  new BufferedReader(new InputStreamReader(client.getInputStream()));
    	BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    	String input;
    	
    	while(!(input = reader.readLine()).equalsIgnoreCase("bye"))
    	{
	        String[] arr = input.split(" "); 
	        
			if (arr[0].equalsIgnoreCase("getTime"))
			{
				DateFormat df = new SimpleDateFormat("EEE dd MMM YYYY HH:MM:SS z");
				output.write(df.format(new Date()));
				output.newLine();
				output.flush();
			}
			
//			else if (arr[0].equalsIgnoreCase("Bye"))
//			{
//				bye();
//			}
			
			else if (arr[0].equalsIgnoreCase("Punch"))
			{
				if (arr.length != 2)
				{
					server.log.println((new Date()).toString() + " : An exception : No ip address provided! Try again.");
					//bye();
				}
				
				else
				{
					boolean b = Pattern.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}", arr[1]);
					if (b)
					{
						server.whitelist.add(InetAddress.getByName(arr[1])); 
					}
						
					else
					{
						server.log.println((new Date()).toString() + " : An exception : " + arr[1] + " is not an ip address.");
						//bye();
					}
				}
					
			}
			
			else if (arr[0].equalsIgnoreCase("Plug"))
			{
				if (arr.length != 2)
				{
					server.log.println((new Date()).toString() + " : An exception : No ip address provided! Try again.");
					//bye();
				}
				else
				{
					boolean b = Pattern.matches("\\d{1,3}.\\d{1,3}.\\d{1,3}.\\d{1,3}", arr[1]);
					if (b)
					{
						if (server.whitelist.contains(InetAddress.getByName(arr[1])))
						{
							server.whitelist.remove(InetAddress.getByName(arr[1]));
						}
							
						else
						{
							server.log.println((new Date()).toString() + " : An exception : " + arr[1] + " does not exists in whitelist. Cannot remove it.");
							//bye();
						}
					}
						
					else
					{
						server.log.println((new Date()).toString() + " : An exception : " + arr[1] + " is not an ip address.");
						//bye();
					}
				}
			}
			
			else if(arr[0].equalsIgnoreCase("Prime"))
			{
				if (arr.length != 2)
				{
					server.log.println((new Date()).toString() + " : An exception : No digits provided! Try again.");
					//bye();
				}
			
				else
				{
					boolean b = Pattern.matches("[1-9]\\d*", arr[1]);
					if (b)
					{
						int size = (Integer.parseInt(arr[1]) - 1);	
						String s = "1";
						for (int i = 0; i < size; i ++)
						{
							s = s + "0";
						}
						
						BigInteger num = new BigInteger(s);
						BigInteger nextNum = num.nextProbablePrime();
						
						output.write(nextNum.toString());
						output.newLine();
						output.flush();
					}
						
					else
					{
						server.log.println((new Date()).toString() + " : An exception : " + arr[1] + " must not start with 0 and should be a whole number.");
						//bye();
					}
				}
			}

			else if(arr[0].equalsIgnoreCase("Auth"))
			{
				if (arr.length != 3)
				{
					server.log.println((new Date()).toString() + " : An exception : Username and/or Password missing! Try again.");
					//bye();
				}
				
				else
				{
					boolean b = Pattern.matches("[a-zA-Z0-9]{7,10}", arr[1]);
					boolean a = Pattern.matches("[a-zA-Z0-9_!*]{5,10}", arr[2]);
					if (!b)
					{
						server.log.println((new Date()).toString() + " : An exception : " + arr[1] + " is not according to username rules. It should be alphnumeric and 7-10 long.");
						//bye();
					}
					
					else if (!a)
					{
						server.log.println((new Date()).toString() + " : An exception : " + arr[2] + " is not according to password rules. It should be alphnumeric or (_!*) and 5-10 long.");
						//bye();
					}
					
					else
					{
						Hashtable<String, String> hashtable = new Hashtable<String, String>();
						hashtable.put("Shivalika","sharma27");
						
						for (int i = 0; i < hashtable.size(); i ++)
						{
							if (!hashtable.containsKey(arr[1]))
							{
								output.write("Auth Failure");
								output.newLine();
								output.flush();
								server.log.println((new Date()).toString() + " : An exception : Username does not exist.");
							}
							
							else
							{
								if(!arr[2].equals(hashtable.get(arr[1])))
								{
									output.write("Auth Failure");
									output.newLine();
									output.flush();
									server.log.println((new Date()).toString() + " : An exception : Password is not correct.");
								}
								
								else
								{
									output.write("You are in!");
									output.newLine();
									output.flush();
								}
							}
						}
					}
				}			
			}
			
			else if(arr[0].equalsIgnoreCase("Roster"))
			{
				if (arr.length != 3)
				{
					server.log.println((new Date()).toString() + " : An exception : Course and/or Format missing! Try again.");
					//bye();
				}
				
				else
				{
					boolean b = Pattern.matches("[eE][eE][cC][sS][1-9]\\d{3}", arr[1]);
					boolean a = (arr[2].equalsIgnoreCase("xml") || arr[2].equalsIgnoreCase("json"));
					
					if (!b)
					{
						server.log.println((new Date()).toString() + " : An exception : Course should be of the format eecsXXXX, where X is any number (the first X cannot be zero).");
						//bye();
					}
					
					else if (!a)
					{
						server.log.println((new Date()).toString() + " : An exception : Format is wrong. It should be either XML or JSON.");
						//bye();
					}
					
					else
					{
						Course course = Util.getCourse(arr[1]);
						if (arr[2].equalsIgnoreCase("xml"))
						{
							JAXBContext context = JAXBContext.newInstance(Course.class);
							Marshaller m = context.createMarshaller();
							m.marshal(course, output); 
							output.newLine();
							output.flush();
						}
						
						else
						{
							Gson gson = new Gson();
							String json = gson.toJson(course);
							output.write(json);
							output.newLine();
							output.flush();
						}
					}
				}
			}
			
			else
			{
				output.write("Don't understand " + input);
				output.newLine();
				output.flush();
			}
    	}
    	bye();
    }

	private void bye() throws IOException
	{
		client.close();
		server.log.println((new Date()).toString() + " : A disconnection : " + client.getInetAddress());
	}

	public void run() 
	{
		try 
		{
			this.handle();
		} 
		catch (IOException e)
		{
			server.log.println((new Date()).toString() + " : An exception : e.printStackTrace()");
		} 
		catch (JAXBException e)
		{
			server.log.println((new Date()).toString() + " : An exception : e.printStackTrace()");
		}
	}	
}
