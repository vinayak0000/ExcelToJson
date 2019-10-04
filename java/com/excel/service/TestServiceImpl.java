package com.excel.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.excel.model.ExcelToJson;
import com.excel.model.InputColumn;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import ch.qos.logback.classic.Logger;

@Component
public class TestServiceImpl implements TestService {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(RestsServiceImpl.class);

	@Value("${excel.read.input.path}")
	private String filePath;

	@Value("${excel.readJson.input.path}")
	private String jsonPath;

	@Value("${excel.readFile.input.path}")
	private String filePath2;

	public ExcelToJson[] readExcel() {

		ExcelToJson[] excelJson = null;
		System.out.println(filePath);
		try (FileInputStream file = new FileInputStream(new File(filePath));
				XSSFWorkbook workbook = new XSSFWorkbook(file);) {

			// DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
			DateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
			ArrayList<String> columnNames = new ArrayList<>();
			HashMap<String, Object> params = new HashMap<String, Object>();
			ArrayList<HashMap<String, Object>> paramsArray = new ArrayList<HashMap<String, Object>>();

			XSSFSheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rowIterator = sheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();

				if (row.getRowNum() != 0) {

					for (int j = 0; j < columnNames.size(); j++) {
						if (row.getCell(j) != null) {

							if (row.getCell(j).getCellType() == Cell.CELL_TYPE_NUMERIC) {

								if (DateUtil.isCellDateFormatted(row.getCell(j))) {
									// System.out.println(row.getCell(j).getDateCellValue());
									// Date date = formatter.parse(row.getCell(j).getDateCellValue().toString());
									params.put(columnNames.get(j), row.getCell(j).getDateCellValue());
								} else {
									params.put(columnNames.get(j), row.getCell(j).getNumericCellValue());
								}
							} else if (row.getCell(j).getCellType() == Cell.CELL_TYPE_STRING) {
								params.put(columnNames.get(j), row.getCell(j).getStringCellValue());
							}

						}
					}
					paramsArray.add(params);
					params = new HashMap<String, Object>();

				} else {

					for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {

						columnNames.add(row.getCell(k).getStringCellValue());
					}
				}

			}

			// String[] frnames=paramsArray.toArray(new String[paramsArray.size()]);
			// String one = frnames.toString();

			Gson gson = new Gson();
			String jsonArray = gson.toJson(paramsArray);

			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.setDateFormat(formatter2);
			// excelJson = mapper.readValue(one, new TypeReference<ExcelToJson[]>() {
			// });

			excelJson = mapper.readValue(jsonArray, new TypeReference<ExcelToJson[]>() {
			});

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return excelJson;
	}

	public Map<String, Object> readWithPartition(ExcelToJson[] excelJson) throws ParseException {

		Map<String, Object> detailsJson = new HashMap<String, Object>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
		Date start = sdf.parse("08/07/2019 12:59:00 AM");
		Date end = sdf.parse("08/07/2019 01:14:00 AM");
		try {

			DateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

			ObjectMapper mapper = new ObjectMapper();
			mapper.setDateFormat(formatter2);
			List<HashMap<String, Object>> maps = mapper.convertValue(excelJson,
					new TypeReference<List<HashMap<String, Object>>>() {
					});
			System.out.println(maps);
			int value = 1;

			for (int i = 1; i <= 4; i++) {

				List<Map<String, Object>> filteredList = filterByDate(maps, start, end);

				System.out.println(start);
				System.out.println(end);
				System.out.println(filteredList);

				detailsJson.put(Integer.toString(value), filteredList);
				value++;
				start = DateUtils.addMinutes(start, 15);

				end = DateUtils.addMinutes(end, 15);

			}

			System.out.println(detailsJson);

		}

		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return detailsJson;

	}

	public List<Map<String, Object>> filterByDate(List<HashMap<String, Object>> maps, Date start, Date end) {

		DateFormat formatter2 = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

		List<Map<String, Object>> filteredList = maps.stream()
				.filter(map -> Objects.nonNull((String) map.get("dateOfMove"))).filter(map -> {
					try {
						return (boolean) formatter2.parse((String) map.get("dateOfMove")).after(start)
								& (boolean) formatter2.parse((String) map.get("dateOfMove")).before(end);
					} catch (ParseException e) {

						logger.error(e.getMessage(), e);
					}
					return false;
				}

				).collect(Collectors.toList());

		return filteredList;

	}

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

									if (str.equals(cell.getStringCellValue().trim())) {

										//System.out.println(inp.getClass().getMethod(method.getName()).invoke(inp));
										if (dateIdx == 0 && !method.getName().equals("getSheetName")) {
											logger.error("Given Date is not found");
											// throw new IllegalArgumentException ("Given Date is not found");
										} else {
											try {

												if (sheet1.getRow(rowCount).getCell(dateIdx).getCellStyle()
														.getDataFormatString().contains("%")) {
													params.put(str, sheet1.getRow(rowCount).getCell(dateIdx)
															.getNumericCellValue());
												} else {
													//System.out.println(Math.round(sheet1.getRow(rowCount).getCell(dateIdx).getNumericCellValue()));

													params.put(str, Math.round(sheet1.getRow(rowCount).getCell(dateIdx)
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


}
