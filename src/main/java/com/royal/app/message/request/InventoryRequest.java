package com.royal.app.message.request;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class InventoryRequest {

    @NotBlank
    @Size(min = 3)
    private String inventoryType;

    @NotNull
    private List<String> names;

    @NotBlank
    private String status;

    public String getInventoryType() {
	return inventoryType;
    }

    public void setInventoryType(String inventoryType) {
	this.inventoryType = inventoryType;
    }

    public List<String> getNames() {
	return names;
    }

    public void setNames(List<String> names) {
	this.names = names;
    }

    public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

}
