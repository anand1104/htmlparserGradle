package com.ahoy.parser.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="merchant_category")
public class MerchantCategoryDo {

	@Id
	@GeneratedValue
	@Column(name="cat_id")
	private long catId;
	
	@Column(name="cat_name")
	private String catName;
	
	@ManyToOne
	@JoinColumn(name="merchant_fkey")
	private MerchantDo merchantDo;	
	
	@OneToMany(mappedBy="merchantCategoryDo")
	Set<MerchantSubCatDo> merchantSubCatDos;
	
//	@OneToMany(mappedBy="merchantCategoryDo")
//	Set<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos; 
	
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
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

	public MerchantDo getMerchantDo() {
		return merchantDo;
	}

	public void setMerchantDo(MerchantDo merchantDo) {
		this.merchantDo = merchantDo;
	}

	public Set<MerchantSubCatDo> getMerchantSubCatDos() {
		return merchantSubCatDos;
	}

	public void setMerchantSubCatDos(Set<MerchantSubCatDo> merchantSubCatDos) {
		this.merchantSubCatDos = merchantSubCatDos;
	}
				
}
