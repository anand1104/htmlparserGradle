package com.ahoy.parser.api;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
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


public class MangoShoppers extends HttpServlet{

	private static final long serialVersionUID = -4967995709660272536L;
	private Logger logger = LoggerFactory.getLogger(MangoShoppers.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp="";
		try{
			out = response.getWriter();
			HashMap<String, HashMap<String, String>> mapCategories  = getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("mangoshoppers");
				
				if(merchantDo!=null){
				
					for(Map.Entry<String, HashMap<String, String>> mapCategory: mapCategories.entrySet()){
						
						String categoryName = mapCategory.getKey();
						HashMap<String, String> mapSubCat = mapCategory.getValue();
						
						for(Map.Entry<String, String> mapSubCategory: mapSubCat.entrySet()){
							
							String subCatName = mapSubCategory.getKey();
							String subCatUrl = mapSubCategory.getValue();
							logger.info("[MangoShoppers][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl);
							
							Document doc = hitUrlByJsoup(subCatUrl);
							if(doc!=null){
								this.parseData(categoryName,subCatName,doc,merchantDo);
							
								TreeMap<String, String> mapPaging = this.getPagination(doc);
								
								if(mapPaging!=null && mapPaging.size()>0){
									for(Map.Entry<String, String> mapPage:mapPaging.entrySet()){								
										
										Document docPage = hitUrlByJsoup(mapPage.getValue());
										if(docPage!=null){
											this.parseData(categoryName,subCatName,docPage,merchantDo);
											logger.info("[MangoShoppers][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | page: "+mapPage.getKey()+" | url: "+mapPage.getValue()+" | complete!!");
										}else{
											logger.info("[MangoShoppers][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | page: "+mapPage.getKey()+" | url: "+mapPage.getValue()+" doc is"+docPage);
										}
									}
								}
							}else{
								logger.info("[MangoShoppers][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl+" | doc is "+doc);
							}
						
						}
					}
				}else{
					logger.info("[MangoShoppers][process] Merchant detail not found for \"mangoshoppers\"");
				}
				
			}else{
				logger.info("[MangoShoppers][process] mapCategories size: "+(mapCategories!=null?mapCategories.size():0));
			}			
			resp="process complete";
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[MangoShoppers][process] Exception: "+e);
		}finally{
			out.println(resp);
			logger.info("[MangoShoppers][process] resp: "+resp);
		}
		
	}
	
	
	public static HashMap<String, HashMap<String, String>> getCategories(){
		
		HashMap<String, HashMap<String, String>> map = new HashMap<String, HashMap<String, String>>();
		try{
			
			Document doc = hitUrlByJsoup("http://www.mangoshoppers.com/");
			
			Elements elements = doc.getElementsByClass("box-content");
			 for (Element element : elements) {
				Elements elements2 = element.getElementsByTag("li");
				for (Element element2 : elements2) {
					Attributes attributes = element2.attributes();
					for (Attribute attribute : attributes) {
						
						if(attribute.getKey().equals("class")){
							HashMap<String, String> mapSubCats = new HashMap<String, String>();
							Elements elements3 = element2.getElementsByTag("a");
							String catName = "";
							String catUrl = "";
							for (Element element3 : elements3) {
								catName = element3.text();
								catUrl = element3.attr("href");
								break;
							}
							
							if(attribute.getValue().equalsIgnoreCase("haschild")){
								Elements elements4 = element2.getElementsByTag("ul");
								
								for (Element element3 : elements4) {
									Elements elements5 = element3.getElementsByTag("a");
									for (Element element4 : elements5) {
										String subCatName = element4.text();
										String subCatUrl = element4.attr("href");
										mapSubCats.put(subCatName, subCatUrl);
									}
								}
							}else{
								mapSubCats.put(catName, catUrl);
							}						
							map.put(catName, mapSubCats);
						}
					}
				}
			}
		}catch (Exception e) {
//			logger.error("[MangoShoppers][getCategories] Exception: "+e);
		}
		return map;
	}
	
	private static Document hitUrlByJsoup(String url){
		
		Document document = null;		
		try{
			Connection connection = Jsoup.connect(url).timeout(25000);
			Response response = connection.execute();
			document = response.parse();	
			
		}catch (Exception e) {
//			logger.error("[MangoShoppers][hitUrlByJsoup] url: "+url+" | document:"+document+" | Exception: "+e);
		}
		return document;				
	}
	
	private void parseData(String categoryName,String subCatName,Document doc,MerchantDo merchantDo){		
		try{
			DBUtil dbUtil = new DBUtil();
			 Elements elements = doc.select("div.col-xs-6.col-lg-4.col-sm-6.col-xs-12.product_block");
			 
			 if(elements!=null && !elements.isEmpty()){
				 for (Element element : elements) {
					 String imgUrl = "";
					 String name = "";
					 String maxPrice = "";
					 String sellPrice = "";
					 Elements elements2 = element.getElementsByClass("image");
					 name  = element.getElementsByClass("name").text();
					 for (Element element2 : elements2) {
						if(element2.select("img.sold-img-category").isEmpty()){
							Elements elements3 = element2.select("a.img");
							for (Element element3 : elements3) {
								Elements elements4 = element3.getElementsByTag("img");
								for (Element element4 : elements4) {
									imgUrl = element4.attr("src");
								}
							}						
							if(!element.getElementsByClass("price-old").isEmpty()&&!element.getElementsByClass("price-new").isEmpty()){
								maxPrice = element.getElementsByClass("price-old").text();
								sellPrice = element.getElementsByClass("price-new").text();
							}else{
								sellPrice = element.getElementsByClass("price").text();
							}						
//							String resp = dbUtil.saveOrUpdateDB(categoryName, subCatName, name, "", maxPrice, "", sellPrice, imgUrl, "", "", merchantDo, "");
//							logger.info("[MangoShoppers][parseData] cat: "+categoryName+" | subCat: "+subCatName+" | product: "+name+" | maxPrice: "+maxPrice+" | sell price: "+sellPrice+" | imgexist: "+(!imgUrl.equals("")?"available":"not available")+" | resp: "+resp);
						}else{
							logger.info("[MangoShoppers][parseData] cat: "+categoryName+" | subCat: "+subCatName+" | product: "+name+" | status: out of stock");
						}
					}						
				}
			 }else{
				 logger.info("[MangoShoppers][parseData] cat: "+categoryName+" | subCat: "+subCatName+" | elements is empty");
			 }
			 
			
		}catch (Exception e) {
			logger.error("[MangoShoppers][parseData] cat: "+categoryName+" | subCat: "+subCatName+" | Exception: "+e);
		}
		
	}
	
	private TreeMap<String, String> getPagination(Document doc){
		TreeMap<String, String> mappages = new TreeMap<String, String>();
		try{
			Elements elements = doc.getElementsByClass("pagination");
			 for (Element element : elements) {
				Elements elements2 = element.getElementsByTag("a");				
				for (Element element2 : elements2) {
					if(element!=null &&element2.text().matches("[0-9]+")){
						mappages.put(element2.text(), element2.attr("href"));
					}
				}				
			 }
		}catch (Exception e) {
			logger.error("[MangoShoppers][getPagination] Exception: "+e);
		}
		return mappages;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
	public static void main(String[] args){
		try{
			Document doc = hitUrlByJsoup("http://www.mangoshoppers.com/beverages/cold-drinks");
//			System.out.println(doc);
			
			Element element = doc.select("div.product-grid").first();
			System.out.println(element);
			
			Elements elements = element.select("div.row");
			
			for (Element element2 : elements) {
//				System.out.println(element2);
				Elements elements2 = element2.select("div.product-block");
				for (Element element3 : elements2) {
					Element elementImage = element3.select("div.image").first();
					System.out.println("==================================");
					System.out.println(elementImage);
					
					Element elementProd = element3.select("div.product-meta").first();
					System.out.println("==================================");
					System.out.println(elementProd);
					
					break;
				}
				break;
			}
			
//			HashMap<String, HashMap<String, String>> mapCategories  = getCategories();
//			if(mapCategories!=null && mapCategories.size()>0){
//				for(Map.Entry<String, HashMap<String, String>> mapCategory: mapCategories.entrySet()){
//					String categoryName = mapCategory.getKey();
//					HashMap<String, String> mapSubCat = mapCategory.getValue();
//					
//					for(Map.Entry<String, String> mapSubCategory: mapSubCat.entrySet()){					
//										String subCatName = mapSubCategory.getKey();
//					String subCatUrl = mapSubCategory.getValue();
//					System.out.println(subCatName+" | "+subCatUrl);
//					Document doc = hitUrlByJsoup(subCatUrl);
//					System.out.println(doc);
//					
//					
//					}
//				}
//			}
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
