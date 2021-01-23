package com.royal.app.message.response;

import java.math.BigInteger;

public class ProcessResponse {
	
	private BigInteger inventoryProcessId;
	
	private String inventoryProcessName;

	public BigInteger getInventoryProcessId() {
		return inventoryProcessId;
	}

	public void setInventoryProcessId(BigInteger inventoryProcessId) {
		this.inventoryProcessId = inventoryProcessId;
	}

	public String getInventoryProcessName() {
		return inventoryProcessName;
	}

	public void setInventoryProcessName(String inventoryProcessName) {
		this.inventoryProcessName = inventoryProcessName;
	}

}
