package com.excel.service;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.excel.model.ExcelToJson;

public interface TestService {

public ExcelToJson[] readExcel();
	
	public Map<String, Object> readWithPartition(ExcelToJson[] excelJson) throws ParseException;
	
	public ArrayList<HashMap<String, Object>> readWithSheet();
	
	
	
	
}
