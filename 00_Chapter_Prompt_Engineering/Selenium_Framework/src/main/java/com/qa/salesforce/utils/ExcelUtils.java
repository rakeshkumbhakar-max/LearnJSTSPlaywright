package com.qa.salesforce.utils;

import com.qa.salesforce.exceptions.FrameworkException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public final class ExcelUtils {

    private ExcelUtils() {
    }

    public static Object[][] readSheet(String classpathResource, String sheetName) {
        try (InputStream inputStream = openStream(classpathResource);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new FrameworkException("Sheet '" + sheetName + "' not found in " + classpathResource);
            }
            DataFormatter formatter = new DataFormatter();
            List<Object[]> rows = new ArrayList<>();
            for (int rowIndex = sheet.getFirstRowNum() + 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) {
                    continue;
                }
                List<Object> cells = new ArrayList<>();
                boolean blankRow = true;
                for (int cellIndex = 0; cellIndex < row.getLastCellNum(); cellIndex++) {
                    Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.RETURN_BLANK_AS_NULL);
                    String value = cell == null ? "" : formatter.formatCellValue(cell);
                    if (!value.isBlank()) {
                        blankRow = false;
                    }
                    cells.add(value);
                }
                if (!blankRow) {
                    rows.add(cells.toArray());
                }
            }
            return rows.toArray(new Object[0][]);
        } catch (IOException e) {
            throw new FrameworkException("Failed to read Excel resource: " + classpathResource, e);
        }
    }

    private static InputStream openStream(String classpathResource) {
        InputStream inputStream = ExcelUtils.class.getClassLoader().getResourceAsStream(classpathResource);
        if (inputStream == null) {
            throw new FrameworkException("Excel resource not found on classpath: " + classpathResource);
        }
        return inputStream;
    }
}
