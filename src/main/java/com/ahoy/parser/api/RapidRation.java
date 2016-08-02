package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import org.jsoup.Connection;
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

public class RapidRation extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(RapidRation.class);
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out =null;
		try{
			
			out = response.getWriter();
			
			HashMap<String, HashMap<String, String>> categoryMap = getCategory();
			
			if(categoryMap!=null && categoryMap.size()>0){
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("rapidration");
				CityDo  cityDo = new CityDaoImpl().getByName("gurgaon");
				if(merchantDo!=null){
				
					for(Map.Entry<String, HashMap<String, String>> map:categoryMap.entrySet()){
						String categoryName = map.getKey();
						
						HashMap<String, String> subMap = map.getValue();
						
						if(subMap!=null&&subMap.size()>0){
							
							for(Map.Entry<String, String> map1:subMap.entrySet()){
								String subCatName =map1.getKey();
								String subCatUrl = map1.getValue();
								logger.info("[RapidRation][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl+" START");
								Document doc = getConnectionViaJsoup(subCatUrl, null, false).get();
								if(doc!=null){
									Element element = doc.select("div.category-products")!=null?doc.select("div.category-products").first():null;
									Element element2 = element!=null? (element.select("div.pages")!=null?element.select("div.pages").first() : null ): null;
									Elements productGridselements = element!=null? element.select("ul.products-grid"):null;
									
									if(element2!=null){
										Elements elements = element2.getElementsByTag("li");
										if(elements!=null&&elements.size()>0){
											for(int i=0 ; i < elements.size()-1 ; i++){
												if(i!=0){
													String pageUrl = elements.get(i).getElementsByTag("a").first().attr("href");
													doc = getConnectionViaJsoup(pageUrl, null, false).get();
													if(doc!=null){
														element = doc.select("div.category-products").first();
														productGridselements = element.select("ul.products-grid");
														FetchProductSave(categoryName,subCatName,productGridselements,merchantDo,cityDo);
													}
												}else{
													FetchProductSave(categoryName,subCatName,productGridselements,merchantDo,cityDo);
												}
											}
										}else{
											FetchProductSave(categoryName,subCatName,productGridselements,merchantDo,cityDo);
										}
									}else{
										FetchProductSave(categoryName,subCatName,productGridselements,merchantDo,cityDo);
									}								
								}
								
								logger.info("[RapidRation][process] categoryName: "+categoryName+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl+" END");
							}
						}
					}
				}else{
					logger.info("[RapidRation][process] Merchant detail not found for \"rapidration\"");
				}
			}else{
				logger.info("[RapidRation][process] category size is "+(categoryMap!=null?categoryMap.size():0));
			}
			
			
		}catch(Exception e){
			logger.error("[RapidRation][process] Exception: "+e+" | getStacktrace: "+GetStackElements.getRootCause(e, this.getClass().getClass().getName()));
		}finally{
			out.println("Process Complete!!");
		}
	}
	
	private void FetchProductSave(String categoryName,String subCatName, Elements elements,MerchantDo merchantDo,CityDo cityDo){
		
		try{
			if(elements!=null && elements.size()>0){
				DBUtil dbUtil = new DBUtil();
				for (Element element : elements) {
					Elements elements2 = element.select("li.item");
					for (Element element2 : elements2) {
						Element imageElement=element2.select("p.product-image").first();
						Element productElement = element2.select("h2.product-name").first();
						Element priceElement = element2.select("div.price-box").first();
						
						String imageName = imageElement.select("img").attr("src");
						String productName = productElement.text();
//						String url = productElement.getElementsByTag("a").attr("href");
						Element element3 = priceElement.select("span.regular-price").first();
						String mrp="";
						String sp = "";
						if(element3!=null){
							sp = element3.select("span.price").text();
						}else{
							mrp = priceElement.select("p.old-price").first().select("span.price").text();
							sp = priceElement.select("p.special-price").first().select("span.price").text();
						}
						String respDB = dbUtil.saveOrUpdateDB(categoryName, subCatName, productName, "", mrp, "", sp, imageName, cityDo, merchantDo, "");
						logger.info("[RapidRation][FetchProductSave] catName: "+categoryName+" | subCatname: "+subCatName+" | productName: "+productName+" | imageUrl: "+imageName+" | sellPrice: "+sp+" | maxPrice: "+mrp+" | respDB: "+respDB);
					}
				}
			}
		}catch(Exception e){
			logger.error("[RapidRation][FetchProductSave] Exception: "+e+" | getStacktrace: "+GetStackElements.getRootCause(e, this.getClass().getClass().getName()));
		}
		
	}
	
	private HashMap<String, HashMap<String, String>> getCategory(){
		
		HashMap<String, HashMap<String, String>> hashMap = new  HashMap<String, HashMap<String,String>>();
		
		try{
			Document doc = getConnectionViaJsoup("http://www.rapidration.com/", null, false).get();
			
			Element element = doc.getElementById("nav");
			Elements elements = element.children();
			
			
			
			for(int i=1; i<elements.size()-1;i++ ){
				
				Element element2 = elements.get(i);
				Elements elements2 = element2.children();
				
				if(elements2!=null && elements2.size()>=2){
					
					Element catElement = elements2.get(0);					
					Element SubCatelement = elements2.get(1);
					
					String categoryName = catElement.text();					
					Elements elements3 = SubCatelement.children();
					
					HashMap<String, String> subCatMap = new HashMap<String, String>();
					
					for (Element element3 : elements3) {
						Elements elements4 = element3.children();
						
						if(elements4!=null && elements4.size()==1){
							
							Element element4 = elements4.get(0).getElementsByTag("a").first();
							String subCatName = element4.text();
							String subCatUrl = element4.attr("href");
							subCatMap.put(subCatName, subCatUrl);
							
						}else if(elements4!=null && elements4.size()>1){
							
							Element element4 = elements4.get(0).getElementsByTag("a").first();
							String subCat = element4.text();
							Element element5 = elements4.get(1);
							Elements elements5 = element5.children();
							
							for (Element element6 : elements5) {
								
								Element element7 = element6.getElementsByTag("a").first();
								String subCatName = subCat+" - "+element7.text();
								String subCatUrl = element7.attr("href");
								subCatMap.put(subCatName, subCatUrl);
								
							}							
						}
					}					
					hashMap.put(categoryName, subCatMap);
				}
			}
		}catch(Exception e){
			logger.error("[RapidRation][getCategory] Exception: "+e+" | getStacktrace: "+GetStackElements.getRootCause(e, this.getClass().getClass().getName()));
		}
		return hashMap;
	}
	
	private Connection getConnectionViaJsoup(String url,Map<String, String> map,boolean ignoreContentType){
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.ignoreContentType(ignoreContentType);
				if(map!=null && map.size()>0)
					connection = connection.cookies(map);
						
			
		}catch (Exception e) {
			logger.error("[RapidRation][getConnectionViaJsoup] Exception: "+e);
		}
		return connection;				
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
