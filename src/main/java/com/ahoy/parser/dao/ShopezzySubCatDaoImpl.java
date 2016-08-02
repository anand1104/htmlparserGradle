package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShopezzySubCatDaoImpl implements ShopezzySubCatDao{

	Logger logger = LoggerFactory.getLogger(this.getClass());
	SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public ShopezzySubCatDo selectBySubCatNameAndCat(String subCatName,ShopezzyCategoryDo shopezzyCategoryDo) {
		
		ShopezzySubCatDo shopezzySubCatDo = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			
			Criteria criteria = session.createCriteria(ShopezzySubCatDo.class);
			
			if(shopezzyCategoryDo!=null){
				criteria = criteria.add(Restrictions.eq("shopezzyCategoryDo", shopezzyCategoryDo));
			}
			
			List<ShopezzySubCatDo> shopezzySubCatDos = criteria.add(Restrictions.eq("subCatName", subCatName)).list();
			
			if(shopezzySubCatDos!=null && shopezzySubCatDos.size()>0){
				shopezzySubCatDo = shopezzySubCatDos.get(0);
			}
					
			
		}catch (Exception e) {
			logger.error("[ShopezzySubCatDaoImpl][selectBySubCatNameAndCat] Exception: "+e);
		}finally{
			session.close();
		}
		return shopezzySubCatDo;
	}

	@Override
	public boolean saveOrUpdate(ShopezzySubCatDo shopezzySubCatDo) {
		Session session = null;
		boolean flag = false;
		
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(shopezzySubCatDo);
			tx.commit();
			flag = true;
		}catch (Exception e) {
			logger.error("[ShopezzySubCatDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ShopezzySubCatDo> selectByCategory(ShopezzyCategoryDo shopezzyCategoryDo) {
		List<ShopezzySubCatDo> shopezzySubCatDos = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			shopezzySubCatDos = session.createCriteria(ShopezzySubCatDo.class)
					.add(Restrictions.eq("shopezzyCategoryDo", shopezzyCategoryDo))
					.list();
			
		}catch (Exception e) {
			logger.error("[ShopezzySubCatDaoImpl][selectByCategory] Exception: "+e);
		}finally{
			session.close();
		}
		return shopezzySubCatDos;
	}

	@Override
	public ShopezzySubCatDo selectById(long id) {
		ShopezzySubCatDo shopezzySubCatDo = null;
		Session  session = null;
		
		try{
			session = factory.openSession();
			shopezzySubCatDo = (ShopezzySubCatDo)session.get(ShopezzySubCatDo.class, id);
			
		}catch (Exception e) {
			logger.error("[ShopezzySubCatDaoImpl][selectById] Exception: "+e);
		}finally{
			session.close();
		}
		
		return shopezzySubCatDo;
	}
	
}
