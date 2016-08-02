package com.ahoy.parser.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


@Entity
@Table(name="shopezzy_sub_category")
public class ShopezzySubCatDo {
	
	@Id
	@GeneratedValue
	@Column(name="subcat_id")
	private long subCatId;
	
	@Column(name="sub_cat_name")
	private String subCatName;
	
	@ManyToOne
	@JoinColumn(name="cat_fkey")
	private ShopezzyCategoryDo shopezzyCategoryDo;
	
	@OneToMany(mappedBy="shopezzySubCatDo")
	Set<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos; 
	
	@OneToMany(mappedBy="shopezzySubCatDo")
	Set<ParseDataDo> parseDataDos ;
	
	@OneToMany(mappedBy="shopezzySubCatDo")
	Set<ItemListDo> itemListDos;
	
	@OneToMany(mappedBy="shopezzySubCatDo")
	Set<ProductDo> productDos;
	
	@Column(name="created_on")
	private Timestamp createdOn;

	public long getSubCatId() {
		return subCatId;
	}

	public void setSubCatId(long subCatId) {
		this.subCatId = subCatId;
	}

	public String getSubCatName() {
		return subCatName;
	}

	public void setSubCatName(String subCatName) {
		this.subCatName = subCatName;
	}

//	public ShopezzyCategoryDo getShopezzyCategoryDo() {
//		return shopezzyCategoryDo;
//	}
//
//	public void setShopezzyCategoryDo(ShopezzyCategoryDo shopezzyCategoryDo) {
//		this.shopezzyCategoryDo = shopezzyCategoryDo;
//	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public ShopezzyCategoryDo getShopezzyCategoryDo() {
		return shopezzyCategoryDo;
	}

	public void setShopezzyCategoryDo(ShopezzyCategoryDo shopezzyCategoryDo) {
		this.shopezzyCategoryDo = shopezzyCategoryDo;
	}

	public Set<ParseDataDo> getParseDataDos() {
		return parseDataDos;
	}

	public void setParseDataDos(Set<ParseDataDo> parseDataDos) {
		this.parseDataDos = parseDataDos;
	}

	public Set<MerchantShopezzyCatMapDo> getMerchantShopezzyCatMapDos() {
		return merchantShopezzyCatMapDos;
	}

	public void setMerchantShopezzyCatMapDos(
			Set<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos) {
		this.merchantShopezzyCatMapDos = merchantShopezzyCatMapDos;
	}

	public Set<ItemListDo> getItemListDos() {
		return itemListDos;
	}

	public void setItemListDos(Set<ItemListDo> itemListDos) {
		this.itemListDos = itemListDos;
	}

	public Set<ProductDo> getProductDos() {
		return productDos;
	}

	public void setProductDos(Set<ProductDo> productDos) {
		this.productDos = productDos;
	}
	
}
