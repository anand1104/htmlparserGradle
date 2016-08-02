package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name="city")
public class CityDo implements Serializable{

	@Id
	@GeneratedValue
	@Column(name="city_pkey")
	private long cityId;
	
	@Column(name="city_name")
	private String cityName;
	
	@Column(name="created_on")
	private Timestamp createdOn;

	@ManyToMany(mappedBy="cityDos",fetch=FetchType.EAGER)
	Set<MerchantDo> merchantDos = new HashSet<MerchantDo>();
	
	

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Set<MerchantDo> getMerchantDos() {
		return merchantDos;
	}

	public void setMerchantDos(Set<MerchantDo> merchantDos) {
		this.merchantDos = merchantDos;
	}

	
}
