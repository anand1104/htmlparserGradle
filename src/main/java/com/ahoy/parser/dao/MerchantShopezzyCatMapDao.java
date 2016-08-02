package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantShopezzyCatMapDo;
import com.ahoy.parser.domain.MerchantSubCatDo;
import com.ahoy.parser.domain.ShopezzySubCatDo;

public interface MerchantShopezzyCatMapDao {
	
	public MerchantShopezzyCatMapDo select(MerchantSubCatDo merchantSubCatDo, ShopezzySubCatDo shopezzySubCatDo);
	
	public boolean saveOrUpdate(MerchantShopezzyCatMapDo merchantShopezzyCatMapDo);
	
	public MerchantShopezzyCatMapDo select(MerchantSubCatDo merchantSubCatDo);
}