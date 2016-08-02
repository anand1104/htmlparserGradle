package com.ahoy.parsetest;

import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.DBUtil;
import com.ahoy.parser.util.SrsVariant;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class Test {
	public static void main(String[] args){
		
		try{
			
			Connection connection= Jsoup.connect("http://www.sangamdirect.com/").timeout(25000);
			
			Response response = connection.execute();
			Map<String, String> responeCookies = response.cookies();
			System.out.println("responeCookies: "+responeCookies.size());
			for(Map.Entry<String, String> respCookie:responeCookies.entrySet()){
				System.out.println(respCookie.getKey()+" : "+respCookie.getValue());
			}
			
//			Map<String, String> responseHeader = response.headers();
//			System.out.println("responseHeader: "+responseHeader.size());
//			for(Map.Entry<String, String> respHeader:responseHeader.entrySet()){
//				System.out.println(respHeader.getKey()+" : "+respHeader.getValue());
//			}
			
			Connection connection1= Jsoup.connect("http://www.sangamdirect.com/city?city=DELHI&redirect=ref")
//					.cookie("PHPSESSID", responeCookies.get("PHPSESSID"))
//					.cookie("cityselection", "DELHI")
					.followRedirects(true)
					.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.103 Safari/537.36")
					.header("Accept-Encoding", "gzip, deflate, sdch")
					.header("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6")
					.header("Connection", "keep-alive")
					.header("Host", "www.sangamdirect.com")
					.header("Upgrade-Insecure-Requests","1")
					.referrer("http://www.sangamdirect.com/")
					.timeout(25000);
			
			Response response1 = connection1.execute();
			System.out.println(response1.url()+" | "+response1.statusCode());
//			System.out.println(response1.cookies());
			
			Map<String, String> co =response1.cookies();
			for(Map.Entry<String, String> mp:co.entrySet()){
				System.out.println(mp.getKey()+"| "+mp.getValue());
			}
			
			
			
			
//			Document doc = Jsoup.connect("C:\\Users\\Vivek\\Downloads\\arc-response-2016 Feb 3 10-50-14.html").get();
//			
//			Document doc = Jsoup.connect("http://nginx.api.askmegrocery.com/api/category")
//					.ignoreContentType(true).get();
//			String json = doc.body().ownText();
//			System.out.println(json);
//			JSONArray jsonArray = new JSONArray(json);
//			for(int i=0 ; i <jsonArray.length() ; i++){
//				JSONObject jsonObject = jsonArray.getJSONObject(i);
//				String AreaID = jsonObject.getString("AreaID");
//				String AreaName = jsonObject.getString("AreaName");
//				String ZonalCode = jsonObject.getString("ZonalCode");
//				System.out.println("AreaID: "+AreaID+" | AreaName: "+AreaName+" | ZonalCode: "+ZonalCode);
//			}
			
				
				
			
//			String ua = "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36";
//			
//			Response response=Jsoup.connect("http://www.zopnow.com")
//					.header("User-Agent", ua)
//					.method(Method.GET)
//					.timeout(15000).execute();
			
//			Map<String, String> map = response.cookies();
			
//			for(Map.Entry<String, String> mp: map.entrySet()){
//				System.out.println(mp.getKey()+": "+mp.getValue());
//			}
			
//			Response response1 = JsoupConnection("http://www.zopnow.com/pincode.json",response.cookies(), "122001", ua, (short)1);
//			
//			Map<String, String> map = response1.cookies();
//			System.out.println(map.size()+" | "+response1.body());
//			TreeMap<String, String> mapCities = citymap();
//			for(Map.Entry<String, String> mapCity:mapCities.entrySet()){
//				
//				String cityKey = mapCity.getKey();
//				String cityValue = mapCity.getValue();
//				if(!cityKey.equals("Gurgaon"))
//					continue;
//				System.out.println(cityKey+" | "+cityValue);
//				
//				String setCityUrl ="http://www.srsgrocery.com/SelectLocation.aspx?Location="+cityValue.trim()+"&RetUrl=http://www.srsgrocery.com/";
//				Connection connection1=null;
//				
//				connection1 = getConnectionViaJsoup(setCityUrl, null, true);
//				Response res = connection1.execute();
//				Map<String, String> mapResCookies = res.cookies();
//				
//				HashMap<String, HashMap<String,String>> mapGetCategories = getCategories(res.parse());
//				
//				if(mapGetCategories!=null && mapGetCategories.size()>0)	{
//					for(Map.Entry<String,HashMap<String,String>> mapGetCategory:mapGetCategories.entrySet()){
//						
//
//						String catName = mapGetCategory.getKey();
//						HashMap<String,String> mapSubCategories = mapGetCategory.getValue();
//						for(Map.Entry<String, String> mapSubCategory:mapSubCategories.entrySet()){
//							String subCatName =mapSubCategory.getKey();
//							String subCatUrl=mapSubCategory.getValue();
//							Document doc1=null;
//							try{
//							do {
//								connection1 = getConnectionViaJsoup(mapSubCategory.getValue(), mapResCookies, false);
//								doc1 = connection1.get();
//							}while (doc1==null);
//							
//							if(doc1!=null){
//								
//								processDoc(doc1, mapResCookies, catName, subCatName,mapCity.getValue(),null,null);
//								System.out.println("[SrsGrocery][process]["+mapCity.getValue()+"] categoryName:"+catName+" | subCatName: "+subCatName+" | subCatUrl:"+subCatUrl+" | process is complete");
//							}
//							}catch (Exception e) {
//								System.out.println("[SrsGrocery][process] subCatName:"+subCatName+" | subCatUrl:"+subCatUrl+" | Exception: "+e);
//							}	
//						}
//					
//						
//					}
//				}
//			}
//			String  str = "a%3A5%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%22dc8b39334b396e21df34f48020f6a40c%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A11%3A%2252.74.90.73%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A13%3A%22Java%2F1.6.0_17%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1432031391%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3B%7Da52df914d7a40f8114479c02099cea7e";
//			System.out.println("str: "+URLDecoder.decode(str,"UTF-8"));
//			String str1 = "lb_session=a%3A7%3A%7Bs%3A10%3A%22session_id%22%3Bs%3A32%3A%224aa6d628ca32f4131a56716a66357682%22%3Bs%3A10%3A%22ip_address%22%3Bs%3A11%3A%2252.74.90.73%22%3Bs%3A10%3A%22user_agent%22%3Bs%3A102%3A%22Mozilla%2F5.0+%28Windows+NT+6.1%29+AppleWebKit%2F537.36+%28KHTML%2C+like+Gecko%29+Chrome%2F42.0.2311.152+Safari%2F537.36%22%3Bs%3A13%3A%22last_activity%22%3Bi%3A1432033036%3Bs%3A9%3A%22user_data%22%3Bs%3A0%3A%22%22%3Bs%3A14%3A%22SESS_CITY_DATA%22%3Ba%3A4%3A%7Bs%3A4%3A%22MQ%3D%3D%22%3Bs%3A6%3A%22Mumbai%22%3Bs%3A4%3A%22Mg%3D%3D%22%3Bs%3A4%3A%22Pune%22%3Bs%3A4%3A%22NA%3D%3D%22%3Bs%3A9%3A%22Hyderabad%22%3Bs%3A4%3A%22OA%3D%3D%22%3Bs%3A5%3A%22Delhi%22%3B%7Ds%3A17%3A%22SESS_CITYSPX_DATA%22%3Ba%3A2%3A%7Bs%3A8%3A%22actualDN%22%3Ba%3A2%3A%7Bs%3A4%3A%22MzI%3D%22%3Bs%3A3%3A%22120%22%3Bs%3A4%3A%22NjQ%3D%22%3Bs%3A3%3A%22120%22%3B%7Ds%3A9%3A%22DisplayDN%22%3Ba%3A4%3A%7Bs%3A4%3A%22MQ%3D%3D%22%3Bs%3A1%3A%220%22%3Bs%3A4%3A%22NA%3D%3D%22%3Bs%3A1%3A%220%22%3Bs%3A4%3A%22MzI%3D%22%3Bs%3A3%3A%22120%22%3Bs%3A4%3A%22NjQ%3D%22%3Bs%3A3%3A%22120%22%3B%7D%7D%7D63f078441282ee19dea37281df9ad4c5";
//			System.out.println("str1: "+URLDecoder.decode(str1,"UTF-8"));
		
		}catch (Exception e) {
			System.out.println(e);
		}
		
	}
	
	private static Response JsoupConnection(String url,Map<String, String> cookies, String pincode, String userAgent, short mtype){
		Response response = null;
		try{			
			Connection con =Jsoup.connect(url)
			.data("pincode",pincode)
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
			e.printStackTrace();
		}
		return response;
		
	}
	
	private static void processDoc(Document doc,Map<String, String> map,String catName,String subCatname,String cityName,MerchantDo merchantDo,CityDo cityDo){
		try{
			
			Elements elements3 = doc.getElementsByTag("script");
			if(!elements3.isEmpty()){
				DBUtil dbUtil = new DBUtil();
				for (Element element : elements3) {
										
					if(element.toString().contains("var objShowCaseInputs")){
						 String scriptString = element.toString();
						String inputPara ="";
						if(scriptString!=null){
							String[] splt = scriptString.split("\n");
							for (String string : splt) {
								if(string.contains("var objShowCaseInputs")){
									inputPara = string.substring(string.indexOf("{"), string.indexOf("}")+1);
								}
							}
						}
						String url = "http://www.srsgrocery.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput="+inputPara.replaceAll("\"", "%22");
						Connection connection =  getConnectionViaJsoup(url, map, false);
						Document doc2 = connection.get();
						Elements elementsPager = doc2.getElementsByClass("pagerdiv");
						int pages = 1;
						if(!elementsPager.isEmpty()){
							for (Element element2 : elementsPager) {
								pages = element2.getElementsByClass("pager").size();
								pages=pages>0?pages:1;
							}
						}						
						for(int i=0 ; i < pages ; i++){
							if(i!=0){
								inputPara = inputPara.replace("\"PageNo\":1", "\"PageNo\":"+(i+1));
								doc2 = null;
								url = "http://www.srsgrocery.com/Handler/ProductShowcaseHandler.ashx?ProductShowcaseInput="+inputPara.replaceAll("\"", "%22");
								connection =  getConnectionViaJsoup(url, map, false);
								doc2 = connection.get();
							}
							
							Element element3 = doc2.select("div.bucketgroup").first();
							Elements elements = element3.select("div.bucket");
							for (Element element4 : elements) {
								String productId = element4.attr("data-productid");
								String productName = element4.select("h4.mtb-title").first().text();
								String imageUrl = element4.select("img.mtb-img").first().attr("original");	
								
								List<SrsVariant> srsVariants = getVariants(productId);
								
								for (SrsVariant srsVariant : srsVariants) {
									String respDB = dbUtil.saveOrUpdateDB(catName, subCatname, productName, srsVariant.getVariant(), srsVariant.getMrp(), srsVariant.getOffer(), srsVariant.getSellPrice(), imageUrl, cityDo, merchantDo, "") ;
									System.out.println("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | productId: "+productId+" | productName"+productName+" | imageUrl: "+imageUrl+" | variantId: "+srsVariant.getVariantId()+" | variant: "+srsVariant.getVariant()+" | maxPrice: "+srsVariant.getMrp()+" | sellPrice: "+srsVariant.getSellPrice()+" | offer: "+srsVariant.getOffer()+" | respDB: ");
								}
							}
						}
					}
				}
			}else{
				System.out.println("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | elements3 is empty");
			}
		}catch (Exception e) {
			System.out.println("[SrsGrocery][processDoc]["+cityName+"] catName: "+catName+" | subCatname: "+subCatname+" | Exception: "+e);
		}
	}
	
	private static List<SrsVariant> getVariants(String productId){	
		List<SrsVariant> srsVariants = new ArrayList<SrsVariant>();
		try{
			String url = "http://www.srsgrocery.com/Handler/ProductQuickViewHandler.ashx?ActionType=GetVariants&MID=600f0371-3cf1-478b-a67e-c33fe2b5523b&PIDS="+productId+"&LID=13080&SortVarientsBy=d&SortDirection=d";
			Document doc = getConnectionViaJsoup(url, null, true).get();
			String json = doc.body().text();
			if(json!=null&&!"".equals(json)){
				JSONArray jsonArray = new JSONArray(doc.body().text());
				JSONArray jsonArray2 = new JSONArray(jsonArray.get(0).toString());
				System.out.println(jsonArray2.length());
				for(int i =0; i < jsonArray2.length() ; i++){
					JSONObject jsonObject = (JSONObject) jsonArray2.get(i);					
					JSONArray jsonArray3 = jsonObject.getJSONArray("lstProductVariantValueView");
					JSONObject jsoObject = (JSONObject)jsonArray3.get(0);
					long variantId = Long.valueOf(jsonObject.get("variantProductId").toString());
					String sku =  jsonObject.get("sku").toString();
					String variant = jsoObject.get("variantValue").toString();
					String mrp = jsonObject.get("mrp").toString();
					String sellPrice = jsonObject.get("webPrice").toString();					
//					String saveRs = jsonObject.get("SaveOnMRPShow").toString();
					String saveRs = jsonObject.get("SaveOnMRPAmount").toString();
					
					SrsVariant srsVariant = new SrsVariant(variantId, sku, variant, mrp, sellPrice, saveRs);
					srsVariants.add(srsVariant);
				}
			}
		}catch (Exception e) {
			System.out.println("[SrsGrocery][getWeight]["+productId+"] Exception: "+e);
		}
		return srsVariants;
	}
	
	private static HashMap<String, HashMap<String,String>> getCategories(Document doc){
		HashMap<String, HashMap<String,String>> getCategories=null;
		try{
//			Connection connection1=this.getConnectionViaJsoup("http://www.srsgrocery.com", mapResCookies, false);				
//			Document doc = connection1.get();
			if(doc!=null){
				getCategories = new HashMap<String, HashMap<String,String>>();
				Element element = !doc.select("div.l2_wrapper.w190").isEmpty()?doc.select("div.l2_wrapper.w190").first().select("ul.l2_inner_ul").first():null;
				Elements elements = element.children();
				for (Element element2 : elements) {
					String categoryName = "";
//					String catUrl ="";
					Elements elements2 = element2.children();
					HashMap<String, String> mapSubCategories = new HashMap<String, String>();
					for (Element element3 : elements2) {
						
						if(element3.tagName().equals("a")){
							categoryName=element3.text();
//							catUrl = element3.attr("href");
						}
						if(element3.tagName().equals("div") && element3.attr("class").equals("l3_wrapper")){
							Elements elements3 = element3.select("div.cols");
							for (Element element4 : elements3) {
								Element element5 = element4.select("ul").first();
								
								Elements elements4 = element5.children();
								for (Element element6 : elements4) {
									
									if(elements4.size()>1){
										if(element6.tagName().equals("li")&& !element6.attr("class").equals("cat_title")){
											Element element7 = element6.select("a").first();
											mapSubCategories.put(element7.text(), element7.attr("href"));
										}
									}else{
										Element element7 = element6.select("a").first();
										mapSubCategories.put(element7.text(), element7.attr("href"));
									}								
								}
								
							}
						}						
					}
					getCategories.put(categoryName, mapSubCategories);
				}
				
				element = null;
				element = doc.select("div.offers-slots").first();
				String categoryName = element.select("span").first().text();				
				Elements elements2 = element.select("div.show-me").first().select("ul").first().select("li");
				HashMap<String, String> mapSubCategories = new HashMap<String, String>();
				for (Element element2 : elements2) {
					Element element3 = element2.getElementsByTag("a").first();
					String subCatName = element3.text();
					String subCatUrl = element3.attr("href");
					mapSubCategories.put(subCatName, subCatUrl);
				}
				getCategories.put(categoryName, mapSubCategories);
			}else{
				System.out.println("[SrsGrocery][getCategories] doc is null");
			}			
		}catch (Exception e) {
			System.out.println("[SrsGrocery][getCategories] Exception: "+e);
		}
		return getCategories;
	}
	
	private static Connection getConnectionViaJsoup(String url,Map<String, String> map,boolean ignoreContentType) throws Exception{
		Connection connection = null;		
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)
					.timeout(25000)
					.ignoreContentType(ignoreContentType);
				if(map!=null && map.size()>0)
					connection = connection.cookies(map);
						
			
		}catch (Exception e) {
			throw e;
		}
		return connection;				
	}
	
	private static TreeMap<String, String> citymap() throws Exception{
		TreeMap<String, String> map = null;
		try{
			map = new TreeMap<String, String>();
			
			Connection con = getConnectionViaJsoup("http://www.srsgrocery.com/", null, false);
			Document doc = con.get();
			Elements cityElements = doc.getElementById("ddlLocationNames").children();
			for (Element element : cityElements) {
				String value = element.attr("value");
				String key= element.text();
				map.put(key, value);
			}
		}catch (Exception e) {
			throw e;
		}
		return map;
	}
}
