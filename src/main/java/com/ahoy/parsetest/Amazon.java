package com.ahoy.parsetest;//package com.ahoy.parsetest;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.TreeMap;
//import java.util.zip.GZIPInputStream;
//
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.ss.usermodel.Row;
//import org.codehaus.jettison.json.JSONArray;
//import org.codehaus.jettison.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.ahoy.parser.util.GetStackElements;
//import com.ahoy.parser.util.UtilConstants;
//
//@WebServlet("/amazon")
//public class Amazon extends HttpServlet{
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	static Logger logger = LoggerFactory.getLogger(Amazon.class);
//
//	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{
//		
//		FileWriter out = null;
//		try {
//			out = new FileWriter(new File("F:/amazonxmls/in_amazon_apparel.txt"));
//
//				FileInputStream fin = new FileInputStream("F:/amazonxmls/in_amazon_apparel.xml.gz");
//				GZIPInputStream gzis = new GZIPInputStream(fin);
//				InputStreamReader isr = new InputStreamReader(gzis,"UTF-8");			    
//				BufferedReader br = new BufferedReader(isr);
//								
//				   String line = "";
//				   StringBuilder builder = new StringBuilder();
//				   int i = 0;
//				   int lineNo = 0;
//				   long itemCount = 0;
//				   String startTags = "";
//				   out.write("item_unique_id | item_sku | parent_asin | item_mpn | item_brand | item_name | item_model | item_category | item_short_desc | item_page_url | amzn_page_url | fm_page_url | offer_page_url | offer_used_url | tp_fba_url | item_image_url | item_salesrank | item_price | item_inventory | item_shipping_charge | item_merchant_id | fm_price | fm_inventory | fm_shipping_charge | fm_merchant_id | tp_new_price | tp_new_inventory | tp_new_shipping_charge | tp_new_merchant_id | color | size | department | savings_basis | tp_fba_price | tp_fba_inventory | tp_fba_shipping_charge | tp_fba_merchant_id | merch_cat_name | merch_cat_path\n");
//				   while ((line = br.readLine()) != null) {
//					if(lineNo<2){
//						lineNo++;
//						startTags+=line+"\n";
//						builder.append(line+"\n");
//						
//					}else{
//						if(line.trim().contains("</item_data>")){
//							i++;
//							itemCount++;
//							if(itemCount == 1000){
//								builder.append(line+"\n</DataFeeds>");
//								processData(builder.toString(),out);
//								builder = new StringBuilder();
//								builder.append(startTags);
//								itemCount = 0;
//								System.out.println(i);
//							}else{
//								builder.append(line+"\n");
//							}							
//						}else{
//							builder.append(line+"\n");
//						}
//						
//					}
//
//				   }
//
//				   if(builder!=null && !"".equals(builder.toString())){
//					   processData(builder.toString(), out);
//				   }
//				   
//				   System.out.println("Total COunt: "+i);
//				   System.out.println(builder);
//				    br.close();
//			    } catch (Exception e) {
//				e.printStackTrace();
//			    }	
//		 finally{
//			 try {
//				out.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		 }
//		
//	}
//
//
////	method for url as per category
//	public Map<String,String> getCategoryMap(String affilatedId,String token){
//		Map<String,String> map =new TreeMap<String,String>();
//		try{
////			List<String> list = Arrays.asList("Household_Essentials","Health_Wellness_Medicine");
//			List<String> list = Arrays.asList("Household_Essentials","Beauty_Personal_Care","Gourmet","Chocolates_Snacks","Health_Wellness_Medicine","Baby_Care","Fragrances");
//			
//			String categoryUrl  = "http://affiliate-feeds.snapdeal.com/feed/"+affilatedId+".json";
//			String categoryJson = processURL(categoryUrl, affilatedId, token);
//			if(categoryJson!=null &&!"".equals(categoryJson.trim())&& UtilConstants.isJSONValid(categoryJson.trim())){
//				JSONObject jsonObject = new JSONObject(categoryJson).getJSONObject("apiGroups").getJSONObject("Affiliate").getJSONObject("listingsAvailable");
//				for (String category : list) {
//					String catUrl = jsonObject.getJSONObject(category).getJSONObject("listingVersions").getJSONObject("v1").getString("get");
//					map.put(category, catUrl);
//				}		
//			}else{
//				logger.error("[SnapDeal][getCategoryMap] categoryJson: "+categoryJson+" | json invalid or not found");
//			}
//			
//		}catch(Exception e){
//			logger.error("[SnapDeal][getCategoryMap] "+GetStackElements.getRootCause(e, getClass().getName()));
//		}		
//		return map;
//	}
//	
////	for fetching data from url
//	public String processURL(String address,String affId,String tknId) {
//
//		  StringBuffer res= new StringBuffer();
//		  try{
//		   URL url = new URL(address);
//		      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
//		      conn.setConnectTimeout(10000);
//		      conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36" );
//		      conn.setRequestProperty("Snapdeal-Affiliate-Id", affId);
//		      conn.setRequestProperty("Snapdeal-Token-Id",tknId);
//		      conn.setReadTimeout(40000);
//		      conn.setRequestMethod("GET");
//		      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//		      String inputLine;
//
//		      while ((inputLine = br.readLine()) != null) {
//		       res.append(inputLine);
//		      }
//		      br.close();
//
//		  }catch(Exception e){
////			  logger.error("[SnapDealGrocery] [processURL] :"+GetStackElements.getRootCause(e, getClass().getName()));
//		      res.append(e.getMessage());
//		  }
//		  return res.toString();
//		 }
//
//	@Override
//	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
//		  process(request,response);
//	}
//	 
//	@Override
//	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		  process(request,response);
//	}
//	
////	public static void main(String[] args){
////		try {
////			String affId ="91905";
////			String tknID ="346819ebd7a4e083cfa22da150cb60";
////			SnapDeal snapDeal = new SnapDeal();
////			Map<String, String> categoryMap = snapDeal.getCategoryMap(affId, tknID);
////			
////			if(categoryMap!=null && categoryMap.size()>0){
////				
////				for(Map.Entry<String, String> mp:categoryMap.entrySet()){
////					String category =mp.getKey();
////					String catUrl =mp.getValue();
////					
////					do{
////						String json =snapDeal.processURL(catUrl,affId,tknID);
////						if(json!=null &&!"".equals(json) && UtilConstants.isJSONValid(json)){					    
////						    
////						    JSONObject obj= new JSONObject(json);
////							JSONArray jsonArray= obj.has("products") ? obj.getJSONArray("products") : null;
////							catUrl = obj.has("nextUrl") ? obj.getString("nextUrl") : null;
////							if(jsonArray!=null &&jsonArray.length() >0){
////								
////								for(int i=0 ; i<jsonArray.length() ; i++){
////									JSONObject jsonObject = jsonArray.getJSONObject(i);
////									
////									String brand = jsonObject.has("brand") ? jsonObject.getString("brand") : "";
////									Long id = jsonObject.has("id") ? Long.valueOf(jsonObject.getString("id")) : 0l;
////									String link =jsonObject.has("link") ? jsonObject.getString("link") : "";
////									Long mrp =jsonObject.has("mrp") ? Long.valueOf(jsonObject.getString("mrp")) : 0l;
////									Long effectivePrice =jsonObject.has("effectivePrice") ? Long.valueOf(jsonObject.getString("effectivePrice")) : 0l;
////									String categoryName =jsonObject.has("categoryName") ? jsonObject.getString("categoryName") : "";
////									Long subcategoryId =jsonObject.has("subCategoryId") ? Long.valueOf(jsonObject.getString("subCategoryId")) : 0l;
////									String title =jsonObject.has("title") ? jsonObject.getString("title") : "";
////									String description =jsonObject.has("description") ? jsonObject.getString("description") : "";
////									String imageLink =jsonObject.has("imageLink") ? jsonObject.getString("imageLink") : "";
////									Long offerprice =jsonObject.has("offerprice") ? Long.valueOf(jsonObject.getString("offerprice")):0l;
////									String availablity =jsonObject.has("availability") ? jsonObject.getString("availability"):"";
////									String subCategoryName =jsonObject.has("subCategoryName") ? jsonObject.getString("subCategoryName") : "";	
////									
////									
////									if(id==508295||id==652297601665l){
////										System.out.println("id: "+id+" | "+title+" | "+mrp+" | "+effectivePrice+" | "+jsonObject);
////										break;
////									}
////								}
////							}else{
////								logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | products key not found");
////							}					    
////						}else{
////							logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | not valid json");
////						}						
////					}while(catUrl!=null && !"".equals(catUrl.trim())&&!"null".equalsIgnoreCase(catUrl.trim()));	
////					
////					}}
////		} catch (Exception e) {
////			// TODO: handle exception
////		}
////	}
//}
