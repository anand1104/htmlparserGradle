package com.ahoy.parser.ui;

import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.ParseDataDo;
import com.ahoy.parser.util.UtilConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class View extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(View.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String cityid = request.getParameter("cityid");
		String merchantid = request.getParameter("merchantid");
		String listtype = request.getParameter("listtype");
		String redirectPath="";

		try{
			 if(merchantid!=null && merchantid.trim().matches("[0-9]+")){
				 
				 int pageno = (request.getParameter("pageno")!=null&&request.getParameter("pageno").matches("[0-9]+"))?Integer.valueOf(request.getParameter("pageno")):0;
				 long totalpage = (request.getParameter("totalpage")!=null&&request.getParameter("totalpage").matches("[0-9]+"))?Integer.valueOf(request.getParameter("totalpage")):0;
				 int curpagegroup = (request.getParameter("curpagegroup")!=null&&request.getParameter("curpagegroup").matches("[0-9]+"))?Integer.valueOf(request.getParameter("curpagegroup")):0;
				 int recordPerPage = UtilConstants.RECORD_PER_PAGE;	
				 logger.info("pageno: "+pageno+" | totalpage: "+totalpage+" | curpagegroup: "+curpagegroup+" | recordPerPage: "+recordPerPage+" | ");
					
				 CityDao cityDao = new CityDaoImpl();
				 CityDo cityDo = cityDao.getByCityId(Long.valueOf(cityid));
				 
				 
				 MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantId(Long.valueOf(merchantid));
				 ParseDataDao parseDataDao = new ParseDataDaoImpl();
				 
				 SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				 String todayDate = format.format(new Date());
				 
				 format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				 Date startDate = format.parse(todayDate+" 00:00:00");
				 Date endDate = format.parse(todayDate+" 23:59:59");
				 
				 List<ParseDataDo> parseDataDos = parseDataDao.select(listtype.equals("1")?"createdOn":(listtype.equals("2")?"updatedOn":"all"),cityDo.getCityId(),merchantDo.getMerchantId(), startDate, endDate,(short)-1, pageno*recordPerPage, recordPerPage);
				 long totalCount = parseDataDao.getCount(listtype.equals("1")?"createdOn":(listtype.equals("2")?"updatedOn":"all"),cityDo.getCityId(), merchantDo.getMerchantId(), startDate, endDate);
				 logger.info("[View][doPost] parseDataDos Size: "+(parseDataDos!=null?parseDataDos.size():null)+" | totalCount: "+totalCount);
				 
				 request.setAttribute("parseDataDos", parseDataDos);
				 request.setAttribute("totalCount", totalCount);
				 request.setAttribute("merchantid", merchantid);
				 request.setAttribute("merchantName", merchantDo.getMerchantName());
				 request.setAttribute("cityid", cityid);
				 request.setAttribute("cityName", cityDo.getCityName());
				 request.setAttribute("listtype", listtype);
				 
				 
				 request.setAttribute("curpage", pageno);
//					Start Pagination
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
//					End Pagination				 
				 
				 redirectPath="view.jsp";
			 }else{
				 logger.info("[View][doPost] merchantid is not valid");
				 request.setAttribute("resp", "Please Select Merchant");
				 redirectPath="selectmerchant.jsp";
			 }
		 } catch(Exception e){
			logger.error("[View][doPost] Exception: "+e); 
		 }finally{
			 logger.info("[View][doPost] redirectPath: "+redirectPath);
			 RequestDispatcher rd = request.getRequestDispatcher(redirectPath);
			 rd.forward(request, response);
		 }
		
	}
	  
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
}
