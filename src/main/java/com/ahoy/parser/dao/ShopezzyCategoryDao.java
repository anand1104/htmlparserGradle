package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ShopezzyCategoryDo;

import java.util.List;

public interface ShopezzyCategoryDao {
	public ShopezzyCategoryDo selectByCatName(String catName) ;
	
	public boolean saveOrUpdate(ShopezzyCategoryDo shopezzyCategoryDo);
	
	public List<ShopezzyCategoryDo> selectAll();
	
	
}
