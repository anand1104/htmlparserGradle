package com.ahoy.parser.ui;

import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.domain.ParseDataDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UpdateCategory extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(UpdateCategory.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String id = request.getParameter("id");
		String sCat = request.getParameter("scat");
		String sSubCat = request.getParameter("ssubcat");
		String idx = request.getParameter("idx");
		String status =request.getParameter("status");
		try{
			ParseDataDao parseDataDao = new ParseDataDaoImpl();
			ParseDataDo parseDataDo = parseDataDao.selectById(Long.valueOf(id.trim()));
			
			if(parseDataDo!=null){
				
				if(status!=null && status.trim().matches("[0-2]{1}")){
					parseDataDo.setStatus(Short.valueOf(status));
				}else if(sCat!=null && sCat.trim().matches("[0-9]+")&&sSubCat!=null && sSubCat.matches("[0-9]+")){
					ShopezzySubCatDo shopezzySubCatDo = new ShopezzySubCatDo();
					shopezzySubCatDo.setSubCatId(Long.valueOf(sSubCat.trim()));				
					parseDataDo.setShopezzySubCatDo(shopezzySubCatDo);
					parseDataDo.setStatus((short)1);
				}
				
				
				parseDataDao.saveOrUpdate(parseDataDo);
				
				parseDataDo = parseDataDao.selectById(Long.valueOf(id.trim()));
				request.setAttribute("parseDataDo", parseDataDo);
				request.setAttribute("idx", idx);
				
			}else{
				
			}
			
			
		}catch (Exception e) {
			logger.error("[UpdateCategory][process] Exception: "+e);
		}finally{
			logger.info("[UpdateCategory][process] id: "+id+" | sCat: "+sCat+" | sSubCat: "+sSubCat+" | idx: "+idx);
			RequestDispatcher rd = request.getRequestDispatcher("jsps/div/update.jsp");
			rd.forward(request, response);
		}
	}

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		this.process(request, response);
	}
}
