package com.skyward.hrms;

import java.util.Date;

public class Customer {
	String customerName;
	String customerCode;
	int customerId;
	String panNo;
	String passportNo;
	String clinicAdress;
	String mobileNo;
	String emailId;
	String patients;
	String anniversaryDate;
	String DateOfBirth;

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerCode() {
		return customerCode;
	}

	public void setCustomerCode(String customerCode) {
		this.customerCode = customerCode;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getPanNo() {
		return panNo;
	}

	public void setPanNo(String panNo) {
		this.panNo = panNo;
	}

	public String getPassportNo() {
		return passportNo;
	}

	public void setPassportNo(String passportNo) {
		this.passportNo = passportNo;
	}

	public String getClinicAdress() {
		return clinicAdress;
	}

	public void setClinicAdress(String clinicAdress) {
		this.clinicAdress = clinicAdress;
	}

	public String getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getPatients() {
		return patients;
	}

	public void setPatients(String patients) {
		this.patients = patients;
	}

	public String getAnniversaryDate() {
		return anniversaryDate;
	}

	public void setAnniversaryDate(String anniversaryDate) {
		this.anniversaryDate = anniversaryDate;
	}

	public String getDateOfBirth() {
		return DateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}

	@SuppressWarnings("unused")
	private Customer() {
	}

	Customer(String customerName,String customerCode,int customerId,
			String panNo, String passportNo, String clinicAdress,
			String mobileNo, String emailId, String patients,
			String anniversaryDate, String DateOfBirth) {
		this.customerName = customerName;
		this.customerCode = customerCode;
		this.customerId = customerId;
		this.panNo = panNo;
		this.passportNo = passportNo;
		this.clinicAdress = clinicAdress;
		this.mobileNo = mobileNo;
		this.emailId = emailId;
		this.patients = patients;
		this.anniversaryDate = anniversaryDate;
		this.DateOfBirth = DateOfBirth;

	}

}
