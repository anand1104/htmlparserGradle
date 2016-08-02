package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HypercityIndia extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(HypercityIndia.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp="";
		try{
			out = response.getWriter();
			HashMap<String,TreeMap<String, String>> mapCategories = new HypercityIndia().getCategories();
			
			if(mapCategories!=null&&mapCategories.size()>0){
				DBUtil dbUtil = new DBUtil();
				CityDo  cityDo = new CityDaoImpl().getByName("mumbai");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("grosseryhub");
				
				if(merchantDo!=null){
				
					for(Map.Entry<String,TreeMap<String, String>> mapCategory:mapCategories.entrySet()){
						
						String catName = mapCategory.getKey();
						TreeMap<String, String> mapSubCategories = mapCategory.getValue();
						
						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
							
							String subCatName = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							
							Document doc = new HypercityIndia().hitUrlByJsoup(subCatUrl);
							
							if(doc!=null){
								Element element = doc.select("ul.products-grid").first();
								Elements elements = element!=null?element.children():null;
								
								if(elements!=null && !elements.isEmpty()){
									for (Element element2 : elements) {
										String product ="";
										String price="";
										String weight = "";
										String imageUrl ="";
										String mrp="";
										String offer="";
										
										Element element3prgtext = element2.select("div.prgtext").first();									
										product = element3prgtext.select("h2.product-name").first().select("a").first().text();
										mrp = !element3prgtext.select("span").isEmpty()?this.junkBoucer(element3prgtext.select("span").first().text()).trim():"";
										mrp = mrp.replaceAll("Rs. ", "Rs.").replace("MRP:", "").replace("mrp:", "").trim();
										
										offer = element2!=null?(!element2.select("p.savblurb").isEmpty()?this.junkBoucer(element2.select("p.savblurb").first().text()).replace("Rs. ", "Rs."):""):"";
										imageUrl = element2.select("p.cuprodPan").first().select("a").first().select("img").first().attr("src");
										
										Element element3 = !element2.select("div.top2off").isEmpty()?element2.select("div.top2off").first():element2.select("div.topoff").first();	
										Element element4 = element3!=null?(!element3.select("p.cupantxt").isEmpty()?element3.select("p.cupantxt").first():null):null;																
										
										String element4Text = element4!=null?this.junkBoucer(element4.text().replace("Our Price", "").trim()):"";									
										element4Text = element4Text.replaceAll("Rs. ", "Rs.");
										
										if(element4Text!=null && element4Text.contains("%")){
											if(element4Text.trim().toLowerCase().startsWith("rs.")){
												
												price = element4Text.substring(0,element4Text.indexOf(" ", element4Text.indexOf("Rs.")));
												offer = element4Text.substring(element4Text.indexOf(" ", element4Text.indexOf("Rs.")), element4Text.length()).trim();
												
											}else{
												
												offer = element4Text.substring(0, element4Text.indexOf("Rs."));
												price = element4Text.substring(element4Text.indexOf("Rs."), element4Text.length());
												
											}
										}else if(element4Text!=null && element4Text.trim().toLowerCase().startsWith("rs.") 
												&& (element4Text.trim().toLowerCase().contains("buy")||element4Text.trim().toLowerCase().contains("off"))){
											
											price = element4Text.substring(0,element4Text.indexOf(" ", element4Text.indexOf("Rs.")));
											offer = element4Text.substring(element4Text.indexOf(" ", element4Text.indexOf("Rs.")), element4Text.length()).trim();
											
										}else if(element4Text!=null 
												&& (element4Text.trim().toLowerCase().startsWith("get") ||element4Text.trim().toLowerCase().startsWith("buy"))
												&& (element4Text.trim().toLowerCase().contains("rs."))){
											
											offer = element4Text.substring(0,element4Text.indexOf("Rs."));
											price = element4Text.substring(element4Text.indexOf("Rs."));
											
										}else if(element4Text!=null && element4Text.toLowerCase().contains("starting")){
											if(element4Text!=null && element4Text.toLowerCase().contains("kg")){
												
												price = element4Text.substring(0,element4Text.indexOf(" ", element4Text.indexOf("Rs.")));
												weight = element4Text.substring(element4Text.indexOf(" ", element4Text.indexOf("Rs.")), element4Text.length()).trim();
												
											}else{
												price =element4Text;
											}
										}else if(element4Text!=null && element4Text.toLowerCase().contains("kg")){
											
											price = element4Text.substring(0,element4Text.indexOf(" ", element4Text.indexOf("Rs.")));
											weight = element4Text.substring(element4Text.indexOf(" ", element4Text.indexOf("Rs.")), element4Text.length());
											
										}else{
											price = element4Text;
											
										}
										String respDB = dbUtil.saveOrUpdateDB(catName, subCatName, product, weight, mrp, offer, price, imageUrl,cityDo, merchantDo, "");
										logger.info("[HypercityIndia][process] catName: "+catName+" | subCatName: "+subCatName+" | product: "+product+" | imageUrl: "+imageUrl+" | offer: "+offer+" | price: "+price+" | weight: "+weight+" | mrp: "+mrp+" | respDB: "+respDB);
									}
								}
							}else{
								logger.info("[HypercityIndia][process] doc is null !!");
							}
						}
					}
				}else{
					logger.info("[HypercityIndia][process] merchant detail not found \"hypercityindia\"");
				}
			}else{
				logger.info("[HypercityIndia][process] mapCategories size: "+(mapCategories!=null?mapCategories.size():0)+" !!");
			}
			resp="process complete";
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[HypercityIndia][process] Exception: "+e);
		}
		finally{
			out.println(resp);
			logger.info("[HypercityIndia][process] resp: "+resp);
		}
	}
	
	private HashMap<String, TreeMap<String, String>> getCategories(){
		
		HashMap<String, TreeMap<String, String>> mapCategories = null;
		
		try{
			Document doc = this.hitUrlByJsoup("http://www.hypercityindia.com");
			
			if(doc!=null){
				
				mapCategories = new HashMap<String, TreeMap<String,String>>();				
				Element element = doc.select("div.nav-container").first().select("div.ddsmoothmenu").first().children().first();
				Elements elements = element.children();
				
				for (Element element2 : elements) {
					
					Elements elements2 = element2.children();
					String catName = elements2.size()>0?elements2.get(0).text():"";
					Element element3 = elements2.size()>1?elements2.get(1):null;
					TreeMap<String, String> mapSubCategories = new TreeMap<String, String>();
					
					if(element3!=null){						
						Elements elements3 = element3.children();
						for (Element element4 : elements3) {
							
							Element element5 = element4.children().first();
							String subCatName = element5.select("span").first().text();
							String subCatUrl = element5.attr("href");
							mapSubCategories.put(subCatName, subCatUrl);
							
						}
					}else{
						
						String subCatName = catName;
						String subCatUrl = elements2.get(0).attr("href");
						mapSubCategories.put(subCatName, subCatUrl);
						
					}
					
					mapCategories.put(catName, mapSubCategories);
				}
			}else{
				logger.info("[HypercityIndia][getCategories] doc is null");
			}
			
		}catch (Exception e) {
			logger.info("[HypercityIndia][getCategories] Exception: "+e);
		}
		
		return mapCategories;
	}
	
	
	private Document hitUrlByJsoup(String url){
		
		Document document = null;		
		try{
			Connection connection = Jsoup.connect(url).timeout(25000);
			Response response = connection.execute();
			document = response.parse();			
		}catch (Exception e) {
			logger.error("[HypercityIndia][hitUrlByJsoup]["+url+"] document:"+document+" Exception: "+e);
		}
		return document;				
	}
		
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	

	private String junkBoucer(String str){
		StringBuilder builder =new StringBuilder();
		try{
			
			for (int i=0;i<str.length();i++){				
				if (str.charAt(i)<127){
	                   builder.append(str.charAt(i));
	            }else{
	            	builder.append("Rs.");
	            }
	         }
			
		}catch (Exception e) {
			logger.error("[RelianceFreshDirect][hitUrlByJsoup] Exception: "+e);
		}
		
		return builder.toString();
	}
}
