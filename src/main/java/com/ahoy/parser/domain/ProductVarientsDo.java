package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@SuppressWarnings("serial")
@Entity
@Table(name="product_varients")
public class ProductVarientsDo implements Serializable {

	@Id
	@GeneratedValue
	@Column(name="product_varients_id")
	private long productVarientId;
	
	@Column(name="varient")
	private String varient;
	
	@Column(name="mrp")
	private String mrp;
	
	@Column(name="image_name")
	private String imageName;
	
	@ManyToOne
	@JoinColumn(name="product_fkey")
	private ProductDo productDo;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	

	public long getProductVarientId() {
		return productVarientId;
	}

	public void setProductVarientId(long productVarientId) {
		this.productVarientId = productVarientId;
	}

	public String getVarient() {
		return varient;
	}

	public void setVarient(String varient) {
		this.varient = varient;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public ProductDo getProductDo() {
		return productDo;
	}

	public void setProductDo(ProductDo productDo) {
		this.productDo = productDo;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}	
}
