package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

@WebServlet("/Time.do")
public class Time extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public Time()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
	  //response.getWriter().append("Served at: ").append(request.getContextPath());
	  if (request.getParameter("calc") == null)
	  {
	     this.getServletContext().getRequestDispatcher("/Time.html").forward(request, response);
	  }
	  else
	  {
		 Brain model = Brain.getInstance();
		 
		 if (!request.getParameter("hours").isEmpty())
		 {
			 try
		     {
				String time = model.doTime();
		        String hours = model.addTime("hours");
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = "<html><body>";
		        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
		        html += "<p>Server Time: </p>";
		        html += "<p>" + time + "</p>";
		        html += "<p>" + hours + "</p>";
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
		 else
		 {
			 try
		     {
		        String time = model.doTime();
		        response.setContentType("text/html");
		        Writer out = response.getWriter();
		        String html = "<html><body>";
		        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
		        html += "<p>Server Time: " + time + "</p>";
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
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		doGet(request, response);
	}

}
