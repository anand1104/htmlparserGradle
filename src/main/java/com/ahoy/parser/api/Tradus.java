package com.ahoy.parser.api;

import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.TradusDo;
import org.json.JSONArray;
import org.json.JSONObject;
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class Tradus extends HttpServlet{

	private static final long serialVersionUID = -1588205974888145528L;
	Logger logger = LoggerFactory.getLogger(Tradus.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		PrintWriter out = null;
		String resp = "";
		try{			
			out = response.getWriter();	
			
//			Hit to get cookies
			Connection connection = this.getConnectionViaJsoup("http://tradus.com/", Method.GET, null, false);
			Response res = connection.execute();			
			Map<String,String> mapCookies = res.cookies();	
			String cookie = "";
			for(Map.Entry<String,String> map : mapCookies.entrySet()){
				cookie=cookie+map.getKey()+"="+map.getValue()+";";
			}
			
			
			HashMap<String, Integer> mapCities = this.cityMap();
			
			for(Map.Entry<String, Integer> mapCity:mapCities.entrySet()){
			
				String cityName = mapCity.getKey();
				int cityId = mapCity.getValue();
				
				List<TradusDo> tradusDos = this.setCity(cookie, cityId);
				
					if(tradusDos!=null && tradusDos.size()>0){
						for (TradusDo tradusDo : tradusDos) {

							String locality = tradusDo.getName();
							Map<String, String> map1cookies = this.setCookie(tradusDo, cityName, mapCookies);
							
							if(map1cookies!=null){
								TreeMap<String, TreeMap<String, String>> mapCategories = this.getCategoryMap(map1cookies);
								
								if(mapCategories!=null && mapCategories.size()>0){
									DBUtil dbUtil = new DBUtil();
									MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("tradus");
									if(merchantDo!=null){
									
										for(Map.Entry<String, TreeMap<String, String>> mapCategory:mapCategories.entrySet()){
											String categoryName = mapCategory.getKey();
											TreeMap<String, String> mapSubCategories = mapCategory.getValue();
											for(Map.Entry<String, String> mapSubCategory: mapSubCategories.entrySet()){
												
												String subCatName = mapSubCategory.getKey();
												String subCatUrl = mapSubCategory.getValue();
												logger.info("[Tradus][process] city: "+cityName+" | localaity: "+locality+" | subCatName: "+subCatName+" | subCatUrl: "+subCatUrl);
												
												Document document = this.getConnectionViaJsoup(subCatUrl, Method.GET, map1cookies, false)
																.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0")
																.get();
												
												if(document!=null){
													
													String filterCount = document.getElementsByClass("after_filter_area").text();
													int count = Integer.valueOf( filterCount.trim().contains(" ")?(filterCount.trim().split(" ")[0].matches("[0-9]+")?filterCount.trim().split(" ")[0]:"0"):"0");
													int page = count>24?count/24+(count%24!=0?1:0):1;
													
													String pagingUrl = "";
													for(int j = 0 ; j < page;j++){													
														if(j==0){
															if(page>1){
																pagingUrl = document.getElementById("next").attr("href");
																pagingUrl = "http://tradus.com"+pagingUrl.substring(0, pagingUrl.indexOf("page=")+3);
															}
														}else{
															document = null;
															document = this.getConnectionViaJsoup(pagingUrl+(j+1), Method.GET, map1cookies, false)
																	.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0")
																	.get();
														}
														if(document!=null){
															Elements elements = document.getElementsByClass("skuContainer");
															
															for(Element element:elements){
	
	
																Elements elements1 = element.getElementsByClass("sku__item");
																for (Element element1 : elements1) {
																	String itemName = "";
																	String itemImage = "";
																	String maxPrice="";
																	String offer = "";
																	String sellPrice="";
																	String vendor = "";
																	Elements elements4 = element1.getElementsByClass("item__image");
																	itemImage = elements4.attr("style").substring(elements4.attr("style").indexOf("http"), elements4.attr("style").indexOf("');"));
																	
																	for (Element element3 : elements4) {
																		Elements elements5 = element3.getElementsByTag("span");
																		if(!elements5.isEmpty())
																			offer= elements5.text();
																	}
																	  
																	vendor = element1.getElementsByClass("item__seller").text();
																	itemName = element1.getElementsByClass("item__name").text();
																	sellPrice = element1.getElementsByClass("item__sprice").text();
																	maxPrice = element1.getElementsByClass("item__lprice").text();
//																	String respDB = dbUtil.saveOrUpdateDB(categoryName, subCatName, itemName, "", maxPrice, offer, sellPrice, itemImage, cityName, locality, merchantDo,vendor);
//																	
//																	logger.info("[Tradus][process] city: "+cityName+" | localaity: "+locality+" | productName: "+itemName+" | image: "+itemImage+" | maxprice: "+maxPrice+" | offer: "+offer+" | sellprice: "+sellPrice+" | vender: "+vendor+" | respDB: "+respDB);
																		
																}												
															}
														}										
													}										
												}										
											}
										}
									}else{
										logger.info("[Tradus][process] Merchant detail not found \"tradus\"");
									}			
								}else{
									logger.info("[Tradus][process] cityName:"+cityName+" | cityId: "+cityId+" | mapCategoryWithUrl is null or 0");
								}						
							}else{
								logger.info("[Tradus][process] cityName:"+cityName+" | cityId: "+cityId+" | map1cookies is null");
							}
						
						}
					}else{
						logger.info("[Tradus][process] cityName:"+cityName+" | cityId: "+cityId+" | tradusDos size: "+(tradusDos!=null?tradusDos.size():0));
					}
				break;	
			}
			
			resp="process complete";
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[Tradus][process] Exception: "+e);
		}finally{
			logger.info("[Tradus][process] resp: "+resp);
			out.println(resp);
		}
		
	}
	
	private HashMap<String, Integer> cityMap(){
		HashMap<String, Integer> mapCities = new HashMap<String, Integer>();
		mapCities.put("Bangalore", 41);
		mapCities.put("Faridabad", 174);
		mapCities.put("Ghaziabad", 191);
		mapCities.put("Gurgaon", 207);
		mapCities.put("Mumbai", 382);
		mapCities.put("New Delhi", 412);
		mapCities.put("Noida", 623);
		return mapCities;
	}
	
	private List<TradusDo> setCity(String cookie,int cityId){
		List<TradusDo> tradusDos = null;
		try{
			
			String cityUrl = "http://www.tradus.com/locality_city?city_id="+cityId;
			
			URL url = new URL(cityUrl);
			HttpURLConnection connection1 = (HttpURLConnection)url.openConnection();				
			connection1.setRequestProperty("Accept","*/*");
			connection1.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			connection1.setRequestProperty("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6");
			connection1.setRequestProperty("Connection","keep-alive");
			connection1.setRequestProperty("X-Requested-With","XMLHttpRequest");
			connection1.setRequestProperty("Cookie",cookie);
			
			BufferedReader br = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
			String line = "";
			StringBuilder builder = new StringBuilder();
			while((line=br.readLine())!=null){
				builder.append(line);
			}	
			
			if(builder !=null && !"".equals(builder)){
				
				JSONObject jsonObject = new JSONObject(builder.toString());
				String status = jsonObject.getString("status");
				
				if(status !=null && status.equalsIgnoreCase("true")){
					
					JSONObject jsonObject2 = jsonObject.getJSONObject("data");
					JSONArray jsonArray = jsonObject2.getJSONArray("other_localities");
					tradusDos = new ArrayList<TradusDo>();
					for(int i = 0; i < jsonArray.length();i++){
						
						JSONObject jsonObject3 = (JSONObject)jsonArray.get(i);
						String pincode = jsonObject3.get("pincode").toString();
						String id = jsonObject3.get("id").toString();
						String name = jsonObject3.get("name").toString();
						String latitude = jsonObject3.get("latitude").toString();
						String longitude = jsonObject3.get("longitude").toString();
						
						if(pincode!=null&&!"".equals(pincode) && id!=null&&!id.equals("0") && name!=null&&!"".equals(name) && latitude!=null&&!"".equals(latitude) && longitude!=null&&!"".equals(longitude)){
							TradusDo tradusDo = new TradusDo();
							tradusDo.setId(id);
							tradusDo.setName(name);
							tradusDo.setPincode(pincode);
							tradusDo.setLat(latitude);
							tradusDo.setLng(longitude);
							tradusDos.add(tradusDo);
						}
					}
					
				}else{
					logger.info("[Tradus][setCity] we got invalid status ["+status+"]");
				}
				
			}else{
				logger.info("[Tradus][setCity] Something went wrong to set city");
			}
		}catch (Exception e) {
			logger.error("[Tradus][setCity] Exception: "+e);
		}
		return tradusDos;
	}
	
	private Map<String, String> setCookie(TradusDo tradusDo,String cityName,Map<String,String> mapCookies){
		Map<String, String> map1cookies = null;
		try{
			String setCookie = "http://www.tradus.com/setTradusCookie?cookieName=PINCODE|LOCALITY|LOCALITY_ID|CITY&cookieValue="+tradusDo.getPincode()+"|"+URLEncoder.encode(tradusDo.getName(), "UTF-8")+"|"+tradusDo.getId()+"|"+URLEncoder.encode(cityName, "UTF-8")+"&cookieExpireTime=31536000|31536000|31536000|31536000";
			Connection connection2 = getConnectionViaJsoup(setCookie, Method.GET, map1cookies, false)
				    .header("Accept", "*/*")
				    .header("Accept-Encoding","gzip,deflate,sdch")
				    .header("Accept-Language","en-GB,en-US;q=0.8,en;q=0.6")
				    .header("Connection","keep-alive")
				    .header("X-Requested-With","XMLHttpRequest");
			
			Response res1 = connection2.execute();	
			
			logger.info("[Tradus][setCookie] status code: "+res1.statusCode()+" | status message: "+res1.statusMessage());
			
			map1cookies = res1.cookies();
		}catch (Exception e) {
			logger.error("[Tradus][setCookie] Exception: "+e);
		}
		
		return map1cookies;
	}
	
private TreeMap<String, TreeMap<String, String>> getCategoryMap(Map<String,String> map1cookies){
		
		TreeMap<String, TreeMap<String, String>> mapCategories = null;
		try{
			Connection connection3 = this.getConnectionViaJsoup("http://www.tradus.com/", Method.GET, map1cookies, false)
			.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:34.0) Gecko/20100101 Firefox/34.0");
			
			Response res3 = connection3.execute();
			Document doc = res3.parse();
			Elements elements = doc.getElementsByClass("category-tree-nodes");
			Element element = elements.first();
			Elements elements2 = element.select("ul.catTree__nodes.storeCatTree.fixUL").first().children();
			mapCategories = new TreeMap<String, TreeMap<String,String>>();
			for (Element element2 : elements2) {
				Elements elements3 = element2.children();
				String catName = "";
				for (Element element3 : elements3) {
					
					if(element3.tagName().equals("span")&& element3.attr("class").trim().contains("treeL1")){
						catName = element3.text();
					}	
					
					if(element3.tagName().equals("ul") && element3.attr("class").equals("fixUL")){
						Elements elements4 = element3.children();
						for (Element element4 : elements4) {
							Elements elements5 = element4.children();
							String category = "";
							
							TreeMap<String, String> mapSubCategories = new TreeMap<String, String>();
							for (Element element5 : elements5) {
								
								if(element5.tagName().equals("span") && element5.attr("data-eventcategory").equals("category_menu")){
									category = catName+" - "+element5.text();
								}else if(element5.tagName().equals("ul") && element5.attr("class").equals("catTree__submenu storeCatTree__submenu fixUL")){
									                                                                       
									Elements elements6 = element5.children();
									for (Element element6 : elements6) {
										if(element6.tagName().equals("li")&&element6.attr("class").equals("catTree__item storeCatTree__item")){
																					
											Elements elements7 = element6.children();
											String subCatName = "";
											for (Element element7 : elements7) {
												
												if(element7.tagName().equals("span") && element7.attr("data-eventcategory").equals("category_menu")){
													subCatName = element7.text();
												}
												if(element7.tagName().equals("ul") && element7.attr("class").equals("fixUL")){
													Elements elements8 = element7.children();
													for (Element element8 : elements8) {
														Element element9 = element8.select("a").first();
														String subSubCat = subCatName+" - "+element9.text();
														String subSubCatUrl = "http://www.tradus.com"+element9.attr("href").trim();
														mapSubCategories.put(subSubCat, subSubCatUrl);
													}
												}												
//												if(element7.tagName().equals("a")){
//													String subSubCat = element7.text();
//													String subSubCatUrl = "http://www.tradus.com"+element7.attr("href").trim();
//													mapSubCategories.put(subSubCat, subSubCatUrl);
//												}
											}
										}
									}
								}								
							}
							if(mapSubCategories!=null && mapSubCategories.size()>0)
								mapCategories.put(category, mapSubCategories);
							
						}
					}
					
				}
			}
			
		}catch (Exception e) {
			logger.error("[Tradus][getCategoryMap] Exception: "+e);
		}
		return mapCategories;
	}
		
	private Connection getConnectionViaJsoup(String url,Method method,Map<String, String> map,boolean ignoreContentType){
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(method)
					.timeout(25000)
					.ignoreContentType(ignoreContentType);
				
			connection = map!=null && map.size()>0?connection.cookies(map):connection;
						
			
		}catch (Exception e) {
			logger.error("[Tradus][getConnectionViaJsoup] Exception: "+e);
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
