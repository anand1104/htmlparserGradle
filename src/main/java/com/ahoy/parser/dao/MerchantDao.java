package com.ahoy.parser.dao;

import com.ahoy.parser.domain.MerchantDo;

import java.util.List;

public interface MerchantDao {
	public MerchantDo getByMerchantname(String merchantName);
	
	public MerchantDo getByMerchantId(long merchantId);
	public List<MerchantDo> selectAll();
	
	public List<MerchantDo> getByType(short type);
	
	
}
