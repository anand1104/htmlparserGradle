package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ParseDataDo;
import com.ahoy.parser.util.HSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SuppressWarnings("unchecked")
public class ParseDataDaoImpl implements ParseDataDao{
	
	Logger logger = LoggerFactory.getLogger(ParseDataDaoImpl.class);
	private SessionFactory factory = HSession.createSessionFactory();
	
	@Override
	public boolean saveOrUpdate(ParseDataDo parseDataDo) {
		Session session = null;
		boolean flag = false;
		
		try{
			session = factory.openSession();
			Transaction tx = session.beginTransaction();
			session.saveOrUpdate(parseDataDo);
			tx.commit();
			flag=true;
		}finally{			
			session.close();
		}
		return flag;
	}

	@Override
	public ParseDataDo select(long cityId, String description,
			String weight, String maxPrice, String offer, String sellPrice,
			String imageUrl, long merchantId) throws Exception {
		Session session = null;
		ParseDataDo parseDataDo = null;
		try{
			session = factory.openSession();
			
			List<ParseDataDo> parseDataDos = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.eq("cityId", cityId))
					.add(Restrictions.eq("description", description))
					.add(Restrictions.eq("weight", weight))
					.add(Restrictions.eq("maxPrice", maxPrice))
					.add(Restrictions.eq("offer", offer))
					.add(Restrictions.eq("sellPrice", sellPrice))
					.add(Restrictions.eq("imageUrl", imageUrl))					
					.list();
			
			if(parseDataDos!=null&&parseDataDos.size()>0){
				parseDataDo = parseDataDos.get(0);
			}
			
		}catch (Exception e) {
			logger.error("[ParseDataDaoImpl][select] Exception: "+e);
			throw e;
		}finally{
			
			session.close();
		}
		return parseDataDo;
	}

	@Override
	public List<ParseDataDo> select(long merchantId, Date startDate,Date endDate,int startIndex,int offset) {
		List<ParseDataDo> parseDataDos=null;
		Session session = null;
		try{
			
			
			session = factory.openSession();
			parseDataDos= session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.between("createdOn", new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())))
					.setFirstResult(startIndex)
					.setMaxResults(offset).list();
		}finally{
			session.close();
		}
		return parseDataDos;
	}

	@Override
	public long getCount(String colName,long cityId,long merchantId, Date startDate, Date endDate) {
		long count=0;
		Session session = null;
		
		try{
			session = factory.openSession();
			
			Criteria criteria = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId));
			
			if(cityId>0){
				criteria.add(Restrictions.eq("cityId", cityId));
			}
			
			if(colName.equalsIgnoreCase("updatedon")){
				criteria.add(Restrictions.not(Restrictions.between("createdOn", startDate, endDate)));
			}
			
			colName = colName.equalsIgnoreCase("all")?"updatedOn":colName;
			criteria.add(Restrictions.between(colName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())));
			
			count = Long.valueOf(criteria.setProjection(Projections.rowCount()).list().get(0).toString());
			
		}finally{
			session.close();
		}
		
		return count;
	}

	@Override
	public List<ParseDataDo> select(String colName, long cityId,
			long merchantId, Date startDate, Date endDate,short status,
			int startIndex, int offset) {
		List<ParseDataDo> parseDataDos=null;
		Session session = null;
		try{
			
			
			session = factory.openSession();
			
			Criteria criteria = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("cityId", cityId))
					.add(Restrictions.eq("merchantId", merchantId));
			
			if(colName.equalsIgnoreCase("updatedon")){
				criteria.add(Restrictions.not(Restrictions.between("createdOn", startDate, endDate)));
			}
			
			colName = colName.equalsIgnoreCase("all")?"updatedOn":colName;
			criteria.add(Restrictions.between(colName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())))
					.setFirstResult(startIndex);
			
			if(status!=-1){
				criteria.add(Restrictions.eq("status", status));
			}
						
			if(offset>0){
				criteria = criteria.setMaxResults(offset);
			}
			
			parseDataDos=criteria.list();
		}finally{
			session.close();
		}
		return parseDataDos;
	}

	@Override
	public ParseDataDo selectById(long id) {
		Session session = null;
		ParseDataDo parseDataDo = null;
		try{
			session = factory.openSession();
			parseDataDo = (ParseDataDo)session.get(ParseDataDo.class, id);
			
		}finally{
			session.close();
		}
		return parseDataDo;
	}
	
	public List<Object[]> selectByMerchant(String colName,long merchantId, Date startDate, Date endDate){
		List<Object[]> objects = null;
		Session session = null;
		try{
			session = factory.openSession();
			
			Criteria criteria = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.between(colName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())));
			
			ProjectionList projList = Projections.projectionList()
					.add(Projections.groupProperty("status"))
					.add(Projections.rowCount());
	        
	        criteria.setProjection(projList);
			objects = criteria.addOrder(Order.asc("status")).list();
			
		}finally{
			session.close();
		}
		
		return objects;
	}

	@Override
	public ParseDataDo getByPIdAndVid(long merchantId, long cityId,
			long pid, long vid) {
		ParseDataDo parseDataDo = null;
		Session session = null;
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.eq("cityId", cityId))
					.add(Restrictions.eq("storePId", pid));
			
			if(vid>0){
				criteria.add(Restrictions.eq("storeVId", vid));
			}		
			
			if(!criteria.list().isEmpty() && criteria.list().size()>0){
				parseDataDo = (ParseDataDo)criteria.list().get(0);
			}
		}finally{
			session.close();
		}
		return parseDataDo;
	}

	@Override
	public List<Object[]> selectByMerchantAndCity(String colName,
			long merchantId, long cityId, Date startDate, Date endDate) {
		
		List<Object[]> objects = null;
		Session session = null;
		try{
			session = factory.openSession();
			
			Criteria criteria = session.createCriteria(ParseDataDo.class)
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.eq("cityId", cityId));
			
			if(colName.equalsIgnoreCase("updatedon")){
				criteria.add(Restrictions.not(Restrictions.between("createdOn", startDate, endDate)));
			}
			criteria.add(Restrictions.between(colName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())));
			
			ProjectionList projList = Projections.projectionList()
					.add(Projections.groupProperty("status"))
					.add(Projections.rowCount());
	        
	        criteria.setProjection(projList);
			objects = criteria.addOrder(Order.asc("status")).list();
			
		}finally{
			session.close();
		}
		
		return objects;
	}

	@Override
	public List<Object[]> get(String colName,long merchantId, Date startDate, Date endDate) {
		
		Session session = null;
		List<Object[]> objects = null;
		try{
			session = factory.openSession();
			
			ProjectionList projList = Projections.projectionList()
					.add(Projections.groupProperty("cityDo"))
					.add(Projections.rowCount());
			
			Criteria criteria = session.createCriteria(ParseDataDo.class);
			
						
			objects =criteria
					.add(Restrictions.eq("merchantId", merchantId))
					.add(Restrictions.between(colName, new Timestamp(startDate.getTime()), new Timestamp(endDate.getTime())))
					.setProjection(projList).list();
			
		}finally{
			session.close();
		}
		return objects;
	}

	@Override
	public List<ParseDataDo> getByCityDoAndMerchantDo(long merchantId,
			long cityId, Date sdate, Date edate) {
		Session session = null;
		List<ParseDataDo> parseDataDos = null;
		try{
			session = factory.openSession();
			Criteria criteria = session.createCriteria(ParseDataDo.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
				.add(Restrictions.eq("cityId", cityId))
				.add(Restrictions.eq("merchantId", merchantId))
				.add(Restrictions.between("updatedOn", sdate, edate));
			
			parseDataDos = criteria.list();
			
		}finally{
			session.close();
		}
		return parseDataDos;
	}

}
