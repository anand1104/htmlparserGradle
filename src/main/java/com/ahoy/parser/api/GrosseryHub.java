package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection.Method;
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
import java.util.Map;
import java.util.TreeMap;

public class GrosseryHub extends HttpServlet{
	private static final long serialVersionUID = 5655919563736178419L;
	Logger logger = LoggerFactory.getLogger(GrosseryHub.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp="";
		try{
			out = response.getWriter();
			TreeMap<String,TreeMap<String, String>> mapCategories = this.getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				DBUtil dbUtil = new DBUtil();
				CityDo  cityDo = new CityDaoImpl().getByName("gurgaon");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("grosseryhub");
				
				if(merchantDo!=null){
					for(Map.Entry<String, TreeMap<String, String>> mapCategory:mapCategories.entrySet()){
						
						String categoryName=mapCategory.getKey();
						TreeMap<String, String> mapSubCategories = mapCategory.getValue();
						
						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
							
							String subCatName = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							logger.info("[GrosseryHub][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl);
							
							Document doc = this.hitUrlByJsoup(subCatUrl);
							
							if(doc!=null){
								Element element = doc.select("div.product").first();
								Elements elements = element.select("div.product_one");
								if(elements!=null && !elements.isEmpty()){
									for (Element element2 : elements) {
										String imgUrl = element2.select("img").first().attr("src");
										imgUrl = imgUrl!=null?"http://grosseryhub.com/"+imgUrl:"";
										String productName = element2.select("span").first().text();
										String offer = element2.select("div.discount_tag")!=null&&!element2.select("div.discount_tag").isEmpty()?element2.select("div.discount_tag").first().text():"";
										
										Element element3 = element2.select("div.price").first();
										String maxPrice = element3.select("span.pricemrp")!=null&&!element3.select("span.pricemrp").isEmpty()?element3.select("span.pricemrp").first().text():"";
										String sellPrice = element3.select("span.pricedis").first().text();
										String respDB = dbUtil.saveOrUpdateDB(categoryName, subCatName, productName, "", maxPrice, offer, sellPrice, imgUrl,cityDo, merchantDo, "");
										logger.info("[GrosseryHub][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | productName: "+productName+" | imgUrl: "+imgUrl+" | offer: "+offer+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | respDB: "+respDB);
									}
								}else{
									logger.info("[GrosseryHub][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | elements: "+elements);
								}
							}else{
								logger.info("[GrosseryHub][process] doc: "+doc);
							}
						}
					}
				}else{
					logger.info("[GrosseryHub][process] merchant detail not found \"grosseryhub\"");
				}
			}else{
				logger.info("[GrosseryHub][process] mapCategories: "+mapCategories);
			}
			
			resp="process complete";
			
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[GrosseryHub][process] Exception: "+e);
		}finally{
			out.println(resp);
		}
	}
	
	private TreeMap<String,TreeMap<String, String>> getCategories(){
		
		TreeMap<String,TreeMap<String, String>> mapCategories = null;
		
		try{
			
			Document doc = this.hitUrlByJsoup("http://grosseryhub.com/");
			
			if(doc!=null){
				
				mapCategories = new TreeMap<String, TreeMap<String,String>>();		
				
				Element element = doc.select("ul.m_mainMenu").first();
				Elements elements = element.select("li.m_mainLi");
				
				for (Element element2 : elements) {
					
					Element element3 = element2.select("a.main_m").first();
					String categoryName = element3.text();
					Element element4 = element2.select("ul.inneMain_Menu")!=null?element2.select("ul.inneMain_Menu").first():null;
					if(element4!=null){
						Element element5 = element4.select("div.pro_list").first();
						Elements elements2 = element5.getElementsByTag("a");
						TreeMap<String,String> mapSubCategories = new TreeMap<String, String>();
						for (Element element6 : elements2) {
							String subCatName = element6.text();
							String subCatUrl =  "http://grosseryhub.com/"+element6.attr("href");
							mapSubCategories.put(subCatName, subCatUrl);
						}
						mapCategories.put(categoryName, mapSubCategories);
					}
				}
			}else{
				logger.info("[GrosseryHub][getCategories] Doc is null");
			}		
		}catch (Exception e) {
			logger.error("[GrosseryHub][getCategories] Exception: "+e);
		}
		return mapCategories;
	}
	
	private Document hitUrlByJsoup(String url) {
		Document document = null;

		try {
			document = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.get();

		} catch (Exception e) {
			logger.error("[GrosseryHub][hitUrlByJsoup] Exception: "+e);
		}
		return document;
	}
	
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
