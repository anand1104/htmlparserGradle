package com.ahoy.parser.api;

import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.UtilConstants;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@WebServlet("/snapDeal")
public class SnapDeal extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static Logger logger = LoggerFactory.getLogger(SnapDeal.class);

	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException{

		HSSFWorkbook workbook = new HSSFWorkbook();
		String resp = null;
		try {
			String affId ="91905";
			String tknID ="346819ebd7a4e083cfa22da150cb60";
			
			Map<String, String> categoryMap = this.getCategoryMap(affId, tknID);
			
			if(categoryMap!=null && categoryMap.size()>0){
				
				for(Map.Entry<String, String> mp:categoryMap.entrySet()){
					String category =mp.getKey();
					String catUrl =mp.getValue();
					HSSFSheet sheet = workbook.createSheet(category);
				    Row header = sheet.createRow(0);
				    header.createCell(0).setCellValue("SNo");
				    header.createCell(1).setCellValue("id");
				    header.createCell(2).setCellValue("title");
				    header.createCell(3).setCellValue("mrp");
				    header.createCell(4).setCellValue("effectivePrice");
				    header.createCell(5).setCellValue("offerprice");
				    header.createCell(6).setCellValue("link");
				    header.createCell(7).setCellValue("availablity");
				    header.createCell(8).setCellValue("brand");
				    header.createCell(9).setCellValue("Category");
				    header.createCell(10).setCellValue("subcategory");
				    header.createCell(11).setCellValue("subcategoryId");			    			    
				    header.createCell(12).setCellValue("description");
				    header.createCell(13).setCellValue("imageLink");
				    int rowNum=0;
					do{
						String json =this.processURL(catUrl,affId,tknID);
						if(json!=null &&!"".equals(json) && UtilConstants.isJSONValid(json)){					    
						    
						    JSONObject obj= new JSONObject(json);
							JSONArray jsonArray= obj.has("products") ? obj.getJSONArray("products") : null;
							catUrl = obj.has("nextUrl") ? obj.getString("nextUrl") : null;
							if(jsonArray!=null &&jsonArray.length() >0){
								
								for(int i=0 ; i<jsonArray.length() ; i++){
									JSONObject jsonObject = jsonArray.getJSONObject(i);
									
									String brand = jsonObject.has("brand") ? jsonObject.getString("brand") : "";
									Long id = jsonObject.has("id") ? Long.valueOf(jsonObject.getString("id")) : 0l;
									String link =jsonObject.has("link") ? jsonObject.getString("link") : "";
									Long mrp =jsonObject.has("mrp") ? Long.valueOf(jsonObject.getString("mrp")) : 0l;
									Long effectivePrice =jsonObject.has("effectivePrice") ? Long.valueOf(jsonObject.getString("effectivePrice")) : 0l;
									String categoryName =jsonObject.has("categoryName") ? jsonObject.getString("categoryName") : "";
									Long subcategoryId =jsonObject.has("subCategoryId") ? Long.valueOf(jsonObject.getString("subCategoryId")) : 0l;
									String title =jsonObject.has("title") ? jsonObject.getString("title") : "";
									String description =jsonObject.has("description") ? jsonObject.getString("description") : "";
									String imageLink =jsonObject.has("imageLink") ? jsonObject.getString("imageLink") : "";
									Long offerprice =jsonObject.has("offerprice") ? Long.valueOf(jsonObject.getString("offerprice")):0l;
									String availablity =jsonObject.has("availability") ? jsonObject.getString("availability"):"";
									String subCategoryName =jsonObject.has("subCategoryName") ? jsonObject.getString("subCategoryName") : "";	
									
									Row dataRow = sheet.createRow(++rowNum);
									dataRow.createCell(0).setCellValue(rowNum);
									dataRow.createCell(1).setCellValue(id);
									dataRow.createCell(2).setCellValue(title);
									dataRow.createCell(3).setCellValue(mrp);
								    dataRow.createCell(4).setCellValue(effectivePrice);
								    dataRow.createCell(5).setCellValue(offerprice);
								    dataRow.createCell(6).setCellValue(link);
								    dataRow.createCell(7).setCellValue(availablity);							    
								    dataRow.createCell(8).setCellValue(brand);
									dataRow.createCell(9).setCellValue(categoryName);
									dataRow.createCell(10).setCellValue(subCategoryName);
									dataRow.createCell(11).setCellValue(subcategoryId);
									dataRow.createCell(12).setCellValue(description);
								    dataRow.createCell(13).setCellValue(imageLink);							    								
								}
							}else{
								logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | products key not found");
							}					    
						}else{
							logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | not valid json");
						}						
					}while(catUrl!=null && !"".equals(catUrl.trim())&&!"null".equalsIgnoreCase(catUrl.trim()));	
						
				}
				resp = "Success";
				
			}else{
				resp = "category url not found";
			}
			
		}catch (Exception e) {
			resp = "Internal Error";
			logger.error("[SnapDeal][process] "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			FileOutputStream out = new FileOutputStream(new File("/home/applogs/ShopEZZY/HtmlParser/SnapdealGrocery.xls"));
	        workbook.write(out);
	        out.close();
	        logger.info("[SnapDeal][process] resp: "+resp);

		}		
	}


//	method for url as per category
	public Map<String,String> getCategoryMap(String affilatedId,String token){
		Map<String,String> map =new TreeMap<String,String>();
		try{
//			List<String> list = Arrays.asList("Household_Essentials","Health_Wellness_Medicine");
			List<String> list = Arrays.asList("Household_Essentials","Beauty_Personal_Care","Gourmet","Chocolates_Snacks","Health_Wellness_Medicine","Baby_Care","Fragrances");
			
			String categoryUrl  = "http://affiliate-feeds.snapdeal.com/feed/"+affilatedId+".json";
			String categoryJson = processURL(categoryUrl, affilatedId, token);
			if(categoryJson!=null &&!"".equals(categoryJson.trim())&& UtilConstants.isJSONValid(categoryJson.trim())){
				JSONObject jsonObject = new JSONObject(categoryJson).getJSONObject("apiGroups").getJSONObject("Affiliate").getJSONObject("listingsAvailable");
				for (String category : list) {
					String catUrl = jsonObject.getJSONObject(category).getJSONObject("listingVersions").getJSONObject("v1").getString("get");
					map.put(category, catUrl);
				}		
			}else{
				logger.error("[SnapDeal][getCategoryMap] categoryJson: "+categoryJson+" | json invalid or not found");
			}
			
		}catch(Exception e){
			logger.error("[SnapDeal][getCategoryMap] "+GetStackElements.getRootCause(e, getClass().getName()));
		}		
		return map;
	}
	
//	for fetching data from url
	public String processURL(String address,String affId,String tknId) {

		  StringBuffer res= new StringBuffer();
		  try{
		   URL url = new URL(address);
		      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		      conn.setConnectTimeout(10000);
		      conn.setRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.110 Safari/537.36" );
		      conn.setRequestProperty("Snapdeal-Affiliate-Id", affId);
		      conn.setRequestProperty("Snapdeal-Token-Id",tknId);
		      conn.setReadTimeout(40000);
		      conn.setRequestMethod("GET");
		      BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		      String inputLine;

		      while ((inputLine = br.readLine()) != null) {
		       res.append(inputLine);
		      }
		      br.close();

		  }catch(Exception e){
//			  logger.error("[SnapDealGrocery] [processURL] :"+GetStackElements.getRootCause(e, getClass().getName()));
		      res.append(e.getMessage());
		  }
		  return res.toString();
		 }

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	 
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
//	public static void main(String[] args){
//		try {
//			String affId ="91905";
//			String tknID ="346819ebd7a4e083cfa22da150cb60";
//			SnapDeal snapDeal = new SnapDeal();
//			Map<String, String> categoryMap = snapDeal.getCategoryMap(affId, tknID);
//			
//			if(categoryMap!=null && categoryMap.size()>0){
//				
//				for(Map.Entry<String, String> mp:categoryMap.entrySet()){
//					String category =mp.getKey();
//					String catUrl =mp.getValue();
//					
//					do{
//						String json =snapDeal.processURL(catUrl,affId,tknID);
//						if(json!=null &&!"".equals(json) && UtilConstants.isJSONValid(json)){					    
//						    
//						    JSONObject obj= new JSONObject(json);
//							JSONArray jsonArray= obj.has("products") ? obj.getJSONArray("products") : null;
//							catUrl = obj.has("nextUrl") ? obj.getString("nextUrl") : null;
//							if(jsonArray!=null &&jsonArray.length() >0){
//								
//								for(int i=0 ; i<jsonArray.length() ; i++){
//									JSONObject jsonObject = jsonArray.getJSONObject(i);
//									
//									String brand = jsonObject.has("brand") ? jsonObject.getString("brand") : "";
//									Long id = jsonObject.has("id") ? Long.valueOf(jsonObject.getString("id")) : 0l;
//									String link =jsonObject.has("link") ? jsonObject.getString("link") : "";
//									Long mrp =jsonObject.has("mrp") ? Long.valueOf(jsonObject.getString("mrp")) : 0l;
//									Long effectivePrice =jsonObject.has("effectivePrice") ? Long.valueOf(jsonObject.getString("effectivePrice")) : 0l;
//									String categoryName =jsonObject.has("categoryName") ? jsonObject.getString("categoryName") : "";
//									Long subcategoryId =jsonObject.has("subCategoryId") ? Long.valueOf(jsonObject.getString("subCategoryId")) : 0l;
//									String title =jsonObject.has("title") ? jsonObject.getString("title") : "";
//									String description =jsonObject.has("description") ? jsonObject.getString("description") : "";
//									String imageLink =jsonObject.has("imageLink") ? jsonObject.getString("imageLink") : "";
//									Long offerprice =jsonObject.has("offerprice") ? Long.valueOf(jsonObject.getString("offerprice")):0l;
//									String availablity =jsonObject.has("availability") ? jsonObject.getString("availability"):"";
//									String subCategoryName =jsonObject.has("subCategoryName") ? jsonObject.getString("subCategoryName") : "";	
//									
//									
//									if(id==508295||id==652297601665l){
//										System.out.println("id: "+id+" | "+title+" | "+mrp+" | "+effectivePrice+" | "+jsonObject);
//										break;
//									}
//								}
//							}else{
//								logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | products key not found");
//							}					    
//						}else{
//							logger.info("[SnapDeal][process] category: "+category+" | json: "+json+" | not valid json");
//						}						
//					}while(catUrl!=null && !"".equals(catUrl.trim())&&!"null".equalsIgnoreCase(catUrl.trim()));	
//					
//					}}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
}
