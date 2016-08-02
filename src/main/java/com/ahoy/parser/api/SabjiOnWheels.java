package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection.Method;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SabjiOnWheels extends HttpServlet{
	
	private static final long serialVersionUID = 5655919563736178419L;
	Logger logger = LoggerFactory.getLogger(SabjiOnWheels.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {

		try {
			TreeMap<String, String> mapCategories = this.getCategories();
			
			if(mapCategories!=null && mapCategories.size()>0){
				CityDo  cityDo = new CityDaoImpl().getByName("gurgaon");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("sabjionwheels");
				if(merchantDo!=null){
				
					for (Map.Entry<String, String> mapCategory : mapCategories.entrySet()) {
						
						String categoryName = mapCategory.getKey();
						String categoryUrl = mapCategory.getValue();
						logger.info("[SabjiOnWheels][process] categoryName: "+categoryName+" | categoryUrl: "+categoryUrl);
						
						
						
						if(mapCategory.getKey().equalsIgnoreCase("FRUIT BASKETS") 
								|| mapCategory.getKey().equalsIgnoreCase("GREEN TEA") 
								|| mapCategory.getKey().equalsIgnoreCase("HEALTH BOXES") 
								|| mapCategory.getKey().equalsIgnoreCase("WEEKLY BOXES")){
							Document doc = this.hitUrlByJsoup(categoryUrl);
							this.processDoc1(doc, categoryName,merchantDo,cityDo);
							logger.info("[SabjiOnWheels][process] categoryName: "+categoryName+" | categoryUrl: "+categoryUrl+" | finished");
							
						}else if(mapCategory.getKey().equalsIgnoreCase("FRUITS") 
								|| mapCategory.getKey().equalsIgnoreCase("VEGETABLES")){
							Document doc = this.hitUrlByJsoup(categoryUrl);
							this.processDoc2(doc, categoryName,merchantDo,cityDo);
							logger.info("[SabjiOnWheels][process] categoryName: "+categoryName+" | categoryUrl: "+categoryUrl+" | finished");
							
						}else if(mapCategory.getKey().equalsIgnoreCase("SPICES")){
							Document doc = this.hitUrlByJsoup(categoryUrl);
							this.processDoc3(doc, categoryName, merchantDo, cityDo);
						}else{
							logger.info("[SabjiOnWheels][process] categoryName: "+categoryName+" | categoryUrl: "+categoryUrl+" No parsing");
						}					
					}
				}else{
					logger.info("[SabjiOnWheels][process] Merchant detail not found \"sabjionwheel\"");
				}
			}else{
				logger.info("[SabjiOnWheels][process] mapCategories size: "+(mapCategories!=null?mapCategories.size():mapCategories));
			}
		} catch (Exception e) {
			logger.error("[SabjiOnWheels][process] Exception: "+e);
		}

	}
	
	private void processDoc1(Document doc,String categoryName,MerchantDo merchantDo,CityDo cityDo){
		
		try{
			Element element = doc.getElementById("container");
			Elements elements = categoryName.equalsIgnoreCase("HEALTH BOXES")?element.select("div.original-box.proDetail"):element.select("div.original-box2.proDetail");
			
			if(elements!=null && !elements.isEmpty()){		
				DBUtil dbUtil = new DBUtil();
				for (Element element2 : elements) {
					
					String productName="";
					String imgUrl ="";
					String sellPrice="";
					String maxPrice="";
					
					productName = element2.getElementsByTag("h2").first().getElementsByTag("strong").first().text();
					
					Element element3 = categoryName.equalsIgnoreCase("HEALTH BOXES")?element2.select("div.popup-box.popup-box-left").first():element2.select("div.popup-box2.popup-box2-left").first();
					if(!categoryName.equalsIgnoreCase("GREEN TEA")){
						productName+="("+element3.getElementsByTag("strong").first().text()+" ";
						List<TextNode> textNodes =  element3.textNodes();
						for (TextNode textNode : textNodes) {
							if(!textNode.isBlank())
								productName = productName+textNode.toString().trim()+", ";
						}
						productName = productName.substring(0,productName.length()-2)+")";
					}else{
						
						if(productName.contains("-")){
							String[] splt = productName.split("-");
							productName = splt[0].trim();
							maxPrice = splt[1].trim().replace("MRP.", "").replace("/", "").trim();
						}
					}					
					
					sellPrice = element2.select("span.s_pirce").text(); 
					imgUrl = "http://www.sabjionwheels.com/"+element2.select("img").first().attr("src");
					String respDB = dbUtil.saveOrUpdateDB(categoryName, "", productName, "", maxPrice, "", sellPrice, imgUrl,cityDo, merchantDo, "");
					logger.info("[SabjiOnWheels][processDoc1] categoryName: "+categoryName+" | productName: "+productName+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | imgUrl: "+imgUrl+" | respDB: "+respDB);
				}
			}else{
				logger.info("[SabjiOnWheels][processDoc1] elements is empty or null");
			}
		}catch (Exception e) {
			logger.error("[SabjiOnWheels][processDoc1] Exception: "+e);
		}
	}
	private void processDoc2(Document doc,String categoryName,MerchantDo merchantDo,CityDo cityDo){
		
		try{			
			Element element = doc.select("div.feedback-inner").first();
			Elements elements = element.select("tr.proDetail");
			if(elements!=null&&!elements.isEmpty()){
				DBUtil dbUtil = new DBUtil();
				for (Element element2 : elements) {
					
					String imageUrl = "";
					String productName = "";
					String maxPrice = "";
					String sellPrice = "";
					
					Elements elements2 = element2.getElementsByTag("td");
					
					for (Element element3 : elements2) {
						
						Attributes attributes = element3.attributes();
						
						for (Attribute attribute : attributes) {
							if(attribute.getKey()!=null&&attribute.getKey().equalsIgnoreCase("valign") && attribute.getValue()!=null&&attribute.getValue().equalsIgnoreCase("top")){
								imageUrl = "http://www.sabjionwheels.com/"+element3.getElementsByTag("img").first().attr("src");
							}else if(attribute.getKey()!=null&&attribute.getKey().equalsIgnoreCase("valign") && attribute.getValue()!=null&&attribute.getValue().equalsIgnoreCase("middle")){
								productName = element3.getElementsByTag("strong").text();
								maxPrice = element3.select("span.market_pirce").text();
								sellPrice = element3.select("span.s_pirce").text();
							}
						}
					}
					String respDB = dbUtil.saveOrUpdateDB(categoryName, "", productName, "", maxPrice, "", sellPrice, imageUrl,cityDo, merchantDo, "");
					logger.info("[SabjiOnWheels][processDoc2] categoryName: "+categoryName+" | productName: "+productName+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | imgUrl: "+imageUrl+" | respDB: "+respDB);
				}
			}else{
				logger.info("[SabjiOnWheels][processDoc2] elements is empty or null");
			}
			
		}catch (Exception e) {
			logger.error("[SabjiOnWheels][processDoc2] Exception: "+e);
		}
	}
	
	private void processDoc3(Document doc,String categoryName,MerchantDo merchantDo,CityDo cityDo){
		try{
			if(doc!=null){
				Element element = doc.getElementById("container");
				if(element!=null){
					DBUtil dbUtil = new DBUtil();
					Elements elements = element.select("div.original-box.proDetail");
					if(elements!=null && elements.size()>0){
						for (Element element2 : elements) {
							
							String imageUrl = "http://www.sabjionwheels.com/"+element2.getElementsByTag("img").attr("src");
							String productName = element2.getElementsByTag("h2").first().getElementsByTag("strong").first().text();
							
							Element element3 = element2.select("span.org-text").first();
							
							String maxPrice =element3.getElementsByTag("strike").first().text().replace("MRP:", "").trim()+" "+ element3.select("span.market_pirce").first().text();
							String sellPrice = element2.select("span.s_pirce").first().text();
							
							String respDB = dbUtil.saveOrUpdateDB(categoryName, "", productName, "", maxPrice, "", sellPrice, imageUrl,cityDo, merchantDo, "");
							logger.info("[SabjiOnWheels][processDoc3] categoryName: "+categoryName+" | productName: "+productName+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | imgUrl: "+imageUrl+" | respDB: "+respDB);
						
						}
					}else{
						logger.info("[SabjiOnWheels][processDoc3] elements is "+(elements!=null?elements.size():elements));
					}
				}else{
					logger.info("[SabjiOnWheels][processDoc3] element is "+element);
				}
			}else{
				logger.info("[SabjiOnWheels][processDoc3] doc is "+doc);
			}
		}catch (Exception e) {
			logger.error("[SabjiOnWheels][processDoc3] Exception: "+e);
		}
	}

	private TreeMap<String, String> getCategories() {

		TreeMap<String, String> mapCategories = null;

		try {
			mapCategories = new TreeMap<String, String>();

			Document doc = this.hitUrlByJsoup("http://www.sabjionwheels.com/");
			
			if(doc!=null){
				Element element1 = doc.select("div.menu-bar-outer").first();
				Element element2 = element1.select("ul.orion-menu").first();
	
				Elements elements = element2.getElementsByTag("a");
				
				if(elements!=null && !elements.isEmpty()){
					for (Element element : elements) {
						String catName = element.text();
						String catUrl = "http://www.sabjionwheels.com"+ element.attr("href");
						mapCategories.put(catName.trim(), catUrl.trim());
					}
				}else{
					logger.info("[SabjiOnWheels][getCategories] elements is null or empty");
				}
			}else{
				logger.info("[SabjiOnWheels][getCategories] document is null");
			}
		} catch (Exception e) {
			logger.error("[SabjiOnWheels][getCategories] Exception: "+e);
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
			logger.error("[SabjiOnWheels][hitUrlByJsoup] Exception: "+e);
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
