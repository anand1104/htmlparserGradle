package com.ahoy.parser.ui;

import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.ItemListDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.OfflineItemDo;
import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.UtilConstants;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

@SuppressWarnings("serial")
@WebServlet("/offlineitem")
public class OfflineItemDetail extends HttpServlet{
	
Logger logger = LoggerFactory.getLogger(ItemList.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String merchantId = request.getParameter("merchantid");		
		
		try{
			
			
			if(ServletFileUpload.isMultipartContent(request)){
				
				URL url = this.getClass().getResource("/parser.properties");					
				Properties properties = new Properties();
				properties.load(url.openStream());
				
				String filepath = properties.getProperty("filepath"); 
				
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");				
				List<FileItem> multipart = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				
				merchantId = multipart.get(0).getString();				
				String fileName =  multipart.get(1).getName();
				logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | Uploaded fileName: "+fileName);
				fileName =fileName.substring(0, fileName.lastIndexOf("."))+"_"+format.format(new Date().getTime())+fileName.substring(fileName.lastIndexOf("."));
				
				logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | change fileName: "+fileName);
				
				File file = new File(filepath+File.separator+fileName);
				multipart.get(1).write(file);
				
				FileInputStream fis = new FileInputStream(file);
				Workbook workbook = null;
				if(fileName.toLowerCase().endsWith(".xlsx")){
					workbook = new XSSFWorkbook (fis);
				}else{
					workbook = new HSSFWorkbook(fis);
				}
				
				Sheet mySheet = workbook.getSheetAt(0);
				
				Iterator<Row> rowIterator = mySheet.iterator();			
				
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					
					if(row.getRowNum()==0){
						continue;
					}
					
					String itemId = this.getCellValue(row.getCell(1));
					String itemName = this.getCellValue(row.getCell(2));
					String category = this.getCellValue(row.getCell(3));
					String subCategory = this.getCellValue(row.getCell(4));
					String weight = this.getCellValue(row.getCell(5));
					String mrp = this.getCellValue(row.getCell(6));
					String sellPrice = this.getCellValue(row.getCell(7));
					String offer = this.getCellValue(row.getCell(8));
					String availability = this.getCellValue(row.getCell(9));
					
					
					
					if(availability!=null && availability.trim().equalsIgnoreCase("y")){
						
						itemId =  itemId.matches("[0-9]+(?:\\.[0-9]+)?")?Math.round(Double.parseDouble(itemId))+"":itemId;
						
						if((itemId.matches("[0-9]+")|| !itemName.equals(""))&&!"".equals(weight)&&!"".equals(sellPrice)){
							ItemListDo itemListDo = null;
							if(itemId.trim().matches("[0-9]+")){
								itemListDo = new ItemListDo();
								itemListDo.setItemId(Long.valueOf(itemId));
							}else {
								ItemListDao itemListDao = new ItemListDaoImpl();
								itemListDo = itemListDao.getByItemName(itemName.trim().toLowerCase());
							}
							
							ItemDetailDao itemDetailDao = new ItemDetailDaoImpl();
							ItemDetailDo itemDetailDo = itemDetailDao.selectByWeightAnditemList(itemListDo, weight);
							
							OfflineItemDao offlineItemDao = new OfflineItemDaoImpl();
							MerchantDo merchantDo = new MerchantDo();
							merchantDo.setMerchantId(Long.valueOf(merchantId));
							
							OfflineItemDo offlineItemDo = offlineItemDao.getByMerchantAndItemDetail(merchantDo, itemDetailDo);
							
							if(offlineItemDo==null){
								offlineItemDo = new OfflineItemDo();
								offlineItemDo.setMerchantDo(merchantDo);
								offlineItemDo.setItemDetailDo(itemDetailDo);
								offlineItemDo.setSellPrice(sellPrice);
								offlineItemDo.setOffer(offer);
								offlineItemDo.setCreatedOn(new Timestamp(new Date().getTime()));
								offlineItemDo.setUpdatedOn(new Timestamp(new Date().getTime()));
								
								boolean flag = offlineItemDao.saveOrUpdate(offlineItemDo);
								
								if(flag){
									logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | itemId: "+itemId+" | itemName: "+itemName+" | category: "+category+" | subCategory: "+subCategory+" | weight: "+weight+" | mrp: "+mrp+" | sellPrice: "+sellPrice+" | offer: "+offer+" | availability: "+availability+" | save successfully");
								}else{
									logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | itemId: "+itemId+" | itemName: "+itemName+" | category: "+category+" | subCategory: "+subCategory+" | weight: "+weight+" | mrp: "+mrp+" | sellPrice: "+sellPrice+" | offer: "+offer+" | availability: "+availability+" | some problem in offlineItem addition");
								}
							}else{
								logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | itemId: "+itemId+" | itemName: "+itemName+" | category: "+category+" | subCategory: "+subCategory+" | weight: "+weight+" | mrp: "+mrp+" | sellPrice: "+sellPrice+" | offer: "+offer+" | availability: "+availability+" | Item already available for this merchant");
							}
							
						}else{
							logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | itemId: "+itemId+" | itemName: "+itemName+" | category: "+category+" | subCategory: "+subCategory+" | weight: "+weight+" | mrp: "+mrp+" | sellPrice: "+sellPrice+" | offer: "+offer+" | availability: "+availability+" | something is missed !!");
						}						
					}else{
						logger.info("[OfflineItemDetail][process] merchantId: "+merchantId+" | itemId: "+itemId+" | itemName: "+itemName+" | category: "+category+" | subCategory: "+subCategory+" | weight: "+weight+" | mrp: "+mrp+" | sellPrice: "+sellPrice+" | offer: "+offer+" | availability: "+availability);
					}				
				}
				request.setAttribute("resp", "Upload Successfully");	
			}	
			
			 int pageno = (request.getParameter("pageno")!=null&&request.getParameter("pageno").matches("[0-9]+"))?Integer.valueOf(request.getParameter("pageno")):0;
			 long totalpage = (request.getParameter("totalpage")!=null&&request.getParameter("totalpage").matches("[0-9]+"))?Integer.valueOf(request.getParameter("totalpage")):0;
			 int curpagegroup = (request.getParameter("curpagegroup")!=null&&request.getParameter("curpagegroup").matches("[0-9]+"))?Integer.valueOf(request.getParameter("curpagegroup")):0;
			 int recordPerPage = UtilConstants.RECORD_PER_PAGE;	
			 logger.info("[OfflineItemDetail][process] pageno: "+pageno+" | totalpage: "+totalpage+" | curpagegroup: "+curpagegroup+" | recordPerPage: "+recordPerPage+" | ");
			
			
			 MerchantDao merchantDao = new MerchantDaoImpl();
			 List<MerchantDo> merchantDos = merchantDao.getByType((short)0);
			 request.setAttribute("merchantDos", merchantDos);
			 request.setAttribute("merchantId", merchantId);
			 logger.info("[OfflineItemDetail][process] merchantDos: "+(merchantDos!=null?merchantDos.size():null));
			 
			 MerchantDo merchantDo = merchantId!=null&&merchantId.trim().matches("[0-9]+")?merchantDao.getByMerchantId(Long.valueOf(merchantId)):null;
			 
			 OfflineItemDao offlineItemDao = new OfflineItemDaoImpl();
			
			 long totalCount = offlineItemDao.getCount(merchantDo);
			 List<OfflineItemDo> offlineItemDos = offlineItemDao.getByMerchant(merchantDo, pageno*recordPerPage, recordPerPage);
			 logger.info("[OfflineItemDetail][process] totalCount: "+totalCount+" | offlineItemDos: "+(offlineItemDos!=null?offlineItemDos.size():null));
			 
			 request.setAttribute("offlineItemDos", offlineItemDos);
			 request.setAttribute("totalCount", totalCount);
			 
			 request.setAttribute("curpage", pageno);
//				Start Pagination
				pageno = ((curpagegroup * 10));
	            
	            if(totalCount != 0 && totalCount> recordPerPage){
	            	totalpage = (totalCount / recordPerPage);
	    			if (totalCount % recordPerPage != 0) {
	    				totalpage = totalpage + 1;
	    			}
	            }
	            
	            long pagegroup = totalpage / 10;
				if (totalpage % 10 != 0) {
					pagegroup = pagegroup + 1;
				}
				pagegroup = pagegroup - 1;
				
				boolean resultpagegroup = false;
				if (curpagegroup < pagegroup) {
					resultpagegroup = true;
				}
				
				request.setAttribute("pageno", pageno);
				request.setAttribute("pagegroup", pagegroup);			
				request.setAttribute("totalpage", totalpage);			
				request.setAttribute("curpagegroup", curpagegroup);
				request.setAttribute("resultpagegroup", resultpagegroup);	
//				End Pagination	
			 
			 
		}catch (Exception e) {
			logger.error("[OfflineItemDetail][process] Exception: "+e+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			RequestDispatcher rd = request.getRequestDispatcher("offlineitem.jsp");
			rd.forward(request, response);
		}
	}
	
	private String getCellValue(Cell cell){
		String str="";
		try{
			if(cell!=null){
				switch (cell.getCellType()) {
		        case Cell.CELL_TYPE_STRING:
		            str = cell.getStringCellValue();
		            break;
		        case Cell.CELL_TYPE_NUMERIC:
		        	
		        	str = cell.getNumericCellValue()+"";
		            break;
		        case Cell.CELL_TYPE_BOOLEAN:
		            str = cell.getBooleanCellValue()+"";;
		            break;
		        default :
		        	str = cell.toString();
		        }
			}
		}catch (Exception e) {
			logger.error("[ItemList][getCellValue] Exception: "+e);
		}
		return str;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	public static void main(String[] args){
		System.out.println(""+Math.round(Double.parseDouble("10.6")));
	}
	
}
