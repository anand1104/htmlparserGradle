package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
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


public class NatureBasket extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(NatureBasket.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String resp="";
		PrintWriter out = null;
		try {
			
			out = response.getWriter();
			TreeMap<String, String> map = this.citymap();
			
			for(Map.Entry<String, String> mapEntry:map.entrySet()){				
				CityDo  cityDo = new CityDaoImpl().getByName(mapEntry.getValue());
//				Set City and get cookie
				String setCity ="http://www.naturesbasket.co.in/Handler/PinCodePopUpHandler.ashx?action=SUBMIT&PinCode=&MID=23c09df6-eb5f-49c2-b8a7-48a42be26ed9&IsLocCode=true&LocCode="+mapEntry.getKey()+"&LocName="+mapEntry.getValue();
				Connection connection1 = this.getConnectionViaJsoup(setCity, null, true);			
				Response response1 = connection1.execute();
				Map<String, String> mapResCookies = response1.cookies();				
				
//				GetCategories
				connection1=this.getConnectionViaJsoup("http://www.naturesbasket.co.in", mapResCookies, false);				
				Document doc = connection1.get();
				
				if(doc!=null){
					
					HashMap<String, HashMap<String, String>> mapCategories= this.getCategories(doc);
					
					if(mapCategories!=null && mapCategories.size()>0){
						MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("naturebasket");
						if(merchantDo!=null){
							for(Map.Entry<String, HashMap<String, String>> mapCategory:mapCategories.entrySet()){
								
								String categoryName = mapCategory.getKey();
								HashMap<String, String> mapSubCategories = mapCategory.getValue();
								
								for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
									
									String subCatName = mapSubCategory.getKey();
									String subCatUrl = mapSubCategory.getValue();
										
									connection1 = this.getConnectionViaJsoup(subCatUrl, mapResCookies, false);
									Document doc1 = connection1.get();
									if(doc1!=null){
										this.processDoc(doc1, mapResCookies, categoryName, subCatName,mapEntry.getValue(),merchantDo,cityDo);
										logger.info("[NatureBasket][process]["+mapEntry.getValue()+"] categoryName:"+categoryName+" | subCatName: "+subCatName+" | subCatUrl:"+subCatUrl+" | process is complete");
									}else{
										logger.info("[NatureBasket][process]["+mapEntry.getValue()+"] categoryName:"+categoryName+" | subCatName: "+subCatName+" | subCatUrl:"+subCatUrl+" | doc1 is null");
									}								
								}
							}
						}else{
							logger.info("[NatureBasket][process] merchant detail not found for \"naturebasket\"");
						}
					}else{
						logger.info("[NatureBasket][process]["+mapEntry.getValue()+"] mapCategories is null or 0");
					}
					
					
				}else{
					logger.info("[NatureBasket][process]["+mapEntry.getValue()+"] document is null");
				}				
			}
			
			resp="process complete";
		} catch (Exception e) {
			resp = "Internal Server error";
			logger.error("[NatureBasket][process] Exception: "+e);
		}finally{
			out.println(resp);
			logger.info("[NatureBasket][process] resp: "+resp);
		}	
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
			logger.error("[NatureBasket][hitUrlByJsoup] Exception: "+e);
		}
		return connection;				
	}
	
	private TreeMap<String, String> citymap(){
		TreeMap<String, String> map = null;
		try{
			map = new TreeMap<String, String>();
			map.put("01Mumbai", "Mumbai");
			map.put("02Pune", "Pune");
			map.put("03Bengaluru", "Bengaluru");
			map.put("04Delhi", "Delhi");
			map.put("05Gurgaon", "Gurgaon");
			map.put("06Noida", "Noida");
			map.put("07Hyderabad", "Hyderabad");
		}catch (Exception e) {
			logger.error("[NatureBasket][citymap] Exception: "+e);
		}
		return map;
	}
	
	private HashMap<String, HashMap<String, String>> getCategories(Document doc){
		
		HashMap<String, HashMap<String, String>> getCategories = null;	
		
		try{		
			Element element = doc.getElementById("nav");
			Element element2 = element!=null?element.select("ul.sf-menu").first():null;
			Elements elements = element2!=null?element2.select("li.current"):null;
			
			if(elements!=null&&!elements.isEmpty()){
				
				getCategories = new HashMap<String, HashMap<String,String>>();
				
				for (Element element3 : elements) {
					Element element4 = element3.getElementsByTag("a").first();
					String categoryName= element4.text();
					
					Element element5 = element3.select("ul.pulldownul").first();
					Elements elements2 = element5!=null?element5.children():null;
					
					HashMap<String, String> mapSubCategories = new HashMap<String, String>();
					
					if(elements2!=null &&!elements2.isEmpty()){
						
						for (Element element6 : elements2) {
							
							Element element7 = element6.getElementsByTag("a").first();							
							String subCatName = element7.text();
							String subCatUrl = element7.attr("href");
							Element element8 = element6.select("ul.pulldownul").first();
							
							if(element8!=null&&!"".equals(element8)){
								
								Elements elements3 = element8.getElementsByTag("li");
								
								for (Element element9 : elements3) {
									
									Element element10 = element9.getElementsByTag("a").first();
									String subSubCatName = element10.text();
									String subSubCatUrl = element10.attr("href");
									mapSubCategories.put(subSubCatName, subSubCatUrl);
									
								}								
							}else{
								
								String subSubCatName = subCatName;
								String subSubCatUrl = subCatUrl;
								mapSubCategories.put(subSubCatName, subSubCatUrl);
								
							}							
						}
					}else{
						logger.info("[NatureBasket][getCategories] categoryName: "+categoryName+" | No sub category");
					}
					if(mapSubCategories!=null&&mapSubCategories.size()>0)
						getCategories.put(categoryName, mapSubCategories);				}
			}else{
				logger.info("[NatureBasket][getCategories] elements is empty or null");
			}					
		
		}catch (Exception e) {
			logger.info("[NatureBasket][getCategories] Exception: "+e);
		}
		return getCategories;
	}

	private void processDoc(Document doc,Map<String, String> map,String catName,String subCatname,String cityName,MerchantDo merchantDo,CityDo cityDo){
		
		try{
			
			Elements elements3 = doc.getElementsByTag("script");
			
			if(elements3!=null&&!elements3.isEmpty()){
				DBUtil dbUtil = new DBUtil();
				for (Element element9 : elements3) {
					if(element9.toString().contains("#ctl00_ContentPlaceHolder1_ctl00_ctl02_Showcase") 
							|| element9.toString().contains("#ctl00_ContentPlaceHolder1_ctl00_ctl03_Showcase")
							|| element9.toString().contains("#ctl00_ContentPlaceHolder1_ctl00_ctl04_Showcase")){

						String jsonString= element9.toString().substring(element9.toString().indexOf("{"), element9.toString().indexOf("}")).replace("{ ShowCaseInputs: {", "");
						jsonString=jsonString.trim().replaceAll(" ", "").trim();
						String[] splt = jsonString.split("\n");
						String inputPara ="";
						for (String string : splt) {
							inputPara+=string.trim();
						}
						String url = "http://www.naturesbasket.co.in/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput={"+inputPara.replaceAll("\"", "%22").replaceAll("\n", "")+"}";
						Connection connection =  this.getConnectionViaJsoup(url, map, false);
						Document doc2 = connection.get();
						if(doc2!=null){
							Element element10 = doc2.select("div.pagercontrol").first();
							
							Elements elements4 = element10.getElementsByClass("pager");
							int size = elements4.isEmpty()?1:elements4.size();
							for(int i=0; i< size ; i++){
								if(i!=0){
									jsonString = jsonString.replace("\"PageNo\":1", "\"PageNo\":"+(i+1))
											.replace("\"SortingValues\":\"\"", "\"SortingValues\":\"LIFO\"")
											.replace("\"ShowViewType\":\"\"", "\"ShowViewType\":\"H\"")
											.replace("\"IsRefineExsists\":true", "\"IsRefineExsists\":false")
											.replace("\"ContentType\":\"A\"", "\"ContentType\":\"B\"");

									inputPara ="";
									splt = null;
									url=null;
									splt = jsonString.split("\n");
									for (String string : splt) {
										inputPara+=string.trim();
									}
									
									url = "http://www.naturesbasket.co.in/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput={"+inputPara.replaceAll("\"", "%22").replaceAll("\n", "")+"}";
									doc2 = null;
									connection =  this.getConnectionViaJsoup(url, map, false);
									doc2 = connection.get();
								}
								
								Element element11 = doc2.select("div.bucketgroup").first();
								Elements elements5 = element11.select("div.bucket");
								for (Element element12 : elements5) {
									String product = element12.select("h4.mtb-title").first().text();
									String imageUrl = !element12.select("div.imgcont").isEmpty()?element12.select("div.imgcont").first().select("img").first().attr("original"):element12.select("img").first().attr("original");
									Element element13 = !element12.select("span.mtb-price").isEmpty()?element12.select("span.mtb-price").first():null;
									String sellPrice = element13!=null?element13.select("span.sp_currencysyb.WebRupee").first().text()+element13.select("span.sp_amt").first().text():"";
									String offer = !element12.select("div.catlog_block").isEmpty()?element12.select("div.catlog_block").first().text():"";
									
									String respDB = dbUtil.saveOrUpdateDB(catName, subCatname, product, "", "", offer, sellPrice, imageUrl, cityDo, merchantDo, "");
									logger.info("[NatureBasket][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | product: "+product+" | imageUrl: "+imageUrl+" | sellPrice: "+sellPrice+" | offer: "+offer+" | respDB: "+respDB);
								}
							}
						}else{
							logger.info("[NatureBasket][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | doc2 is null");
						}					
					}else{
//						System.out.println("[NatureBasket][processDoc]catName: "+catName+" | subCatname: "+subCatname+" #ctl00_ContentPlaceHolder1_ctl00_ctl02_Showcase not found");
					}
				}
			}else{
				logger.info("[NatureBasket][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | elements3 is empty or null");
			}
			
		}catch (Exception e) {
			logger.info("[NatureBasket][processDoc]["+cityName+"] catName:"+catName+" | subCatName:"+subCatname+" | Exception: "+e);
		}
		
	}
			
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
