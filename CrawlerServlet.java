package com.rd.mypkg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/crawler")
public class CrawlerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public CrawlerServlet() {
		super();        
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String urls[] = new String[1000];
		List<String> digiLinks = new ArrayList<String>();
		List<String> otherLinks = new ArrayList<String>();
		String url = "https://wiprodigital.com/";
		int i=0,j=0,tmp=0,total=0, MAX = 100;
		int start=0, end=0;
		String webpage = "";
		try {
			webpage = getWeb(url);
		} catch (Exception e1) {			
		}
		end = webpage.indexOf("<body");
		for(i=total;i<MAX; i++, total++){
			start = webpage.indexOf("http://", end);
			if(start == -1){
				start = 0;
				end = 0;
				try{
					webpage = getWeb(urls[j++]);
				}catch(Exception e){
				}

				/*logic to fetch urls out of body of webpage only */
				end = webpage.indexOf("<body");
				if(end == -1){
					end = start = 0;
					continue;
				}       
			}
			end = webpage.indexOf("\"", start);
			tmp = webpage.indexOf("'", start);
			if(tmp < end && tmp != -1){
				end = tmp;
			}
			url = webpage.substring(start, end);			
			if(url.contains("wiprodigital.com")){
			   	digiLinks.add(url);
			}			
			else if(url.contains("http://"))
				otherLinks.add(url);			
		}
		PrintWriter out = response.getWriter();
		out.print("<html><body>");
		out.print("<h4>Crawler Site Map</h4>");
		out.print("<ul>");
		if(!digiLinks.isEmpty())
			for (String link : digiLinks) {
				out.print("<li><a href="+link+">"+link+"</a></li>");
			}
		out.print("</ul>");
		out.print("<ul>");
		if(!otherLinks.isEmpty())
			for (String link : otherLinks) {
				out.print("<li><a href="+link+">"+link+"</a></li>");
			}
		out.print("</ul>");
		out.print("</body></html>");
	}

	public static String getWeb(String address)throws Exception{
		String webpage = "";
		String inputLine = "";
		URL url = new URL(address);
		BufferedReader in = new BufferedReader(
				new InputStreamReader(url.openStream()));
		while ((inputLine = in.readLine()) != null)
			webpage += inputLine;
		in.close();
		return webpage;
	}
}
