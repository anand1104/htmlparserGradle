package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
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
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@WebServlet("/zopnow")
public class Zopnow extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger logger = LoggerFactory.getLogger(Zopnow.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp = null;
		try{
			out = response.getWriter();
			Map<String, String> cityMap = this.citymap();
			if(cityMap!=null && cityMap.size()>0){
				
				String ua = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36";
//				String ua = "Mozilla";
				Map<String, String> cookies=Jsoup.connect("http://www.zopnow.com")
						.header("User-Agent", ua)
						.method(Method.GET)
						.ignoreHttpErrors(Boolean.TRUE)
						.timeout(15000).execute().cookies();
				
				for(Map.Entry<String, String> cityKeyValue: cityMap.entrySet()){
					String city = cityKeyValue.getKey();
					String pincode = cityKeyValue.getValue();					
					Response response1 = JsoupConnection("http://www.zopnow.com/pincode.json",cookies, pincode, ua, (short)1);
					
					if(response1!=null && response1.statusCode()==200){
						String json = response1.body();
						logger.info("[zopnow][process] pincode: "+pincode+" | json"+json);
						
						JSONArray jsonArray = new JSONArray(json);
						JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
						jsonObject = new JSONObject(jsonObject.getString("data"));
						String service = jsonObject.getString("type");

						if(service !=null && service.equalsIgnoreCase("success")){
							MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("Zop Now");
							CityDo cityDo = new CityDaoImpl().getByName(city);
							if(merchantDo!=null && cityDo!=null){
								DBUtil dbUtil = new DBUtil();
								Map<String, Map<String, String>> categoriesMap = this.getCategoriesMap(city, pincode);
								if(categoriesMap!=null && categoriesMap.size()>0){
									for(Map.Entry<String, Map<String, String>> categoryMap:categoriesMap.entrySet()){
										String categoryName = categoryMap.getKey();
										Map<String, String> subCatsMap = categoryMap.getValue();
										if(subCatsMap!=null && subCatsMap.size()>0){
											for(Map.Entry<String, String> subCatMap: subCatsMap.entrySet()){
												String[] splt = subCatMap.getKey().split("\\|");
												String subCatName = splt[0];
												int totalItems = splt[1]!=null && splt[1].trim().matches("[0-9]+")?Integer.valueOf(splt[1].trim()):0;
												String url = subCatMap.getValue();
												
												int pages = totalItems>21?(totalItems%21==0?totalItems/21:(totalItems/21+1)):1;
												for(int k = 0 ; k < pages ; k++){
													if(k==0){
														String catUrl = "http://www.zopnow.com"+url;
														Response response2 = this.JsoupConnection(catUrl, pincode, ua, (short)0);
														
														if(response2!=null && response2.statusCode()==200){
															Document doc1 = response2.parse();
															if(doc1!=null){
																try{
																	if(doc1.getElementsByClass("jsProductContainer")!=null && !doc1.getElementsByClass("jsProductContainer").isEmpty()&& doc1.getElementsByClass("jsProductContainer").size()>0){
																		Element  element = doc1.getElementsByClass("jsProductContainer").get(0);
																		Elements elements3 = element.children();
																		if(elements3!=null && !elements3.isEmpty()&& elements3.size()>0){
																			for (Element element2 : elements3) {
																				String productId = element2.attr("data-item-id");
																				String productName = element2.attr("data-item-name");
																				Elements variantElements = element2.getElementsByClass("js-variant");
																				for (Element element3 : variantElements) {
																					String variantId = element3.attr("data-id");
																					String variant = element3.attr("data-name");
																					String mrp = element3.attr("data-mrp");
																					String image = element3.attr("data-image");
																					String stock = element3.attr("data-stock");
																					String discount = element3.attr("data-discount");
																					String productLink = "http://www.zopnow.com/"+element3.attr("data-link");
																					discount = discount!=null && discount.trim().matches("[0-9]+")&&Integer.valueOf(discount.trim())>0?"Rs. "+discount:"";
																					String price = element3.getElementsByClass("price").first().ownText().replaceAll("[^0-9A-Za-z.%]", "");
																					if(stock!=null && stock.trim().matches("[0-9]+")&&Integer.valueOf(stock)>0){
																						String respDB = dbUtil.saveOrUpdateByPIdAndVId(categoryName, subCatName, productName, variant, mrp, discount, price, image, cityDo, merchantDo, "", Long.valueOf(productId), Long.valueOf(variantId), "",(short)1,productLink);
																						logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | pid: "+productId+" | pname: "+productName+" | vid: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | discount: "+discount+" | sp: "+price+" | productLink: "+productLink+" | stock: "+stock +" | respDB: "+respDB);
																					}else{
																						String respDB = dbUtil.saveOrUpdateByPIdAndVId(categoryName, subCatName, productName, variant, mrp, discount, price, image, cityDo, merchantDo, "", Long.valueOf(productId), Long.valueOf(variantId), "",(short)0,productLink);
																						logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | pid: "+productId+" | pname: "+productName+" | vid: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | discount: "+discount+" | sp: "+price+" | productLink: "+productLink+" | stock: "+stock+" | respDB: "+respDB);
																					}														
																				}
																			}
																		}
																	}else{
																		
																	}
																}catch (Exception e) {
																	logger.error(doc1+" \nException: "+GetStackElements.getRootCause(e, getClass().getName()));
																	throw e;
																}
																
															}else{
																logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catUrl: "+catUrl+" | doc1 is null");
															}															
														}else{
															logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catUrl: "+catUrl+" | response2 is null");
														}														
													}else{
														NumberFormat formatter = new DecimalFormat("#0.00");
														String pageurl = "http://www.zopnow.com"+url.replace(".php", ".json")+"?page="+(k+1) ;
														
														Response response2 = this.JsoupConnection(pageurl, pincode, ua, (short)0);
														if(response2!=null && response2.statusCode()==200){															
															
															Document doc1 = response2.parse();															
															String productJson = doc1!=null?doc1.body().ownText():null;
															if(productJson!=null && !"".equals(productJson.trim())){
																try{

																	JSONArray jsonArray2 = new JSONArray(productJson);
																	if(jsonArray2!=null && jsonArray2.length()>0){
																		JSONObject jsonObject2 = new JSONObject(jsonArray2.get(1).toString());
																		JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("data"));
																		JSONArray jsonArray3 = jsonObject3.getJSONArray("products");
																		for(int l=0; l<jsonArray3.length(); l++){
																			JSONObject jsonObject4 = jsonArray3.getJSONObject(l);
																			String productId = jsonObject4.getString("id");
																			String productName = jsonObject4.getString("full_name");
																			JSONArray jsonArray4 = jsonObject4.getJSONArray("variants");
																			for(int m=0 ; m<jsonArray4.length() ; m++){
																				JSONObject jsonObject5 = jsonArray4.getJSONObject(m);
																				String variantId =  jsonObject5.getString("id");
																				String variant = jsonObject5.getJSONObject("properties").getString("Quantity");
																				String mrp = jsonObject5.getString("mrp"); 
																				String discount = jsonObject5.getString("discount");
																				discount = discount!=null && discount.trim().matches("[0-9]+")&&Integer.valueOf(discount.trim())>0?"Rs. "+discount:"";
																				
																				String productLink = "http://www.zopnow.com/"+jsonObject5.getString("url");
																				String stock = jsonObject5.getString("stock");
																				String image = jsonObject5.getJSONArray("images").get(0).toString();
																				String price = String.valueOf(formatter.format(discount!=null && discount.trim().matches("[0-9]+(?:\\.[0-9]+)?")?Float.valueOf(mrp.trim())-Float.parseFloat(discount.trim()):0));
																				if(stock!=null && stock.trim().matches("[0-9]+")&&Integer.valueOf(stock)>0){
																					String respDB = dbUtil.saveOrUpdateByPIdAndVId(categoryName, subCatName, productName, variant, mrp, discount, price, image, cityDo, merchantDo, "", Long.valueOf(productId), Long.valueOf(variantId), "",(short)1,productLink);
																					logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | pid: "+productId+" | pname: "+productName+" | vid: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | discount: "+discount+" | sp: "+price+" | productLink: "+productLink+" | stock: "+stock +" | respDB: "+respDB);
																				}else{
																					String respDB = dbUtil.saveOrUpdateByPIdAndVId(categoryName, subCatName, productName, variant, mrp, discount, price, image, cityDo, merchantDo, "", Long.valueOf(productId), Long.valueOf(variantId), "",(short)1,productLink);
																					logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | pid: "+productId+" | pname: "+productName+" | vid: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | discount: "+discount+" | sp: "+price+" | productLink: "+productLink+" | stock: "+stock+" | respDB: "+respDB);
																				}
																			}
																		}
																		
																	}else{
																		logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | jsonArray: "+(jsonArray!=null?jsonArray.length():null));
																	}
																
																}catch (Exception e) {
																	logger.error("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | pageUrl: "+pageurl+" | JsonException: "+GetStackElements.getRootCause(e, getClass().getName()));
																}
															}else{
																logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | pageUrl: "+pageurl+" | productJson: "+productJson);
															}														
														
														}else{
															logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | subCatName: "+subCatName+" | pageUrl: "+pageurl+" | response2 is null");
														}
														
													}
													Thread.sleep(300);
												}
											}
										}else{
											logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | catName: "+categoryName+" | "+(subCatsMap!=null?subCatsMap.size():null));
										}
									}
									String cityResp = "Process-Complete";
									
//									URL url = UtilConstants.class.getResource("/parser.properties");					
//									Properties properties = new Properties();
//									properties.load(url.openStream());
//									
//									String saveRes = UtilConstants.saveFileOnServer(properties,cityDo, merchantDo);
//									if(saveRes.startsWith("Success")){	
//										String onlineUrl = properties.getProperty("onlineurl");
//										onlineUrl=onlineUrl.replace("<MERCHANT-NAME>", UtilConstants.pathVariable(merchantDo.getMerchantName(), (short)1))
//												.replace("<CITY-NAME>", UtilConstants.pathVariable(cityDo.getCityName(), (short)1))+URLEncoder.encode(saveRes.split("\\|")[1].trim(), "UTF-8");
//										
//										cityResp +=" | saveRes: "+saveRes.split("\\|")[0]+" | onlineHitRes: "+UtilConstants.processURL(onlineUrl);
//									}else{
//										cityResp +=" | saveRes: "+saveRes;
//									}
									
									logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | cityResp: "+cityResp);
								}else{
									logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | categories: not found");
								}
								
							}else{
								logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | merchantDo: "+merchantDo+" | cityDo: "+cityDo);
							}
						}else{
							logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | service unavailable");
						}
					}else{
						logger.info("[Zopnow][process]city: "+city+" | pincode: "+pincode+" | response is null");
					}
					
					Thread.sleep(5000);
				}
				
				resp ="Finished!!";
			}else{
				resp = "cities size is "+(cityMap!=null?cityMap.size():0);
			}
			
		}catch (Exception e) {
			resp = "Internal Error";
			logger.error("[Zopnow][process] "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			logger.info("[Zopnow][process] resp: "+resp);
			out.println(resp);
		}
		
	}
	
	private Response JsoupConnection(String url, String pincode, String userAgent, short mtype){
		Response response = null;
		try{			
			response=Jsoup.connect(url)
			.data("pincode",pincode)
			.ignoreHttpErrors(Boolean.TRUE)
			.header("User-Agent", userAgent)
			.method(mtype>0?Method.POST:Method.GET)
			.header("Connection", "keep-alive")
			.timeout(15000).execute();
		}catch (Exception e) {
			response = null;
			logger.error("[Zopnow][JsoupConnection] pincode: "+pincode+" | url: "+url+" | mtype: "+(mtype>0?"Post":"Get")+" | Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return response;		
	}
	
	private Response JsoupConnection(String url,Map<String, String> cookies, String pincode, String userAgent, short mtype){
		Response response = null;
		try{			
			Connection con =Jsoup.connect(url)
			.data("pincode",pincode)
			.ignoreHttpErrors(Boolean.TRUE)
			.cookies(cookies)
			.header("Accept", "application/json")
			.header("User-Agent", userAgent)
			.header("Connection", "keep-alive")
			.header("Content-Type","application/x-www-form-urlencoded; charset=UTF-8")
			.header("Host", "www.zopnow.com")
			.header("Origin","http://www.zopnow.com")
			.header("X-Requested-With","XMLHttpRequest")
			.referrer("http://www.zopnow.com/")			
			.method(mtype>0?Method.POST:Method.GET)
			.timeout(15000);
			
			
			response = con.execute();
						
		}catch (Exception e) {
			response = null;
			logger.error("[Zopnow][JsoupConnection] "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return response;
		
	}
	
	private Map<String, Map<String, String>> getCategoriesMap(String city,String pincode){
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		try{
			Document doc = Jsoup.connect("http://www.zopnow.com")
					.cookie("pincode", pincode)
					.ignoreHttpErrors(Boolean.TRUE)
					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
					.timeout(15000)
					.get();
			
			logger.info("[Zopnow][process] city: "+city+" | pincode: "+pincode+" | settled pincode: "+doc.getElementsByClass("js-showpincode").first());
			
			Elements menuElements = doc.getElementById("c-menu")!=null?doc.getElementById("c-menu").children():null;
			if(menuElements!=null){
				Element categoryElement  =  menuElements.get(0);
				if(categoryElement!=null){
					Elements catsBlock =  categoryElement.children()!=null?(categoryElement.children()!=null?categoryElement.children().first().children():null):null;
					if(catsBlock!=null && catsBlock.size()>0){
						for(Element catblock: catsBlock){
							Map<String, String> map1 = new HashMap<String, String>();
							Elements catblockChildElements = catblock.children();
							String categoryName = catblockChildElements.get(0).text();
							Elements subCatElements = catblockChildElements.get(1).children();
							for(int i=1 ;i <subCatElements.size() ; i++){
								Element subCatElement = subCatElements.get(i);
								String subcatName = subCatElement.children().get(0).ownText();
								Elements elements = subCatElement.children().get(1).children();
								for(int j=1 ; j<elements.size() ; j++){
									Elements elements2 = elements.get(j).select("a[href]");
									for(Element element:elements2){
										String subSubCatName = subcatName+"-"+element.ownText();
										int totalItems = Integer.valueOf(element.getElementsByTag("small").first().text().trim());
										String subSubUrl = element.attr("href");
										map1.put((subSubCatName+"|"+totalItems), subSubUrl);
									}
								}
							}							
							map.put(categoryName, map1);
						}
					}else{
						logger.info("[Zopnow][process] city: "+city+" | pincode: "+pincode+" | catsBlock: "+(catsBlock!=null?catsBlock.size():null));
					}
				}else{
					logger.info("[Zopnow][process] city: "+city+" | pincode: "+pincode+" | categoryElement is null");
				}
			}else{
				logger.info("[Zopnow][process] city: "+city+" | pincode: "+pincode+" | menuElements is "+menuElements);
			}
		}catch (Exception e) {
			logger.error("[Zopnow][getCategoriesMap] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return map;
	}
	
	private Map<String, String> citymap(){
		Map<String, String> map = new HashMap<String, String>();
		try{
			URL propUrl = Zopnow.class.getResource("/parser.properties");
			Properties properties = new Properties();
			properties.load(propUrl.openStream());			
			String cityProp = properties.getProperty("zopnowcities");
			if(cityProp!=null){
				String[] splt = cityProp.split(";");
				for(String str : splt){
					String[] splt1 = str.split(":");
					if(splt1!=null && splt1.length==2){
						map.put(splt1[0], splt1[1]);
					}
				}
			}else{
				logger.info("[Zopnow][citymap] cityProp is "+cityProp);
			}
		}catch (Exception e) {
			logger.error("[Zopnow][citymap] "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return map;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
}
