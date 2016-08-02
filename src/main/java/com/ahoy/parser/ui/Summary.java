package com.ahoy.parser.ui;

import com.ahoy.parser.dao.MerchantDao;
import com.ahoy.parser.dao.MerchantDaoImpl;
import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;

@SuppressWarnings("serial")
public class Summary extends HttpServlet{
	
	Logger logger = LoggerFactory.getLogger(Summary.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		
		try{
			MerchantDao merchantDao = new MerchantDaoImpl();
			List<MerchantDo> merchantDos = merchantDao.getByType((short)1);
			
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			String todayDate = format.format(new Date());			 
			format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date startDate = format.parse(todayDate+" 00:00:00");
			Date endDate = format.parse(todayDate+" 23:59:59");
			
			HashMap<String, HashMap<String, HashMap<String,String>>> map = new HashMap<String, HashMap<String, HashMap<String,String>>>();
			List<MerchantDo> merchantDos1 = new ArrayList<MerchantDo>(); 
			ParseDataDao parseDataDao = new ParseDataDaoImpl();
			if(merchantDos!=null && merchantDos.size()>0){
				
				for (MerchantDo merchantDo : merchantDos) {
					 
					List<CityDo> cityList = sort(merchantDo.getCityDos(), on(CityDo.class).getCityName());
					
					merchantDo.setCityList(cityList);
					for (CityDo cityDo : cityList) {
						HashMap<String, HashMap<String,String>> hashMap2 = new HashMap<String, HashMap<String,String>>();
						
						List<Object[]> newCountStatusWise = parseDataDao.selectByMerchantAndCity("createdOn", merchantDo.getMerchantId(),cityDo.getCityId(), startDate, endDate);
						HashMap<String, String> newItemsMap = null;
						
						if(newCountStatusWise!=null && newCountStatusWise.size()>0){
							newItemsMap = new HashMap<String, String>();
							for (Object[] objects : newCountStatusWise) {
								if(objects!=null && objects.length==2){
									newItemsMap.put(objects[0].toString(),objects[1].toString());		
								}					
							}
						}
						
						List<Object[]> updateCountStatusWise = parseDataDao.selectByMerchantAndCity("updatedOn", merchantDo.getMerchantId(),cityDo.getCityId(), startDate, endDate);
						HashMap<String, String> updateItemsMap = null;
						
						if(updateCountStatusWise!=null && updateCountStatusWise.size()>0){
							updateItemsMap = new HashMap<String, String>();
							for (Object[] objects : updateCountStatusWise) {
								if(objects!=null && objects.length==2){
									updateItemsMap.put(objects[0].toString(),objects[1].toString());
								}						
							}
						}					
						
						hashMap2.put("createdOn", newItemsMap);
						hashMap2.put("updatedOn", updateItemsMap);
						map.put(merchantDo.getMerchantName()+"-"+cityDo.getCityName(), hashMap2);
						
						
					}	
					merchantDos1.add(merchantDo);
				}
				
			}else{
				logger.info("[Summary][process] merchantDos size "+(merchantDos!=null?merchantDos.size():null));
			}			
			
			request.setAttribute("merchantDos", merchantDos1);
			request.setAttribute("map", map);
			
		}catch (Exception e) {
			logger.error("[Summary][process] Exception: "+e);
		}finally{
			RequestDispatcher rd = request.getRequestDispatcher("summary.jsp");
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
