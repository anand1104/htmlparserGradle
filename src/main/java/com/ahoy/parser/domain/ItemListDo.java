package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;


@SuppressWarnings("serial")
@Entity
@Table(name="item_list")
public class ItemListDo implements Serializable {

	@Id
	@GeneratedValue
	@Column(name="item_id")
	private long itemId;
	
	@Column(name="item_name")
	private String itemName;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;
	
	@ManyToOne
	@JoinColumn(name="sub_cat_fkey")
	private ShopezzySubCatDo shopezzySubCatDo;
	
	
	
	@OneToMany(mappedBy="itemListDo",fetch=FetchType.EAGER)
	Set<ItemDetailDo> itemDetailDos ;

	public long getItemId() {
		return itemId;
	}

	public void setItemId(long itemId) {
		this.itemId = itemId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}

	public ShopezzySubCatDo getShopezzySubCatDo() {
		return shopezzySubCatDo;
	}

	public void setShopezzySubCatDo(ShopezzySubCatDo shopezzySubCatDo) {
		this.shopezzySubCatDo = shopezzySubCatDo;
	}

	public Set<ItemDetailDo> getItemDetailDos() {
		return itemDetailDos;
	}

	public void setItemDetailDos(Set<ItemDetailDo> itemDetailDos) {
		this.itemDetailDos = itemDetailDos;
	}
	
	
}
