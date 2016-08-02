package com.ahoy.parser.dao;

import com.ahoy.parser.domain.CityDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class CityDaoImpl implements CityDao{
	
	private Logger logger = LoggerFactory.getLogger(CityDo.class);
	SessionFactory factory = HSession.createSessionFactory();
	
	@SuppressWarnings("unchecked")
	@Override
	public CityDo getByName(String city) {
		CityDo cityDo = null;
		Session session = null;
		try{
			session = factory.openSession();
			List<CityDo> cityDos = session.createCriteria(CityDo.class)
					.add(Restrictions.eq("cityName", city.toLowerCase()))
					.list();
			
			if(cityDos!=null&& cityDos.size()>0){
				cityDo = cityDos.get(0);
			}
		}catch(Exception e){
			logger.error("[CityDaoImpl][getByName] Exception: "+e);
		}finally{
			session.close();
		}
		return cityDo;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CityDo> selectAll() {
		List<CityDo> cityDos=null;
		Session session =null;
		
		try{
			session = factory.openSession();
			cityDos = session.createCriteria(CityDo.class)
					.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
					.addOrder(Order.asc("cityName"))
//					.setProjection(Projections.distinct(Projections.property("cityName")))
					.list();
		}catch(Exception e){
			logger.error("[CityDaoImpl][selectAll] Exception: "+e);
		}finally{
			session.close();
		}
		return cityDos;
	}

	@Override
	public CityDo getByCityId(long cityId) {
		CityDo cityDo = null;
		Session session = null;
		try{
			session = factory.openSession();
			cityDo  = (CityDo)session.get(CityDo.class, cityId);			
			
		}catch(Exception e){
			logger.error("[CityDaoImpl][getByName] Exception: "+e);
		}finally{
			session.close();
		}
		return cityDo;
	}

}
