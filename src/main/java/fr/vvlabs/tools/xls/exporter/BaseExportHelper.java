package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@Slf4j
public abstract class BaseExportHelper {

	// ===========================================================
	// Fields
	// ===========================================================


	private final SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");

	// ===========================================================
	// Methods
	// ===========================================================

	/**
	 * Export.
	 *
	 * @param exportParams the export params
	 * @param data         the data
	 */
	public abstract File export(Map<String, String> columnsMappings, List<?> data) throws IOException;

	/**
	 * Gets the headers line.
	 *
	 * @param exportParams the export params
	 * @return the headers line
	 */
	protected List<String> getHeadersLine(Map<String, String> columnsMappings) {
		return new ArrayList<>(columnsMappings.keySet());
	}

	/**
	 * Gets the data lines.
	 *
	 * @param exportParams    the export params
	 * @param objectsToExport the objects to export
	 * @return the data lines
	 */
	protected List<List<Object>> getDataLines(Map<String, String> columnsMappings, List<?> objectsToExport) {
		List<List<Object>> dataLines = new ArrayList<>();
		if (columnsMappings != null && objectsToExport != null) {
			log.trace("getDataLines() start...");
			for (Object objectToExport : objectsToExport) {
				dataLines.add(getDataLine(columnsMappings, objectToExport));
			}
		}
		return dataLines;
	}

	/**
	 * Gets the data line.
	 *
	 * @param columnsMappings the columns mappings
	 * @param objectToExport the object to export
	 * @return the data line
	 */
	protected List<Object> getDataLine(Map<String, String> columnsMappings, Object objectToExport) {
		List<Object> dataLine = new ArrayList<>();
		
		if (objectToExport != null && columnsMappings != null) {
			// iterate over column names
			for (String column : columnsMappings.values()) {
				String[] path = column.split("\\.");
				log.trace("getDataLine() currentColumn={} , path={}", column, path);
	
				// retrieve matching field recursively
				Object currentObject = objectToExport;
				for (String field : path) {
					if (currentObject != null) {
						log.trace("getDataLine() currentObject={}, field={}", currentObject.getClass().getSimpleName(),
								field);
						currentObject = getFieldValue(currentObject, field);
					} else {
						log.trace("getDataLine() currentObject is null, stop the loop");
						break;
					}
				}
				getDataForObject(dataLine, currentObject);
			}
		}
		log.trace("getDataLine() result={}", Arrays.toString(dataLine.toArray()));
		return dataLine;
	}

	/**
	 * Gets the data for object.
	 *
	 * @param dataLine the data line
	 * @param currentObject the current object
	 * @return the data for object
	 */
	private void getDataForObject(List<Object> dataLine, Object currentObject) {
		if (currentObject != null) {
			log.trace("getDataLine() currentObject={}, value={}", currentObject.getClass().getSimpleName(),
					currentObject.toString());
			if (currentObject instanceof Date) {
				// format dates
				dataLine.add(dateFormatter.format(currentObject));
            } else if (currentObject instanceof byte[]) {
                dataLine.add(currentObject);
			} else {
				// just use toString
				dataLine.add(currentObject.toString());
			}
		} else {
			log.trace("getDataLine() currentObject null !");
			dataLine.add("");
		}
	}

	/**
	 * Gets the field value.
	 *
	 * @param objectToExport the object to export
	 * @param field          the field
	 * @return the field value
	 */
	private Object getFieldValue(Object objectToExport, String fieldName) {
		if (objectToExport == null || fieldName == null)
			return null;

		log.trace("getFieldValue(objectToExport={}, fieldName={})", objectToExport.getClass().getSimpleName(),
				fieldName);

		Object value = null;
		try {
            Field field = objectToExport.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            value = field.get(objectToExport);
		} catch (Exception e) {
			log.error("getFieldValue() KO : " + e.getMessage(), e);
		}

		log.trace("getFieldValue() result class={}, value={}", value != null ? value.getClass().getSimpleName() : null,
				value);
		return value;
	}
}
