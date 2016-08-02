package com.ahoy.parser.dao;

import com.ahoy.parser.domain.TradusDataDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TradusDataDaoImpl implements TradusDataDao{
	
	Logger logger = LoggerFactory.getLogger(TradusDataDaoImpl.class);
	private SessionFactory factory = HSession.createSessionFactory();

	@Override
	public void saveorUpdate(TradusDataDo tradusDataDo) {
		Session session = null;
		try{
			
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(tradusDataDo);
			tx.commit();
				
		}catch (Exception e) {
			logger.error("[TradusDataDaoImpl][saveorUpdate] Exception: "+e);
		}finally{
			session.close();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TradusDataDo select(String city, String locality, String productName) {
		TradusDataDo tradusDataDo = null;
		Session session = null;
		try{
			session = factory.openSession();
			
			List<TradusDataDo> tradusDataDos = session.createCriteria(TradusDataDo.class)
					.add(Restrictions.eq("city", city))
					.add(Restrictions.eq("locality", locality))
					.add(Restrictions.eq("productName", productName))
					.list();
			
			if(tradusDataDos!=null && tradusDataDos.size()>0){
				tradusDataDo = tradusDataDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[TradusDataDaoImpl][select] Exception: "+e);
		}finally{
			session.close();
		}
		
		return tradusDataDo;
	}
 
	
}
