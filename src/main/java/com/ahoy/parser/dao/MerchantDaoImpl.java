package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MerchantDaoImpl implements MerchantDao{
	
	private static Logger logger =  LoggerFactory.getLogger(MerchantDaoImpl.class);
	private SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public MerchantDo getByMerchantname(String merchantName) {
		MerchantDo merchantDo = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			List<MerchantDo> merchantDos = session.createCriteria(MerchantDo.class)
					.add(Restrictions.eq("merchantName", merchantName))
					.addOrder(Order.asc("merchantName")).list();
			
			if(merchantDos!=null && merchantDos.size()>0){
				merchantDo = merchantDos.get(0);
			}
			
		}catch(Exception e){
			logger.error("[MerchantDaoImpl][getByMerchantname] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantDo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantDo> selectAll() {
		List<MerchantDo> merchantDos=null;
		Session session =null;
		try{
			session = factory.openSession();
			merchantDos = session.createCriteria(MerchantDo.class)
							.add(Restrictions.eq("status", (short)0))
							.addOrder(Order.asc("merchantName")).list();
		}catch(Exception e){
			logger.error("[MerchantDaoImpl][selectAll] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantDos;
	}

	@Override
	public MerchantDo getByMerchantId(long merchantId) {
		MerchantDo merchantDo = null;
		Session session = null;
		
		try{
			session = factory.openSession();
			merchantDo = (MerchantDo)session.get(MerchantDo.class, merchantId);			
		}catch(Exception e){
			logger.error("[MerchantDaoImpl][getByMerchantname] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantDo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<MerchantDo> getByType(short type) {
		List<MerchantDo>  merchantDos=null;
		Session session = null;
		try{
			
			session = factory.openSession();
			merchantDos = session.createCriteria(MerchantDo.class)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.createAlias("cityDos", "c")
					.add(Restrictions.eq("status", (short)0))					
					.add(Restrictions.eq("type", type))
					.addOrder(Order.asc("merchantName"))
					.addOrder(Order.asc("c.cityName"))
					.list();
			
		}finally{
			session.close();
		}
		
		return merchantDos;
	}
	
	

}
