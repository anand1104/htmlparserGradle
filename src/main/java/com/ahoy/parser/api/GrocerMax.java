/**
 * 
 */
package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import org.jsoup.Connection;
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
import java.util.HashMap;
import java.util.Map;

/**
 * @author Vivek
 *
 */
@SuppressWarnings("serial")
@WebServlet("/grocermax")
public class GrocerMax extends HttpServlet{

	Logger logger = LoggerFactory.getLogger(GrocerMax.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String resp ="";
		PrintWriter out = null;
		try{
			out = response.getWriter();
			MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("grocermax");
			CityDo cityDo = new CityDaoImpl().getByName("Gurgaon");
			
			if(merchantDo!=null){
				
				Map<String, Map<String, String>> mapCat =this.categories();
				logger.info("[GrocerMax][process] mapCat size: "+(mapCat!=null?mapCat.size():null));
				
				DBUtil dbUtil = new DBUtil();
				
				for(Map.Entry<String, Map<String, String>> mapcat: mapCat.entrySet()){
					String catName = mapcat.getKey();
					Map<String, String> mapSubCat = mapcat.getValue();
					logger.info("[GrocerMax][process] catName:"+catName+" | mapSubCat size: "+(mapSubCat!=null?mapSubCat.size():null));
					
					for(Map.Entry<String, String> mapsubcat:mapSubCat.entrySet()){
						String subCatName = mapsubcat.getKey();
						String subCatUrl = mapsubcat.getValue();
						Document doc = this.hitUrlByJsoup(subCatUrl+"?limit=all");
						if(doc!=null && !doc.getElementsByClass("products-grid").isEmpty()){
							Element element = doc.getElementsByClass("products-grid").first();
							Elements elements = element.getElementsByClass("item");
							for (Element element2 : elements) {
								
								Elements elesAvail = !element2.getElementsByClass("product-addcart").isEmpty()?element2.getElementsByClass("product-addcart").first().getElementsByClass("availability"):null;
								boolean isAvail = elesAvail!=null && elesAvail.isEmpty()?Boolean.TRUE:Boolean.FALSE;
								
											
								Element element3 = !element2.getElementsByClass("product-info").isEmpty()?element2.getElementsByClass("product-info").first():null;
								if(element3!=null){
									Element priceElement = element3!=null? !element3.getElementsByClass("product-price").isEmpty()?element3.getElementsByClass("product-price").first():null:null;
									
									if(priceElement!=null){
									
										Elements elements2 = element2.getElementsByClass("product-image");
										String image="";
										if(!elements2.isEmpty()){
											Elements elements3 = elements2.first().getElementsByTag("img");
											if(elements3!=null && !elements3.isEmpty()&& elements3.size()>0){
												image = elements3.first().attr("src");
											}
										}
										
										
										String productId = !priceElement.getElementsByClass("old-price").isEmpty()?!priceElement.getElementsByClass("old-price").first().getElementsByClass("price").isEmpty()?priceElement.getElementsByClass("old-price").first().getElementsByClass("price").attr("id"):"":"";
										productId = productId!=null && productId.length()>0?productId.substring(productId.lastIndexOf("-")+1).trim():"";
										
										
										String mrp = !priceElement.getElementsByClass("old-price").isEmpty()?!priceElement.getElementsByClass("old-price").first().getElementsByClass("price").isEmpty()?priceElement.getElementsByClass("old-price").first().getElementsByClass("price").first().text().substring(1).trim():"":"";
										String sp=null;
										
										Elements elements3 = priceElement.getElementsByClass("special-price");
										if(!elements3.isEmpty()){
											Elements elements4 = elements3.first().getElementsByClass("special-price");
											if(!elements4.isEmpty()){
												sp = elements4.first().text();
												if(sp!=null &&!"".equals(sp.trim())){
													sp = sp.substring(1);
												} else{
													sp = mrp;
												}
											}else{
												sp = mrp;
											}
										}else{
											sp = mrp;
										}
										
//										try{
//											sp = !priceElement.getElementsByClass("special-price").isEmpty()?!priceElement.getElementsByClass("special-price").first().getElementsByClass("special-price").isEmpty()?priceElement.getElementsByClass("special-price").first().getElementsByClass("special-price").first().text().substring(1).trim():null:null;
//										}catch (Exception e) {
//											logger.error("priceElement: "+priceElement);
//											throw e;
//										}
										
										String offer = !priceElement.getElementsByClass("yousave").isEmpty()?priceElement.getElementsByClass("yousave").first().text():"";
										
										Element element4 = !element3.getElementsByClass("product-name").isEmpty()?element3.getElementsByClass("product-name").first().getElementsByTag("a").first():null;
										String productName = element4!=null? (!element4.getElementsByClass("p_brand").isEmpty()?element4.getElementsByClass("p_brand").first().text().trim()+" ":"")+(!element4.getElementsByClass("p_name").isEmpty()?element4.getElementsByClass("p_name").first().text().trim():null):null;
										
										String varient = element4!=null?!element4.getElementsByClass("p_pack").isEmpty()?element4.getElementsByClass("p_pack").first().text().trim():"":"";
										varient = (varient!=null && varient.contains("|"))?varient.split("\\|")[1].trim():varient;
										
										if(isAvail && productName!=null&& productId!=null && productId.trim().matches("[0-9]+")){
//											String respDB = dbUtil.saveOrUpdateByPIdAndVId(catName, subCatName, productName, varient, mrp, offer, sp, image, cityDo, merchantDo, "", Long.valueOf(productId.trim()), (long)0, "");
//											String respDB = dbUtil.saveOrUpdateDB(catName, subCatName, productName, varient, mrp, offer, sp, image, cityDo, merchantDo, "");
											logger.info("[GrocerMax][process] catName: "+catName+" | subCatName:"+subCatName+" | productName: "+productName+" | varient: "+varient+" | mrp: "+mrp+" | offer: "+offer+" | sp: "+sp+" | image: "+image+" | respDB: ");
										}else{
											logger.info("[GrocerMax][process] catName: "+catName+" | subCatName:"+subCatName+" | productName: "+productName+" | varient: "+varient+" | mrp: "+mrp+" | offer: "+offer+" | sp: "+sp+" | image: "+image+" | availablity: Out of stock");
										}										
									}else{
										logger.info("[GrocerMax][process] catName: "+catName+" | subCatName:"+subCatName+" | product Price not found");
									}
								}else{
									logger.info("[GrocerMax][process] catName: "+catName+" | subCatName:"+subCatName+" | product Detail not found");
								}							
							}
						}else{
							logger.info("[GrocerMax][process] catName: "+catName+" | subCatName:"+subCatName+" | doc or product-grid is empty");
						}
						
					}
				}
				resp="Process-Complete";
			}else{
				logger.info("[GrocerMax][process] Merchant not found \"grocermax\"");
				resp="Merchant not found";
			}			
		}catch (Exception e) {
			resp ="Internal Error";
			logger.error("[GrocerMax][process] Exception: "+e+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			out.println(resp);
			logger.info("[GrocerMax][process] ");
		}
	}
	
	private Map<String, Map<String, String>> categories(){
		Map<String, Map<String, String>> mapCat = new HashMap<String, Map<String,String>>();
		
		try{
			Connection con = Jsoup.connect("http://grocermax.com").timeout(25000);
			Response response = con.execute();
			Document doc = response.parse();
			Element element = doc.getElementById("nav");
			
			Elements elements = element.children();
			for (Element element2 : elements) {
				Elements elements2 = element2.children();
				if(elements2!=null && elements2.size()==2){
					String catName =  elements2.get(0).getElementsByTag("span").first().text().trim();
					Map<String, String> mapSubCat = new HashMap<String, String>();
					if(catName!=null){
						if(catName.trim().equalsIgnoreCase("Frozen")|| catName.trim().equalsIgnoreCase("non-veg")||catName.trim().equalsIgnoreCase("family care")||catName.trim().equalsIgnoreCase("home care") || catName.trim().equalsIgnoreCase("home needs")){
							Element element3 = elements2.get(1).getElementsByClass("catagory_children").first();
							Elements elements3 = element3.children();
							for (Element element4 : elements3) {
								String subCatName = element4.getElementsByTag("a").first().getElementsByTag("span").get(0).text();
								String subCatUrl = element4.getElementsByTag("a").first().attr("href");
								mapSubCat.put(subCatName, subCatUrl);
							}
							
						}else{
							Elements elements3 = elements2.get(1).children().get(0).children().get(0).children().get(0).children();
							for (Element element3 : elements3) {
								String subCatName = element3.getElementsByTag("a").first().getElementsByTag("span").get(0).text();
								String subCatUrl = element3.getElementsByTag("a").first().attr("href");
								mapSubCat.put(subCatName, subCatUrl);
							}
						}
						mapCat.put(catName, mapSubCat);
					}else{
						
					}
					
				}
			}
		}catch (Exception e) {
			logger.error("[GrocerMax][categories] Exception: "+e+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return mapCat;
	}
	
	private Document hitUrlByJsoup(String url){
		Document document = null;
		
		try{
			document = Jsoup.connect(url)
							.timeout(25000)
							.get();
			
		}catch (Exception e) {
			logger.error("[GrocerMax][hitUrlByJsoup]["+url+"] document:"+document+" Exception: "+e);
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
