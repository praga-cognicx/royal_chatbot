package com.royal.app.message.request;

import java.math.BigInteger;

public class GenericeRequest {
	
	private String id;
	
	private BigInteger inventoryRegionId;
	
	private BigInteger inventoryCenterId;
	
	private BigInteger inventoryClientId;
	
	private BigInteger inventoryCategoryId;
	
	private String inventoryType;

	public String getInventoryType() {
		return inventoryType;
	}

	public void setInventoryType(String inventoryType) {
		this.inventoryType = inventoryType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigInteger getInventoryRegionId() {
		return inventoryRegionId;
	}

	public void setInventoryRegionId(BigInteger inventoryRegionId) {
		this.inventoryRegionId = inventoryRegionId;
	}

	public BigInteger getInventoryCenterId() {
		return inventoryCenterId;
	}

	public void setInventoryCenterId(BigInteger inventoryCenterId) {
		this.inventoryCenterId = inventoryCenterId;
	}

	public BigInteger getInventoryClientId() {
		return inventoryClientId;
	}

	public void setInventoryClientId(BigInteger inventoryClientId) {
		this.inventoryClientId = inventoryClientId;
	}

	public BigInteger getInventoryCategoryId() {
		return inventoryCategoryId;
	}

	public void setInventoryCategoryId(BigInteger inventoryCategoryId) {
		this.inventoryCategoryId = inventoryCategoryId;
	}

	
	
}
