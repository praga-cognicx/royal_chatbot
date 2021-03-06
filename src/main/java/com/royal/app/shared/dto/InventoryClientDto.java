package com.royal.app.shared.dto;

import java.math.BigInteger;

public class InventoryClientDto{
	
	private BigInteger inventoryClientId;
	
	private String inventoryClientName;
	

	public BigInteger getInventoryClientId() {
		return inventoryClientId;
	}

	public void setInventoryClientId(BigInteger inventoryClientId) {
		this.inventoryClientId = inventoryClientId;
	}

	public String getInventoryClientName() {
		return inventoryClientName;
	}

	public void setInventoryClientName(String inventoryClientName) {
		this.inventoryClientName = inventoryClientName;
	}	

}
