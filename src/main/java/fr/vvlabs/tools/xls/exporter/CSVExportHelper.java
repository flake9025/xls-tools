package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;

import fr.vvlabs.tools.xls.exporter.dto.ExportParamsDto;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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

	private char recordDelimiter = RECORD_DELIMITER;
	private String recordSeparator = RECORD_SEPARATOR;

	// ===========================================================
	// Constructors
	// ===========================================================

	public CSVExportHelper(String recordDelimiter, String recordSeparator) {
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
	public File export(ExportParamsDto exportParams, List<?> data) throws IOException {

		// create temporaryFile
		File exportFile = File.createTempFile(exportParams.getFileName(), ".csv");
		exportFile.deleteOnExit();

		// Create the CSVFormat object with custom record delimiter and separator
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withDelimiter(recordDelimiter).withRecordSeparator(recordSeparator);

		try (Writer fileWriter = new OutputStreamWriter(new FileOutputStream(exportFile), StandardCharsets.UTF_8); //
				CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);) {

			// Create header with fields names
			List<String> columnNames = getHeadersLine(exportParams);
			csvFilePrinter.printRecord(columnNames);

			// Create data lines
			List<List<String>> dataLines = getDataLines(exportParams, data);
			for (List<String> dataLine : dataLines) {
				csvFilePrinter.printRecord(dataLine);
			}

		} catch (IOException e) {
			log.error("export() KO : " + e.getMessage(), e);
			throw e;
		}
		
		return exportFile;
	}
}
