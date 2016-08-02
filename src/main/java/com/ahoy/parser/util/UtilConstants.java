package com.ahoy.parser.util;

import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.ParseDataDo;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class UtilConstants {
	
	public static int RECORD_PER_PAGE=50;
	
	public static boolean isJSONValid(String jsonString) {
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
	
	public static String toCamelCase(String str) {
		StringBuilder builder = new StringBuilder();
	    
		try{
			if(str!=null && str.length()>0){
				
		       char firstChar = str.charAt(0);
		       char firstCharToUpperCase = Character.toUpperCase(firstChar);
		       builder.append(firstCharToUpperCase);

		       for (int i = 1; i < str.length(); i++) {
		           char currentChar = str.charAt(i);
		           char previousChar = str.charAt(i - 1);
		           
		           if (previousChar == ' ') {
		               char currentCharToUpperCase = Character.toUpperCase(currentChar);
		               builder.append(currentCharToUpperCase);
		           } else {
		               char currentCharToLowerCase = Character.toLowerCase(currentChar);
		               builder.append(currentCharToLowerCase);
		           }
		       }
	    	   
			}else{
				builder.append("");
			}
	       }catch (Exception e) {
	    	   builder=null;
	    	   builder = new StringBuilder();
	    	   builder.append(str);
	       }	       
	       return builder.toString();
	}

	
	public static String saveFileOnServer(Properties properties,CityDo cityDo,MerchantDo merchantDo){
		FileOutputStream fileOutputStream = null;
		String fileNameWithpath = null;
		String resp = null;
		try{
			
						
			String filepath = properties.getProperty("parseXls");
			
			if(filepath!=null &&!"".equals(filepath.trim())&&cityDo!=null && merchantDo!=null){
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = format.format(new Date());			 
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date sdate = format.parse(todayDate+" 00:00:00");
				Date edate = format.parse(todayDate+" 23:59:59");
				
				fileNameWithpath=filepath+File.separator+merchantDo.getMerchantName().trim().replaceAll(" ", "_")+"_"+cityDo.getCityName()+".xls";
								
				List<ParseDataDo> parseDataDos = new ParseDataDaoImpl().getByCityDoAndMerchantDo(merchantDo.getMerchantId(), cityDo.getCityId(), sdate, edate);
				
				if(parseDataDos!=null && parseDataDos.size()>0){					
					
					fileOutputStream = new FileOutputStream(new File(fileNameWithpath));
					HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
					HSSFSheet hssfSheet = hssfWorkbook.createSheet(merchantDo.getMerchantName()+"_"+cityDo.getCityName());
					short rowNum =0;
					HSSFRow row = hssfSheet.createRow(rowNum);
					
					HSSFCellStyle cellStyle = hssfWorkbook.createCellStyle();
					cellStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);
					cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
					cellStyle.setBorderBottom((short)1);
					cellStyle.setBorderTop((short)1);
					cellStyle.setBorderLeft((short)1);
					cellStyle.setBorderRight((short)1);
					cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER );
					
					HSSFCell cell = row.createCell(0);
					cell.setCellValue("SNo.");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(1);
					cell.setCellValue("Item Name");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(2);
					cell.setCellValue("Category");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(3);
					cell.setCellValue("Sub Category");
					cell.setCellStyle(cellStyle);
													
					cell = row.createCell(4);
					cell.setCellValue("Variant");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(5);
					cell.setCellValue("MRP");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(6);
					cell.setCellValue("Sell Price");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(7);
					cell.setCellValue("Offer");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(8);
					cell.setCellValue("item id");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(9);
					cell.setCellValue("Variant id");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(10);
					cell.setCellValue("sku");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(11);
					cell.setCellValue("Availablity");
					cell.setCellStyle(cellStyle);
					
					cell = row.createCell(12);
					cell.setCellValue("Link");
					cell.setCellStyle(cellStyle);
					
					cellStyle = hssfWorkbook.createCellStyle();
					cellStyle.setBorderBottom((short)1);
					cellStyle.setBorderTop((short)1);
					cellStyle.setBorderLeft((short)1);
					cellStyle.setBorderRight((short)1);
					
					for (ParseDataDo parseDataDo : parseDataDos) {
						row = hssfSheet.createRow(++rowNum);
						cell = row.createCell(0);
						cell.setCellValue(rowNum);
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(1);
						cell.setCellValue(parseDataDo.getDescription());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(2);
						cell.setCellValue(parseDataDo.getMerchantSubCatDo().getMerchantCategoryDo().getCatName());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(3);
						cell.setCellValue(parseDataDo.getMerchantSubCatDo().getSubCatName());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(4);
						cell.setCellValue(parseDataDo.getWeight());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(5);
						cell.setCellValue(parseDataDo.getMaxPrice());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(6);
						cell.setCellValue(parseDataDo.getSellPrice());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(7);
						cell.setCellValue(parseDataDo.getOffer());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(8);
						cell.setCellValue(parseDataDo.getStorePId());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(9);
						cell.setCellValue(parseDataDo.getStoreVId());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(10);
						cell.setCellValue(parseDataDo.getSku());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(11);
						cell.setCellValue(parseDataDo.getAvailablity()==1?"Available":"Out of Stock");
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(12);
						cell.setCellValue(parseDataDo.getLink());
						cell.setCellStyle(cellStyle);
					}
					
					hssfWorkbook.write(fileOutputStream);
				}
				resp = "Success|"+fileNameWithpath;
			}else{
				if(cityDo==null){
					resp = "cityDo is "+cityDo;
				}else if(merchantDo==null){
					resp="merchantDo is "+merchantDo;
				}else{
					resp = "file path is "+filepath;
				}
			}			
		}catch(Exception e){
			resp = GetStackElements.getRootCause(e, UtilConstants.class.getName());
		}finally{
			if(fileOutputStream!=null){
				try{
					fileOutputStream.flush();
					fileOutputStream.close();
				}catch(IOException ie){}
				
			}
		}
		return resp;
	}
		
	public static String processURL(String address) {
		
		StringBuffer res= new StringBuffer();		     
		try{
			URL url = new URL(address);
		    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		    conn.setConnectTimeout(5000);
		    conn.setReadTimeout(2000);
		    conn.setRequestMethod("POST");
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
	
	public static String pathVariable(String str, Short type){		
		return type==1?str.replaceAll(" & ", "-and-").replaceAll(" ", "-"):str.replaceAll("-and-", " & ").replaceAll("-", " ");
	}
	
}
