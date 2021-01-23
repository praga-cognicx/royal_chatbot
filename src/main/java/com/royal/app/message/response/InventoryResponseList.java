package com.royal.app.message.response;

import java.util.List;
import com.royal.app.model.KeyValueObject;

public class InventoryResponseList {
	List<KeyValueObject> regions;
	List<KeyValueObject> centers;
	List<KeyValueObject> clients;
	List<KeyValueObject> processes;
	List<KeyValueObject> categories;
	public List<KeyValueObject> getRegions() {
		return regions;
	}
	public void setRegions(List<KeyValueObject> regions) {
		this.regions = regions;
	}
	public List<KeyValueObject> getCenters() {
		return centers;
	}
	public void setCenters(List<KeyValueObject> centers) {
		this.centers = centers;
	}
	public List<KeyValueObject> getClients() {
		return clients;
	}
	public void setClients(List<KeyValueObject> clients) {
		this.clients = clients;
	}
	public List<KeyValueObject> getProcesses() {
		return processes;
	}
	public void setProcesses(List<KeyValueObject> processes) {
		this.processes = processes;
	}
	public List<KeyValueObject> getCategories() {
		return categories;
	}
	public void setCategories(List<KeyValueObject> categories) {
		this.categories = categories;
	}
	
}
