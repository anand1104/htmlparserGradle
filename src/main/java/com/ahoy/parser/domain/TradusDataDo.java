package com.ahoy.parser.domain;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name="tradus_data")
public class TradusDataDo {
	
	@Id
	@GeneratedValue
	@Column(name="tradus_data_pkey")
	private long tradusDataPkey;
	
	@Column(name="city")
	private String city;
	
	@Column(name="locality")
	private String locality;
	
	@Column(name="category_name")
	private String categoryName;
	
	@Column(name="product_name")
	private String productName;
	
	@Column(name="product_image")
	private String productImage;
	
	@Column(name="max_price")
	private String maxPrice;
	
	@Column(name="offer")
	private String offer;
	
	@Column(name="sell_price")
	private String sellPrice;
	
	@Column(name="last_max_price")
	private String lastMaxPrice;
	
	@Column(name="last_offer")
	private String lastOffer;
	
	@Column(name="last_sell_price")
	private String lastSellPrice;
	
	@Column(name="changes_made")
	private String changesMade;
	
	@Column(name="vendor")
	private String vendor;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;

	
	public long getTradusDataPkey() {
		return tradusDataPkey;
	}

	public void setTradusDataPkey(long tradusDataPkey) {
		this.tradusDataPkey = tradusDataPkey;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}
		
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductImage() {
		return productImage;
	}

	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getLastMaxPrice() {
		return lastMaxPrice;
	}

	public void setLastMaxPrice(String lastMaxPrice) {
		this.lastMaxPrice = lastMaxPrice;
	}

	public String getLastOffer() {
		return lastOffer;
	}

	public void setLastOffer(String lastOffer) {
		this.lastOffer = lastOffer;
	}

	public String getLastSellPrice() {
		return lastSellPrice;
	}

	public void setLastSellPrice(String lastSellPrice) {
		this.lastSellPrice = lastSellPrice;
	}

	public String getChangesMade() {
		return changesMade;
	}

	public void setChangesMade(String changesMade) {
		this.changesMade = changesMade;
	}
	
	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Timestamp getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Timestamp createdOn) {
		this.createdOn = createdOn;
	}

	public Timestamp getUpdatedOn() {
		return updatedOn;
	}

	public void setUpdatedOn(Timestamp updatedOn) {
		this.updatedOn = updatedOn;
	}
	
}
