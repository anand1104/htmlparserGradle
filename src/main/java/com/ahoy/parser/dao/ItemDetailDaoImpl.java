package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.ItemListDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ItemDetailDaoImpl implements ItemDetailDao{

	Logger logger = LoggerFactory.getLogger(ItemDetailDaoImpl.class);
	SessionFactory factory = HSession.createSessionFactory();
	
	@Override
	public long save(ItemDetailDo itemDetailDo) {
		long id = 0;
		Session session = null;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			id = Long.valueOf(session.save(itemDetailDo).toString());
			tx.commit();
			
		}catch (Exception e) {
			logger.error("[ItemDetailDaoImpl][save] Exception: "+e);
		}finally{
			session.close();
		}
		return id;
	}
	
	@Override
	public boolean saveOrUpdate(ItemDetailDo itemDetailDo) {
		boolean flag = false;
		Session session = null;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(itemDetailDo);
			tx.commit();
			flag = true;
			
		}catch (Exception e) {
			logger.error("[ItemDetailDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		return flag;
	}

	@Override
	public ItemDetailDo selectByWeightAnditemList(ItemListDo itemListDo,String weight) {
		ItemDetailDo itemDetailDo = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ItemDetailDo.class)
					.add(Restrictions.eq("itemListDo", itemListDo))
					.add(Restrictions.eq("weight",weight));
			
			if(criteria.list()!=null && criteria.list().size()>0){
				itemDetailDo = (ItemDetailDo)criteria.list().get(0);
			}
			
		}catch (Exception e) {
			logger.error("[ItemDetailDaoImpl][selectByWeightAnditemList] Exception: "+e);
		}finally{
			session.close();
		}		
		return itemDetailDo;
	}

}
