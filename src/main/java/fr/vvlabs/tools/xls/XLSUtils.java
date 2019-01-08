package fr.vvlabs.tools.xls;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class XLSUtils.
 */
@Slf4j
public class XLSUtils {

    private static DataFormatter objDefaultFormat = new DataFormatter();

    // ===========================================================
    // Constructors
    // ===========================================================

    // Hide public constructor
    private XLSUtils() {
    }

    // ===========================================================
    // Methods
    // ===========================================================

    /**
     * Convert an XLS file to CSV file.
     *
     * @param inputXlsFile
     *            the input xls file
     * @param outputCsvFile
     *            the output csv file
     * @return true, if successful
     */

    public static boolean convertToCsv(final Path inputXlsFile, final Path outputCsvFile) {

        boolean result = false;

        try (
                    // Get Input Stream
                    InputStream xlsInputStream = Files.newInputStream(inputXlsFile);
                    // Read file as workbook
                    Workbook xlsWorkbook = WorkbookFactory.create(xlsInputStream);

                    // CSV writer
                    CSVWriter csvWriter = new CSVWriter(Files.newBufferedWriter(outputCsvFile, StandardCharsets.UTF_8,
                                StandardOpenOption.CREATE));) {

            FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
            // Read the first worksheet
            Sheet xlsSheet = xlsWorkbook.getSheetAt(0);

            Iterator<Row> rowIterator = xlsSheet.iterator();
            // Loop through rows.
            while (rowIterator.hasNext()) {
                List<String> dataLine = extractLine(rowIterator.next(), evaluator);
                // write line
                csvWriter.writeNext(dataLine.stream().toArray(String[]::new));
            }
            result = true;
        } catch (IOException e) {
            XLSUtils.log.error("I/O error : {}", e.getMessage(), e);
        }
        return result;
    }

    /**
     * Extract xls line. Apache POI ignore empty case : solution While until we obtain the just
     * number of column
     *
     * @param row
     *            the row
     * @param evaluator
     * @return the list
     */
    private static List<String> extractLine(final Row row, final FormulaEvaluator evaluator) {

        List<String> dataLine = new ArrayList<>();

        Iterator<Cell> cellIterator = row.cellIterator();
        int numCell = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            while (cell.getColumnIndex() != numCell++) {
                dataLine.add("");
            }

            dataLine.add(getCellValue(evaluator, cell));
        }
        return dataLine;
    }

    /**
     * Gets the cell value.
     *
     * @param evaluator
     *            the evaluator
     * @param cell
     *            the cell
     * @return the cell value
     */
    private static String getCellValue(final FormulaEvaluator evaluator, final Cell cell) {
        // This will evaluate the cell, And any type of cell will return string value
        evaluator.evaluate(cell);
        // get original string
        return XLSUtils.objDefaultFormat.formatCellValue(cell, evaluator);
    }

    /**
     * Gets the cell value.
     *
     * @param inputXlsFile
     *            the input xls file
     * @param lineNumber
     *            the line number
     * @param cellNumber
     *            the cell number
     * @return the cell value
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static String getCellValue(final Path inputXlsFile, final int lineNumber, final int cellNumber)
                throws IOException {

        XLSUtils.log.debug("getCellValue: path={}, lineNumber={}, cellNumber={}", inputXlsFile.getFileName(),
                    lineNumber, cellNumber);

        String cellValue = null;
        try (
                    // Get Input Stream
                    InputStream xlsInputStream = Files.newInputStream(inputXlsFile);
                    // Read file as workbook
                    Workbook xlsWorkbook = WorkbookFactory.create(xlsInputStream);) {

            FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
            // Read the first worksheet
            Sheet xlsSheet = xlsWorkbook.getSheetAt(0);

            Row row = xlsSheet.getRow(lineNumber);
            Cell cell = row.getCell(cellNumber);
            cellValue = getCellValue(evaluator, cell);
        }
        return cellValue;
    }

    /**
     * Gets the headers line.
     *
     * @param inputXlsFile
     *            the input xls file
     * @param expectedHeaders
     *            the expected
     * @return the headers xls line
     */
    public static List<String> getHeadersLine(final Path inputXlsFile) throws IOException {
        List<String> result = null;
        try (
                    // Get Input Stream
                    InputStream xlsInputStream = Files.newInputStream(inputXlsFile);
                    // Read file as workbook
                    Workbook xlsWorkbook = WorkbookFactory.create(xlsInputStream);) {

            FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
            // Read the first worksheet
            Sheet xlsSheet = xlsWorkbook.getSheetAt(0);

            // Default header row : first line
            Row headerRow = xlsSheet.getRow(0);

            // Loop through rows to check for the real header line
            Iterator<Row> rowIterator = xlsSheet.iterator();
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                // header row should be the one with the max number of cells
                if (currentRow.getLastCellNum() > headerRow.getLastCellNum()) {
                    XLSUtils.log.debug("row {} contains more cells than row {}", currentRow.getRowNum(),
                                headerRow.getRowNum());
                    headerRow = currentRow;
                }
            }
            XLSUtils.log.debug("header row is number {}", headerRow.getRowNum());
            result = extractLine(headerRow, evaluator);
        }
        XLSUtils.log.debug("headers = {}", result);
        return result;
    }

    /**
     * Contains line.
     *
     * @param inputXlsFile
     *            the input xls file
     * @param expectedValues
     *            the expected headers
     * @return true, if successful
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static boolean containsLine(final Path inputXlsFile, final Collection<String> expectedValues)
                throws IOException {
        if (expectedValues == null) {
            throw new IllegalArgumentException("headers are null");
        }

        boolean result = false;
        try (
                    // Get Input Stream
                    InputStream xlsInputStream = Files.newInputStream(inputXlsFile);
                    // Read file as workbook
                    Workbook xlsWorkbook = WorkbookFactory.create(xlsInputStream);) {

            FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
            // Read the first worksheet
            Sheet xlsSheet = xlsWorkbook.getSheetAt(0);

            // Loop through rows to check for the matching line
            Iterator<Row> rowIterator = xlsSheet.iterator();
            while (rowIterator.hasNext()) {
                Row currentRow = rowIterator.next();
                // row should be the one containing all expected names
                List<String> lineValues = extractLine(currentRow, evaluator);
                if (lineValues.containsAll(expectedValues)) {
                    XLSUtils.log.debug("row {} contains all expected values", currentRow.getRowNum());
                    result = true;
                    break;
                }
            }
        }
        XLSUtils.log.debug("result = {}", result);
        return result;
    }

    /**
     * Extract line data.
     *
     * @param inputXlsFile
     *            the input xls file
     * @param lineNumber
     *            the line number
     * @return the list
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static List<String> extractLine(final Path inputXlsFile, final int lineNumber) throws IOException {
        XLSUtils.log.debug("extractLine: path={}, lineNumber={}", inputXlsFile.getFileName(), lineNumber);

        List<String> dataLine = null;
        try (
                    // Get Input Stream
                    InputStream xlsInputStream = Files.newInputStream(inputXlsFile);
                    // Read file as workbook
                    Workbook xlsWorkbook = WorkbookFactory.create(xlsInputStream);) {

            FormulaEvaluator evaluator = xlsWorkbook.getCreationHelper().createFormulaEvaluator();
            // Read the first worksheet
            Sheet xlsSheet = xlsWorkbook.getSheetAt(0);

            Row row = xlsSheet.getRow(lineNumber);
            dataLine = extractLine(row, evaluator);
        }
        return dataLine;
    }
}
