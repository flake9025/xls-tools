package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XLSExportHelper extends BaseExportHelper {
    
    // ===========================================================
    // Constants
    // ===========================================================

    protected static final Logger logger = LoggerFactory.getLogger(XLSExportHelper.class);

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
   @Override
   public void export(ExportParams exportParams, List<?> data) {
       
      try ( HSSFWorkbook workbook = new HSSFWorkbook();){
          // create temporaryFile
          File exportFile = File.createTempFile(exportParams.getFileName(), exportParams.getFileFormat());
          exportFile.deleteOnExit();
          // expose temporary file information (used for file downloading...)
          this.setFileName(exportFile.getName());
          //TODO add variable
          // ExportedFilePath = temp/sub_temp/admin3456544.csv
          //   "temp" is for the servlet mapping in web.xml.
          //   "sub_temp" is for the method mapping in FileDownloadController.
          this.setFilePath(exportFile.getName());
          this.setFile(exportFile);

          // initialize workbook and sheet
         HSSFSheet sheet = workbook.createSheet("Inventory");

         // Create header with fields names
         printHeaderLine(sheet, exportParams);
         
         // Create data lines
         List<List<String>> dataLines = getDataLines(exportParams, data);
         int lineNumber = 1;
         for(List<String> dataLine : dataLines) {
             printDataLine(sheet, lineNumber, dataLine);
             lineNumber++;
         }

         FileOutputStream out = new FileOutputStream(exportFile);
         workbook.write(out);
         out.close();
      }
      catch (IOException e)
      {
         logger.error("export() KO : " + e.getMessage(), e);
      }
   }

    /**
     * Prints the header line.
     *
     * @param sheet the sheet
     * @param exportParams the export params
     */
    private void printHeaderLine(HSSFSheet sheet, ExportParams exportParams) {
        HSSFRow headerLine = sheet.createRow(0);
        List<String> columnNames = exportParams.getColumns().stream().map(ExportColumn::getHeader).collect(Collectors.toList());
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
     * @param row the row
     * @param col the column
     * @param value the value
     * @return the HSSF cell
     */
    private static HSSFCell addCell(HSSFRow row, int col, String value) {
        HSSFCell c = row.createCell(col);
        c.setCellValue(value);
        return c;
    }
}
