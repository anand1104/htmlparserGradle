package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantCategoryDo;
import com.ahoy.parser.domain.MerchantSubCatDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MerchantSubCatDaoImpl implements MerchantSubCatDao{

	Logger logger = LoggerFactory.getLogger(MerchantSubCatDaoImpl.class);
	SessionFactory factory = HSession.createSessionFactory(); 
	
	@SuppressWarnings("unchecked")
	@Override
	public MerchantSubCatDo selectSubCatNameAndCatgory(String subCatName,MerchantCategoryDo merchantCategoryDo) {
		Session session = null;
		MerchantSubCatDo merchantSubCatDo=null;		
		
		try{
			session =  factory.openSession();
			List<MerchantSubCatDo> merchantSubCatDos =  session.createCriteria(MerchantSubCatDo.class)
					.add(Restrictions.eq("subCatName", subCatName))
					.add(Restrictions.eq("merchantCategoryDo", merchantCategoryDo)).list();
			
			if(merchantSubCatDos!=null && merchantSubCatDos.size()>0){
				merchantSubCatDo = merchantSubCatDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[MerchantSubCatDaoImpl][selectSubCatNameAndCatgory] Exception: "+e);
		}finally{
			session.close();
		}
		
		return merchantSubCatDo;
	}
	@Override
	public boolean saveOrUpdate(MerchantSubCatDo merchantSubCatDo) {
		Session session = null;
		boolean flag = false;
		
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(merchantSubCatDo);
			tx.commit();
			flag = true;
		}catch (Exception e) {
			logger.error("[MerchantSubCatDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		return flag;
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantSubCatDo> selectByCategory(MerchantCategoryDo merchantCategoryDo) {
		List<MerchantSubCatDo> merchantSubCatDos = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			merchantSubCatDos = session.createCriteria(MerchantSubCatDo.class)
					.add(Restrictions.eq("merchantCategoryDo", merchantCategoryDo)).list();
			
		}catch (Exception e) {
			logger.error("[MerchantSubCatDaoImpl][selectByCategory] Exception: "+e);
		}finally{
			session.close();
		}
		
		
		return merchantSubCatDos;
	}

	
}
