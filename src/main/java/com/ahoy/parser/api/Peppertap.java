package com.ahoy.parser.api;

import com.ahoy.parser.dao.CityDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.UtilConstants;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Peppertap extends HttpServlet{
	
	private static final long serialVersionUID = -5972050735857792193L;
	Logger logger = LoggerFactory.getLogger(Peppertap.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		PrintWriter out = null;
		String resp = "";
		try{
			out = response.getWriter();
			Map<String,Integer> citiesMap = this.getCityMap();
			Map<String, Map<Integer, String>> categoriesMap = this.getCategories();
			if(citiesMap!=null && citiesMap.size()>0 && categoriesMap!=null && categoriesMap.size()>0){
				DBUtil dbUtil = new DBUtil();
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("peppertap");
				
				if(merchantDo!=null){
					for(Map.Entry<String, Integer> cityMap:citiesMap.entrySet()){
					
						String cityName = cityMap.getKey();
						int cityId = cityMap.getValue();
						CityDo cityDo = new CityDaoImpl().getByName(cityName);
						
						if(cityDo!=null){
							String json1 = getConnectionViaJsoup("http://api.peppertap.com/location/v1/areas/?format=json&city_id="+cityId);
					
							if(json1 !=null && isJSONValid(json1)){
								JSONArray jsonArray = new JSONArray(json1);							
								JSONObject jsonObject = null;								
								if(cityName.equalsIgnoreCase("faridabad")){
									for(int i=0 ; i<jsonArray.length() ; i++){
										jsonObject = jsonArray.getJSONObject(i);
										if(jsonObject.getString("id").trim().equalsIgnoreCase("2699")){
											break;
										}
									}
									jsonObject = jsonObject==null?jsonArray.getJSONObject(0):jsonObject;
								}else if(cityName.equalsIgnoreCase("ghaziabad")){
									for(int i=0 ; i<jsonArray.length() ; i++){
										jsonObject = jsonArray.getJSONObject(i);
										if(jsonObject.getString("id").trim().equalsIgnoreCase("356")){
											break;
										}
									}
									jsonObject = jsonObject==null?jsonArray.getJSONObject(0):jsonObject;
								}else{
									jsonObject = jsonArray.getJSONObject(0);
								}
								
								
								String locId = jsonObject.getString("id");
								String zoneId = jsonObject.getString("zone_id");
								String locality = jsonObject.getString("area");
							
//								Map<String, Map<Integer, String>> getCategories = getCategories(); 
							
								for(Map.Entry<String, Map<Integer, String>> map : categoriesMap.entrySet()){
									String[] splt = map.getKey().split("\\|");
									String catName = splt[0];
									String catId = splt[1];
								
									for(Map.Entry<Integer, String> subCatMap:map.getValue().entrySet()){
										Integer subCatId = subCatMap.getKey();
										String subCatName = subCatMap.getValue();
									
										String json2 = getConnectionViaJsoup("http://api.peppertap.com/user/shop/"+subCatId+"/products/?zone_id="+zoneId);
									
										if(json2!=null && isJSONValid(json2)){
											JSONObject jsonObject2 = new JSONObject(json2);
											String status = jsonObject2.getString("status");
											
											if(status!=null && status.trim().equalsIgnoreCase("ok")){
												JSONArray jsonArray2 = jsonObject2.getJSONArray("pl");
												
												if(jsonArray2!=null &&jsonArray2.length()>0){
													for(int i=0 ; i<jsonArray2.length() ; i++){
														JSONObject jsonObject3 = jsonArray2.getJSONObject(i);
														String productName = jsonObject3.getString("tle");
														String link = "http://shop.peppertap.com/categories/"+catName.toLowerCase().replaceAll(" ", "-")+"/"+subCatName.toLowerCase().replaceAll(" ", "-")+"/";
	//													String type = jsonObject3.getString("typ");
														JSONArray jsonArray3 = jsonObject3.getJSONArray("ps");
													
														if(jsonArray3!=null && jsonArray3.length()>0){
															for( int j=0 ; j<jsonArray3.length() ; j++){
																JSONObject jsonObject4 = jsonArray3.getJSONObject(j);
																int id =  jsonObject4.getInt("id");
																String uid = jsonObject4.getString("uid");
																String variant = jsonObject4.getString("da");
																String sp = jsonObject4.getString("sp");
																String sst = jsonObject4.getString("sst");
																String mrp = jsonObject4.getString("mrp");
																String image = "http://peppertap.s3.amazonaws.com/images/product/"+uid+"/0/"+uid+"_300x300.jpg";
																String respDB = dbUtil.saveOrUpdateByPIdAndVId(catName, subCatName, productName, variant, mrp, "", sp, image, cityDo, merchantDo, "", Long.valueOf(id), 0, "",(short)1,link);
																logger.info("[Peppertap][process] city:"+cityName+"("+cityId+") locality:"+locality+"("+locId+","+zoneId+") | cat:"+catName+"("+catId+") | subcat: "+subCatName+"("+subCatId+") | productName: "+productName+" | vid: "+id+" | variant: "+variant+" | mrp: "+mrp+" | sp: "+sp+" | sst: "+sst+" | uid: "+uid+" | link: "+link+" | respDB: "+respDB);
															}
														}else{
															logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | locality: "+locality+"("+locId+","+zoneId+") | cat: "+catName+"("+catId+") | subCat: "+subCatName+"("+subCatId+") | jsonArray3: "+(jsonArray3!=null?jsonArray3.length():null));
														}											
													}
												}else{
													logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | locality: "+locality+"("+locId+","+zoneId+") | cat: "+catName+"("+catId+") | subCat: "+subCatName+"("+subCatId+") | jsonArray2: "+(jsonArray2!=null?jsonArray2.length():null));
												}
											}else{
												logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | locality: "+locality+"("+locId+","+zoneId+") | cat: "+catName+"("+catId+") | subCat: "+subCatName+"("+subCatId+") | status is "+status);
											}
										}else{
											logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | locality: "+locality+"("+locId+","+zoneId+") | cat: "+catName+"("+catId+") | subCat: "+subCatName+"("+subCatId+") | json is not found or invalid");
										}
									}
								}
								String cityResp = "Process Complete";
								
									URL url = UtilConstants.class.getResource("/parser.properties");					
									Properties properties = new Properties();
									properties.load(url.openStream());
									
									String saveRes = UtilConstants.saveFileOnServer(properties,cityDo, merchantDo);
									if(saveRes.startsWith("Success")){	
										String onlineUrl = properties.getProperty("onlineurl");
										onlineUrl=onlineUrl.replace("<MERCHANT-NAME>", UtilConstants.pathVariable(merchantDo.getMerchantName(), (short)1))
												.replace("<CITY-NAME>", UtilConstants.pathVariable(cityDo.getCityName(), (short)1))+URLEncoder.encode(saveRes.split("\\|")[1].trim(), "UTF-8");
										
										cityResp +=" | saveRes: "+saveRes.split("\\|")[0]+" | onlineHitRes: "+UtilConstants.processURL(onlineUrl);
									}else{
										cityResp +=" | saveRes: "+saveRes;
									}
								
								
								logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | cityResp: "+cityResp);
								
							}else{
								logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | json invalid or not found");
							}
						}else{
							logger.info("[Peppertap][process] city: "+cityName+"("+cityId+") | City not found in DB");
						}					
					}
					resp="Finished!!";
				}else{
					resp = "Merchant not found in DB";
				}			
			}else{
				resp = (citiesMap!=null && citiesMap.size()>0?"Categories not found":"Cities not found");
			}
			
		}catch (Exception e) {
			resp="Internal Server Error";
			logger.error("[Peppertap][process] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			logger.info("[Peppertap][process] resp: "+resp);
			out.println(resp);
		}		
	}
	
	private Map<String,Integer> getCityMap(){
		Map<String,Integer> map = new HashMap<String, Integer>();
		try{
			String json = getConnectionViaJsoup("http://api.peppertap.com/location/v1/cities/?format=json");
			if(json!=null && isJSONValid(json)){
				JSONArray jsonArray = new JSONArray(json);
				for(int i=0 ; i<jsonArray.length() ; i++){
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String id = jsonObject.getString("id");
					String name = jsonObject.getString("name");
					map.put(name, Integer.valueOf(id));
					
				}
			}else{
				logger.info("[Peppertap][getCityMap] json is not valid");
			}
						
		}catch (Exception e) {
			logger.error("[Peppertap][getCityMap] Exception: "+GetStackElements.getRootCause(e, Peppertap.class.getName()));
		}
		return map;
	}
	

	private Map<String, Map<Integer, String>> getCategories(){
		Map<String, Map<Integer, String>> categoriesMap = new HashMap<String, Map<Integer,String>>();
		try{
			
			String json = getConnectionViaJsoup("http://api.peppertap.com/user/shop/categories/?zone_id=");
			
			if(json!=null && isJSONValid(json)){
				JSONObject jsonObject = new JSONObject(json);
				String status = jsonObject.getString("status");
				if(status!=null && status.trim().equalsIgnoreCase("ok")){
					JSONArray jsonArray = jsonObject.getJSONArray("categories");			
					for(int i=0; i<jsonArray.length() ; i++){
						JSONObject jsonObject2 =jsonArray.getJSONObject(i);
						String catId = jsonObject2.getString("id");
						String catName = jsonObject2.getString("name");				
						JSONArray jsonArray2 = jsonObject2.getJSONArray("children");
						if(jsonArray2!=null && jsonArray2.length()>0){
							Map<Integer, String> map = new HashMap<Integer, String>();
							for(int j=0 ; j<jsonArray2.length() ; j++){
								JSONObject jsonObject3= jsonArray2.getJSONObject(j);
								String subCatId = jsonObject3.getString("id");
								String subCatName = jsonObject3.getString("name");
								map.put(Integer.valueOf(subCatId.trim()), subCatName);
							}
							categoriesMap.put(catName+"|"+catId, map);
						}
					}
				}else{
					logger.info("[Peppertap][getCategories] status is "+status);
				}
			}else{
				logger.info("[Peppertap][getCategories] invalid json");
			}
			
			
		}catch (Exception e) {
			logger.error("[Peppertap][getCategories] Exception: "+GetStackElements.getRootCause(e, Peppertap.class.getName()));
		}
		return categoriesMap;
	}
	
	private boolean isJSONValid(String jsonString) {
	    try {
	        new JSONObject(jsonString);
	    } catch (JSONException ex) {
	        try {
	            new JSONArray(jsonString);
	        } catch (JSONException ex1) {
	            return false;
	        }
	    }
	    return true;
	}
	
	private String getConnectionViaJsoup(String url){
		String json = null;	
		try{
			
			Document doc = Jsoup.connect(url)
					.header("Content-Type", "application/json")
					.ignoreContentType(true)
					.timeout(10000).get();
			
			if(doc!=null){
				json = doc.body().ownText();
			}			
		}catch (Exception e) {
			logger.error("[Peppertap][getConnectionViaJsoup] Exception: "+e);
		}
		return json;				
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
}
