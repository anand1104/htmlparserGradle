package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name="company")
public class CompanyDo implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name="company_id")
	private long campanyId;
	
	@Column(name="company_name")
	private String companyName;
	
	@Column(name="created_on")
	private Timestamp created_on;
	
	@OneToMany(mappedBy="companyDo")
	Set<BrandDo> brandDos;
	

	public long getCampanyId() {
		return campanyId;
	}

	public void setCampanyId(long campanyId) {
		this.campanyId = campanyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Timestamp getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Timestamp created_on) {
		this.created_on = created_on;
	}

	public Set<BrandDo> getBrandDos() {
		return brandDos;
	}

	public void setBrandDos(Set<BrandDo> brandDos) {
		this.brandDos = brandDos;
	}	
	
	
}
