package com.ahoy.parser.ui;

import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.dao.ShopezzySubCatDaoImpl;
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

public class Update extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		String id = request.getParameter("id");
		String desc = request.getParameter("desc");
		String mPrice = request.getParameter("mPrice");
		String offer = request.getParameter("offer");
		String sprice = request.getParameter("sprice");
		String weight = request.getParameter("weight");
		String sCat = request.getParameter("scat");
		String sSubCat = request.getParameter("ssubcat");		
		String merchantName = request.getParameter("merchantName");
		String cityName =  request.getParameter("cityName");
		String listtype =  request.getParameter("listtype");
		
		
		try{
			
			ParseDataDao parseDataDao = new ParseDataDaoImpl();
			ParseDataDo parseDataDo = parseDataDao.selectById(Long.valueOf(id.trim()));
			
			if(desc!=null && !"".equals(desc.trim())&&!"null".equalsIgnoreCase(desc.trim())){
				parseDataDo.setDescription(desc.trim());
			}
			
			if(mPrice!=null && !"".equals(mPrice.trim()) && !"null".equalsIgnoreCase(mPrice)){
				parseDataDo.setMaxPrice(mPrice);
			}
			
			if(offer!=null && !"".equals(offer.trim()) && !"null".equalsIgnoreCase(offer.trim())){
				parseDataDo.setOffer(offer);
			}
			
			if(sprice!=null &&!"".equals(sprice)&&!"null".equalsIgnoreCase(sprice.trim())){
				parseDataDo.setSellPrice(sprice);
			}
			
			if(weight!=null && !"".equals(weight.trim())&&!"null".equalsIgnoreCase(weight.trim())){
				parseDataDo.setWeight(weight);
			}
			
			if(sSubCat!=null && sSubCat.trim().matches("[0-9]+")){
				ShopezzySubCatDo shopezzySubCatDo = new ShopezzySubCatDaoImpl().selectById(Long.valueOf(sSubCat.trim()));
				parseDataDo.setShopezzySubCatDo(shopezzySubCatDo);
			}
			
			boolean flag = parseDataDao.saveOrUpdate(parseDataDo);
			if(flag){
				request.setAttribute("resp", "Update Successfully");
			}else{
				request.setAttribute("resp", "Problem in Update");
			}			
			
		}catch (Exception e) {
			request.setAttribute("resp", "Internal Error");
			logger.error("[Update][process] Exception: "+e);
		}finally{
			
			logger.info("[Update][process] merchantName:"+merchantName+" | cityName: "+cityName+" | id: "+id+" | desc: "+desc+" | mPrice: "+mPrice+" | offer: "+offer+" | sprice: "+sprice+" | weight: "+weight+" | sCat: "+sCat+" | sSubCat: "+sSubCat);
			
			RequestDispatcher rd = request.getRequestDispatcher("edit?id="+id+"&merchantName="+merchantName+"&cityName="+cityName+"&listtype="+listtype);
			rd.forward(request, response);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
}
