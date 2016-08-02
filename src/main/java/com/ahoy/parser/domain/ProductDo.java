package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="product")
@SuppressWarnings("serial")
public class ProductDo implements Serializable{

	@Id
	@GeneratedValue
	@Column(name="product")
	private long productId;
	
	@Column(name="product_name")
	private String productName;
	
	@Column(name="varient")
	private String varient;
	
	@Column(name="perishable")
	private short perishable;
	
	@Column(name="comment")
	private String comment;
	
	@Column(name="image_name")
	private String imageName;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@ManyToOne
	@JoinColumn(name="sub_cat_fkey")
	private ShopezzySubCatDo shopezzySubCatDo;
	
	@ManyToOne
	@JoinColumn(name="brand_fkey")
	private BrandDo brandDo;
	
	@OneToMany(mappedBy="productDo")
	private Set<ProductVarientsDo> productVarientsDos;

	
	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getVarient() {
		return varient;
	}

	public void setVarient(String varient) {
		this.varient = varient;
	}

	public short getPerishable() {
		return perishable;
	}

	public void setPerishable(short perishable) {
		this.perishable = perishable;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public ShopezzySubCatDo getShopezzySubCatDo() {
		return shopezzySubCatDo;
	}

	public void setShopezzySubCatDo(ShopezzySubCatDo shopezzySubCatDo) {
		this.shopezzySubCatDo = shopezzySubCatDo;
	}

	public BrandDo getBrandDo() {
		return brandDo;
	}

	public void setBrandDo(BrandDo brandDo) {
		this.brandDo = brandDo;
	}

	public Set<ProductVarientsDo> getProductVarientsDos() {
		return productVarientsDos;
	}

	public void setProductVarientsDos(Set<ProductVarientsDo> productVarientsDos) {
		this.productVarientsDos = productVarientsDos;
	}
	
	
}
