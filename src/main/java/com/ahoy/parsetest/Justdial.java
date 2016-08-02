package com.ahoy.parsetest;

import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.UtilConstants;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet("/justdial")
public class Justdial extends HttpServlet{

	private static Logger logger = LoggerFactory.getLogger(Justdial.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String city = request.getParameter("city");
		String keyword = request.getParameter("keyword");

		PrintWriter out = response.getWriter();
		String resp = null;
		int count = 0;
		try {
			
			if(city!=null && !"".equals(city.trim())&& keyword!=null && !"".equals(keyword.trim())){
//				String[] cityArray = {"Delhi","Ghaziabad","Faridabad","Gurgaon","Noida","Lucknow","Kanpur","Ludhiana","Indore","Bhopal","Jaipur","Mumbai","Kolkata","Vijayawada","Visakhapatnam","Hyderabad","Bangalore","Gulbarga","Hubli","Udupi","Belgaum","Mangalore","Hassan","Mysore","Davangere","Hosur","Chennai","North Tripura","South Tripura","West Tripura","Coimbatore","Madurai","Rajahmundry","Vizag"};
				city = city.replaceAll(" ","-");	
				keyword = keyword.replaceAll(" ","-");
				
				response.setContentType("application/text");
				response.addHeader("Content-Disposition","attachment;filename="+keyword+"_"+city+".txt");
				out.println("Store Name|Contact Detail|Address");
//				
				
				Response  res = Jsoup.connect("http://www.justdial.com/").userAgent("Mozilla").execute();
				Map<String, String> mp = res.cookies();				
				
				String nextUrl = "http://www.justdial.com/"+UtilConstants.toCamelCase(city)+"/"+UtilConstants.toCamelCase(keyword);
				do{
					
					res = Jsoup.connect(nextUrl).userAgent("Mozilla").cookies(mp).timeout(15000).execute();
					Document doc1 = res.parse();			
					Elements elements = doc1.getElementsByClass("rsl").get(0).getElementsByClass("cntanr");

					for(Element element:elements){
						String storeName = element.getElementsByClass("store-name").get(0).getElementsByClass("jcn").get(0).getElementsByTag("a").get(0).ownText();
						Elements contactElement = element.getElementsByClass("contact-info").get(0).getElementsByTag("a");
						String contactNo = "";
						for (Element element2 : contactElement) {
							if(element2.getElementsByTag("b").isEmpty()&&element2.getElementsByTag("b").size()==0){
								contactNo+=element2.ownText()+",";
							}else{
								contactNo+= element2.getElementsByTag("b").get(0).ownText();
							}
						}				
						Elements elements2 = element.getElementsByClass("address-info");
						Elements elements3 = !elements2.isEmpty()&&elements2.size()>0?elements2.get(0).getElementsByClass("mrehover"):elements2;
						String address = !elements3.isEmpty()&&elements3.size()>0?elements3.get(0).ownText():"";					
						out.println(storeName+"|"+contactNo+"|"+address);
						count++;
					}
					
					Element pageElement = doc1.getElementById("srchpagination");			
					Elements elements2 = pageElement.getElementsByTag("a");	
					nextUrl= null;
					for (Element element : elements2) {
						if(element.hasAttr("rel")&& element.attr("rel").equalsIgnoreCase("next")){
							nextUrl = element.attr("href");
							break;
						}
					}				
				}while(nextUrl!=null && nextUrl.startsWith("http"));
				
				resp  = "Success";
			}else{
				resp = "Bad Request";
				out.println(resp);
			}
		} catch (Exception e) {
			resp  = "Internal Error";
			logger.error("[Justdial][process] city: "+city+" | Keyword: "+keyword+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			out.close();
			logger.error("[Justdial][process] city: "+city+" | Keyword: "+keyword+" | count: "+count+" | resp: "+resp);
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
