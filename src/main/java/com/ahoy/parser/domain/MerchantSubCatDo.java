package com.ahoy.parser.domain;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;


@Entity
@Table(name="merchant_sub_category")
public class MerchantSubCatDo {

	@Id
	@GeneratedValue
	@Column(name="subcat_id")
	private long subCatId;
	
	@Column(name="sub_cat_name")
	private String subCatName;
	
	@ManyToOne
	@JoinColumn(name="cat_fkey")
	private MerchantCategoryDo merchantCategoryDo;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@OneToMany(mappedBy="merchantSubCatDo")
	Set<MerchantShopezzyCatMapDo> merchantShopezzyCatMapDos; 
	
	@OneToMany(mappedBy="merchantSubCatDo")
	Set<ParseDataDo> parseDataDos ;
		
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

	public MerchantCategoryDo getMerchantCategoryDo() {
		return merchantCategoryDo;
	}

	public void setMerchantCategoryDo(MerchantCategoryDo merchantCategoryDo) {
		this.merchantCategoryDo = merchantCategoryDo;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
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
}
