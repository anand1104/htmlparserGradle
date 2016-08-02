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
import java.util.HashMap;
import java.util.Map;

public class Jagsfresh extends HttpServlet{
	
	private static final long serialVersionUID = 5844068337965142807L;
	private Logger logger = LoggerFactory.getLogger(Jagsfresh.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = null;
		String resp="";
		try{
			
			out = response.getWriter();
			HashMap<String, HashMap<String, String>> mapCategories = this.getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				DBUtil dbUtil = new DBUtil();
				CityDo  cityDo = new CityDaoImpl().getByName("gurgaon");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("jagsfresh");
				
				if(merchantDo!=null){
						
					for(Map.Entry<String, HashMap<String, String>> mapCategory:mapCategories.entrySet()){
						
						String categoryName = mapCategory.getKey();
						HashMap<String, String> mapSubCategories = mapCategory.getValue();
						for(Map.Entry<String,String> mapSubCategory:mapSubCategories.entrySet()){
							
							String subCatName = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							logger.info("[Jagsfresh][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl);
							
							Document doc = this.hitUrlByJsoup(subCatUrl);
							if(doc!=null){
								
								do{
									Element element = doc.select("div.product-container").first();
									Elements elements = element.select("div.item-inner");
									for (Element element2 : elements) {
										String productName = element2.select("h2.product-name").first().text();
										String imageUrl = element2.select("a.product-image").first().select("img").first().attr("src");
										Element element3 = element2.select("div.price-box").first();
										String sellPrice = !element3.select("p.special-price").isEmpty()?element3.select("p.special-price").first().select("span.price").first().text():element3.select("span.regular-price").first().text();
										String maxPrice = !element3.select("p.old-price").isEmpty()?element3.select("p.old-price").first().select("span.price").first().text():"";
										String weight = "";
										Elements elements2 = element2.select("p");
										for (Element element4 : elements2) {
											if((element4.className()==null ||"".equals(element4.className())) && element4.text()!=null&&element4.text().toLowerCase().trim().startsWith("per")){
												weight = element4.text();
												break;
											}											
										}
										String respDB = dbUtil.saveOrUpdateDB(categoryName, subCatName, productName, weight, maxPrice, "", sellPrice, imageUrl, cityDo, merchantDo, "");
										logger.info("[Jagsfresh][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | productName: "+productName+" | weight: "+weight+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | imageUrl: "+imageUrl+" | respDB: "+respDB);
									}
									
	//								next page url
									Element element4 = !doc.select("div.pages").isEmpty()?doc.select("div.pages").first():null;
									Element element5 = element4!=null?(!element4.select("a.next.i-next").isEmpty()?element4.select("a.next.i-next").first():null):null;
									String nextUrl  = element5!=null? element5.attr("href"):"";
										
									doc = nextUrl!=null&&!"".equals(nextUrl)?this.hitUrlByJsoup(nextUrl):null;
									
								}while(doc!=null);
							}						
						}
					}
				}else{
					logger.info("[Jagsfresh][process] merchant detail not found \"jagsfresh\"");
				}
			}else{
				logger.info("[Jagsfresh][process] mapCategories is 0");
			}
			
			resp="process complete";
		}catch (Exception e) {
			resp="internal server error";
			logger.error("[Jagsfresh][process] Exception: "+e);
		}finally{
			out.println(resp);
		}
		
	}
	
	private HashMap<String,HashMap<String, String>> getCategories(){
		
		HashMap<String,HashMap<String, String>> mapCategories=null;
		
		try{
			mapCategories = new HashMap<String, HashMap<String,String>>();
			
			Document doc = this.hitUrlByJsoup("http://www.jagsfresh.com/");
			if(doc!=null){
				Element element = doc.select("div.pt_custommenu").first();
				Elements elements = element.children();
				for (Element element2 : elements) {
					String tagName = element2.tagName();
					String className = element2.className();
					
					if(tagName!=null&&tagName.equals("div") && className!=null&&className.equals("pt_menu")){
						HashMap<String, String> mapSubCategories = new HashMap<String, String>();
						String categoryName = element2.select("div.parentMenu").first().text();
						Element element3 = element2.select("div.itemMenu.level1").first();
						Elements elements2 = element3.getElementsByTag("a");
						for (Element element4 : elements2) {
							String subCatName = element4.text();
							String subCatUrl = element4.attr("href");
							mapSubCategories.put(subCatName, subCatUrl);
						}
						mapCategories.put(categoryName, mapSubCategories);
					}
								
				}
			}else{
				logger.info("[Jagsfresh][getCategories] doc is null");
			}
		}catch (Exception e) {
			logger.error("[Jagsfresh][getCategories] Exception: "+e);
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
			logger.error("[Jagsfresh][hitUrlByJsoup] Exception: "+e);
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
