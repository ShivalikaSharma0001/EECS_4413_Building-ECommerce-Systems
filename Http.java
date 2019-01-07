package ctrl;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Brain;

@WebServlet("/Http.do")
public class Http extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Http() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
	  //response.getWriter().append("Served at: ").append(request.getContextPath());
	  if (request.getParameter("calc") == null)
	  {
	     this.getServletContext().getRequestDispatcher("/Http.html").forward(request, response);
	  }
	  else
	  {
		 Brain model = Brain.getInstance();
	     try
	     {
	        String http = model.doHttp(request.getParameter("country"), request.getParameter("query"));
	        response.setContentType("text/html");
	        Writer out = response.getWriter();
	        String html = "<html><body>";
	        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
	        html += "<p><h1>Country Data:</h1></p>";
	        html += "<p>" + http + "</p>";
	        html += "</body></html>";
	        out.write(html);
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
