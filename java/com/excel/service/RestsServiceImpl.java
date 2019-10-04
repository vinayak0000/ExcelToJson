package com.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.excel.model.InputColumn;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.qos.logback.classic.Logger;


@Component
public class RestsServiceImpl implements RestsService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(RestsServiceImpl.class);


	@Value("${excel.readJson.input.path}")
	private String jsonPath;

	@Value("${excel.readFile.input.path}")
	private String filePath2;
	
	@Value("${excel.Source}")
	private String sourcePath;
	
	@Value("${excel.Destination}")
	private String destinationPath;

	private Path temp;
	

	public ArrayList<HashMap<String, Object>> readWithSheet() {

		int cellCount = 0;

		int rowCount = 0;

		HashMap<String, Object> params = new HashMap<String, Object>();
		ArrayList<HashMap<String, Object>> paramsArray = new ArrayList<HashMap<String, Object>>();

		try (FileInputStream file = new FileInputStream(new File(filePath2));
				XSSFWorkbook workbook = new XSSFWorkbook(file);
				FileInputStream input = new FileInputStream(new File(jsonPath))) {

			Class<?> classObj = Class.forName("com.excel.model.InputColumn");

			InputColumn clm = (InputColumn) classObj.getDeclaredConstructor().newInstance();

			Method[] methods = clm.getClass().getDeclaredMethods();

			InputColumn[] inputCol = null;

			ObjectMapper mapperInput = new ObjectMapper();
			mapperInput.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			inputCol = mapperInput.readValue(input, InputColumn[].class);

			DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			for (InputColumn inp : inputCol) {

				 logger.info(inp.getSheetName());
				params.put("Sheet Name", inp.getSheetName());
				int stIdx = workbook.getSheetIndex(inp.getSheetName());
				XSSFSheet sheet1 = workbook.getSheetAt(stIdx);

				Iterator<Row> rowIterator = sheet1.iterator();

				Date dateIndex = formatter.parse(inp.getDateIndex());

				int dateIdx = 0;

				while (rowIterator.hasNext()) {

					Row row = rowIterator.next();

					Iterator<Cell> cellIterator = row.cellIterator();

					while (cellIterator.hasNext()) {

						Cell cell = cellIterator.next();

						
						if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {

							if (DateUtil.isCellDateFormatted(cell)) {

								if (dateIndex.equals(cell.getDateCellValue())) {

									dateIdx = cellCount + 1;

									params.put("Date", cell.getDateCellValue());
								}
							} else {
								// System.out.println("Missing Date format"+cell.getNumericCellValue());
							}
						} else if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

							for (Method method : methods) {

								if (method.getName().startsWith("get")) {
									String str = "";
									try {
										str = ((String) inp.getClass().getMethod(method.getName()).invoke(inp)).trim();
									} catch (NullPointerException e) {
										str = "";
									}
									//System.out.println(str + " "+cell.getStringCellValue());
									if (str.equals(cell.getStringCellValue().trim())) {

										System.out.println(str+" in");
										if (dateIdx == 0 && !method.getName().equals("getSheetName")) {
											logger.error("Given Date is not found");
											// throw new IllegalArgumentException ("Given Date is not found");
										} else {
											try {

												if (sheet1.getRow(rowCount).getCell(dateIdx).getCellStyle()
														.getDataFormatString().contains("%")) {
													params.put(str, sheet1.getRow(rowCount).getCell(dateIdx)
															.getNumericCellValue());
													System.out.println(sheet1.getRow(rowCount).getCell(dateIdx)
															.getNumericCellValue());
												} else {
													System.out.println(sheet1.getRow(rowCount+1).getCell(dateIdx).getNumericCellValue());

													params.put(str, Math.round(sheet1.getRow(rowCount+1).getCell(dateIdx)
															.getNumericCellValue()));
												}

											} catch (NullPointerException e) {
												if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

													if (!method.getName().equals("getSheetName")) {
														
													
														//System.out.println(sheet1.getRow(rowCount).getCell(dateIdx).getStringCellValue());
														params.put(str, sheet1.getRow(rowCount).getCell(dateIdx)
																.getStringCellValue());
													}
												} else {
													logger.error(e.getMessage(), e);
												}
											}
											catch (IllegalStateException e) {
												if (cell.getCellType() == Cell.CELL_TYPE_STRING) {

													if (!method.getName().equals("getSheetName")) {
														
													
														System.out.println(sheet1.getRow(rowCount+1).getCell(dateIdx).getStringCellValue());
														params.put(str, sheet1.getRow(rowCount+1).getCell(dateIdx)
																.getStringCellValue());
													}
												} else {
													logger.error(e.getMessage(), e);
												}
											}
											catch(Exception e)
											{
												logger.error(e.getMessage(), e);
											}
										}
									}
									
								}
							}

						}
						cellCount++;
					}

					cellCount = 0;
					rowCount++;
				}

				rowCount = 0;
				paramsArray.add(params);
				params = new HashMap<String, Object>();

			}
			logger.info(paramsArray.toString());
			/*
			 * Gson gson = new Gson(); String jsonArray = gson.toJson(paramsArray);
			 * 
			 * ObjectMapper mapper = new ObjectMapper();
			 * mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			 * mapper.setDateFormat(formatter2); //excelJson = mapper.readValue(one, new
			 * TypeReference<ExcelToJson[]>() { //});
			 * 
			 * excelJson = mapper.readValue(jsonArray, new TypeReference<ExcelToJson[]>() {
			 * });
			 */

		} catch (FileNotFoundException | InstantiationException | IllegalAccessException | InvocationTargetException
				| NoSuchMethodException | ClassNotFoundException e) {

			logger.error(e.getMessage(), e);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return paramsArray;
	}




	public void MoveExcelFile() {
		
		try {
			temp = Files.move(Paths.get(sourcePath),Paths.get(destinationPath));
		} catch (Exception e) {
			
			logger.error(e.getMessage(), e);
		} 
		if(temp != null) 
		{ 
			logger.info("File moved successfully"); 
		} 
		else
		{ 
			logger.info("Failed to move the file"); 
		} 
		
		
	}
	
	
	

}
