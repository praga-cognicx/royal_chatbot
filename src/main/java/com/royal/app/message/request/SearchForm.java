package com.royal.app.message.request;

public class SearchForm {
	
	private String fromDate;
	
	private String toDate;
	
	private String searchByName;
	
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public String getSearchByName() {
		return searchByName;
	}

	public void setSearchByName(String searchByName) {
		this.searchByName = searchByName;
	}	
	
	

}
