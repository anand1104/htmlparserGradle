package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShopezzyCategoryDaoImpl implements ShopezzyCategoryDao{

	Logger logger = LoggerFactory.getLogger(MerchantSubCatDaoImpl.class);
	SessionFactory factory = HSession.createSessionFactory(); 
	
	@SuppressWarnings("unchecked")
	@Override
	public ShopezzyCategoryDo selectByCatName(String catName) {
		Session session = null;
		ShopezzyCategoryDo shopezzyCategoryDo=null;		
		
		try{
			session =  factory.openSession();
			List<ShopezzyCategoryDo> shopezzyCategoryDos =  session.createCriteria(ShopezzyCategoryDo.class)
					.add(Restrictions.eq("catName", catName))
					.list();
			
			if(shopezzyCategoryDos!=null && shopezzyCategoryDos.size()>0){
				shopezzyCategoryDo = shopezzyCategoryDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[ShopezzyCategoryDaoImpl][selectByCatName] Exception: "+e);
		}finally{
			session.close();
		}
		
		return shopezzyCategoryDo;
	}

	@Override
	public boolean saveOrUpdate(ShopezzyCategoryDo shopezzyCategoryDo) {
		Session session = null;
		boolean flag = false;
		
		try{
			
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(shopezzyCategoryDo);
			tx.commit();
			flag = true;			
			
		}catch (Exception e) {
			logger.error("[ShopezzyCategoryDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		return flag;
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShopezzyCategoryDo> selectAll() {
		List<ShopezzyCategoryDo> shopezzyCategoryDos = null;
		Session session = null;
		try{
			
			session = factory.openSession();
			shopezzyCategoryDos = session.createCriteria(ShopezzyCategoryDo.class)
					.list();
			
		}catch (Exception e) {
			logger.error("[ShopezzyCategoryDaoImpl][selectAll] Exception: "+e);
		}finally{
			session.close();
		}
		return shopezzyCategoryDos;
	}
}
