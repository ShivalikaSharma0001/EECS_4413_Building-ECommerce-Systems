package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

public class Brain
{
	public static final double BITS_PER_DIGIT = 3.0;
	public static final Random RNG = new Random();	
	public static final String TCP_SERVER = "red.eecs.yorku.ca";
	//public static final String TCP_SERVER = "localhost";
	public static final int TCP_PORT = 12345;
	public static final String DB_URL = "jdbc:derby://localhost:64413/EECS;user=student;password=secret";
	//public static final String DB_URL = "jdbc:derby://red.eecs.yorku.ca:64413/EECS;user=student;password=secret";
	public static final String HTTP_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/World.cgi";
	public static final String ROSTER_URL = "https://www.eecs.yorku.ca/~roumani/servers/4413/f18/Roster.cgi";
	
	private static Brain instance = null;
	private Brain()
	{
		
	}
		
	public static Brain getInstance()
	{
		if (instance == null)
			instance = new Brain();
		return instance;
	}
	public String doTime()
	{
		return new Date().toString();
	}
	
	public String addTime(String hours)
	{
		Calendar cal = Calendar.getInstance(); 
	    cal.setTime(new Date()); 
	    cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(hours)); 
	    return cal.getTime().toString(); 
	}
	
	public String doPrime(String digits)
	{		
		int size = (Integer.parseInt(digits) - 1);	
		String s = "1";
		for (int i = 0; i < size; i ++)
		{
			s = s + "0";
		}
		
		BigInteger num = new BigInteger(s);
		BigInteger nextNum = num.nextProbablePrime();	
		
		return nextNum.toString();
	}
	
	public String doTcp(String digits) throws IOException
	{
		Socket client = new Socket(TCP_SERVER, TCP_PORT);
	    
		BufferedReader reader =  new BufferedReader(new InputStreamReader(client.getInputStream()));
    	BufferedWriter output = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
    	
    	output.write("prime " + digits);
    	output.newLine();
		output.flush();
		
    	String input = reader.readLine();
    	System.out.println(input);
    	
    	output.write("bye");
    	output.newLine();
		output.flush();
    	
    	return input;
	}
	
	public String doDb(String itemNo) throws Exception
	{
		  Class.forName("org.apache.derby.jdbc.ClientDriver").newInstance();
		  Connection con = DriverManager.getConnection(DB_URL);
		  Statement s = con.createStatement();
		  s.executeUpdate("set schema roumani");
		  String query = "select Name, Price from ITEM where number = '"+itemNo+"'";
				  //SQL query to obtain the NAME and PRICE of an item whose number is itemNo in a table ITEM
		  ResultSet r = s.executeQuery(query);
		  String result = "";
		  if (r.next())
		  {
		  	result = "$" + r.getDouble("PRICE") + " - " + r.getString("NAME");
		  }
		  else
		  {
		  	throw new Exception(itemNo + " not found!");
		  }
		  r.close(); 
		  s.close(); 
		  con.close();
		  
		  return result;
	}
	
	public String doHttp(String temp1, String temp2) throws IOException
	{
		String temp = HTTP_URL+"?country="+temp1+"&query="+temp2;
		URL url = new URL(temp);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		URLConnection oc = url.openConnection();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(oc.getInputStream()));
		String inputLine;
		if ((inputLine = in.readLine()) != null)
		{
			return inputLine;
		}
		return inputLine;
	}
	
	public String doRoster(String course) throws IOException
	{
		String temp = ROSTER_URL+"?course="+course;
		URL url = new URL(temp);
		URLConnection oc = url.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(oc.getInputStream()));
		String inputLine;
		if ((inputLine = in.readLine()) != null)
		{
			return inputLine;
		}
		return inputLine;
	}
}
