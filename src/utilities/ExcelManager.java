package utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.*;

public class ExcelManager {

	public static ArrayList<Integer> COLUMNS_TO_SKIP = new ArrayList<Integer>();
	public static Integer TOTAL_EXECUTION_TIME = 0;
	public static Integer TOTAL_NUMBER_SCRIPTS = 0;

	public static void main(String[] args) throws Throwable {

	}

	public static String[][] readCountryData(String countryName) throws Throwable {
		String[][] executionData = null;
		XSSFWorkbook srcBook = new XSSFWorkbook(EnvironmentVariables.EXCEL_DATA_FILE_LOCATION);
		try {
			int numberOfSheets = srcBook.getNumberOfSheets();
			SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
			for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++) {
				XSSFSheet sourceSheet = srcBook.getSheetAt(sheetNumber);
				if (EnvironmentVariables.getAllowedCountries().contains(sourceSheet.getSheetName())
						&& sourceSheet.getSheetName().equals(countryName)) {
					int numberOfValidColumns = getNumberOfValidColumns(sourceSheet, 0);
					int numberOfValidRows = getNumberOfValidRows(sourceSheet);
					executionData = new String[numberOfValidRows + 1][numberOfValidColumns];
					for (int rowNumber = 0; rowNumber <= numberOfValidRows; rowNumber++) {
						int validColumnIndex = 0;
						int numberOfColumns = sourceSheet.getRow(rowNumber).getLastCellNum();
						for (int columnNumber = 0; columnNumber <= numberOfColumns; columnNumber++) {
							XSSFCell dataCell = sourceSheet.getRow(rowNumber).getCell(columnNumber);
							if (validColumnIndex == numberOfValidColumns) {
								break;
							}
							if (COLUMNS_TO_SKIP.contains(columnNumber)) {
								continue;
							}
							if (dataCell == null || dataCell.toString().equals("")) {
								executionData[rowNumber][validColumnIndex] = "";
								validColumnIndex++;
							} else if (dataCell.toString().contains("31-Dec-1899")) {
								setExecutionTime(formatTime.format(dataCell.getDateCellValue()));
								executionData[rowNumber][validColumnIndex] = formatTime
										.format(dataCell.getDateCellValue());
								validColumnIndex++;
							} else {
								executionData[rowNumber][validColumnIndex] = dataCell.toString();
								validColumnIndex++;
							}
						}
					}
					break;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			srcBook.close();
		}
		return executionData;
	}

	public static String[][] readOverallData(String sheetName) throws Throwable {
		String[][] executionData = null;
		String[][] reportData = null;
		XSSFWorkbook srcBook = new XSSFWorkbook(EnvironmentVariables.EXCEL_DATA_FILE_LOCATION);
		try {
			int numberOfSheets = srcBook.getNumberOfSheets();
			for (int sheetNumber = 0; sheetNumber < numberOfSheets; sheetNumber++) {
				XSSFSheet sourceSheet = srcBook.getSheetAt(sheetNumber);
				if (sourceSheet.getSheetName().equals(sheetName)) {
					int numberOfValidColumns = getNumberOfValidColumns(sourceSheet, 1);
					int numberOfValidRows = getNumberOfValidRows(sourceSheet);
					executionData = new String[numberOfValidRows + 1][numberOfValidColumns];
					int numberOfValidRowsTobeAdded = 0;
					for (int rowNumber = 1; rowNumber <= numberOfValidRows; rowNumber++) {
						int validColumnIndex = 0;
						int numberOfColumns = sourceSheet.getRow(rowNumber).getLastCellNum();
						if (sourceSheet.getRow(rowNumber).getCell(0) == null
								|| sourceSheet.getRow(rowNumber).getCell(0).toString().equals("")) {
							break;
						}
						numberOfValidRowsTobeAdded++;
						for (int columnNumber = 0; columnNumber <= numberOfColumns; columnNumber++) {
							XSSFCell dataCell = sourceSheet.getRow(rowNumber).getCell(columnNumber);
							if (validColumnIndex == numberOfValidColumns) {
								break;
							}
							if (COLUMNS_TO_SKIP.contains(columnNumber)) {
								continue;
							}
							if (dataCell == null || dataCell.toString().equals("")) {
								executionData[rowNumber - 1][validColumnIndex] = "";
								validColumnIndex++;
							} else if (dataCell.getCellType() == Cell.CELL_TYPE_FORMULA) {
								switch (dataCell.getCachedFormulaResultType()) {
								case Cell.CELL_TYPE_NUMERIC:
									executionData[rowNumber - 1][validColumnIndex] = String
											.valueOf(Math.round(dataCell.getNumericCellValue()));
									validColumnIndex++;
									break;
								case Cell.CELL_TYPE_STRING:
									executionData[rowNumber - 1][validColumnIndex] = dataCell.getRichStringCellValue()
											+ "";
									validColumnIndex++;
									break;
								}
							} else {
								executionData[rowNumber - 1][validColumnIndex] = dataCell.toString();
								validColumnIndex++;
							}
						}
					}
					reportData = new String[numberOfValidRowsTobeAdded][numberOfValidColumns];
					for (int rowNumber = 0; rowNumber < numberOfValidRowsTobeAdded; rowNumber++) {
						for (int columnNumber = 0; columnNumber < numberOfValidColumns; columnNumber++) {
							reportData[rowNumber][columnNumber] = executionData[rowNumber][columnNumber];
						}
					}
					break;
				}
			}
		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
			srcBook.close();
		}
		return reportData;
	}

	private static void setExecutionTime(String executionTime) {
		String[] timeSubStrings = executionTime.split(":");
		Integer numberOfHours = Integer.parseInt(timeSubStrings[0]);
		Integer numberOfMinutes = Integer.parseInt(timeSubStrings[1]);
		Integer numberOfSeconds = Integer.parseInt(timeSubStrings[2]);
		TOTAL_EXECUTION_TIME = TOTAL_EXECUTION_TIME + numberOfHours * 60 + numberOfMinutes * 60 + numberOfSeconds;
	}

	private static int getNumberOfValidColumns(XSSFSheet sourceSheet, int sourceRowIndex) {
		XSSFRow sourceRow = sourceSheet.getRow(sourceRowIndex);
		COLUMNS_TO_SKIP.clear();
		int numberOfColumns = 0;
		int columnCountIndex = 0;
		while (true) {
			if (sourceRow.getCell(columnCountIndex) == null) {
				return numberOfColumns;
			}
			if (EnvironmentVariables.getAllowedNames().contains(sourceRow.getCell(columnCountIndex).toString())) {
				numberOfColumns++;
				columnCountIndex++;
			} else if (EnvironmentVariables.getOptionalNames()
					.contains(sourceRow.getCell(columnCountIndex).toString())) {
				COLUMNS_TO_SKIP.add(columnCountIndex);
				columnCountIndex++;
			} else {
				return numberOfColumns;
			}
		}
	}

	private static int getNumberOfValidRows(XSSFSheet sourceSheet) {
		int numberOfRows = 0;
		while (true) {
			XSSFRow sourceRow = sourceSheet.getRow(numberOfRows);
			if (sourceRow == null || sourceRow.getCell(0) == null) {
				numberOfRows--;
				return numberOfRows;
			} else {
				numberOfRows++;
			}
		}
	}
}
