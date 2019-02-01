package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Data;

@Data
public class CSVExportHelper extends BaseExportHelper {
    
    // ===========================================================
    // Constants
    // ===========================================================

    protected static final Logger logger = LoggerFactory.getLogger(CSVExportHelper.class);
    
    private static final String RECORD_SEPARATOR = "\r\n";

    // ===========================================================
    // Fields
    // ===========================================================
    
    private char recordDelimiter = ';';
    
    // ===========================================================
    // Constructors
    // ===========================================================
    
    public CSVExportHelper(String recordDelimiter) {
        if(StringUtils.isNotBlank(recordDelimiter)) {
            this.recordDelimiter = recordDelimiter.charAt(0);
        }
    }
    
    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    
    @Override
    public void export(ExportParams exportParams, List<?> data) {
        Writer fileWriter = null;
        CSVPrinter csvFilePrinter = null;

        // Create the CSVFormat object with ";" as a record delimiter
        CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter(recordDelimiter).withRecordSeparator(RECORD_SEPARATOR);

        try {
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

            // initialize FileWriter object - IO 8859-1 for french excel users !
            fileWriter = new OutputStreamWriter(new FileOutputStream(exportFile), StandardCharsets.ISO_8859_1); //new FileWriter(exportFile);

            // initialize CSVPrinter object
            csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

            // Create header with fields names
            List<String> columnNames = exportParams.getColumns().stream().map(column -> column.getHeader()).collect(Collectors.toList());
            csvFilePrinter.printRecord(columnNames);

            // Create data lines
            List<List<String>> dataLines = getDataLines(exportParams, data);
            for(List<String> dataLine : dataLines) {
                csvFilePrinter.printRecord(dataLine);
            }
            
        } catch (IOException e) {
            logger.error("export() KO : " + e.getMessage(), e);
        } finally {
            try {
                fileWriter.flush();
                fileWriter.close();
                csvFilePrinter.close();
            } catch (IOException e) {
                logger.error("export() KO : " + e.getMessage(), e);
            }
        }
    }
}
