package fr.vvlabs.tools.xls;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.opencsv.CSVReader;

import lombok.extern.slf4j.Slf4j;

/**
 * The Class CSVUtils.
 */
@Slf4j
public class CSVUtils {
	
    // ===========================================================
    // Constructors
    // ===========================================================

    // Hide public constructor
    private CSVUtils() {
    }

    // ===========================================================
    // Methods
    // ===========================================================
    
    /**
     * Gets the headers line.
     *
     * @param inputCSVFile
     *            the input CSV file
     * @return the headers line
     */
    public static String[] getHeadersLine(final Path inputCSVFile) {
        String[] result = null;

        try (Reader reader = Files.newBufferedReader(inputCSVFile); //
                    CSVReader csvReader = new CSVReader(reader);) {
            // Reading Records One by One in a String array
            result = csvReader.readNext();
        } catch (IOException e) {
            CSVUtils.log.error("I/O error : {}", e.getMessage(), e);
        }

        return result;
    }
}