package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemListDo;
import com.ahoy.parser.util.GetStackElements;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ItemListDaoImpl implements ItemListDao{

	Logger logger = LoggerFactory.getLogger(ItemListDaoImpl.class);
	SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ItemListDo> selectItems(int idx, int offset) {
		List<ItemListDo> itemListDos = null;
		Session session = null;
		try{
			
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ItemListDo.class);
			
			criteria.setFirstResult(idx);
			
			if(offset>0){
				criteria.setMaxResults(offset);
			}
			
			itemListDos = criteria.list();
			
		}catch (Exception e) {
			logger.error("[ItemListDaoImpl][selectItems] Exception: "+e+" | "+GetStackElements.getRootCause(e, getClass().getName()));
		}finally{
			session.close();
		}
		return itemListDos;
	}

	@Override
	public long getCount() {
		long count = 0;
		Session session=null;
		
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ItemListDo.class);
			
			count = Long.valueOf(criteria.setProjection(Projections.rowCount()).list().get(0).toString());
			
		}catch (Exception e) {
			logger.error("[ItemListDaoImpl][getCount] Exception: "+e);
		}finally{
			session.close();
		}
		
		return count;
	}

	@Override
	public ItemListDo getByItemName(String itemName) {
		ItemListDo itemListDo=null;
		Session session = null;
		try{
			
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ItemListDo.class)
					.add(Restrictions.eq("itemName", itemName));
			
			if(criteria.list()!=null && criteria.list().size()>0){
				itemListDo = (ItemListDo)criteria.list().get(0);
			}
			
		}catch (Exception e) {
			logger.error("[ItemListDaoImpl][getByItemName] Exception: "+e);
		}finally{
			session.close();
		}
		
		return itemListDo;
	}

	@Override
	public long save(ItemListDo itemListDo) {
		long id = 0;
		Session session = null;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			id = Long.valueOf(session.save(itemListDo).toString());
			tx.commit();
			
		}catch (Exception e) {
			logger.error("[ItemListDaoImpl][save] Exception: "+e);
		}finally{
			session.close();
		}
		return id;
	}

}
