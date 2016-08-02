package com.ahoy.parser.ui;

import com.ahoy.parser.api.Meragrocer;
import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.MerchantSubCatDo;
import com.ahoy.parser.domain.ParseDataDo;
import com.ahoy.parser.util.GetStackElements;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DownloadCsv extends HttpServlet{
	

	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(Meragrocer.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String merchantId = request.getParameter("merchantid");
		String cityId = request.getParameter("cityid");
		String listtype = request.getParameter("listtype");
		OutputStream fileOutputStream = null;
		HSSFWorkbook hssfWorkbook = null;
		try{
			if(merchantId!=null && merchantId.trim().matches("[0-9]+")
					&& cityId!=null && cityId.trim().matches("[0-9]+")
						&& listtype!=null && listtype.trim().matches("[0-9-]+")){
				
				
				CityDao cityDao = new CityDaoImpl();
				CityDo cityDo = cityDao.getByCityId(Long.valueOf(cityId));
				
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantId(Long.valueOf(merchantId));
				ParseDataDao parseDataDao = new ParseDataDaoImpl();
				
				response.setContentType("application/xls");
				response.addHeader("Content-Disposition","attachment;filename="+merchantDo.getMerchantName()+"_"+cityDo.getCityName()+"_"+(listtype.equals("1")?"NewItems":"OldItems")+".xls");
				
				fileOutputStream = response.getOutputStream();
				hssfWorkbook = new HSSFWorkbook();
				HSSFSheet hssfSheet = hssfWorkbook.createSheet("Itemlist");
				
				
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
				cell.setCellValue("product Id");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(2);
				cell.setCellValue("Description");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(3);
				cell.setCellValue("Variant Id");
				cell.setCellStyle(cellStyle);				

				cell = row.createCell(4);
				cell.setCellValue("Variant");
				cell.setCellStyle(cellStyle);				

				cell = row.createCell(5);
				cell.setCellValue("MRP");
				cell.setCellStyle(cellStyle);				

				cell = row.createCell(6);
				cell.setCellValue("Offers");
				cell.setCellStyle(cellStyle);
					

				cell = row.createCell(7);
				cell.setCellValue("Sell Price");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(8);
				cell.setCellValue("Image");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(9);
				cell.setCellValue("Category");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(10);
				cell.setCellValue("sub Cat");
				cell.setCellStyle(cellStyle);
				
				cell = row.createCell(11);
				cell.setCellValue("Created On");
				cell.setCellStyle(cellStyle);
				
				if(!listtype.equals("1")){
					cell = row.createCell(12);
					cell.setCellValue("Updated On");
					cell.setCellStyle(cellStyle);
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = format.format(new Date());
				 
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate = format.parse(todayDate+" 00:00:00");
				Date endDate = format.parse(todayDate+" 23:59:59");
				 
				List<ParseDataDo> parseDataDos = parseDataDao.select(listtype.equals("1")?"createdOn":(listtype.equals("2")?"updatedOn":"all"),cityDo.getCityId(),merchantDo.getMerchantId(), startDate, endDate,(short)-1, 0, 0);
				logger.info("[DownloadCsv][process] parseDataDos: "+(parseDataDos!=null?parseDataDos.size():null));
				
				if(parseDataDos!=null && parseDataDos.size()>0){
					
					DateFormat df = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
					
					cellStyle = hssfWorkbook.createCellStyle();
					cellStyle.setBorderBottom((short)1);
					cellStyle.setBorderTop((short)1);
					cellStyle.setBorderLeft((short)1);
					cellStyle.setBorderRight((short)1);
					
					
					for (ParseDataDo parseDataDo : parseDataDos) {
						MerchantSubCatDo merchantSubCatDo = parseDataDo.getMerchantSubCatDo();
						
						row = hssfSheet.createRow(++rowNum);
						cell = row.createCell(0);
						cell.setCellValue(rowNum);
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(1);
						cell.setCellValue(parseDataDo.getStorePId());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(2);
						cell.setCellValue(parseDataDo.getDescription());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(3);
						cell.setCellValue(parseDataDo.getStoreVId());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(4);
						cell.setCellValue(parseDataDo.getWeight());
						cell.setCellStyle(cellStyle);						
						
						cell = row.createCell(5);
						cell.setCellValue(parseDataDo.getMaxPrice());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(6);
						cell.setCellValue(parseDataDo.getOffer());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(7);
						cell.setCellValue(parseDataDo.getSellPrice());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(8);
						cell.setCellValue(parseDataDo.getImageUrl());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(9);
						cell.setCellValue(merchantSubCatDo.getMerchantCategoryDo().getCatName());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(10);
						cell.setCellValue(merchantSubCatDo.getSubCatName());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(11);
						cell.setCellValue(df.format(parseDataDo.getCreatedOn()));
						cell.setCellStyle(cellStyle);
						
						if(!listtype.equals("1")){
							cell = row.createCell(12);
							cell.setCellValue(df.format(parseDataDo.getUpdatedOn()));
							cell.setCellStyle(cellStyle);
						}
					}
				 }
				
			}else{
				logger.info("[DownloadCsv][process] merchantId: "+merchantId+" | cityId: "+cityId+" | listtype: "+listtype+" | invalid");
			}
			
			
		}catch (Exception e) {
			logger.error("[DownloadCsv][process] Exception: "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			hssfWorkbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
		
}
