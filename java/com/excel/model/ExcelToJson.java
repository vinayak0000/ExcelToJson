package com.excel.model;

import java.util.Date;

public class ExcelToJson {
	
	
	private String currentSeatLocation;
	private String seatLocation;
	private int empID;
	private String userName;
	private int projectID;
	private Date dateOfMove;
	
	
	public String getCurrentSeatLocation() {
		return currentSeatLocation;
	}
	public void setCurrentSeatLocation(String currentSeatLocation) {
		this.currentSeatLocation = currentSeatLocation;
	}
	public String getSeatLocation() {
		return seatLocation;
	}
	public void setSeatLocation(String seatLocation) {
		this.seatLocation = seatLocation;
	}
	public int getEmpID() {
		return empID;
	}
	public void setEmpID(int empID) {
		this.empID = empID;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getProjectID() {
		return projectID;
	}
	public void setProjectID(int projectID) {
		this.projectID = projectID;
	}
	public Date getDateOfMove() {
		return dateOfMove;
	}
	public void setDateOfMove(Date dateOfMove) {
		this.dateOfMove = dateOfMove;
	}
	
	
}
