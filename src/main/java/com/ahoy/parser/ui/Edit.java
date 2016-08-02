package com.ahoy.parser.ui;

import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.dao.ShopezzyCategoryDaoImpl;
import com.ahoy.parser.dao.ShopezzySubCatDaoImpl;
import com.ahoy.parser.domain.ParseDataDo;
import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Edit extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(Edit.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String id = request.getParameter("id");
		String merchantName = request.getParameter("merchantName");
		String cityName = request.getParameter("cityName");
		String listtype = request.getParameter("listtype");
		String redirectPath = "";
		try{
			if(id!=null && id.trim().matches("[0-9]+") ){
				ParseDataDao parseDataDao = new ParseDataDaoImpl();
				ParseDataDo parseDataDo = parseDataDao.selectById(Long.valueOf(id.trim()));
				request.setAttribute("parseDataDo", parseDataDo);
				
				List<ShopezzyCategoryDo> shopezzyCategoryDos = new ShopezzyCategoryDaoImpl().selectAll();
				request.setAttribute("shopezzyCategoryDos", shopezzyCategoryDos);
				
				List<ShopezzySubCatDo> shopezzySubCatDos = new ShopezzySubCatDaoImpl().selectByCategory(parseDataDo.getShopezzySubCatDo().getShopezzyCategoryDo());
				request.setAttribute("shopezzySubCatDos", shopezzySubCatDos);
				
				request.setAttribute("merchantName", merchantName);
				request.setAttribute("cityName", cityName);
				request.setAttribute("listtype", listtype);
				
				redirectPath = "edit.jsp";
			}else{
				redirectPath = "view";
			}
			
		}catch (Exception e) {
			redirectPath = "view";
			logger.error("[Edit][process] Exception: "+e);
		}finally{
			logger.info("[Edit][process] id: "+id+" | merchantName: "+merchantName+" | cityName: "+cityName+" | listtype: "+listtype+"redirectPath: "+redirectPath);
			
			RequestDispatcher rd = request.getRequestDispatcher(redirectPath);
			rd.forward(request, response);
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		process(request, response);
	}
}
