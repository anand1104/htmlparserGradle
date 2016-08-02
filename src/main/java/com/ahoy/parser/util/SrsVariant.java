package com.ahoy.parser.util;

public class SrsVariant {

	private long variantId;
	
	private String sku;
	
	private String variant;
	
	private String mrp;
	
	private String sellPrice;
	
//	private String saveRs;
	
	private String offer;
	
	

	public SrsVariant(long variantId, String sku, String variant, String mrp,
			String sellPrice, String offer) {
		this.variantId = variantId;
		this.sku = sku;
		this.variant = variant;
		this.mrp = mrp;
		this.sellPrice = sellPrice;
		this.offer = offer;
	}

	public long getVariantId() {
		return variantId;
	}

	public void setVariantId(long variantId) {
		this.variantId = variantId;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
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
}
