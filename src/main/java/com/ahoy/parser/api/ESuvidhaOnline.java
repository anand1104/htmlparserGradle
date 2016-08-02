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
import java.util.Map;
import java.util.TreeMap;

public class ESuvidhaOnline extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(ESuvidhaOnline.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		try{
			TreeMap<String, String> mapCategory = this.mapCategory();
			
			if(mapCategory!=null){
				CityDo  cityDo = new CityDaoImpl().getByName("delhi");
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("esuvidhaonline");
				
				if(merchantDo!=null){
					for(Map.Entry<String, String> maEntry:mapCategory.entrySet()){
						String categoryName = maEntry.getKey();
						String catUrl = maEntry.getValue();
						Document doc = this.hitUrlByJsoup(catUrl);
						
						if(doc!=null){
							
							if(categoryName!=null && !categoryName.equalsIgnoreCase("New Arrivals")){
								String nextUrl ="";
								do{
									nextUrl = this.getNextUrl(doc);
									this.processDoc(categoryName, doc,merchantDo,cityDo);
									if(nextUrl!=""){
										doc = null;
										doc = nextUrl!=""? this.hitUrlByJsoup(nextUrl):doc;							
										System.out.println("[ESuvidhaOnline][process] categoryName: "+categoryName+" | catUrl: "+catUrl+" | nextUrl: "+nextUrl);
									}
								}while(nextUrl!="");	
								
							}else{
								this.processDoc(categoryName, doc, merchantDo,cityDo);
							}
							
						}else{
							logger.info("[ESuvidhaOnline][process] categoryName: "+categoryName+" | catUrl: "+catUrl+" | doc: "+doc);
						}
					}
				}else{
					logger.error("[ESuvidhaOnline][process] merchant detail not found \"esuvidhaonline\"");
				}
			}
		}catch (Exception e) {
			logger.error("[ESuvidhaOnline][process] Exception: "+e);
			e.printStackTrace();
		}
	}
	
	
	private void processDoc(String categoryName,Document doc,MerchantDo merchantDo,CityDo cityDo){
		try{
			
			Element elementProGrid = null;
			
				elementProGrid = doc.select("ul.products-grid.row").first();
					
			if(elementProGrid!=null){
				DBUtil dbUtil = new DBUtil();
				Elements elementsProduct = elementProGrid.select("li.col-sm-4");
				if(!elementsProduct.isEmpty()){
					for (Element element : elementsProduct) {
						String productName = "";
						String imageUrl = "";
						String maxPrice = "";
						String sellPrice = "";
						String offer = "";
						String weight = "";
						productName = element!=null? (!element.select("h2.product-name").isEmpty()? element.select("h2.product-name").first().text():element.select("h3.product-name").first().text()):"";
						Element element2 = element!=null?element.select("div.product-image").first():null;
						Element element3 = element2!=null? (!element2.select("img.primary-image").isEmpty()?element2.select("img.primary-image").first():element2.getElementsByTag("img").first()):null;
						imageUrl = element3!=null? element3.attr("src"):"";

						Element elementPrice = element.select("div.price-box").first();
						Element elementOldPrice = elementPrice.select("p.old-price").first();
						Element elementOfferPrice = elementPrice.select("p.special-price").first();
						
						if(elementOldPrice!=null && elementOfferPrice!=null){
							maxPrice = elementOldPrice.select("span.price").text();
							sellPrice = elementOfferPrice.select("span.price").text();
						}else{
							sellPrice = elementPrice.text();
						}
						
						offer = !element.select("div.home-offer").isEmpty()?element.select("div.home-offer").first().text():"";
						weight = !element.select("div.labels.top-right").isEmpty()? element.select("div.labels.top-right").first().text():"";
						
						String respDB = dbUtil.saveOrUpdateDB(categoryName, "", productName, weight, maxPrice, offer, sellPrice, imageUrl,cityDo,merchantDo , "");
						logger.info("[ESuvidhaOnline][processDoc] categoryName: "+categoryName+" | productName: "+productName+" | imageUrl: "+imageUrl+" | maxPrice: "+maxPrice+" | sellPrice: "+sellPrice+" | offer: "+offer+" | weight: "+weight+" | respDB: "+respDB);
						
					}
				}else{
					logger.info("[ESuvidhaOnline][processDoc] categoryName: "+categoryName+" | elementsProduct is empty: "+elementsProduct.isEmpty());
				}
			}else{
				logger.info("[ESuvidhaOnline][processDoc] categoryName: "+categoryName+" | elementProGrid: "+elementProGrid);
			}
			
		
		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private  String getNextUrl(Document doc){
		String nextUrl ="";
		try{
			Element elementPaging = doc.select("div.pages")!=null?doc.select("div.pages").first():null;				
			Elements elementsPages = elementPaging!=null?elementPaging.select("a.button"):null;
			if(elementsPages!=null && !elementsPages.isEmpty()){
				for (Element element : elementsPages) {
					Attributes attributes = element.attributes();
					for (Attribute attribute : attributes) {
						if(attribute.getKey()!=null && attribute.getKey().equals("class")&&attribute.getValue()!=null&&attribute.getValue().equalsIgnoreCase("button next i-next")){
							nextUrl = element.attr("href");
						}
					}
				}
			}
		}catch (Exception e) {
			logger.error("[ESuvidhaOnline][getNextUrl] Exception: "+e);
		}
		return nextUrl;
	}
	
	
	private TreeMap<String, String> mapCategory(){
		
		TreeMap<String, String> mapCategory = new TreeMap<String, String>();
		
		try{
			
			Document doc = this.hitUrlByJsoup("http://www.esuvidhaonline.in/");
			Element element = doc.getElementById("custommenu");
			Elements elements = element.children();
			for (Element element2 : elements) {
				Attributes attributes = element2.attributes();
				for (Attribute attribute : attributes) {
					String key = attribute.getKey();
					String value = attribute.getValue();
					if(key!=null && key.equals("id") && value!=null&&!value.equalsIgnoreCase("menu")){
						Element element3 = element2.getElementById(value);
						Element element4 = element3.getElementsByTag("a").first();
						String categoryName = element4.text();
						String categoryurl = element4.attr("href");

						mapCategory.put(categoryName, categoryurl);
					}
				}
			}
		}catch (Exception e) {
			logger.error("[ESuvidhaOnline][mapCategory] Exception: "+e);
		}
		return mapCategory;
	}
	
	private Document hitUrlByJsoup(String url){
		Document document = null;
		
		try{
			document = Jsoup.connect(url)
							.method(Method.GET)
							.timeout(25000)
							.get();
			
		}catch (Exception e) {
			logger.error("[ESuvidhaOnline][hitUrlByJsoup] Exception: "+e);
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
