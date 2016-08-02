package com.ahoy.parser.dao;

import com.ahoy.parser.domain.ItemListDo;

import java.util.List;

public interface ItemListDao {

	public List<ItemListDo> selectItems(int idx, int offset);
	
	public long getCount();
	
	public ItemListDo getByItemName(String itemName);
	
	public long save(ItemListDo itemListDo);
}
