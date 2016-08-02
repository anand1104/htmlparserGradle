package com.ahoy.parsetest;

import com.ahoy.parser.util.GetStackElements;
import org.jsoup.Connection.Method;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;


public class Zopnow {
	public static void main(String[] args){
		try{
//			String pincode = "122001";
//			String city = "Gurgaon";
//			Response response = Jsoup.connect("http://www.zopnow.com/pincode.json")
//					.data("pincode",pincode)
//					.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
//					.method(Method.POST).execute();
//			
//			String json = response.body();
//			JSONArray jsonArray = new JSONArray(json);
//			JSONObject jsonObject = new JSONObject(jsonArray.getString(0));
//			jsonObject = new JSONObject(jsonObject.getString("data"));
//			String resp = jsonObject.getString("type");
//
//			if(resp !=null && resp.equalsIgnoreCase("success")){
//				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("zopnow");
//				
//				Document doc = Jsoup.connect("http://www.zopnow.com")
//				.cookie("pincode", pincode)
//				.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
//				.get();
//				System.out.println(doc.getElementsByClass("js-showpincode").first());
//				Elements menuElements = doc.getElementById("c-menu").children();
//				Element categoryElement  =  menuElements.get(0);
////				Element offerElement  =  menuElements.get(0);
//				
////				System.out.println(categoryElement.children().first().children());
//				
//				Elements catsBlock =categoryElement.children().first().children();
//				for(Element catblock: catsBlock){
////					System.out.println(catblock);
//					Elements catblockChildElements = catblock.children();
//					System.out.println(catblockChildElements.size());
//					String categoryName = catblockChildElements.get(0).text();
//					Elements subCatElements = catblockChildElements.get(1).children();
//					System.out.println(categoryName);
//					
//					for(int i=1 ;i <subCatElements.size() ; i++){
//						Element subCatElement = subCatElements.get(i);
//						String subcatName = subCatElement.children().get(0).ownText();
//						Elements elements = subCatElement.children().get(1).children();
//						System.out.println(subcatName);
//						for(int j=1 ; j<elements.size() ; j++){
//							Elements elements2 = elements.get(j).select("a[href]");
//							for(Element element:elements2){
//								String subSubCatName = subcatName+"-"+element.ownText();
//								int totalItems = Integer.valueOf(element.getElementsByTag("small").first().text().trim());
//								String subSubUrl = element.attr("href");
//								System.out.println(subSubCatName+" "+subSubUrl+" | "+element.tag()+" | "+totalItems);
//								
//								int pages = totalItems>21?(totalItems%21==0?totalItems/21:(totalItems/21+1)):1;
//								System.out.println(pages+" | "+totalItems%21);
//								for(int k = 0 ; k < pages ; k++){
//									if(k==0){
//										Document doc1 = Jsoup.connect("http://www.zopnow.com"+subSubUrl)
//												.cookie("pincode", pincode)
//												.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
//												.get();
////										System.out.println(doc1.getElementsByClass("jsProductContainer"));
//										Elements elements3 = doc1.getElementsByClass("jsProductContainer").get(0).children();
//										for (Element element2 : elements3) {
//											String productId = element2.attr("data-item-id");
//											String productName = element2.attr("data-item-name");
////											String variantId = element2.attr("data-var-id");
//											Elements variantElements = element2.getElementsByClass("js-variant");
//											System.out.println(productId+" | "+productName+" | "+variantElements.size());
//											for (Element element3 : variantElements) {
//												String variantId = element3.attr("data-id");
//												String variant = element3.attr("data-name");
//												String mrp = element3.attr("data-mrp");
//												String image = element3.attr("data-image");
//												String stock = element3.attr("data-stock");
//												String discount = element3.attr("data-discount");
//												String price = element3.getElementsByClass("price").first().ownText().replaceAll("[^0-9A-Za-z.%]", "");
//												System.out.println("variantId: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | image: "+image+" | stock: "+stock+" | discount: "+discount+" | price: "+price);
//											}
//											break;
//										}
//										
//									}else{
//										NumberFormat formatter = new DecimalFormat("#0.00");
//										String pageurl = subSubUrl.replace(".php", ".json")+"?page="+(k+1) ;
//										Document doc1 = Jsoup.connect("http://www.zopnow.com"+pageurl)
//												.cookie("pincode", pincode)
//												.header("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36")
//												.get();
//										
//										String productJson = doc1.body().ownText();
//										if(productJson!=null && !"".equals(productJson.trim())){
//											JSONArray jsonArray2 = new JSONArray(productJson);
//											if(jsonArray2!=null && jsonArray2.length()>0){
//												JSONObject jsonObject2 = new JSONObject(jsonArray2.get(1).toString());
//												String name = jsonObject2.getString("name");
//												
//												JSONObject jsonObject3 = new JSONObject(jsonObject2.getString("data"));
//												System.out.println("NAME:"+name);
//												System.out.println(jsonObject3);
//												String title = jsonObject3.getString("title");
//												JSONArray jsonArray3 = jsonObject3.getJSONArray("products");
//												for(int l=0; l<jsonArray3.length(); l++){
//													JSONObject jsonObject4 = jsonArray3.getJSONObject(l);
//													String productId = jsonObject4.getString("id");
//													String productName = jsonObject4.getString("full_name");
//													JSONArray jsonArray4 = jsonObject4.getJSONArray("variants");
//													for(int m=0 ; m<jsonArray4.length() ; m++){
//														JSONObject jsonObject5 = jsonArray4.getJSONObject(m);
//														String variantId =  jsonObject5.getString("id");
//														String variant = jsonObject5.getJSONObject("properties").getString("Quantity");
//														String mrp = jsonObject5.getString("id"); 
//														String discount = jsonObject5.getString("discount");
//														String image = jsonObject5.getJSONArray("images").get(0).toString();
//														String price = String.valueOf(formatter.format(discount!=null && discount.trim().matches("[0-9]+(?:\\.[0-9]+)?")?Float.valueOf(mrp.trim())-Float.parseFloat(discount.trim()):0));
//														System.out.println("productId: "+productId+" | productName: "+productName+" | variantId: "+variantId+" | variant: "+variant+" | mrp: "+mrp+" | discount: "+discount+" | price: "+price+" | image: "+image);
//														
//													}
//													break;
//												}
//												
//											}else{
//												
//											}
//										}
//									}
//								}								
//								break;
//							}
//							break;
//						}
//						break;
//					}
//					break;
//				}
//				
//				
//			}else{
//				System.out.println("resp: "+resp);
//			}
			
			System.out.println(Zopnow.JsoupConnection("http://www.zopnow.com/frozen-nonveg-snacks-c.json?page=3", "122001","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/46.0.2490.80 Safari/537.36" , (short)0).statusMessage());
			

		}catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static Response  JsoupConnection(String url, String pincode, String userAgent, short mtype){
		Response response = null;
		try{			
			response=Jsoup.connect(url)
			.data("pincode",pincode)
			.header("User-Agent", userAgent)
			.method(mtype>0?Method.POST:Method.GET)
			.timeout(15000).execute();
		}catch (Exception e) {
			response = null;
			System.out.println("[Zopnow][JsoupConnection] pincode: "+pincode+" | url: "+url+" | mtype: "+(mtype>0?"Post":"Get")+" | Exception: "+GetStackElements.getRootCause(e, Zopnow.class.getName()));
		}
		return response;
		
	}
	private static void getProduct(Elements elements){
		try{
			
		}catch (Exception e) {
			// TODO: handle exception
		}
	}
}
