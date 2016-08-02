package com.ahoy.parser.ui;

import com.ahoy.parser.dao.ParseDataDao;
import com.ahoy.parser.dao.ParseDataDaoImpl;
import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.ParseDataDo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@WebServlet("/pv")
@SuppressWarnings("serial")
public class ProcessVerified extends HttpServlet{
	
	private Logger logger = LoggerFactory.getLogger(ProcessVerified.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String merchantId = request.getParameter("merchantid");
		String listType = request.getParameter("listtype");
		String cityId = request.getParameter("cityid");
		try{
			
			if(merchantId !=null && merchantId.matches("[0-9]+")
					&& listType!=null && listType.matches("[0-9]{1,2}")
					&& cityId!=null && cityId.matches("[0-9]+")){
				
				CityDo cityDo = new CityDo();
				cityDo.setCityId(Long.valueOf(cityId));
				
				MerchantDo merchantDo = new MerchantDo();
				merchantDo.setMerchantId(Long.valueOf(merchantId));
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String todayDate = format.format(new Date());
				 
				format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date startDate = format.parse(todayDate+" 00:00:00");
				Date endDate = format.parse(todayDate+" 23:59:59");
				
				ParseDataDao parseDataDao = new ParseDataDaoImpl();
				
				List<ParseDataDo> parseDataDos = parseDataDao.select(listType.equals("1")?"createdOn":"updatedOn",cityDo.getCityId(),merchantDo.getMerchantId(), startDate, endDate,(short)1, 0, 0);
				
				if(parseDataDos!=null && parseDataDos.size()>0){
					for (ParseDataDo parseDataDo : parseDataDos) {
						parseDataDo.setStatus((short)2);
						boolean flag = parseDataDao.saveOrUpdate(parseDataDo);
						if(flag){
							logger.info("[ProcessVerified][process] merchantId: "+merchantId+" | listType: "+listType+" | cityId: "+cityId+" | processed: "+parseDataDo.getDataPkey());
						}else{
							logger.info("[ProcessVerified][process] merchantId: "+merchantId+" | listType: "+listType+" | cityId: "+cityId+" | not processed: "+parseDataDo.getDataPkey());
						}
					}
				}else{
					logger.info("[ProcessVerified][process] merchantId: "+merchantId+" | listType: "+listType+" | cityId: "+cityId+" | parseDataDos : "+(parseDataDos!=null?parseDataDos.size():null));
				}
				
				
			}else{
				logger.info("[ProcessVerified][process] merchantId: "+merchantId+" | listType: "+listType+" | cityId: "+cityId+" | invalid Para !!");
			}
			
			
		}catch (Exception e) {
			logger.error("[ProcessVerified][process] Exception: "+e);
		}finally{
			RequestDispatcher rd = request.getRequestDispatcher("view?merchantid="+merchantId+"&cityid="+cityId+"&listtype="+listType);
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
