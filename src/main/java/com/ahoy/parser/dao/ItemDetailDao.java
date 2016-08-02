package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemDetailDo;
import com.ahoy.parser.domain.ItemListDo;

public interface ItemDetailDao {
	
	public long save(ItemDetailDo itemDetailDo);
	
	public boolean saveOrUpdate(ItemDetailDo itemDetailDo);
	
	public ItemDetailDo selectByWeightAnditemList(ItemListDo itemListDo, String weight);
}
