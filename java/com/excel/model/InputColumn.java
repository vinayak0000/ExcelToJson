package com.excel.model;


/**
 * @author 710149
 *
 */
public class InputColumn {

	private String sheetName;
	private String dateIndex;
	private String headcountColumnRowIndex;
	private String callForecastRowIndex;
	private String fteRequiredRowIndex;
	private String ahtRowIndex;
	private String shrinkageRowIndex;
	private String trainingHcRowIndex;
	private String productionHcRowIndex;
	private String overallHcRowIndex;
	private String attritionRowIndex;
	private String attritionPerRowIndex;
	private String deltaRowIndex;
	


	
	public String getSheetName() {
		return sheetName;
	}

	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}

	public String getDateIndex() {
		return dateIndex;
	}

	public void setDateIndex(String dateIndex) {
		this.dateIndex = dateIndex;
	}

	public String getHeadcountColumnRowIndex() {
		return headcountColumnRowIndex;
	}

	public void setHeadcountColumnRowIndex(String headcountColumnRowIndex) {
		this.headcountColumnRowIndex = headcountColumnRowIndex;
	}

	public String getCallForecastRowIndex() {
		return callForecastRowIndex;
	}

	public void setCallForecastRowIndex(String callForecastRowIndex) {
		this.callForecastRowIndex = callForecastRowIndex;
	}

	public String getFteRequiredRowIndex() {
		return fteRequiredRowIndex;
	}

	public void setFteRequiredRowIndex(String fteRequiredRowIndex) {
		this.fteRequiredRowIndex = fteRequiredRowIndex;
	}

	
	public String getAhtRowIndex() {
		return ahtRowIndex;
	}

	public void setAhtRowIndex(String ahtRowIndex) {
		this.ahtRowIndex = ahtRowIndex;
	}

	public String getShrinkageRowIndex() {
		return shrinkageRowIndex;
	}

	public void setShrinkageRowIndex(String shrinkageRowIndex) {
		this.shrinkageRowIndex = shrinkageRowIndex;
	}

	public String getTrainingHcRowIndex() {
		return trainingHcRowIndex;
	}

	public void setTrainingHcRowIndex(String trainingHcRowIndex) {
		this.trainingHcRowIndex = trainingHcRowIndex;
	}

	public String getProductionHcRowIndex() {
		return productionHcRowIndex;
	}

	public void setProductionHcRowIndex(String productionHcRowIndex) {
		this.productionHcRowIndex = productionHcRowIndex;
	}

	public String getOverallHcRowIndex() {
		return overallHcRowIndex;
	}

	public void setOverallHcRowIndex(String overallHcRowIndex) {
		this.overallHcRowIndex = overallHcRowIndex;
	}

	public String getAttritionRowIndex() {
		return attritionRowIndex;
	}

	public void setAttritionRowIndex(String attritionRowIndex) {
		this.attritionRowIndex = attritionRowIndex;
	}

	public String getAttritionPerRowIndex() {
		return attritionPerRowIndex;
	}

	public void setAttritionPerRowIndex(String attritionPerRowIndex) {
		this.attritionPerRowIndex = attritionPerRowIndex;
	}

	public String getDeltaRowIndex() {
		return deltaRowIndex;
	}

	public void setDeltaRowIndex(String deltaRowIndex) {
		this.deltaRowIndex = deltaRowIndex;
	}

	@Override
	public String toString() {
		return "InputColumn [sheetName=" + sheetName + ", dateIndex=" + dateIndex + ", headcountColumnRowIndex="
				+ headcountColumnRowIndex + ", callForecastRowIndex=" + callForecastRowIndex + ", fteRequiredRowIndex="
				+ fteRequiredRowIndex + ", ahtRowIndex=" + ahtRowIndex + ", shrinkageRowIndex=" + shrinkageRowIndex
				+ ", trainingHcRowIndex=" + trainingHcRowIndex + ", productionHcRowIndex=" + productionHcRowIndex
				+ ", overallHcRowIndex=" + overallHcRowIndex + ", attritionRowIndex=" + attritionRowIndex
				+ ", attritionPerRowIndex=" + attritionPerRowIndex + ", deltaRowIndex=" + deltaRowIndex + "]";
	}

	

}
