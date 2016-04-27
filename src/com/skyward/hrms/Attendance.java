package com.skyward.hrms;

public class Attendance {
	String remarks;
	String date;
	String visitTime;
	String visitType;
	int CustomerVisitID;

	@SuppressWarnings("unused")
	private Attendance() {
	}

	Attendance(String remarks, String date, String visitTime, String visitType,
			int CustomerVisitID) {
		this.remarks = remarks;
		this.date = date;
		this.visitTime = visitTime;
		this.visitType = visitType;
		this.CustomerVisitID = CustomerVisitID;
	}

	public int getCustomerVisitID() {
		return CustomerVisitID;
	}

	public void setCustomerVisitID(int customerVisitID) {
		CustomerVisitID = customerVisitID;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getVisitTime() {
		return visitTime;
	}

	public void setVisitTime(String visitTime) {
		this.visitTime = visitTime;
	}

	public String getVisitType() {
		return visitType;
	}

	public void setVisitType(String visitType) {
		this.visitType = visitType;
	}
}
