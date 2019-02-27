package fr.vvlabs.tools.xls.importer;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CSVUtils {

    private CSVUtils() {
    }

    /**
     * Gets the headers csv line.
     *
     * @param inputCSVFile
     *            the input CSV file
     * @param separator
     *            the separator
     * @return the headers csv line
     */
    public static String[] getHeadersCSVLine(final Path inputCSVFile, final char separator) {
        String[] result = null;

        CSVParser parser = new CSVParserBuilder().withSeparator(separator).withIgnoreQuotations(true).build();
        try (Reader reader = Files.newBufferedReader(inputCSVFile); //
                    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser)
                                .build();) {
            result = csvReader.readNext();
        } catch (IOException e) {
            CSVUtils.log.error("getHeadersCSVLine() KO : {}", e.getMessage(), e);
        }

        return result;
    }

    /**
     * Extract line.
     *
     * @param inputCSVFile
     *            the input CSV file
     * @param lineNumber
     *            the line number
     * @param separator
     *            the separator
     * @return the string[]
     */
    public static String[] extractLine(final Path inputCSVFile, final int lineNumber, final char separator) {
        String[] result = null;

        CSVParser parser = new CSVParserBuilder().withSeparator(separator).withIgnoreQuotations(true).build();
        try (Reader reader = Files.newBufferedReader(inputCSVFile); //
                    CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(0).withCSVParser(parser)
                                .build();) {
            for (int i = 0; i <= lineNumber; i++) {
                if (i == lineNumber) {
                    result = csvReader.readNext();
                } else {
                    csvReader.readNext();
                }
            }
        } catch (IOException e) {
            CSVUtils.log.error("extractLine() KO : {}", e.getMessage(), e);
        }

        return result;
    }
}