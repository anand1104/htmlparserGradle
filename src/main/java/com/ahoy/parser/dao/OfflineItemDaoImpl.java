package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.OfflineItemDo;
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

public class OfflineItemDaoImpl implements OfflineItemDao{

	private Logger logger = LoggerFactory.getLogger(getClass());
	SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public List<OfflineItemDo> getByMerchant(MerchantDo merchantDo, int index,int offset) {
		Session session = null;
		List<OfflineItemDo> offlineItemDos=null;
		
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(OfflineItemDo.class);
			
			if(merchantDo!=null){
				criteria.add(Restrictions.eq("merchantDo", merchantDo));
			}
			
			criteria.setFirstResult(index);
			
			if(offset>0){
				criteria.setMaxResults(offset);
			}
			
			offlineItemDos = criteria.list();
			
		}catch (Exception e) {
			logger.error("[OfflineItemDaoImpl][getByMerchant] Exception: "+e);
		}finally{
			session.close();
		}
		
		return offlineItemDos;
	}

	@Override
	public long getCount(MerchantDo merchantDo) {
		Session session = null;
		long count = 0;
		
		try{
			
			session = factory.openSession();
			Criteria criteria = session.createCriteria(OfflineItemDo.class);
			
			if(merchantDo!=null){
				criteria.add(Restrictions.eq("merchantDo", merchantDo));
			}
			
			count = Long.valueOf(criteria.setProjection(Projections.rowCount()).list().get(0).toString());
			
		}catch (Exception e) {
			logger.error("[OfflineItemDaoImpl][getCount] Exception: "+e);
		}finally{
			session.close();
		}
		
		
		return count;
	}

	@Override
	public OfflineItemDo getByMerchantAndItemDetail(MerchantDo merchantDo,ItemDetailDo itemDetailDo) {
		Session session = null;
		OfflineItemDo offlineItemDo=null;
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(OfflineItemDo.class);
			
			if(merchantDo!=null){
				criteria.add(Restrictions.eq("merchantDo", merchantDo));
			}
			
			if(itemDetailDo!=null){
				criteria.add(Restrictions.eq("itemDetailDo", itemDetailDo));
			}
			
			if(criteria.list()!=null && criteria.list().size()>0){
				offlineItemDo = (OfflineItemDo) criteria.list().get(0);
			}
			
		}catch (Exception e) {
			logger.error("[OfflineItemDaoImpl][getCount] Exception: "+e);
		}finally{
			session.close();
		}
		
		return offlineItemDo;
	}

	@Override
	public boolean saveOrUpdate(OfflineItemDo offlineItemDo) {
		Session session = null;
		boolean flag = false;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(offlineItemDo);
			tx.commit();
			flag =true;
		}catch (Exception e) {
			logger.error("[OfflineItemDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		
		return flag;
	}
	
}
