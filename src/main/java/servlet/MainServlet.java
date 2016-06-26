package servlet;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import core.HostTester;

/**
 * Servlet implementation class MainServlet
 */
public class MainServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(MainServlet.class
			.getName());
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MainServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		LOG.info("GET request");
		String reqUrl = request.getPathInfo();
		String directive = getDirective(reqUrl);
		
		if(directive != null) {
			switch(directive) {
			case "get_ip":
				processIP(request, response);
				break;
			case "test_connection":
				processTestConnection(request, response);
				break;
			default:
				break;
			}
		}
	}

	private void processTestConnection(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String hostAddr = request.getParameter("host");
		int port = 80;	// Default
		String portString = request.getParameter("port");
		if(portString != null) {
			try {
				port = Integer.parseInt(portString);
			} catch(NumberFormatException e) {
				LOG.info("Port number of '" + portString + "' is invalid" );
			}
		}
		if(hostAddr != null) {
			HostTester hostTester = new HostTester();
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("address", hostAddr + ":" + port);
			result.put("reachable", hostTester.testAddress(hostAddr, port));
			String jsonResponse = new Gson().toJson(result);
			response.setStatus(HttpURLConnection.HTTP_OK);
			response.setContentType("application/json");
			response.getWriter().println(jsonResponse);
		} else {
			LOG.warning("No 'host' parameter");
			logRequestParamNames(request);
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		}
	}
	
	private void processIP(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String hostAddr = request.getParameter("host");
		
		if(hostAddr != null) {
			HostTester hostTester = new HostTester();
			List<String> ipAddresses = hostTester.getIPaddresses(hostAddr);
			List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
			for(String ip: ipAddresses) {
				Map<String, Object> row = new HashMap<String, Object>();
				row.put("address", ip);
				results.add(row);
			}
			String jsonResponse = new Gson().toJson(results);
			response.setStatus(HttpURLConnection.HTTP_OK);
			response.setContentType("application/json");
			response.getWriter().println(jsonResponse);
		} else {
			LOG.warning("No 'host' parameter");
			logRequestParamNames(request);
			response.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
		}
	}
	
	private String getDirective(String reqUrl) {
		LOG.info("Request URL: " + reqUrl);
		if (reqUrl == null || reqUrl.equals("/") || reqUrl.split("/")[1] == "") {
			LOG.info("Request no directive");
			return null;
		} else {
			String directive = reqUrl.split("/")[1];
			LOG.info("Request directive: " + directive);
			return directive;
		}
	}
	
	private void logRequestParamNames(HttpServletRequest request) {
		Enumeration<String> enumer = request.getParameterNames();
		while(enumer.hasMoreElements()) {
			LOG.info("Request parameter: " + enumer.nextElement());
		}
	}
}
