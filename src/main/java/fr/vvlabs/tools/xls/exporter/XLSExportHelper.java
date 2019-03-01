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
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;

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
			List<List<Object>> dataLines = getDataLines(columnsMappings, data);
			int lineNumber = 1;
			for (List<?> dataLine : dataLines) {
				printDataLine(sheet, lineNumber, dataLine, workbook);
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

	/**
	 * Prints the data line.
	 *
	 * @param sheet the sheet
	 * @param lineNumber the line number
	 * @param fieldsValues the fields values
	 * @param workbook the workbook
	 */
	private void printDataLine(HSSFSheet sheet, int lineNumber, List<?> fieldsValues, final HSSFWorkbook workbook) {
		HSSFRow row = sheet.createRow(lineNumber);
		for (int column = 0; column < fieldsValues.size(); column++) {
            Object value = fieldsValues.get(column);
            if (value == null) {
                value = "";
            }
            if (value instanceof byte[]) {
                addImageCell(row, column, (byte[]) value, sheet, workbook);
            } else {
                addCell(row, column, value.toString());
            }
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
	private static void addCell(HSSFRow row, int col, String value) {
		HSSFCell c = row.createCell(col);
		c.setCellValue(value);
	}
	
    /**
     * Adds the image cell.
     *
     * @param row the row
     * @param col the col
     * @param value the value
     * @param sheet the sheet
     * @param workbook the workbook
     */
    private static void addImageCell(final HSSFRow row, final int col, final byte[] value, final HSSFSheet sheet,
            final HSSFWorkbook workbook) {
    int pictureureIdx = workbook.addPicture(value, Workbook.PICTURE_TYPE_PNG);
    CreationHelper helper = workbook.getCreationHelper();
    Drawing<?> drawing = sheet.createDrawingPatriarch();

    ClientAnchor anchor = helper.createClientAnchor();

    anchor.setCol1(col);
    anchor.setRow1(row.getRowNum());
    anchor.setCol2(col + 1);
    anchor.setRow2(row.getRowNum() + 1);

    drawing.createPicture(anchor, pictureureIdx);
    // We lose aspect ratio but that's not the worst thing
    row.setHeightInPoints(150);
    sheet.setColumnWidth(col, 5000);

}
}
