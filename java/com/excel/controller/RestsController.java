package com.excel.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.excel.model.ExcelToJson;
import com.excel.service.RestsService;
import com.excel.service.TestService;

import ch.qos.logback.classic.Logger;

/**
 * @author 710149
 *
 */
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class RestsController {

	  private static final Logger logger = (Logger) LoggerFactory.getLogger(RestsController.class);
	  
	@Autowired
	private RestsService restsService;
	
	@Autowired
	private TestService testService;

	@RequestMapping(value = "/read", method = RequestMethod.GET)
	public ExcelToJson[] readExcel() {

		ExcelToJson[] excelJson = null;
		try {
			excelJson = testService.readExcel();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelJson;
	}
	
	@RequestMapping(value = "/readWithPartition", method = RequestMethod.GET)
	public Map<String, Object> readWithPartition() {

		ExcelToJson[] excelJson = null;
		Map<String, Object> detailsJson = null;
		try {
			excelJson = testService.readExcel();
			
			
			detailsJson = testService.readWithPartition(excelJson);
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return detailsJson;
	}
	
	@RequestMapping(value = "/readExcelValues", method = RequestMethod.GET)
	public ArrayList<HashMap<String, Object>> readExcelValues() {

		logger.info("Read excel values");
		ArrayList<HashMap<String, Object>> excelJson = null;
		try {
			excelJson = restsService.readWithSheet();
			
			//restsService.MoveExcelFile();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelJson;
	}
	
	@RequestMapping(value = "/readFromDB", method = RequestMethod.GET)
	public ArrayList<HashMap<String, String>> readFromDB() {

		logger.info("Read excel values");
		ArrayList<HashMap<String, String>> excelJson = null;
		try {
		//	excelJson = restsService.readWithSheet();
			
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		return excelJson;
	}
	
	
	@RequestMapping(value= "/deviceIngestion", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String deviceIngestion (@RequestParam Map<String,String> allParams) {

		System.out.println( "Parameters are " + allParams.entrySet());
	    return "string";
	}
	

}
