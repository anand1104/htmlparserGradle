package com.ahoy.parser.util;

import com.ahoy.parser.dao.*;
import com.ahoy.parser.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBUtil {
	
	private Logger logger = LoggerFactory.getLogger(DBUtil.class);
	
	public String saveOrUpdateDB(String categoryName,String subCatName,String desc,String weight,String maxPrice,String offer,String offerPrice,String imageUrl,CityDo cityDo,MerchantDo merchantDo,String vendor){
		String resp="";
		try{
			
			
			MerchantCategoryDo merchantCategoryDo =this.getMerchantCategoryDo(categoryName, merchantDo);
			MerchantSubCatDo merchantSubCatDo = this.getMerchantSubCatDo(subCatName, merchantCategoryDo);
			
			MerchantShopezzyCatMapDao merchantShopezzyCatMapDao = new MerchantShopezzyCatMapDaoImpl();
			MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select( merchantSubCatDo);
			
			ShopezzySubCatDo shopezzySubCatDo = null;
			if(merchantShopezzyCatMapDo!=null){
				shopezzySubCatDo = merchantShopezzyCatMapDo.getShopezzySubCatDo();
			}else{				
				shopezzySubCatDo = new ShopezzySubCatDaoImpl().selectBySubCatNameAndCat("others", null);
			}			
			ParseDataDao parseDataDao = new ParseDataDaoImpl();
			
			ParseDataDo parseDataDo = parseDataDao.select(cityDo.getCityId(),desc, weight, maxPrice, offer, offerPrice,imageUrl, merchantDo.getMerchantId());
			
			if(parseDataDo==null){
				parseDataDo = new ParseDataDo();
				parseDataDo.setDescription(desc);
				parseDataDo.setMaxPrice(maxPrice);
				parseDataDo.setSellPrice(offerPrice);
				parseDataDo.setOffer(offer);
				parseDataDo.setImageUrl(imageUrl);
				parseDataDo.setMerchantId(merchantDo.getMerchantId());
				parseDataDo.setWeight(weight);
				parseDataDo.setCityId(cityDo.getCityId());
				parseDataDo.setVendor(vendor);
				parseDataDo.setCreatedOn(new Timestamp(new Date().getTime()));
				
				parseDataDo.setShopezzySubCatDo(shopezzySubCatDo);
				parseDataDo.setMerchantSubCatDo(merchantSubCatDo);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");				
				parseDataDo.setUpdatedOn(new Timestamp(format.parse("1991-01-01 00:00:00").getTime()));				
				parseDataDao.saveOrUpdate(parseDataDo);
				resp="save";
			}else{
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String dbCreateDate = format.format(parseDataDo.getCreatedOn()); 
				String todayDate = format.format(new Date());
				if(!dbCreateDate.equals(todayDate)){
					String dbUpdateDate = format.format(parseDataDo.getCreatedOn());
					if(!dbUpdateDate.equals(todayDate)){
						parseDataDo.setUpdatedOn(new Timestamp(new Date().getTime()));
						parseDataDao.saveOrUpdate(parseDataDo);
						resp="update";
					}else{
						resp="todays-create";
					}
				}else{
					resp="already up-to-date";
				}
			}
			
		}catch (Exception e) {
			resp="internal DB Error";
			logger.error("[DBUtil][saveOrUpdateDB] Exception: "+e);
		}
		return resp;
	}
	
	public String saveOrUpdateByPIdAndVId(String categoryName,String subCatName,String desc,String weight,String maxPrice,String offer,String offerPrice,String imageUrl,CityDo cityDo,MerchantDo merchantDo,String vendor,long pid,long vid, String sku,short avail, String link){
		String resp="";
		try{
			
			ParseDataDao parseDataDao = new ParseDataDaoImpl();
			
			ParseDataDo parseDataDo = parseDataDao.getByPIdAndVid(merchantDo.getMerchantId(), cityDo.getCityId(), pid, vid);
						
			if(parseDataDo==null){
												
				MerchantCategoryDo merchantCategoryDo =this.getMerchantCategoryDo(categoryName, merchantDo);			
				MerchantSubCatDo merchantSubCatDo = this.getMerchantSubCatDo(subCatName, merchantCategoryDo);
				
				MerchantShopezzyCatMapDao merchantShopezzyCatMapDao = new MerchantShopezzyCatMapDaoImpl();
				MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select( merchantSubCatDo);
				ShopezzySubCatDo shopezzySubCatDo = null;
				if(merchantShopezzyCatMapDo!=null){
					shopezzySubCatDo = merchantShopezzyCatMapDo.getShopezzySubCatDo();
				}else{				
					shopezzySubCatDo = new ShopezzySubCatDaoImpl().selectBySubCatNameAndCat("others", null);
				}
				
				parseDataDo = new ParseDataDo();
				parseDataDo.setStorePId(pid);
				parseDataDo.setStoreVId(vid);
				parseDataDo.setSku(sku);
				parseDataDo.setDescription(desc);
				parseDataDo.setMaxPrice(maxPrice);
				parseDataDo.setSellPrice(offerPrice);
				parseDataDo.setOffer(offer);
				parseDataDo.setImageUrl(imageUrl);
				parseDataDo.setMerchantId(merchantDo.getMerchantId());
				parseDataDo.setWeight(weight);
				parseDataDo.setCityId(cityDo.getCityId());
				parseDataDo.setVendor(vendor);
				parseDataDo.setCreatedOn(new Timestamp(new Date().getTime()));
				parseDataDo.setAvailablity(avail);
				
				parseDataDo.setShopezzySubCatDo(shopezzySubCatDo);
				parseDataDo.setMerchantSubCatDo(merchantSubCatDo);
				parseDataDo.setUpdatedOn(new Timestamp(new Date().getTime()));	
				
				parseDataDo.setLink(link);
				
				parseDataDao.saveOrUpdate(parseDataDo);
				resp="save";
			}else{
				parseDataDo.setLink(link);
				parseDataDo.setSku(sku);	
				parseDataDo.setMaxPrice(maxPrice);
				parseDataDo.setSellPrice(offerPrice);
				parseDataDo.setOffer(offer);
				parseDataDo.setWeight(weight);
				parseDataDo.setAvailablity(avail);
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				String dbCreateDate = format.format(parseDataDo.getCreatedOn()); 
				String todayDate = format.format(new Date());
				if(!dbCreateDate.equals(todayDate)){
					String dbUpdateDate = format.format(parseDataDo.getCreatedOn());
					if(!dbUpdateDate.equals(todayDate)){										
						parseDataDo.setUpdatedOn(new Timestamp(new Date().getTime()));												
						resp="update";
					}else{
						resp="todays-create";
					}
				}else{
					resp="already up-to-date";
				}					
				parseDataDao.saveOrUpdate(parseDataDo);
			}
			
		}catch (Exception e) {
			resp="internal DB Error";
			logger.error("[DBUtil][saveOrUpdateByPIdAndVId] "+GetStackElements.getRootCause(e, getClass().getName()));
		}
		return resp;
	}
	
	private MerchantCategoryDo getMerchantCategoryDo(String catName,MerchantDo merchantDo){
		MerchantCategoryDo merchantCategoryDo = null;
		MerchantCategoryDao merchantCategoryDao = new MerchantCategoryDaoImpl();
		merchantCategoryDo = merchantCategoryDao.selectByCategoryNameAndMerchant(catName, merchantDo);
			
		if(merchantCategoryDo==null){
			merchantCategoryDo = new MerchantCategoryDo();
			merchantCategoryDo.setCatName(catName.trim().toLowerCase());
			merchantCategoryDo.setMerchantDo(merchantDo);
			merchantCategoryDo.setCreatedOn(new Timestamp(new Date().getTime()));
			boolean flag = merchantCategoryDao.saveOrUpdate(merchantCategoryDo);
			logger.info("[DBUtil][getMerchantCategoryDo]["+merchantDo.getMerchantName()+"] category: \" "+catName+"\""+(flag?"Add Sucess":"problem in add"));
		}	
		
		return merchantCategoryDo;		
	}
	
	private MerchantSubCatDo getMerchantSubCatDo(String subCatName,MerchantCategoryDo merchantCategoryDo){
		MerchantSubCatDo merchantSubCatDo = null;
		
		MerchantSubCatDao merchantSubCatDao = new MerchantSubCatDaoImpl();
		merchantSubCatDo = merchantSubCatDao.selectSubCatNameAndCatgory(subCatName, merchantCategoryDo);
		if(merchantSubCatDo==null){
			
			merchantSubCatDo = new MerchantSubCatDo();
			merchantSubCatDo.setSubCatName(subCatName);
			merchantSubCatDo.setMerchantCategoryDo(merchantCategoryDo);
			merchantSubCatDo.setCreatedOn(new Timestamp(new Date().getTime()));
			boolean flag = merchantSubCatDao.saveOrUpdate(merchantSubCatDo);
			logger.info("[DBUtil][getMerchantSubCatDo] merchant subcategoryname: "+subCatName+" | "+(flag?"Added":"Error"));
									
		}
		
		return merchantSubCatDo;
		
	}
	
//	private MerchantShopezzyCatMapDo getMerchantShopezzyCatMapDo(MerchantCategoryDo merchantCategoryDo,MerchantSubCatDo merchantSubCatDo,MerchantShopezzyCatMapDao merchantShopezzyCatMapDao){
//		MerchantShopezzyCatMapDo merchantShopezzyCatMapDo=null;
//		
//		try{
////			merchantShopezzyCatMapDo = merchantShopezzyCatMapDao.select(merchantCategoryDo, merchantSubCatDo);
////			if(merchantShopezzyCatMapDo==null){
////				ShopezzyCategoryDo shop
////			}
//			
//			
//		}catch (Exception e) {
//			logger.error("[DBUtil][getMerchantShopezzyCatMapDo] Exception: "+e);
//		}
//		return merchantShopezzyCatMapDo;
//	}
}
