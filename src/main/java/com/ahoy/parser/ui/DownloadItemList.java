package com.ahoy.parser.ui;

import com.ahoy.parser.dao.ItemListDao;
import com.ahoy.parser.dao.ItemListDaoImpl;
import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.ItemListDo;
import com.ahoy.parser.util.UtilConstants;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@SuppressWarnings("serial")
@WebServlet("/ditemlist")
public class DownloadItemList extends HttpServlet{
	
	Logger logger = LoggerFactory.getLogger(DownloadItemList.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		OutputStream fileOutputStream = null;
		HSSFWorkbook hssfWorkbook = null;
		try{
			DateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
			response.setContentType("application/xls");
			response.addHeader("Content-Disposition", "attachment; filename=ItemList_"+format.format(new Date().getTime())+".xls");
			
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
			cell.setCellValue("Item Id");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(2);
			cell.setCellValue("Item Description");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(3);
			cell.setCellValue("Category");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(4);
			cell.setCellValue("Sub-Category");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(5);
			cell.setCellValue("Weight");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(6);
			cell.setCellValue("MRP");
			cell.setCellStyle(cellStyle);			
			
			cell = row.createCell(7);
			cell.setCellValue("Sell Price");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(8);
			cell.setCellValue("Offer");
			cell.setCellStyle(cellStyle);
			
			cell = row.createCell(9);
			cell.setCellValue("Availability");
			cell.setCellStyle(cellStyle);
			
			ItemListDao itemListDao = new ItemListDaoImpl();
			List<ItemListDo> itemListDos = itemListDao.selectItems(0, 0);
			
			if(itemListDos!=null && itemListDos.size()>0){
				
				cellStyle = hssfWorkbook.createCellStyle();
				cellStyle.setBorderBottom((short)1);
				cellStyle.setBorderTop((short)1);
				cellStyle.setBorderLeft((short)1);
				cellStyle.setBorderRight((short)1);
				
				for (ItemListDo itemListDo : itemListDos) {
					
					Set<ItemDetailDo> itemDetailDos = itemListDo.getItemDetailDos();
					
					for (ItemDetailDo itemDetailDo : itemDetailDos) {					
					
						row = hssfSheet.createRow(++rowNum);
						
						cell = row.createCell(0);
						cell.setCellValue(rowNum);
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(1);
						cell.setCellValue(itemListDo.getItemId());
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(2);
						cell.setCellValue(UtilConstants.toCamelCase(itemListDo.getItemName()));
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(3);
						cell.setCellValue(UtilConstants.toCamelCase(itemListDo.getShopezzySubCatDo().getShopezzyCategoryDo().getCatName()));
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(4);
						cell.setCellValue(UtilConstants.toCamelCase(itemListDo.getShopezzySubCatDo().getSubCatName()));
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(5);
						cell.setCellValue(UtilConstants.toCamelCase(itemDetailDo.getWeight()));
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(6);
						cell.setCellValue(UtilConstants.toCamelCase(itemDetailDo.getMrp()));
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(7);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(8);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
						
						cell = row.createCell(9);
						cell.setCellValue("");
						cell.setCellStyle(cellStyle);
					}
				}
			}
			
			
			
		}catch (Exception e) {
			logger.error("[DownloadItemList][process] Exception: "+e);
		}finally{
			hssfWorkbook.write(fileOutputStream);
			fileOutputStream.flush();
			fileOutputStream.close();
		}
		
	}
		
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	
}
