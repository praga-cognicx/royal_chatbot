package com.royal.app.model;

import java.math.BigInteger;

public class Region {
	
	private BigInteger inventoryRegionId;
	private String inventoryRegionName;
	
	public BigInteger getInventoryRegionId() {
		return inventoryRegionId;
	}
	public void setInventoryRegionId(BigInteger inventoryRegionId) {
		this.inventoryRegionId = inventoryRegionId;
	}
	public String getInventoryRegionName() {
		return inventoryRegionName;
	}
	public void setInventoryRegionName(String inventoryRegionName) {
		this.inventoryRegionName = inventoryRegionName;
	}

}
