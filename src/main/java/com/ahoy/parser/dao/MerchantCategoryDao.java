package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantCategoryDo;
import com.ahoy.parser.domain.MerchantDo;

import java.util.List;

public interface MerchantCategoryDao {

	public MerchantCategoryDo selectByCategoryNameAndMerchant(String catName, MerchantDo merchantDo);
	
	public boolean saveOrUpdate(MerchantCategoryDo merchantCategoryDo);
	
	public List<MerchantCategoryDo> selectByMerchant(MerchantDo merchantDo);
}
