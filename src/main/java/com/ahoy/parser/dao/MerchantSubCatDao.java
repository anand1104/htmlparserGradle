package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantCategoryDo;
import com.ahoy.parser.domain.MerchantSubCatDo;

import java.util.List;

public interface MerchantSubCatDao {

	public MerchantSubCatDo selectSubCatNameAndCatgory(String subCatName, MerchantCategoryDo merchantCategoryDo);
	
	public boolean saveOrUpdate(MerchantSubCatDo merchantSubCatDo);
	
	public List<MerchantSubCatDo> selectByCategory(MerchantCategoryDo merchantCategoryDo);
}
