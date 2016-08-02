package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection.Method;
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

public class ShopRation extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(ShopRation.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = null;
		String resp ="";
		try{
			out = response.getWriter();
			TreeMap<String, HashMap<String, String>> mapCategories = this.getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				CityDo  cityDo = new CityDaoImpl().getByName("noida");
				DBUtil dbUtil = new DBUtil();
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("shopration");
				if(merchantDo!=null){
				
					for(Map.Entry<String,HashMap<String, String>> mapEntry: mapCategories.entrySet()){
						String categoryName = mapEntry.getKey();
						HashMap<String, String> mapSubCat = mapEntry.getValue();
						for(Map.Entry<String, String> mapEntry2: mapSubCat.entrySet()){
							String subCat = mapEntry2.getKey();
							String subCatUrl = mapEntry2.getValue();					
							Document doc = this.hitUrlByJsoup(subCatUrl);
							
							if(doc!=null){
								logger.info("[ShopRation][process] categoryName: "+categoryName+" | subCat: "+subCat+" | subCatUrl: "+subCatUrl);
								Element elementPagination = doc.getElementsByClass("pagination").first();					
								String pageText = elementPagination!=null?elementPagination.getElementsByClass("results").first().text():"";
								
								pageText = pageText!=null&&!"".equals(pageText)?pageText.substring(pageText.indexOf("(")+1, pageText.indexOf(")")-1).trim():pageText;
								
								int pages = (pageText.split(" ")[0]!=null&&pageText.split(" ")[0].trim().matches("[0-9]+"))?Integer.valueOf(pageText.split(" ")[0].trim()):1;
							
								for(int i = 0 ; i<pages ; i++){
									
									Element elementProductGrid = null;
									if(i==0){
										elementProductGrid = doc.getElementsByClass("product-grid").first();
									}else{
										doc = null;
										doc = this.hitUrlByJsoup(subCatUrl+"&page="+(i+1));
										elementProductGrid = null;
										elementProductGrid = doc.getElementsByClass("product-grid").first();
									}
									
									Elements elementsItemsBlock = elementProductGrid!=null?elementProductGrid.select("div.item.block"):null;
									
									if(elementsItemsBlock!=null && !elementsItemsBlock.isEmpty()){
										for (Element elementItemBlock : elementsItemsBlock) {
											String imageUrl = "";
											String productName = "";
											String maxPrice = "";
											String sellPrice = "";
											String offer = "";
											
											Element elementImage = elementItemBlock.select("div.image").first();
											Element elementImageTag = elementImage!=null?elementImage.getElementsByTag("img").first():null;
											imageUrl = elementImageTag!=null?elementImageTag.attr("src"):"";
											
											Element elementName = elementItemBlock.select("div.name").first();
											productName = elementName.text();
											
											Element elementPrice = elementItemBlock.select("div.price").first();							
											Element elementOldPrice = elementPrice.select("span.price-old").first();
											maxPrice = elementOldPrice!=null?elementOldPrice.text():"";							
											Element elementNewPrice = elementPrice.select("span.price-new").first();
											sellPrice = elementNewPrice!=null?elementNewPrice.text():"";							
											sellPrice = sellPrice==null||"".equals(sellPrice)?elementPrice.text():sellPrice;
											
											Element elementSaveMoney = elementItemBlock.select("div.savemoney").first();
											offer = elementSaveMoney!=null?elementSaveMoney.text().replace("-", "").trim():"";
											
											String respDB = dbUtil.saveOrUpdateDB(categoryName, subCat, productName, "", maxPrice, offer, sellPrice, imageUrl,cityDo, merchantDo, ""); 
											
											logger.info("[ShopRation][process]categoryName: "+categoryName+" | subCat: "+subCat+"productName: "+productName+" | imageUrl: "+imageUrl+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | offer: "+offer+" | respDB: "+respDB);
										}
									}else{
										logger.info("[ShopRation][process] categoryName: "+categoryName+" | subCat: "+subCat+" | subCatUrl: "+subCatUrl+" product unavailable");
									}
									
															
								}
							}else{
								logger.info("[ShopRation][process] categoryName: "+categoryName+" | subCat: "+subCat+" | subCatUrl: "+subCatUrl+" | doc: "+doc);
							}
						}
					}
				}else{
					logger.info("[ShopRation][process] Merchant detail not found \"shopration\"");
				}
			}else{
				logger.info("[ShopRation][process] mapCategories size: "+(mapCategories!=null?mapCategories.size():0));
			}
			resp = "process complete";
		}catch (Exception e) {
			resp = "internal server error";
			logger.error("[ShopRation][process] Exception: "+e);
			e.printStackTrace();
		}finally{
			out.println(resp);
		}
		
	}
	
	private TreeMap<String, HashMap<String, String>> getCategories(){
		
		TreeMap<String, HashMap<String, String>> mapCategories = null;
		
		try{
			mapCategories = new TreeMap<String, HashMap<String,String>>();
			
			Document doc = Jsoup.connect("http://shopration.com/").timeout(25000).get();
			Element element = doc.getElementById("wrapper_menu");
			Element element2 = element.select("ul.menuResp").first();
		
			Elements elements = element2.children();
			int i = 0;
			for (Element element3 : elements) {
				
				Element element4 = element3.getElementsByTag(element3.tagName()).first();
				Elements elements2 = element4.children();
				
				String categoryName = "";
				String categoryUrl = "";
				
				HashMap<String, String> mapSubCat = new HashMap<String, String>();
				
				for (Element element5 : elements2) {
					
					if(element5.tagName()!=null && element5.tagName().equals("a")){
						if(element5.text()!=null && !"".equals(element5.text().trim()) && !element5.text().trim().equalsIgnoreCase("home")){
							i++;
							categoryName = i+"_"+element5.text();
							categoryUrl = element5.attr("href");
						}
					}
					
					if(element5.tagName()!=null && element5.tagName().equals("div")){
						Elements elements3 = element5.children();
						
						for (Element element6 : elements3) {
							
							if(element6.tagName()!=null&&element6.tagName().equals("ul")){
								
								Elements elements4ultag = element6.getElementsByTag("ul");
								
								for (Element element7ul : elements4ultag) {
									Elements elements4SubCat = element7ul.getElementsByTag("li");
									
									for (Element element7 : elements4SubCat) {
										
										Attributes attributes = element7.attributes();
										
										for (Attribute attribute : attributes) {
											
											if(attribute.getKey()!=null&&attribute.getKey().equals("id") && attribute.getValue()!=null&&attribute.getValue().equals("subcategory")){
												
												Elements elements4 = element7.children();
												
												for (Element element8 : elements4) {
													if(element8.tagName()!=null && element8.tagName().equals("a")){
														Element element9 = element8.getElementsByTag("a").first();
														mapSubCat.put(element9.text(), element9.attr("href"));
													}else if(element8.tagName()!=null && element8.tagName().equals("div")){
														Element element9 = element8.getElementsByTag("div").first();
														Element element10 = element9.getElementsByTag("ul").first();
														Elements elements5 = element10.getElementsByTag("a");
														for (Element element11 : elements5) {
															mapSubCat.put(element11.text(), element11.attr("href"));
														}
													}
												}
											}
										}										
									}
								}							
							}
						}
					}else{
						if(categoryName!=null &&!"".equals(categoryName) && !categoryName.equalsIgnoreCase("home"))
						mapSubCat.put(categoryName, categoryUrl);
					}					
				}
				mapCategories.put(categoryName, mapSubCat);
			}
			
		}catch (Exception e) {
			logger.error("[ShopRation][getCategories] Exception: "+e);
		}
		return mapCategories;
	}
	
	private Document hitUrlByJsoup(String url){
		Document document = null;
		
		try{
			document = Jsoup.connect(url)
							.method(Method.GET)
							.timeout(25000)
							.get();
			
		}catch (Exception e) {
			logger.error("[ShopRation][hitUrlByJsoup] Exception: "+e);
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
