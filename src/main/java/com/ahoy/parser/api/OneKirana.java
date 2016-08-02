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

public class OneKirana extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(OneKirana.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp ="";
		try{
			out = response.getWriter();
			TreeMap<String, TreeMap<String, String>> mapCategories =this.getCategories();		
			
			if(mapCategories!=null && mapCategories.size()>0){
				DBUtil dbUtil = new DBUtil();
				CityDo  cityDo = new CityDaoImpl().getByName("delhi");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("onekirana");
				if(merchantDo!=null){
				
					for(Map.Entry<String,TreeMap<String, String>> mapCategory: mapCategories.entrySet()){
						String catName = mapCategory.getKey();
						TreeMap<String, String> mapSubCategories = mapCategory.getValue();
						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
							String subCat = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							Document doc = this.hitUrlByJsoup(subCatUrl);
							if(doc!=null){
								Element element = !doc.select("div.product-list").isEmpty()?doc.select("div.product-list").first():null;
								if(element!=null){
									Elements elements = element.select("div.itemgrid");
									for (Element element2 : elements) {
										String productName = element2.select("div.product").first().text();
										String imgUrl = element2.select("img.itemImage").first().attr("data-original");
										String maxPrice = !element2.select("del").isEmpty()?element2.select("del").first().text():"";
										String sellPrice = !element2.select("div.cprice").isEmpty()?element2.select("div.cprice").first().text():element2.select("span.cprice").first().text();
										String respDB = dbUtil.saveOrUpdateDB(catName, subCat, productName, "", maxPrice, "", sellPrice, imgUrl, cityDo, merchantDo, "");
										logger.info("[OneKirana][process] catName: "+catName+" | subCat: "+subCat+" | productName: "+productName+" | imgUrl: "+imgUrl+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | respDB: "+respDB);
									}
								}else{
									logger.info("[OneKirana][process] element is blank");
								}
							}else{
								logger.info("[OneKirana][process] doc is null");
							}						
						}
					}
				}else{
					logger.info("[OneKirana][process] Merchant detail not found \"onekirana\"");
				}
			}else{
				logger.info("[OneKirana][process] categorySize is 0");
			}
			
			resp="process complete";
			
		}catch (Exception e) {
			resp="internal server error";
			logger.error("[OneKirana][process] Exception: "+e);
		}finally{
			out.println(resp);
			logger.info("[OneKirana][process] resp: "+resp);
		}
	}
	
	private Document hitUrlByJsoup(String url) {
		Document document = null;
		try {
			
			document = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.get();

		} catch (Exception e) {
			logger.error("[OneKirana][hitUrlByJsoup] Exception: "+e);
		}
		return document;
	}
	
	private TreeMap<String, TreeMap<String, String>> getCategories(){
		
		TreeMap<String, TreeMap<String, String>> mapCategories=null;
		try{
			
			Document doc = this.hitUrlByJsoup("http://www.onekirana.com/");
			if(doc!=null){

				mapCategories = new TreeMap<String, TreeMap<String,String>>();
				
				Element element = doc.select("div.demo-container").first();
				Element element2 = element!=null?element.select("ul.mega-menu").first():null;				
				Elements elements = element2!=null?element2.children():null;
				
				if(elements!=null && !elements.isEmpty()){
					
					for (Element element3 : elements) {
						
						Element element4 = element3.getElementsByTag("li").first();
						Element element5 = element4.getElementsByTag("a").first();						
						if(element5.attr("style")==null||element5.attr("style").isEmpty()){
							
							String categoryName = element5.text();
							String catUrl = "http://www.onekirana.com/"+element5.attr("href");
							
							Element element6 = element4.getElementsByTag("ul").first();
							TreeMap<String, String> mapSubCategories = new TreeMap<String, String>();
							if(element6!=null&&!"".equals(element6)){
								
								Elements elements2 = element6.getElementsByTag("li");
								for (Element element7 : elements2) {
									Element element8 = element7.getElementsByTag("a").first();
									Element element9 = element7.getElementsByTag("ul").first();
									if(element9!=null && !"".equals(element9) && element8!=null&&!"".equals(element8)){
										if(element8.text()!=null && !element8.text().toLowerCase().contains("brand")){
											Elements elements3 = element9.getElementsByTag("a");
											for (Element element10 : elements3) {
												String subCatName = element10.text();
												String subCatUrl = "http://www.onekirana.com"+(!element10.attr("href").startsWith("/")?"/":"")+element10.attr("href");
												mapSubCategories.put(subCatName, subCatUrl);
											}
										}										
									}
								}								
							}else{
								mapSubCategories.put(categoryName, catUrl);
							}							
							mapCategories.put(categoryName, mapSubCategories);
						}
					}
				}else{
					logger.info("[OneKirana][getCategories] elements is blank or empty");
				}
			}else{
				logger.info("[OneKirana][getCategories] document is null!! ");
			}
			
		}catch (Exception e) {
			logger.error("[OneKirana][getCategories] Exception: "+e);
		}
		return mapCategories;
	}
			
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
