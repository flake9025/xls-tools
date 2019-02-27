package fr.vvlabs.tools.xls.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.opencsv.CSVWriter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
public class CSVExportHelper extends BaseExportHelper {

    // ===========================================================
    // Constants
    // ===========================================================

    private static final char RECORD_DELIMITER = ';';
    private static final String RECORD_SEPARATOR = "\r\n";

    // ===========================================================
    // Fields
    // ===========================================================

    private char recordDelimiter = CSVExportHelper.RECORD_DELIMITER;
    private String recordSeparator = CSVExportHelper.RECORD_SEPARATOR;

    // ===========================================================
    // Constructors
    // ===========================================================

    public CSVExportHelper(final String recordDelimiter,
                final String recordSeparator) {
        if (StringUtils.isNotBlank(recordDelimiter)) {
            this.recordDelimiter = recordDelimiter.charAt(0);
        }
        if (StringUtils.isNotBlank(recordSeparator)) {
            this.recordSeparator = recordSeparator;
        }
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public File export(final Map<String, String> columnsMappings, final List<?> data) throws IOException {

        // create temporaryFile
        String fileName = LocalDate.now().toString() + "_" + UUID.randomUUID().toString();
        File exportFile = File.createTempFile(fileName, ".csv");
        exportFile.deleteOnExit();

        // Create the CSVFormat object with custom record delimiter and separator
        try (BufferedWriter writer = Files.newBufferedWriter(exportFile.toPath());
                    CSVWriter csvWriter = new CSVWriter(writer, this.recordDelimiter, CSVWriter.NO_QUOTE_CHARACTER,
                                CSVWriter.DEFAULT_ESCAPE_CHARACTER, this.recordSeparator);) {

            // Create header with fields names
            List<String> columnNames = getHeadersLine(columnsMappings);
            csvWriter.writeNext(columnNames.stream().toArray(String[]::new));

            // Create data lines
            List<List<String>> dataLines = getDataLines(columnsMappings, data);
            for (List<String> dataLine : dataLines) {
                csvWriter.writeNext(dataLine.stream().toArray(String[]::new));
            }

        } catch (IOException e) {
            CSVExportHelper.log.error("export() KO : " + e.getMessage(), e);
            throw e;
        }

        return exportFile;
    }
}