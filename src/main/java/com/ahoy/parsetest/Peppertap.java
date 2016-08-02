package com.ahoy.parsetest;

import com.ahoy.parser.util.GetStackElements;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Peppertap {
	public static void main(String[] args){
		try{
			
			String str = "http://uninor.uahoy.mobi/icontest/quiz?key=5&msisdn=8540822004&from=cg&status=SUCCESS";
			System.out.println(URLEncoder.encode(str, "UTF-8"));
//			Map<String, Integer> citiesMap = getCityMap();
//			for(Map.Entry<String, Integer> cityMap:citiesMap.entrySet()){
//				String cityName = cityMap.getKey();
//				int cityId = cityMap.getValue();
////				Document doc = Jsoup.connect("http://api.peppertap.com/location/v1/areas/?format=json&city_id="+cityId)
////						.header("Conten-Type", "application/json")
////						.ignoreContentType(true)
////						.get();
//				
//				String json1 = getConnectionViaJsoup("http://api.peppertap.com/location/v1/areas/?format=json&city_id="+cityId);
//				JSONArray jsonArray = new JSONArray(json1);
//				JSONObject jsonObject = jsonArray.getJSONObject(0);
//				String locId = jsonObject.getString("id");
//				String zoneId = jsonObject.getString("zone_id");
//				String locality = jsonObject.getString("area");			
//				Map<String, Map<Integer, String>> getCategories = getCategories(); 
//				for(Map.Entry<String, Map<Integer, String>> map : getCategories.entrySet()){
//					String[] splt = map.getKey().split("\\|");
//					String catName = splt[0];
//					String catId = splt[1];
//					for(Map.Entry<Integer, String> subCatMap:map.getValue().entrySet()){
//						Integer subCatId = subCatMap.getKey();
//						String subCatName = subCatMap.getValue();
//						
////						Document doc1 = Jsoup.connect("http://api.peppertap.com/user/shop/"+subCatId+"/products/?zone_id="+zoneId)
////								.header("Content-Type", "application/json")
////								.ignoreContentType(true)
////								.timeout(15000)
////								.get();
//						
//						String json2 = getConnectionViaJsoup("http://api.peppertap.com/user/shop/"+subCatId+"/products/?zone_id="+zoneId);
//						JSONObject jsonObject2 = new JSONObject(json2);
//						String status = jsonObject2.getString("status");
//						JSONArray jsonArray2 = jsonObject2.getJSONArray("pl");
////						System.out.println("status: "+status+" | jsonArray2 size: "+jsonArray2.length());
//						for(int i=0 ; i<jsonArray2.length() ; i++){
//							JSONObject jsonObject3 = jsonArray2.getJSONObject(i);
//							String productName = jsonObject3.getString("tle");
//							String type = jsonObject3.getString("typ");
//							JSONArray jsonArray3 = jsonObject3.getJSONArray("ps");
//							for( int j=0 ; j<jsonArray3.length() ; j++){
//								JSONObject jsonObject4 = jsonArray3.getJSONObject(j);
//								int id =  jsonObject4.getInt("id");
//								String uid = jsonObject4.getString("uid");
//								String variant = jsonObject4.getString("da");
//								String sp = jsonObject4.getString("sp");
//								String sst = jsonObject4.getString("sst");
//								String mrp = jsonObject4.getString("mrp");
//								
//								System.out.println("city:"+cityName+"("+cityId+") locality:"+locality+"("+locId+","+zoneId+") | cat:"+catName+"("+catId+") | subcat: "+subCatName+"("+subCatId+") | productName: "+productName+" | vid: "+id+" | variant: "+variant+" | mrp: "+mrp+" | sp: "+sp+" | sst: "+sst+" | uid: "+uid);
//							}
//							
//						}
//								
//					}					
//				}
//				
//				break;
//			}
			
//			Map<String, Map<Integer, String>> getCategories = getCategories(); 
//			for(Map.Entry<String, Map<Integer, String>> map : getCategories.entrySet()){
//				String[] splt = map.getKey().split("\\|");
//				String catName = splt[0];
//				String catId = splt[1];
//				System.out.println(catName+"("+catId+")");
//				for(Map.Entry<Integer, String> subCatMap:map.getValue().entrySet()){
//					Integer subCatId = subCatMap.getKey();
//					String subCatName = subCatMap.getValue();
//					System.out.println("\t--"+subCatName+"("+subCatId+")");
//					
//				}
//				
//			}
//			String loginResp = login();
//			if(loginResp!=null &&!"".equals(login())){
//				JSONObject jsonObject = new JSONObject(loginResp);
//				String status = jsonObject.getString("status");
//				String msg = jsonObject.getString("msg");
//				String sessionId = jsonObject.getString("session_id");
//				System.out.println("status: "+status+" | sessionId: "+sessionId+" | msg: "+msg);
//				Response response  = Jsoup.connect("http://api.peppertap.com/customer/profile/?session_id="+sessionId)
//						.ignoreContentType(true)
//						.header("Content-Type", "application/json")
//						.execute();
//				System.out.println(response.body());
//				
//				Map<String, String> cookiesMap = response.cookies();
//				for(Map.Entry<String, String> cookiemap: cookiesMap.entrySet()){
//					System.out.println("cookie-->"+cookiemap.getKey()+": "+cookiemap.getValue());
//				}
//				
//				Map<String, String> headersMap = response.headers();
//				for(Map.Entry<String, String> headermap: headersMap.entrySet()){
//					System.out.println("header-->"+headermap.getKey()+": "+headermap.getValue());
//				}
//				
//				Document doc = Jsoup.connect("http://shop.peppertap.com/partials/product_page_category.html").get();
//				System.out.println(doc);
//				
//			}else{
//				System.out.println("Invalid Json");
//			}
			
		}catch (Exception e) {
			System.out.println(GetStackElements.getRootCause(e, Peppertap.class.getName()));
		}
	}
	
	private static Map<String,Integer> getCityMap(){
		Map<String,Integer> map = new HashMap<String, Integer>();
		try{
//			Document doc = Jsoup.connect("http://api.peppertap.com/location/v1/cities/?format=json")
//					.header("Content-Type", "application/json")
//					.ignoreContentType(true).get();
			String json = getConnectionViaJsoup("http://api.peppertap.com/location/v1/cities/?format=json");
			JSONArray jsonArray = new JSONArray(json);
			for(int i=0 ; i<jsonArray.length() ; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String id = jsonObject.getString("id");
				String name = jsonObject.getString("name");
				if(name!=null && Arrays.asList("noida","delhi","gurgaon").contains(name.trim().toLowerCase())){
					map.put(name, Integer.valueOf(id));
				}
			}			
		}catch (Exception e) {
			System.out.println("[Peppertap][getCityMap] Exception: "+GetStackElements.getRootCause(e, Peppertap.class.getName()));
		}
		return map;
	}
	

	private static Map<String, Map<Integer, String>> getCategories(){
		Map<String, Map<Integer, String>> categoriesMap = new HashMap<String, Map<Integer,String>>();
		try{
			
//			Document docCat = Jsoup.connect("http://api.peppertap.com/user/shop/categories/?zone_id=")
//					.header("Content-Type", "application/json")
//					.ignoreContentType(true)
//					.execute().parse();
			String json = getConnectionViaJsoup("http://api.peppertap.com/user/shop/categories/?zone_id=");
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
				System.out.println("[Peppertap][getCategories] status is "+status);
			}

		}catch (Exception e) {
			System.out.println("[Peppertap][getCategories] Exception: "+GetStackElements.getRootCause(e, Peppertap.class.getName()));
		}
		return categoriesMap;
	}
	
	private static String getConnectionViaJsoup(String url){
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
			System.out.println("[Peppertap][getConnectionViaJsoup] url: "+url+"Exception: "+e);
		}
		return json;				
	}
	
	private static String login() throws IOException{
		BufferedReader in = null;
		HttpURLConnection conn = null;
		StringBuffer sb = new StringBuffer();
		try{
			String url = "http://api.peppertap.com/secret/customer/login/?format=json";
            URL obj = new URL(url);
            conn = (HttpURLConnection) obj.openConnection();
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            OutputStreamWriter w = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            w.write("GNVqcWlj8QcBTrMZ+UscvqzcJTLZzSasPa2MSriaUx8uf3pHPu/ownx2ArnpIzX9qV17XfTucjVi5TvLtsx00RpH9cp61dxp28k+y/08MTUCAxc5B+TptNmqSO8kFs+qdSCFFvY0ySrFo8y12bMv5dKT1p0GZmhmvMuY3eIQwJE513bZOzh1Ik9mcKaWFSzTSM7/xF27VQao0f7mnelTxJUclfWhvfEZyWa3/J6SuaBJjm1ELiwcQLM8DKhFaVI8gphBiCWBHvsFLJHR5hxv5xPYamjgabSQK4oKfF8B46aKK7/ejnouawevzStE1SJaKdi4aMTvmLwccx3E/6Tb5g==");
            w.close();
            
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            
            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
            }		
		}catch (Exception e) {
			System.out.println(e);
		}finally{
			in.close();
            conn.disconnect();
		}
		return sb.toString();
	}
	
public static String processURL(String address) {
		
		StringBuffer res= new StringBuffer();		     
		try{
			URL url = new URL(address);
		    URLConnection conn = url.openConnection();
		    
		    conn.setConnectTimeout(25000);
		    conn.setReadTimeout(25000);
		    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		    String inputLine;
		 
		    while ((inputLine = br.readLine()) != null) {
		    	res.append(inputLine);
		    }		    
		    br.close();
		     
		}catch(Exception e){
			res.append(e.getMessage());
			
		}
		return res.toString();		 
	}
	
	
}
