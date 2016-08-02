package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ShopezzyCategoryDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;

import java.util.List;

public interface ShopezzySubCatDao {
	
	public ShopezzySubCatDo selectBySubCatNameAndCat(String subCatName, ShopezzyCategoryDo shopezzyCategoryDo) ;
	
	public boolean saveOrUpdate(ShopezzySubCatDo shopezzySubCatDo);
	
	public List<ShopezzySubCatDo> selectByCategory(ShopezzyCategoryDo shopezzyCategoryDo);
	
	public ShopezzySubCatDo selectById(long id);
	
	
}
