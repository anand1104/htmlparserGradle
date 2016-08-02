package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name="offline_item")
@SuppressWarnings("serial")
public class OfflineItemDo implements Serializable {

	@Id
	@GeneratedValue
	@Column(name="offline_item_pkey")
	private long offlineitemId;
	
	@Column(name="offer")
	private String offer;
	
	@Column(name="sell_price")
	private String sellPrice;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;
	
	@ManyToOne
	@JoinColumn(name="item_detail_fkey")
	private ItemDetailDo itemDetailDo;
	
	@ManyToOne
	@JoinColumn(name="merchant_fkey")
	private MerchantDo merchantDo;
	

	public long getOfflineitemId() {
		return offlineitemId;
	}

	public void setOfflineitemId(long offlineitemId) {
		this.offlineitemId = offlineitemId;
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

	
	public ItemDetailDo getItemDetailDo() {
		return itemDetailDo;
	}

	public void setItemDetailDo(ItemDetailDo itemDetailDo) {
		this.itemDetailDo = itemDetailDo;
	}

	public MerchantDo getMerchantDo() {
		return merchantDo;
	}

	public void setMerchantDo(MerchantDo merchantDo) {
		this.merchantDo = merchantDo;
	}	
	
	
}
