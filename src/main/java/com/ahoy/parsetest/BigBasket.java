package com.ahoy.parsetest;

import com.ahoy.parser.util.GetStackElements;
import org.jsoup.Connection;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BigBasket {
	public static void main(String[] args) throws IOException{
		
	try{
		
//################ First Hit and get response cookies #####################
		Connection connection1 =	Jsoup.connect("http://www.bigbasket.com/choose-city/?next=/")
				.followRedirects(true)
				.userAgent("Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
				.method(Method.GET)
				.header("Connection", "keep-alive")
				.timeout(25000);		
		Response response = connection1.execute();
		System.out.println(response.statusCode()+" | "+response.statusMessage()+" | "+response.cookies().get("_bb_vid"));
		
		String csrfToken = response.cookies().get("csrftoken");
		System.out.println("csrfToken: "+csrfToken);

//################ Set locality and Get categories #####################	
//		Map<String, Map<String, String>> mapCategories = setLocalityAndGetCategories(response.cookies());
//		System.out.println(mapCategories);
//		for(Map.Entry<String, Map<String, String>> map1 : mapCategories.entrySet()){
//			String catName = map1.getKey();
//			Map<String, String> subCatMap = map1.getValue();
//			for(Map.Entry<String, String> map2:subCatMap.entrySet()){
//				String subCatName = map2.getKey();
//				String subCatUrl = map2.getValue();
//				String nextPage = null;
//				
//				do{
//					
//					Connection connection2 = hitSubCatUrl("http://bigbasket.com"+(nextPage==null?subCatUrl:nextPage), response.cookies()) ;
//					
//					Response response3 = connection2.execute();
//					Document doc = response3.parse();
//					
//					Elements elements2 = null;
//					if(nextPage==null){
//						Element element2 = doc.getElementById("products-container");
//						elements2 = element2.getElementsByClass("uiv2-shopping-lis-sku-cards");
//						nextPage = element2.getElementById("next-product-page")!=null?element2.getElementById("next-product-page").attr("href"):null;
//					}else{
//						elements2 = doc.getElementsByClass("uiv2-shopping-lis-sku-cards");
//						nextPage = doc.getElementById("next-product-page")!=null?doc.getElementById("next-product-page").attr("href"):null;
//					}
//					
//					for (Element element3 : elements2) {
//						Element element4 = element3.getElementsByClass("uiv2-our-recommendations-list-box").first();
//						Elements elements3 = element4.children();
////						System.out.println(elements3);
//						for (Element element5 : elements3) {
//							if(element5.hasAttr("id") && element5.hasAttr("name")){
//								String id = element5.attr("id");
//								String name = element5.attr("name");
//								if(id.toLowerCase().contains("product_") && name.toLowerCase().contains("widget_")){
//									
//									String prodId =name.split("_")[1]; 
//									String variant = null;
//									Elements elements = element5.getElementsByClass("uiv2-field-wrap").first().select("select");
//									if(elements!=null && elements.size()>0){
//										Elements elements4 = elements.first().children();
//										for (Element element : elements4) {
//											String value = element.attr("value");
//											if(element.hasAttr("selected") && value!=null && !"".equals(value.trim())){
//												variant = element.text().split("-")[0].trim();
//												break;
//											}
//										}											
//									}else{
//										variant = element5.getElementsByClass("uiv2-field-wrap").first().ownText().trim();	
//									}
//																			
//									String itemName =element5.getElementsByClass("uiv2-tool-tip-hover").first().text().trim().replace(variant, "");
//									String image = "http:"+element5.getElementsByClass("uiv2-list-box-img-block").first().getElementsByTag("img").first().attr("src");
//									
//									String sp = element5.getElementsByClass("uiv2-rate-count-avial").first().text();									
//									Elements elements1 = element5.getElementsByClass("Rate_count_low");
//									String mrp = (elements1==null|| elements1.isEmpty())?sp:(elements1.first().text().split(":")[1]);									
//									String offer = element5.getElementsByClass("uiv2-combo-block")!=null?element5.getElementsByClass("uiv2-combo-block").text():"";
//									
//									Element element = element5.getElementsByClass("uiv2-product-notify")!=null&&!element5.getElementsByClass("uiv2-product-notify").isEmpty()?element5.getElementsByClass("uiv2-product-notify").first().child(0):null;
//									if(element!=null && element.text().trim().equalsIgnoreCase("OUT OF STOCK") ){
//										System.out.println(catName+" | "+subCatName+" | "+itemName+" | "+prodId+" | "+variant+" | "+sp+" | "+mrp+" | offer: "+offer+" | image: "+image+" | out of stock");
//									}else{
//										System.out.println(catName+" | "+subCatName+" | "+itemName+" | "+prodId+" | "+variant+" | "+sp+" | "+mrp+" | offer: "+offer+" | image: "+image+" | stock avail");
//									}
//									
//									
//									
//								}
//							}
//						}
//					}					
//				}while(nextPage!=null);
//		}
//			
//			
//		}
		
		
		
		
		
		System.out.println("=========================2 END =================");
		System.out.println("finished");
				
		
	}catch (Exception e) {
		System.out.println(GetStackElements.getRootCause(e, BigBasket.class.getName()));
		e.printStackTrace();
	}
	}
	
	private static Connection hitSubCatUrl(String url,Map<String, String> cookies){
		Connection connection = null;
		try{
			
			connection = Jsoup.connect(url)
					.method(Method.GET)	
					.cookie("_bb_vid", cookies.get("_bb_vid"))
					.cookies(cookies);
		}catch(Exception e){
			System.out.println("*********************************************************\n");
			e.printStackTrace();
		}
		
		return connection;
		
	}
		
	private  static Map<String, Map<String, String>>  setLocalityAndGetCategories(Map<String, String> cookies){
		
		Map<String, Map<String, String>> map = new HashMap<String, Map<String,String>>();
		
		try{
			
//			String place = "{\"address_components\":[{\"long_name\":\"Mayfield Garden\",\"short_name\":\"Mayfield Garden\",\"types\":[\"sublocality_level_2\",\"sublocality\",\"political\"]},{\"long_name\":\"Sector 51\",\"short_name\":\"Sector 51\",\"types\":[\"sublocality_level_1\",\"sublocality\",\"political\"]},{\"long_name\":\"Gurgaon\",\"short_name\":\"Gurgaon\",\"types\":[\"locality\",\"political\"]},{\"long_name\":\"Gurgaon\",\"short_name\":\"Gurgaon\",\"types\":[\"administrative_area_level_2\",\"political\"]},{\"long_name\":\"Haryana\",\"short_name\":\"HR\",\"types\":[\"administrative_area_level_1\",\"political\"]},{\"long_name\":\"India\",\"short_name\":\"IN\",\"types\":[\"country\",\"political\"]}],\"adr_address\":\"<span class=\\\"extended-address\\\">Mayfield Garden, Sector 51</span>, <span class=\\\"locality\\\">Gurgaon</span>, <span class=\\\"region\\\">Haryana</span>, <span class=\\\"country-name\\\">India</span>\",\"formatted_address\":\"Mayfield Garden, Sector 51, Gurgaon, Haryana, India\",\"geometry\":{\"location\":{\"G\":28.4253544,\"K\":77.06304709999995,\"latitude\":28.4253544,\"longitude\":77.06304709999995},\"viewport\":{\"Ia\":{\"G\":28.4192778,\"j\":28.431145},\"Ca\":{\"j\":77.0553248,\"G\":77.0683401}}},\"icon\":\"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png\",\"id\":\"7cd56ab42afcdee079ded0fe56286d6d4d41c2b6\",\"name\":\"Mayfield Garden\",\"place_id\":\"ChIJMV8U24EYDTkRjiEreqfojG0\",\"reference\":\"CqQBkQAAAOXtChYPJXbwc54vKmN_ZYRMhW37l8era9aY60PPlqNr7x_w_XK8NmJS0Oeuyno08ts_GwGTjAQnRYVbe18GNz4JA0m6FlsFOLadwBA_Z7z6yWItAXJYzj44fxT7ocYG7YPw85AOwYm8-R-F2hzKVftTalSrzh0H_luZUVzcw2-49SlardZB_V5279Z9GhQvjtD3UygpDIdkqTzTVsfWyJASEAFzauCY5w6eywqZgaR_48UaFFCSSydKnEXrK0JxKcf7_RCCk64c\",\"scope\":\"GOOGLE\",\"types\":[\"sublocality_level_2\",\"sublocality\",\"political\"],\"url\":\"https://maps.google.com/maps/place?q=Mayfield+Garden,+Sector+51,+Gurgaon,+Haryana,+India&ftid=0x390d1881db145f31:0x6d8ce8a77a2b218e\",\"vicinity\":\"Mayfield Garden\",\"html_attributions\":[]}";
//			String place = "{\"city\":9,\"display_name\":\"Mayfield garden\",\"name\":\"mayfield garden\",\"hub\":58,\"area\":1460,\"pincode\":\"122017\",\"area_location\":[28.425529093893,77.062893456714],\"city_name\":\"Delhi-NCR\",\"pincode_location\":[28.509238,77.04160399999999]}";
			String place = "{\"city\":9,\"display_name\":\"Nirvana Country, Gurgaon\",\"name\":\"nirvana country, gurgaon\",\"hub\":58,\"area\":8888,\"pincode\":\"122017\",\"area_location\":[28.4124299,77.06652],\"city_name\":\"Delhi-NCR\",\"pincode_location\":[28.509238,77.04160399999999]}";;
			String csrfToken = cookies.get("csrftoken");
			System.out.println("csrfToken: "+csrfToken);
			Connection connection2 = Jsoup.connect("http://www.bigbasket.com/choose-city/")
					.method(Method.POST)
					.cookies(cookies)
					.data("places",place)
					.data("csrfmiddlewaretoken",csrfToken)
					.data("address_id","")
					.data("city_id","9")
					.data("next","")
					.timeout(25000);
			
			Response response2 = connection2.execute();			
			System.out.println("[BigBasket][setLocalityAndGetCategories] status code:"+response2.statusCode()+" | status Message: "+response2.statusMessage()+" | "+response2.url()+" | "+response2.cookies().get("_bb_vid"));
						
			Document doc = response2.parse();
			System.out.println(doc.getElementById("uiv2-selection").ownText());
						
			Element element = doc.getElementById("basket_menu").child(0);
			Elements elements = element.getElementsByClass("normal");
			
			for (Element element2 : elements) {
				Element child1 = element2.child(0);
				String catName = child1.ownText();
//				String catUrl = child1.attr("href");			
				
				Element child2 = element2.child(1);
				Element element3 = child2.select("div.uiv2-dropdown-column").get(0).child(0);
				Elements elements2 = element3.children();
				
				Map<String, String> subCatMap = new HashMap<String, String>();
				for (Element element4 : elements2) {
					Elements elements3 = element4.children();
					if(elements3.size()==1){
						String subCat = elements3.get(0).ownText();
						String subCatUrl = elements3.get(0).attr("href");
						subCatMap.put(subCat, subCatUrl);
					}else{
						String subCat = elements3.get(1).ownText();
						String subCatUrl = elements3.get(1).attr("href");
						subCatMap.put(subCat, subCatUrl);
					}
				}
				map.put(catName, subCatMap);
			}
		}catch(Exception e){
			
		}
		return map;
	}
}
