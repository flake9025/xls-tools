package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class XLSExportHelper extends BaseExportHelper {

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public File export(Map<String, String> columnsMappings, List<?> data) throws IOException {

		// create temporaryFile
		String fileName = LocalDate.now().toString() + "_" + UUID.randomUUID().toString();
		File exportFile = File.createTempFile(fileName, ".xls");
		exportFile.deleteOnExit();

		try (HSSFWorkbook workbook = new HSSFWorkbook(); //
				FileOutputStream out = new FileOutputStream(exportFile)) {

			// initialize workbook and sheet
			HSSFSheet sheet = workbook.createSheet("Inventory");

			// Create header with fields names
			printHeaderLine(sheet, columnsMappings);

			// Create data lines
			List<List<String>> dataLines = getDataLines(columnsMappings, data);
			int lineNumber = 1;
			for (List<String> dataLine : dataLines) {
				printDataLine(sheet, lineNumber, dataLine);
				lineNumber++;
			}

			workbook.write(out);
		} catch (IOException e) {
			log.error("export() KO : " + e.getMessage(), e);
			throw e;
		}
		
		return exportFile;
	}

	/**
	 * Prints the header line.
	 *
	 * @param sheet        the sheet
	 * @param exportParams the export params
	 */
	private void printHeaderLine(HSSFSheet sheet, Map<String, String> columnsMappings) {
		HSSFRow headerLine = sheet.createRow(0);
		List<String> columnNames = getHeadersLine(columnsMappings);
		for (int i = 0; i < columnNames.size(); i++) {
			addCell(headerLine, i, columnNames.get(i));
		}
	}

	private void printDataLine(HSSFSheet sheet, int lineNumber, List<String> fieldsValues) {
		HSSFRow row = sheet.createRow(lineNumber);
		for (int column = 0; column < fieldsValues.size(); column++) {
			addCell(row, column, fieldsValues.get(column));
		}
	}

	/**
	 * Adds the cell.
	 *
	 * @param row   the row
	 * @param col   the column
	 * @param value the value
	 * @return the HSSF cell
	 */
	private static HSSFCell addCell(HSSFRow row, int col, String value) {
		HSSFCell c = row.createCell(col);
		c.setCellValue(value);
		return c;
	}
}
