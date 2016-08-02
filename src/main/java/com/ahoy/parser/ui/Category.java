package com.ahoy.parser.ui;

import com.ahoy.parser.dao.MerchantCategoryDao;
import com.ahoy.parser.dao.MerchantCategoryDaoImpl;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.dao.ShopezzyCategoryDaoImpl;
import com.ahoy.parser.domain.MerchantCategoryDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.util.GetStackElements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class Category extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger = LoggerFactory.getLogger(Category.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String merchantId = request.getParameter("merchantid");		
		
		try{		
			
			MerchantDo merchantDo = (merchantId!=null && merchantId.trim().matches("[0-9]+"))?new MerchantDaoImpl().getByMerchantId(Long.valueOf(merchantId.trim())):null;
				
			MerchantCategoryDao merchantCategoryDao = new MerchantCategoryDaoImpl();		
			List<MerchantCategoryDo> merchantCategoryDos = merchantCategoryDao.selectByMerchant(merchantDo);
			request.setAttribute("merchantDo", merchantDo);
			request.setAttribute("merchantCategoryDos", merchantCategoryDos);
			
			List<ShopezzyCategoryDo> shopezzyCategoryDos = new ShopezzyCategoryDaoImpl().selectAll();
			request.setAttribute("shopezzyCategoryDos", shopezzyCategoryDos);
			
		}catch (Exception e) {
			logger.error("[Category][process] Exception: "+e+" |"+GetStackElements.getRootCause(e,this.getClass().getName()));
		}finally{
			RequestDispatcher rd = request.getRequestDispatcher("jsps/div/categorymap.jsp");
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
