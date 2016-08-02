package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name="brand")
public class BrandDo implements Serializable{
	
	@Id
	@GeneratedValue
	@Column(name="brand_id")
	private long brandId;
	
	@Column(name="brand_name")
	private String brandName;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@ManyToOne
	@JoinColumn(name="company_fkey")
	private CompanyDo companyDo;
	
	@OneToMany(mappedBy="brandDo")
	Set<ProductDo> productDos;
	
	
	public long getBrandId() {
		return brandId;
	}

	public void setBrandId(long brandId) {
		this.brandId = brandId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public CompanyDo getCompanyDo() {
		return companyDo;
	}

	public void setCompanyDo(CompanyDo companyDo) {
		this.companyDo = companyDo;
	}

	public Set<ProductDo> getProductDos() {
		return productDos;
	}

	public void setProductDos(Set<ProductDo> productDos) {
		this.productDos = productDos;
	}	
}
