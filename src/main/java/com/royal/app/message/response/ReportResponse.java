package com.royal.app.message.response;

import java.math.BigInteger;

public class ReportResponse {

    private BigInteger id;

    private String reportName;

    private String status;

    public ReportResponse() {}
    
    public ReportResponse(BigInteger id, String reportName, String status) {

	this.id = id;
	this.reportName = reportName;
	this.status = status;

    }

    public BigInteger getId() {
	return id;
    }

    public void setId(BigInteger id) {
	this.id = id;
    }


    public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}

	public String getStatus() {
	return status;
    }

    public void setStatus(String status) {
	this.status = status;
    }

}
