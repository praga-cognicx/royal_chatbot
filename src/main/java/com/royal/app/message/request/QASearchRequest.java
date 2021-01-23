package com.royal.app.message.request;

import java.math.BigInteger;
import java.util.List;

import javax.validation.constraints.NotNull;

public class QASearchRequest {
	
	@NotNull
	private List<BigInteger> regions;
	@NotNull
	private List<BigInteger> centers;
	@NotNull
	private List<BigInteger> clients;
	@NotNull
	private List<BigInteger> processes;
	
	public List<BigInteger> getRegions() {
		return regions;
	}
	public void setRegions(List<BigInteger> regions) {
		this.regions = regions;
	}
	public List<BigInteger> getCenters() {
		return centers;
	}
	public void setCenters(List<BigInteger> centers) {
		this.centers = centers;
	}
	public List<BigInteger> getClients() {
		return clients;
	}
	public void setClients(List<BigInteger> clients) {
		this.clients = clients;
	}
	public List<BigInteger> getProcesses() {
		return processes;
	}
	public void setProcesses(List<BigInteger> processes) {
		this.processes = processes;
	}
	
	

}
