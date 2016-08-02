package com.ahoy.parser.dao;

import com.ahoy.parser.domain.CityDo;

import java.util.List;

public interface CityDao {
	public CityDo getByName(String city);
	
	public List<CityDo> selectAll();
	
	public CityDo getByCityId(long cityId);
}
