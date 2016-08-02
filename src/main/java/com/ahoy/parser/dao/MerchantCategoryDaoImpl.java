package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantCategoryDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MerchantCategoryDaoImpl implements MerchantCategoryDao {

	Logger logger = LoggerFactory.getLogger(MerchantCategoryDaoImpl.class);
	SessionFactory factory = HSession.createSessionFactory();
	@SuppressWarnings("unchecked")
	@Override
	public MerchantCategoryDo selectByCategoryNameAndMerchant(String catName,MerchantDo merchantDo) {
		Session session = null;
		MerchantCategoryDo merchantCategoryDo = null;
		
		try {
		
			session = factory.openSession();
			List<MerchantCategoryDo> merchantCategoryDos = session.createCriteria(MerchantCategoryDo.class)
					.add(Restrictions.eq("merchantDo", merchantDo))
					.add(Restrictions.eq("catName", catName.trim().toLowerCase())).list();
			
			if(merchantCategoryDos!=null && merchantCategoryDos.size()>0){
				merchantCategoryDo = merchantCategoryDos.get(0);
			}
			
		} catch (Exception e) {
			logger.error("[MerchantCategoryDaoImpl][selectByCategoryNameAndMerchant] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantCategoryDo;
	}
	@Override
	public boolean saveOrUpdate(MerchantCategoryDo merchantCategoryDo) {
		Session session = null;
		boolean flag =false;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(merchantCategoryDo);
			tx.commit();
			flag = true;
		}catch (Exception e) {
			logger.error("[MerchantCategoryDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}		
		return flag;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantCategoryDo> selectByMerchant(MerchantDo merchantDo) {
		Session session =null;
		List<MerchantCategoryDo> merchantCategoryDos = null;
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(MerchantCategoryDo.class)
					.addOrder(Order.asc("merchantDo"));
				
			if(merchantDo!=null){
				criteria.add(Restrictions.eq("merchantDo", merchantDo));
			}
			
			merchantCategoryDos = criteria.list();
			
		}catch (Exception e) {
			logger.error("[MerchantCategoryDaoImpl][selectByMerchant] Exception: "+e);
		}finally{
			session.close();
		}
		
		return merchantCategoryDos;
	}

	
}
