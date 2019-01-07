package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

@WebServlet("/Roster.do")
public class Roster extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Roster() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
	  //response.getWriter().append("Served at: ").append(request.getContextPath());
      if (request.getParameter("calc") == null)
	  {
	     this.getServletContext().getRequestDispatcher("/Roster.html").forward(request, response);
	  }
	  else
	  {
		  Brain model = Brain.getInstance();
	     try
	     {
	        String roster = model.doRoster(request.getParameter("course"));
	        response.setContentType("text/xml");	
	        Writer out = response.getWriter();
	        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>";
	        xml += "<?xml-stylesheet type='text/xsl' href='Roster.xsl'?>";
	     
	        int length = roster.length();
	        int index = roster.indexOf("course");
	        xml += roster.substring(index-1, length);
	       
	        out.write(xml);
	     }
	     catch (Exception e)
	     {
	        response.setContentType("text/html");
	        Writer out = response.getWriter();
	        String html = "<html><body>";
	        html += "<p><a href=' Dash.do'>Back to Dashboard</a></p>";
	        html += "<p>Error " + e.getMessage() + "</p>";
	        out.write(html);
	     }
	  }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		doGet(request, response);
	}
}
