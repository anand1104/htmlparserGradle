package com.ahoy.parser.api;//package com.ahoy.parser.api;
//
//import java.io.IOException;
//import java.util.Iterator;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.jsoup.nodes.Element;
//import org.jsoup.select.Elements;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ahoy.parser.dao.CityDaoImpl;
//import com.ahoy.parser.dao.MerchantDaoImpl;
//import com.ahoy.parser.domain.CityDo;
//import com.ahoy.parser.domain.MerchantDo;
//import com.ahoy.parser.util.DBUtil;
//
//@WebServlet("/RelianceFreshDirect")
//public class RelianceFreshDirect extends HttpServlet {
//	private static final long serialVersionUID = 1L; 
//	
//	static Logger logger = LoggerFactory.getLogger(RelianceFreshDirect.class);
//	
//	@SuppressWarnings("rawtypes")
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		// TODO Auto-generated method stub
//		
//		Document doc;
//		Document prc;
//				try {
//				
////***************************getting page first page*************************
//					
//			doc =Jsoup.connect("http://www.reliancefreshdirect.com").timeout(60000).get();
//			Elements cat =doc.select("ul#rilHoverMenu").select("li.greenBG").select("a");
//			for(Element data:cat){
//				String catname = data.text();
//				String catlink = data.attr("href");
//
////****************************getting to category link*************************				
//				Document doc1 =Jsoup.connect(catlink).timeout(30000).get();
//				Elements sublink =doc1.select("div#search_facet_category").select("a");
//				for(Element data1 :sublink){
//					String subsubcat =data1.text().replaceAll("[0-9.]","" );
//					
//					String subsublink =data1.attr("href");
//
////*************************getting to sub-category****************************					
//					Document doc2 =Jsoup.connect(subsublink).timeout(30000).get();
//					String total_item =doc2.select("div.widget_product_listing").select("div.title").select("span").text().split(" ")[0];
//					System.out.println("total_item ="+total_item);
//					int start_index = 0;
//					int j =0;
//					int a =1;
//					if(Integer.valueOf(total_item)%12!=0){
//						j=Integer.valueOf(total_item)/12 +1;
//					}else{
//						j=Integer.valueOf(total_item)/12;
//					}
//					while(a<=j){
//						a++;
//					String pageloc =doc2.select("input#pageURL").attr("value");
//
////**************************getting pagination done********************************					
//					Document doc3 =Jsoup.connect("http://www.reliancefreshdirect.com/" + pageloc)
//							.timeout(30000)
//							.data("orderBy","")
//							.data("beginIndex",String.valueOf(start_index))
//							.data("isHistory","false")
//							.data("pageView","grid")
//							.data("minPrice","")
//							.data("maxPrice","")
//							.get();
//					start_index =start_index+12;
//					Elements visit =doc3.select("div.widget_product_listing");
//					for(Element el :visit){
//					Elements productlist = el.select("div.product_listing_container");
//					for(Element data2 : productlist){
//						Elements productset =data2.select("div.ril-product");
//						for(Element data3 :productset){
//						
//							String json= null;
//							Elements elements = data3.children();
//							for (Element element : elements) {
//								if(element.attr("id").startsWith("entitledItem_")){
//									json = element.ownText();									
//									break;
//								}
//							}
//							
//							JSONArray jsonArray =new JSONArray();
//							JSONParser parser = new JSONParser();
//						
//							Object details =parser.parse(json);
//							
//							jsonArray = (JSONArray) details;
//						
//							
//							Iterator itr = jsonArray.iterator();
//							int i = 0;
//							int isAvail =1;
//
//							while(itr.hasNext()){
//								itr.next();
//								JSONObject jobj = (JSONObject) jsonArray.get(i);	
//								i++;
//								String mrp =null;
//								String sprice =null;
//								String name = (String) jobj.get("itemName");
//								String image = null;
//								name =name.toLowerCase().replace(" lt ", " l ");
//								String product_id =data3.select("div.pro-price").attr("id").replace("price_display_", "");
//								
//								String varient_id =(String) jobj.get("catentry_id");
//								if(product_id.isEmpty()){
//									product_id =varient_id;
//								}
//								String producturlurl =(String) jobj.get("catentryURL");
//								
//								prc =Jsoup.connect("http://www.reliancefreshdirect.com/webapp/wcs/stores/servlet/GetCatalogEntryDetailsByIDView").timeout(60000)
//										.data("storeId","10151")
//										.data("langId","-1")
//										.data("catalogId", "10001")
//										.data("catalogEntryId",varient_id)
//										.data("productId",product_id)
//										.post();
//								
//								String	json1 =null;
//								Element pdtels =prc.select("body").first();
//								json1 =pdtels.ownText().replace("/*", "[").replace("*/", "]");
//								
//								JSONArray jsonArray1 = new JSONArray();
//								JSONParser parser1 = new JSONParser();
//								if(!json1.isEmpty()){
//								Object j1 =parser1.parse(json1);
//								jsonArray1 =(JSONArray) j1;
//								}
//								
//								Iterator itr1 = jsonArray1.iterator();
//								while(itr1.hasNext()){
//									itr1.next();
//									JSONObject jobj1 = (JSONObject) jsonArray1.get(0);
//									JSONObject catentry =(JSONObject) jobj1.get("catalogEntry");
//									Object img =catentry.get("description");
//									jsonArray1 =(JSONArray) parser1.parse(img.toString());
//									JSONObject img1 =(JSONObject) jsonArray1.get(0);
//									image =(String) img1.get("thumbnail");
//									
//									mrp =(String)catentry.get("offerPrice");
//									sprice =(String)catentry.get("listPrice");
//								}
//								
//								
//								
//								JSONObject pack =(JSONObject) jobj.get("Attributes");
//								String pack_size =pack.keySet().toString().replace("[Pack Size_", "").replace("[Pack Count_", "").replace("]", "").replace("[", "").replace("Volume_", "");
//								
//									pack_size = pack_size.toLowerCase().replace(" lt", " l");
//									
//								
//								if(prc.select("div.pdp-availability").select("div.status").select("span").attr("id").startsWith("InventoryStatus_OnlineStatus_")){
//									String avialability = prc.select("div.pdp-availability").select("div.status").select("span").text();
//									if(avialability.matches("In-Stock")){
//										isAvail =2;
//									}
//								}
//								 
//															
//								String offerDesc =(String) jobj.get("offerDesc");
//								name =name.replace(pack_size.toLowerCase(), "");
//								if(name.contains("tetra pack")){
//									name = name.replace("tetra pack", "");
//								}
//								if(name.contains("tetrapack")){
//									name = name.replace("tetrapack", "");
//								}
//								if(name.contains(" tin")){
//									name = name.replace("tin", "");
//								}
//								if(name.contains(" bag")){
//									name = name.replace("bag", "");
//								}
//								 
//								 if(name.contains(" bottle")){
//										name = name.replace("bottle", "");
//									}
//								 if(name.contains(" tube")){
//										name = name.replace("tube", "");
//									}
//								 if(name.contains(" poly pack")){
//										name = name.replace("poly pack", "");
//									}
//								 if(name.contains(" polypack")){
//										name = name.replace("polypack", "");
//									}
//								 if(name.contains(" pouch")){
//										name = name.replace("pouch", "");
//									}
//								 if(name.contains(" jar")){
//										name = name.replace("jar", "");
//									}
//								 if(name.contains(" pet")){
//										name = name.replace("pet", "");
//									}
//								 if(name.contains(" can")){
//										name = name.replace("can", "");
//									}
//								 if(name.contains(" box")){
//										name = name.replace("box", "");
//									}
//								if(name.split(" ")[name.split(" ").length-1].equals("gm")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("lt")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("s")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("piece")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("ltr")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("l")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("ml") 
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("sticks") 
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("pcs")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("kg")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("mtr")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("pc")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("hdpe")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("pads")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("bunch")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("nos")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].equals("capsules"))
//										
//								{
//									if(name.trim().split(" ")[name.trim().split(" ").length-1].equals("hdpe")){
//										pack_size =name.split(" ")[name.split(" ").length-3]+" "+name.split(" ")[name.split(" ").length-2]+" "+name.split(" ")[name.split(" ").length-1];
//									}
//									pack_size = name.split(" ")[name.split(" ").length-2].replaceAll("[^0-9.]", "")+" "+name.split(" ")[name.split(" ").length-1];
//									name = name.replace(pack_size, "");
//								}else
//								if(name.split(" ")[name.split(" ").length-1].contains("gm")
////										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("lt")
////										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("l")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("ml") 
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("sticks") 
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("pcs")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("kg")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("mtr")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("pc")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("pads")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("bunch")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("nos")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("sachets")
//										|| name.trim().split(" ")[name.trim().split(" ").length-1].contains("capsules"))
//										
//								{
//									pack_size = name.split(" ")[name.split(" ").length-1];
//									name = name.replace(pack_size, "");
//								}
//								if(pack_size.isEmpty()){
//									pack_size ="1 pack";
//								}
//								
////								logger.info("[RelianceFreshDirect][process]["+"Mumbai"+"] category name :" +catname+ " | "+"subsubcat name :"+ subsubcat+" | "+"name :"+name+" | "+"pack :"+pack_size+" | "
////										+"product id :"+product_id+" | "+"varient id :"+varient_id +" | "+"image :"+image+" | "+"product url :"+producturlurl+" | "
////												+"mrp :"+mrp+" | "+"selling price :"+sprice+" | "+"isAvail :" +isAvail+" | "+"offerDesc :"+offerDesc);
//								DBUtil dbUtil =new DBUtil();
//								MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname("RelianceFreshDirect");
//								CityDo cityDo = new CityDaoImpl().getByName("Mumbai");
//						//		logger.info("Image : " + image +"| Product Name :" + name + "| Product Id : " + productid + "| Varient Id : "+ varientId + "| Weight : "+ weight + "| Selling Price : " + saleing + "| M.R.P. : " + MRP +"| offer : " + ofr );
//								if(Float.parseFloat(mrp.replace(",", "")) != 0.00){
//								if(name!=null && pack_size!=null && mrp !=null && sprice!=null&&mrp.trim().matches("[0-9]+(?:\\.[0-9]+)?") && sprice.trim().matches("[0-9]+(?:\\.[0-9]+)?")){
//									String respDB = dbUtil.saveOrUpdateByPIdAndVId(catname, subsubcat, name, pack_size, mrp, offerDesc, sprice, image, cityDo, merchantDo, "", Long.valueOf(product_id.trim()),Long.valueOf(varient_id), "",(short) isAvail,producturlurl);
//									logger.info("[RelianceFreshDirect][process]["+"Mumbai"+"] cat: "+catname+" | subcat: "+subsubcat+" | item: "+name+" | itemid: "+product_id+" | variant: "+pack_size+" | sp: "+sprice+" | mrp: "+mrp+" | offer: "+offerDesc+" | image: "+image+" | avail: "+(isAvail==2?"Out of Stock":"available")+" | respDB: "+respDB);
//								}else{
//									logger.info("[RelianceFreshDirect][process]["+"Mumbai"+"] cat: "+catname+" | subcat: "+subsubcat+" | item: "+name+" | itemid: "+product_id+" | variant: "+pack_size+" | sp: "+sprice+" | mrp: "+mrp+" | offer: "+offerDesc+" | image: "+image+" | avail: "+(isAvail==2?"Out of Stock":"available")+" | Some thing missed");
//								}
//							}
//							}
//						}
//						}
//					}
//				}
//			}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//}
