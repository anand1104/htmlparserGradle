package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ParseDataDo;

import java.util.Date;
import java.util.List;

public interface ParseDataDao {
	
	public boolean saveOrUpdate(ParseDataDo parseDataDo);
	
	public ParseDataDo select(long cityId, String description, String weight, String maxPrice, String offer, String sellPrice, String imageUrl, long merchantId) throws Exception;

	public List<ParseDataDo> select(long merchantId, Date startDate, Date endDate, int startIndex, int offset);
	
	public long getCount(String colName, long cityId, long merchantId, Date startDate, Date endDate);
	
	public List<Object[]> get(String colName, long merchantId, Date startDate, Date endDate);
	
	public List<ParseDataDo> select(String colName, long cityId, long merchantId, Date startDate, Date endDate, short status, int startIndex, int offset);
	
	public ParseDataDo selectById(long id);

	public List<Object[]> selectByMerchant(String colName, long merchantId, Date startDate, Date endDate);
	
	public List<Object[]> selectByMerchantAndCity(String colName, long merchantId, long cityId, Date startDate, Date endDate);
	
	public ParseDataDo getByPIdAndVid(long merchantId, long cityId, long pid, long vid);
	
	public List<ParseDataDo> getByCityDoAndMerchantDo(long merchantId, long cityId, Date sdate, Date edate);
}
