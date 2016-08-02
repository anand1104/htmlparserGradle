package com.ahoy.parsetest;

import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

public class ProcessCategories extends HttpServlet{
		
	Logger logger = LoggerFactory.getLogger(ProcessCategories.class);
	
	protected void process(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String merchantName = request.getParameter("mname");
		String fileName = request.getParameter("file");
		PrintWriter out = null;
		String resp = "";
		try{
			out = response.getWriter();
			
			
			if(merchantName!=null&&!merchantName.trim().equals("")&&!"null".equalsIgnoreCase(merchantName.trim())){
				MerchantDo merchantDo = new MerchantDaoImpl().getByMerchantname(merchantName);
				if(merchantDo!=null){
				
					BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\Vivek\\Desktop\\ushop\\20150507\\"+fileName));
					String line = null;
					
					MerchantCategoryDao merchantCategoryDao = new MerchantCategoryDaoImpl();
					MerchantSubCatDao merchantSubCatDao = new MerchantSubCatDaoImpl();
					
					ShopezzyCategoryDao shopezzyCategoryDao = new ShopezzyCategoryDaoImpl();
					ShopezzySubCatDao shopezzySubCatDao = new ShopezzySubCatDaoImpl();
					
					MerchantShopezzyCatMapDao merchantShopezzyCatMapDao = new MerchantShopezzyCatMapDaoImpl();
					
					while((line=br.readLine())!=null){
						String splt[] = line.split(",");
						if(splt!=null && splt.length==4){
							String mCat = splt[0];
							String mSubCat =splt[1];
							String sCat = splt[2];
							String sSubCat = splt[3];
							
							MerchantCategoryDo merchantCategoryDo = this.getMerchantCategoryDo(mCat, merchantDo, merchantCategoryDao);
							ShopezzyCategoryDo shopezzyCategoryDo = this.getShopezzyCategoryDo(sCat, shopezzyCategoryDao);
							if(merchantCategoryDo!=null && shopezzyCategoryDo!=null){
								
								MerchantSubCatDo merchantSubCatDo = this.getMerchantSubCatDo(mSubCat, merchantCategoryDo, merchantSubCatDao);
							
								ShopezzySubCatDo shopezzySubCatDo = this.getShopezzySubCatDo(sSubCat, shopezzyCategoryDo, shopezzySubCatDao);
								
								if(merchantSubCatDo!=null && shopezzySubCatDo!=null){
									MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select(merchantSubCatDo, shopezzySubCatDo);
									if(merchantShopezzyCatMapDo==null){
										merchantShopezzyCatMapDo = new MerchantShopezzyCatMapDo();
										merchantShopezzyCatMapDo.setMerchantSubCatDo(merchantSubCatDo);
										merchantShopezzyCatMapDo.setShopezzySubCatDo(shopezzySubCatDo);
										boolean flag = merchantShopezzyCatMapDao.saveOrUpdate(merchantShopezzyCatMapDo);
										logger.info("[ProcessCategories][process] mcat: "+mCat+"| mSubCat: "+mSubCat+" | sCat: "+sCat+" | sSubCat: "+sSubCat+" | flag: "+flag);
									}else{
										logger.info("[ProcessCategories][process] mcat: "+mCat+"| mSubCat: "+mSubCat+" | sCat: "+sCat+" | sSubCat: "+sSubCat+" | already mapped");
									}
								}
							}else{
								logger.info("[ProcessCategories][process] merchantCategoryDo: "+merchantCategoryDo+" | shopezzyCategoryDo: "+shopezzyCategoryDo);
							}
							
						}else{
							logger.info("[ProcessCategories][process] line \""+line.trim()+"\" is not valid");
						}
					}
					
					resp="success";
				}else{
					resp="Merchant \""+merchantName+"\" not exist in DB";
				}
			}else{
				resp = "Bad Request";
			}
			
			
		}catch (Exception e) {
			resp = "Internal Server Error";
			logger.error("[ProcessCategories][process] Exception: "+e);
		}finally{
			logger.info("[ProcessCategories][process] resp: "+resp);
			out.println(resp);
		}
		
	}
	
	private MerchantCategoryDo getMerchantCategoryDo(String catName,MerchantDo merchantDo,MerchantCategoryDao merchantCategoryDao){
		MerchantCategoryDo merchantCategoryDo = null;
		try{
			merchantCategoryDo = merchantCategoryDao.selectByCategoryNameAndMerchant(catName, merchantDo);
			
			if(merchantCategoryDo==null){
				merchantCategoryDo = new MerchantCategoryDo();
				merchantCategoryDo.setCatName(catName.trim().toLowerCase());
				merchantCategoryDo.setMerchantDo(merchantDo);
				merchantCategoryDo.setCreatedOn(new Timestamp(new Date().getTime()));
				boolean flag = merchantCategoryDao.saveOrUpdate(merchantCategoryDo);
				logger.info("[ProcessCategories][getMerchantCategoryDo]["+merchantDo.getMerchantName()+"] category: \" "+catName+"\""+(flag?"Add Sucess":"problem in add"));
				
				merchantCategoryDo = merchantCategoryDao.selectByCategoryNameAndMerchant(catName, merchantDo);
			}else{
				logger.info("[ProcessCategories][getMerchantCategoryDo]["+merchantDo.getMerchantName()+"] already exist \" "+catName+"\"");
			}			
			
		}catch (Exception e) {
			logger.error("[ProcessCategories][getMerchantCategoryDo]["+merchantDo.getMerchantName()+"] category: \" "+catName+"\" Exception: "+e);
		}
		
		return merchantCategoryDo;
		
	}
	
	private MerchantSubCatDo getMerchantSubCatDo(String subCatName,MerchantCategoryDo merchantCategoryDo,MerchantSubCatDao merchantSubCatDao){
		MerchantSubCatDo merchantSubCatDo = null;
		
		try{
			merchantSubCatDo = merchantSubCatDao.selectSubCatNameAndCatgory(subCatName, merchantCategoryDo);
			if(merchantSubCatDo==null){
				
				merchantSubCatDo = new MerchantSubCatDo();
				merchantSubCatDo.setSubCatName(subCatName);
				merchantSubCatDo.setMerchantCategoryDo(merchantCategoryDo);
				merchantSubCatDo.setCreatedOn(new Timestamp(new Date().getTime()));
				boolean flag = merchantSubCatDao.saveOrUpdate(merchantSubCatDo);
				logger.info("[ProcessCategories][getMerchantSubCatDo] merchant subcategoryname: "+subCatName+" | "+(flag?"Added":"Error"));
				merchantSubCatDo = merchantSubCatDao.selectSubCatNameAndCatgory(subCatName, merchantCategoryDo);
										
			}else{
				logger.info("[ProcessCategories][getMerchantSubCatDo] merchant subcategoryname: "+subCatName+" | already Exist");
			}
		}catch (Exception e) {
			logger.error("[ProcessCategories][getMerchantSubCatDo] Exception: "+e);
		}
		return merchantSubCatDo;
		
	}
	
	private ShopezzyCategoryDo getShopezzyCategoryDo(String catName,ShopezzyCategoryDao shopezzyCategoryDao){
		ShopezzyCategoryDo shopezzyCategoryDo = null;
		try{
			shopezzyCategoryDo = shopezzyCategoryDao.selectByCatName(catName);
			
			if(shopezzyCategoryDo==null){
				shopezzyCategoryDo = new ShopezzyCategoryDo();
				shopezzyCategoryDo.setCatName(catName.trim().toLowerCase());
				shopezzyCategoryDo.setCreatedOn(new Timestamp(new Date().getTime()));
				boolean flag = shopezzyCategoryDao.saveOrUpdate(shopezzyCategoryDo);
				logger.info("[ProcessCategories][getShopezzyCategoryDo] category: \""+catName+"\" "+(flag?"Add Sucess":"problem in add"));
				
				shopezzyCategoryDo = shopezzyCategoryDao.selectByCatName(catName);
			}else{
				logger.info("[ProcessCategories][getShopezzyCategoryDo] category: \""+catName+" \" already exist");
			}			
			
		}catch (Exception e) {
			logger.error("[ProcessCategories][getShopezzyCategoryDo] category: \""+catName+" \" Exception: "+e);
		}
		
		return shopezzyCategoryDo;
		
	}
	
	private ShopezzySubCatDo getShopezzySubCatDo(String subCatName,ShopezzyCategoryDo shopezzyCategoryDo,ShopezzySubCatDao shopezzySubCatDao){
		ShopezzySubCatDo shopezzySubCatDo = null;
		
		try{
			shopezzySubCatDo = shopezzySubCatDao.selectBySubCatNameAndCat(subCatName, shopezzyCategoryDo);
			if(shopezzySubCatDo==null){
				
				shopezzySubCatDo = new ShopezzySubCatDo();
				shopezzySubCatDo.setSubCatName(subCatName);
				shopezzySubCatDo.setShopezzyCategoryDo(shopezzyCategoryDo);
				shopezzySubCatDo.setCreatedOn(new Timestamp(new Date().getTime()));
				boolean flag = shopezzySubCatDao.saveOrUpdate(shopezzySubCatDo);
				logger.info("[ProcessCategories][getShopezzySubCatDo] shopezzy subcategoryname: "+subCatName+" | "+(flag?"Added":"Error"));
				shopezzySubCatDo = shopezzySubCatDao.selectBySubCatNameAndCat(subCatName, shopezzyCategoryDo);
										
			}else{
				logger.info("[ProcessCategories][getShopezzySubCatDo] shopezzy subcategoryname: "+subCatName+" | already exist");
			}
		}catch (Exception e) {
			logger.error("[ProcessCategories][getMerchantSubCatDo] Exception: "+e);
		}
		return shopezzySubCatDo;
		
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		  process(request,response);
	}
	  
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  process(request,response);
	}
	
}
