package ctrl;

import java.io.IOException;
import java.io.Writer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Brain;

@WebServlet("/OAuth.do")
public class OAuth extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    public OAuth() 
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		if ((request.getParameter("calc") == null) && (request.getParameter("user") == null))
		{
		   this.getServletContext().getRequestDispatcher("/OAuth.html").forward(request, response);
		}
		else if (request.getParameter("calc") != null)
		{
			response.sendRedirect("https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi?back=http://localhost:4413/ProjB/OAuth.do"); 
		}
	    else
	    {
		  //response.sendRedirect("https://www.eecs.yorku.ca/~roumani/servers/auth/oauth.cgi");
		    Writer out = response.getWriter();
	        String html = "<html><body>";
	        html += "<p><a href='Dash.do'>Back to Dashboard</a></p>";
	        html += "<p>" + request.getQueryString() + "</p>";
	        html += "</body></html>";
	        out.write(html);
	   }
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{	
		doGet(request, response);
	}
}
