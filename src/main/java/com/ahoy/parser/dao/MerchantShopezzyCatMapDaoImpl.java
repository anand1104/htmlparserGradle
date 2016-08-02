package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantShopezzyCatMapDo;
import com.ahoy.parser.domain.MerchantSubCatDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MerchantShopezzyCatMapDaoImpl implements MerchantShopezzyCatMapDao{
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public MerchantShopezzyCatMapDo select(MerchantSubCatDo merchantSubCatDo, ShopezzySubCatDo shopezzySubCatDo) {
		Session session = null;
		MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = null;
		try{
			
			session = factory.openSession();
			List<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos = session.createCriteria(MerchantShopezzyCatMapDo.class)
					.add(Restrictions.eq("merchantSubCatDo", merchantSubCatDo))
					.add(Restrictions.eq("shopezzySubCatDo", shopezzySubCatDo)).list();
			
			if(merchantShopezzyCatMapDos!=null && merchantShopezzyCatMapDos.size()>0){
				merchantShopezzyCatMapDo = merchantShopezzyCatMapDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[MerchantShopezzyCatMapDaoImpl][select] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantShopezzyCatMapDo;
	}

	@Override
	public boolean saveOrUpdate(MerchantShopezzyCatMapDo merchantShopezzyCatMapDo) {
		Session session = null;
		boolean flag = false;
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(merchantShopezzyCatMapDo);
			tx.commit();
			flag = true;
			
		}catch (Exception e) {
			logger.error("[MerchantShopezzyCatMapDaoImpl][saveOrUpdate] Exception: "+e);
		}finally{
			session.close();
		}
		
		return flag;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MerchantShopezzyCatMapDo select(MerchantSubCatDo merchantSubCatDo) {
		Session session = null;
		MerchantShopezzyCatMapDo merchantShopezzyCatMapDo = null;
		try{
			
			session = factory.openSession();
			List<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos = session.createCriteria(MerchantShopezzyCatMapDo.class)
					.add(Restrictions.eq("merchantSubCatDo", merchantSubCatDo))
					.list();
			
			if(merchantShopezzyCatMapDos!=null && merchantShopezzyCatMapDos.size()>0){
				merchantShopezzyCatMapDo = merchantShopezzyCatMapDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[MerchantShopezzyCatMapDaoImpl][select] Exception: "+e);
		}finally{
			session.close();
		}
		return merchantShopezzyCatMapDo;
	}

}
