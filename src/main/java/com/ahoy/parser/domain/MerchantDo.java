package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="merchant")
public class MerchantDo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="merchant_pkey")
	private long merchantId;
	
	@Column(name="merchant_name")
	private String merchantName;
	
	@Column(name="created_on")
	private String createdOn;
	
	@Column(name="status")
	private short status=0;
	
	
//	type 1 means online and 0 means offline
	@Column(name="type")
	private short type;
	
//	@OneToMany(mappedBy="merchantDo")
//	Set<ParseDataDo> parseDataDos;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(name="merchant_city_map", 
				joinColumns={@JoinColumn(name="merchant_fkey",referencedColumnName="merchant_pkey")}, 
				inverseJoinColumns={@JoinColumn(name="city_fkey",referencedColumnName="city_pkey")})
	Set<CityDo> cityDos = new HashSet<CityDo>(0);
	
	
	@OneToMany(mappedBy="merchantDo")
	Set<MerchantCategoryDo> merchantCategoryDos;
	
	@Transient
	private List<CityDo> cityList;
	
//	@OneToMany(mappedBy="merchantDo")
//	Set<OfflineItemDo> offlineItemDos;
	

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public String getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(String createdOn) {
		this.createdOn = createdOn;
	}

//	public Set<ParseDataDo> getParseDataDos() {
//		return parseDataDos;
//	}
//
//	public void setParseDataDos(Set<ParseDataDo> parseDataDos) {
//		this.parseDataDos = parseDataDos;
//	}
	public Set<CityDo> getCityDos() {
		return cityDos;
	}

	public void setCityDos(Set<CityDo> cityDos) {
		this.cityDos = cityDos;
	}

	public Set<MerchantCategoryDo> getMerchantCategoryDos() {
		return merchantCategoryDos;
	}

	public void setMerchantCategoryDos(Set<MerchantCategoryDo> merchantCategoryDos) {
		this.merchantCategoryDos = merchantCategoryDos;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}
//
//	public Set<OfflineItemDo> getOfflineItemDos() {
//		return offlineItemDos;
//	}
//
//	public void setOfflineItemDos(Set<OfflineItemDo> offlineItemDos) {
//		this.offlineItemDos = offlineItemDos;
//	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public List<CityDo> getCityList() {
		return cityList;
	}

	public void setCityList(List<CityDo> cityList) {
		this.cityList = cityList;
	}
	
}
