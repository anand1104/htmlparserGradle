package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.MerchantDo;
import com.ahoy.parser.domain.OfflineItemDo;

import java.util.List;

public interface OfflineItemDao {

	public List<OfflineItemDo> getByMerchant(MerchantDo merchantDo, int index, int offset);
	
	public long getCount(MerchantDo merchantDo);
	
	public OfflineItemDo getByMerchantAndItemDetail(MerchantDo merchantDo, ItemDetailDo itemDetailDo);
	
	public boolean saveOrUpdate(OfflineItemDo offlineItemDo);
}
