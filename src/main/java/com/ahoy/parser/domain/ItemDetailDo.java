package com.ahoy.parser.domain;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name="item_detail")
public class ItemDetailDo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	@Column(name="item_detail_id")
	private long itemDetailId;
	
	@Column(name="weight")
	private String weight;
	
	@Column(name="mrp")
	private String mrp;
	
	@Column(name="created_on")
	private Timestamp createdOn;
	
	@Column(name="updated_on")
	private Timestamp updatedOn;
	
	@ManyToOne
	@JoinColumn(name="item_list_fkey")
	private ItemListDo itemListDo;	
	
	@OneToMany(mappedBy="itemDetailDo")
	Set<OfflineItemDo> offlineItemDos;

	public long getItemDetailId() {
		return itemDetailId;
	}

	public void setItemDetailId(long itemDetailId) {
		this.itemDetailId = itemDetailId;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
	}

	public String getMrp() {
		return mrp;
	}

	public void setMrp(String mrp) {
		this.mrp = mrp;
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

	public ItemListDo getItemListDo() {
		return itemListDo;
	}

	public void setItemListDo(ItemListDo itemListDo) {
		this.itemListDo = itemListDo;
	}

	public Set<OfflineItemDo> getOfflineItemDos() {
		return offlineItemDos;
	}

	public void setOfflineItemDos(Set<OfflineItemDo> offlineItemDos) {
		this.offlineItemDos = offlineItemDos;
	}	
}
