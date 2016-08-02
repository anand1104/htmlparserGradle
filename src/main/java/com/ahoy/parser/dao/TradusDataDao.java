package com.ahoy.parser.dao;

import com.ahoy.parser.domain.TradusDataDo;

public interface TradusDataDao {

	public void saveorUpdate(TradusDataDo tradusDataDo);
	
	public TradusDataDo select(String city, String locality, String productName);
	
}
