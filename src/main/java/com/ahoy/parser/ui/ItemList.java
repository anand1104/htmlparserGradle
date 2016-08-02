package com.ahoy.parser.ui;

import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.ItemListDo;
import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;
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
@WebServlet("/itemlist")
public class ItemList extends HttpServlet{
	
	Logger logger = LoggerFactory.getLogger(ItemList.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		try{
			
			if(ServletFileUpload.isMultipartContent(request)){
				
				URL url = this.getClass().getResource("/parser.properties");					
				Properties properties = new Properties();
				properties.load(url.openStream());
				
				String filepath = properties.getProperty("filepath"); 
				
				SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");				
				List<FileItem> multipart = new ServletFileUpload(new DiskFileItemFactory()).parseRequest(request);
				String fileName =  multipart.get(0).getName();
				fileName =fileName.substring(0, fileName.lastIndexOf("."))+"_"+format.format(new Date().getTime())+fileName.substring(fileName.lastIndexOf("."));
				
				File file = new File(filepath+File.separator+fileName);
				multipart.get(0).write(file);
				
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
					
					String itemName = this.getCellValue(row.getCell(1));
					String weight = this.getCellValue(row.getCell(2));
					String mrp = this.getCellValue(row.getCell(3));
					String category = this.getCellValue(row.getCell(4));
					String subCategory = this.getCellValue(row.getCell(5));
					
					ItemListDao itemListDao = new ItemListDaoImpl();
					ItemDetailDao itemDetailDao = new ItemDetailDaoImpl();
					ShopezzySubCatDao shopezzySubCatDao = new ShopezzySubCatDaoImpl();
					ShopezzyCategoryDao shopezzyCategoryDao = new ShopezzyCategoryDaoImpl();
					
					ItemListDo itemListDo = itemListDao.getByItemName(itemName.trim().toLowerCase());
					
					if(itemListDo!=null){
						ItemDetailDo itemDetailDo = itemDetailDao.selectByWeightAnditemList(itemListDo, weight.trim().toLowerCase());
						if(itemDetailDo!=null){
							if(itemDetailDo.getMrp().equals(mrp.toLowerCase())){
								logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" already available in itemlist and itemdetail ");
							}else{
								String oldMrp = itemDetailDo.getMrp();
								itemDetailDo.setMrp(mrp.toLowerCase());
								itemDetailDo.setUpdatedOn(new Timestamp(new Date().getTime()));
								boolean flag = itemDetailDao.saveOrUpdate(itemDetailDo);
								if(flag){
									logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" already available in itemlist and itemdetail is update old mrp: "+oldMrp);
								}
							}
						}else{
							itemDetailDo = new ItemDetailDo();
							itemDetailDo.setItemListDo(itemListDo);
							itemDetailDo.setWeight(weight.toLowerCase());
							itemDetailDo.setMrp(mrp.toLowerCase());
							itemDetailDo.setCreatedOn(new Timestamp(new Date().getTime()));
							itemDetailDo.setUpdatedOn(new Timestamp(new Date().getTime()));
							long itemDeailId = itemDetailDao.save(itemDetailDo);
							
							if(itemDeailId>0){
								logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" already available in itemlist | saved --> itemDeailId: "+itemDeailId);
							}else{
								logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" already available in itemlist | not saved --> itemDeailId: "+itemDeailId);
							}
						}												
					}else{
						
						ShopezzyCategoryDo shopezzyCategoryDo = shopezzyCategoryDao.selectByCatName(category.toLowerCase());
						if(shopezzyCategoryDo!=null){
							
							ShopezzySubCatDo shopezzySubCatDo = shopezzySubCatDao.selectBySubCatNameAndCat(subCategory.toLowerCase(), shopezzyCategoryDo);
							if(shopezzySubCatDo!=null){
						
								itemListDo = new ItemListDo();
								itemListDo.setItemName(itemName.trim().toLowerCase());
								itemListDo.setShopezzySubCatDo(shopezzySubCatDo);
								itemListDo.setCreatedOn(new Timestamp(new Date().getTime()));
								itemListDo.setUpdatedOn(new Timestamp(new Date().getTime()));
								long itemListId = itemListDao.save(itemListDo);
						
								if(itemListId>0){
								
									itemListDo.setItemId(itemListId);
									
									ItemDetailDo itemDetailDo = new ItemDetailDo();
									itemDetailDo.setItemListDo(itemListDo);
									itemDetailDo.setWeight(weight);
									itemDetailDo.setMrp(mrp);
									itemDetailDo.setCreatedOn(new Timestamp(new Date().getTime()));
									itemDetailDo.setUpdatedOn(new Timestamp(new Date().getTime()));
									long itemDeailId = itemDetailDao.save(itemDetailDo);
									if(itemDeailId>0){
										logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" | saved --> itemListId:  "+itemListId+" | itemDeailId: "+itemDeailId);
									}else{
										logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" | not saved --> itemListId:  "+itemListId+" | itemDeailId: "+itemDeailId);
									}
									
								}else{
									logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" | saved --> itemListId:  "+itemListId);
								}
							}else{
								logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" sub category not matched");
							}
						}else{
							logger.info("[ItemList][prcoess] Item Description: "+itemName+" | weight: "+weight+" | mrp: "+mrp+" | category: "+category+" | sub cat Name: "+subCategory+" category not matched");
						}	
					}					
					
					request.setAttribute("resp","upload successfully");
				}
				
			}
			
			int pageno = (request.getParameter("pageno")!=null&&request.getParameter("pageno").matches("[0-9]+"))?Integer.valueOf(request.getParameter("pageno")):0;
			 long totalpage = (request.getParameter("totalpage")!=null&&request.getParameter("totalpage").matches("[0-9]+"))?Integer.valueOf(request.getParameter("totalpage")):0;
			 int curpagegroup = (request.getParameter("curpagegroup")!=null&&request.getParameter("curpagegroup").matches("[0-9]+"))?Integer.valueOf(request.getParameter("curpagegroup")):0;
			 int recordPerPage = UtilConstants.RECORD_PER_PAGE;	
			 logger.info("[ItemList][process] pageno: "+pageno+" | totalpage: "+totalpage+" | curpagegroup: "+curpagegroup+" | recordPerPage: "+recordPerPage+" | ");
			
			
			ItemListDao itemListDao = new ItemListDaoImpl();
			List<ItemListDo> itemListDos = itemListDao.selectItems(pageno*recordPerPage, recordPerPage);
			long totalCount = itemListDao.getCount();
			logger.info("[ItemList][process] itemListDos Size: "+(itemListDos!=null?itemListDos.size():null)+" | getCount: "+totalCount);
			
			request.setAttribute("itemListDos", itemListDos);
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
			request.setAttribute("recordperpage", UtilConstants.RECORD_PER_PAGE);
//				End Pagination				 
			
		}catch (Exception e) {
			logger.error("[ItemList][process] Exception: "+e+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			RequestDispatcher rd = request.getRequestDispatcher("itemlist.jsp");
			rd.forward(request, response);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
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
	
	public static void main(String []args){
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");	
		String fileName =  "text.1.xls";
		fileName =fileName.substring(0, fileName.lastIndexOf("."))+"_"+format.format(new Date().getTime())+fileName.substring(fileName.lastIndexOf("."));
		System.out.println(fileName);
		
	}
}
