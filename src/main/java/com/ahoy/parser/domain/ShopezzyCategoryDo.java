package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name="shopezzy_category")
public class ShopezzyCategoryDo implements Serializable{

	@Id
	@GeneratedValue
	@Column(name="cat_id")
	private long catId;
	
	@Column(name="cat_name")
	private String catName;	
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@OneToMany(mappedBy="shopezzyCategoryDo")
	Set<ShopezzySubCatDo> shopezzySubCatDos;
	
//	@OneToMany(mappedBy="shopezzyCategoryDo")
//	Set<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos ;
	
	public long getCatId() {
		return catId;
	}

	public void setCatId(long catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Set<ShopezzySubCatDo> getShopezzySubCatDos() {
		return shopezzySubCatDos;
	}

	public void setShopezzySubCatDos(Set<ShopezzySubCatDo> shopezzySubCatDos) {
		this.shopezzySubCatDos = shopezzySubCatDos;
	}

				
}
