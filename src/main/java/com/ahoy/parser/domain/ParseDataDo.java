package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name="data")
public class ParseDataDo implements Serializable{
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue
	@Column(name="data_pkey")
	private long dataPkey;
	
	@Column(name="description")
	private String description;
	
	@Column(name="max_price")
	private String maxPrice;
	
	@Column(name="sell_price")
	private String sellPrice;
	
	@Column(name="offer")
	private String offer;
	
	@Column(name="image_url")
	private String imageUrl;

	@Column(name="store_pid")
	private long storePId=0;

	@Column(name="store_vid")
	private long storeVId=0;

	@Column(name="sku")
	private String sku="";
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;

	@Column(name="weight")
	private String weight;	

	@Column(name="locality")
	private String locality="";
	
	@Column(name="vendor")
	private String vendor="";
	
	@Column(name="merchant_fkey")
	private long merchantId;
	
	@Column(name="city_fkey")
	private long cityId;
	
	@Column(name="link")
	private String link;
	
//	@ManyToOne
//	@JoinColumn(name="merchant_fkey")
//	private MerchantDo merchantDo;
//	
//	@ManyToOne
//	@JoinColumn(name="city_fkey")
//	private CityDo cityDo;
	
	
//	@OneToMany(mappedBy="cityDo")
//	Set<ParseDataDo> parseDataDos;
	
//	@ManyToOne
//	@JoinColumn(name="mscm_fkey")
//	private MerchantShopezzyCatMapDo merchantShopezzyCatMapDo;
	
	@ManyToOne
	@JoinColumn(name="m_subcat_fkey")
	private MerchantSubCatDo merchantSubCatDo;
	
	@ManyToOne
	@JoinColumn(name="s_subcat_fkey")
	private ShopezzySubCatDo shopezzySubCatDo;
	
	@Column(name="status")
	private short status=0;
	
//	 1 available, 2 means out of stock
//	alter table data add column availablity smallint default 1;
	@Column(name="availablity")
	private short availablity;
	
	
//	public Set<ParseDataDo> getParseDataDos() {
//		return parseDataDos;
//	}
//
//	public void setParseDataDos(Set<ParseDataDo> parseDataDos) {
//		this.parseDataDos = parseDataDos;
//	}

	public long getDataPkey() {
		return dataPkey;
	}

	public void setDataPkey(long dataPkey) {
		this.dataPkey = dataPkey;
	}
		
//	public String getCategoryName() {
//		return categoryName;
//	}
//
//	public void setCategoryName(String categoryName) {
//		this.categoryName = categoryName;
//	}
//
//	public String getSubCatName() {
//		return subCatName;
//	}
//
//	public void setSubCatName(String subCatName) {
//		this.subCatName = subCatName;
//	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(String maxPrice) {
		this.maxPrice = maxPrice;
	}

	public String getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(String sellPrice) {
		this.sellPrice = sellPrice;
	}

	public String getOffer() {
		return offer;
	}

	public void setOffer(String offer) {
		this.offer = offer;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

//	public String getSource() {
//		return source;
//	}
//
//	public void setSource(String source) {
//		this.source = source;
//	}

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
	
	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getLocality() {
		return locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

//	public MerchantDo getMerchantDo() {
//		return merchantDo;
//	}
//
//	public void setMerchantDo(MerchantDo merchantDo) {
//		this.merchantDo = merchantDo;
//	}

//	public CityDo getCityDo() {
//		return cityDo;
//	}
//
//	public void setCityDo(CityDo cityDo) {
//		this.cityDo = cityDo;
//	}

	public MerchantSubCatDo getMerchantSubCatDo() {
		return merchantSubCatDo;
	}

	public void setMerchantSubCatDo(MerchantSubCatDo merchantSubCatDo) {
		this.merchantSubCatDo = merchantSubCatDo;
	}

	public ShopezzySubCatDo getShopezzySubCatDo() {
		return shopezzySubCatDo;
	}

	public void setShopezzySubCatDo(ShopezzySubCatDo shopezzySubCatDo) {
		this.shopezzySubCatDo = shopezzySubCatDo;
	}

	public short getStatus() {
		return status;
	}

	public void setStatus(short status) {
		this.status = status;
	}

	public long getStorePId() {
		return storePId;
	}

	public void setStorePId(long storePId) {
		this.storePId = storePId;
	}

	public long getStoreVId() {
		return storeVId;
	}

	public void setStoreVId(long storeVId) {
		this.storeVId = storeVId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public short getAvailablity() {
		return availablity;
	}

	public void setAvailablity(short availablity) {
		this.availablity = availablity;
	}

	public long getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(long merchantId) {
		this.merchantId = merchantId;
	}

	public long getCityId() {
		return cityId;
	}

	public void setCityId(long cityId) {
		this.cityId = cityId;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
}
