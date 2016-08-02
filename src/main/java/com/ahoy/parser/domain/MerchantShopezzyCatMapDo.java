package com.ahoy.parser.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;

@Entity
@Table(name="merchant_shopezzy_cat_map")
public class MerchantShopezzyCatMapDo {
	
	@Id
	@GeneratedValue
	@Column(name="merchant_shopezzy_cat_map_id")
	private long merchantShopezzyCatMapId;
	
//	@ManyToOne
//	@JoinColumn(name="merchant_cat_fkey")
//	private MerchantCategoryDo merchantCategoryDo;
	
//	@ManyToOne
//	@JoinColumn(name="shopezzy_cat_fkey")
//	private ShopezzyCategoryDo shopezzyCategoryDo;
	
	@ManyToOne
	@JoinColumn(name="merchant_sub_cat_fkey")
	private MerchantSubCatDo merchantSubCatDo;
	
	@ManyToOne
	@JoinColumn(name="shopezzy_sub_cat_fkey")
	private ShopezzySubCatDo shopezzySubCatDo;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn= new Timestamp(new Date().getTime());
	
//	@OneToMany(mappedBy="merchantShopezzyCatMapDo")
//	Set<ParseDataDo> parseDataDos;

	
	public long getMerchantShopezzyCatMapId() {
		return merchantShopezzyCatMapId;
	}

	public void setMerchantShopezzyCatMapId(long merchantShopezzyCatMapId) {
		this.merchantShopezzyCatMapId = merchantShopezzyCatMapId;
	}

	

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public MerchantSubCatDo getMerchantSubCatDo() {
		return merchantSubCatDo;
	}

	public void setMerchantSubCatDo(MerchantSubCatDo merchantSubCatDo) {
		this.merchantSubCatDo = merchantSubCatDo;
	}

	public ShopezzySubCatDo getShopezzySubCatDo() {
		return shopezzySubCatDo;
	}

	public void setShopezzySubCatDo(ShopezzySubCatDo shopezzySubCatDo) {
		this.shopezzySubCatDo = shopezzySubCatDo;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}		
}
