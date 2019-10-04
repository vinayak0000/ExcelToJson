package com.excel.service;

import java.util.ArrayList;
import java.util.HashMap;


public interface RestsService {
	
	
	public ArrayList<HashMap<String, Object>> readWithSheet();
	
	
	public void MoveExcelFile();
}
