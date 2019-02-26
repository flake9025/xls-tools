package fr.vvlabs.tools.xls.exporter;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import fr.vvlabs.tools.xls.exporter.dto.ExportParamsDto;
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
	public abstract File export(ExportParamsDto exportParams, List<?> data) throws IOException;

	/**
	 * Gets the headers line.
	 *
	 * @param exportParams the export params
	 * @return the headers line
	 */
	protected List<String> getHeadersLine(ExportParamsDto exportParams) {
		return new ArrayList<>(exportParams.getColumnsMappings().keySet());
	}

	/**
	 * Gets the data lines.
	 *
	 * @param exportParams    the export params
	 * @param objectsToExport the objects to export
	 * @return the data lines
	 */
	protected List<List<String>> getDataLines(ExportParamsDto exportParams, List<?> objectsToExport) {
		List<List<String>> dataLines = new ArrayList<>();
		if (exportParams != null && objectsToExport != null) {
			log.trace("getDataLines() start...");
			for (Object objectToExport : objectsToExport) {
				dataLines.add(getDataLine(exportParams, objectToExport));
			}
		}
		return dataLines;
	}

	/**
	 * Gets the data line.
	 *
	 * @param exportParams   the export params
	 * @param objectToExport the object to export
	 * @return the data line
	 */
	protected List<String> getDataLine(ExportParamsDto exportParams, Object objectToExport) {
		if (objectToExport == null || exportParams == null)
			return null;

		List<String> dataLine = new ArrayList<>();

		// iterate over column names
		for (String column : exportParams.getColumnsMappings().values()) {
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
			if (currentObject != null) {
				log.trace("getDataLine() currentObject={}, value={}", currentObject.getClass().getSimpleName(),
						currentObject.toString());
				if (currentObject instanceof Date) {
					// format dates
					dataLine.add(dateFormatter.format(currentObject));
				} else {
					// just use toString
					dataLine.add(currentObject.toString());
				}
			} else {
				log.trace("getDataLine() currentObject null !");
				dataLine.add("");
			}
		}
		log.trace("getDataLine() result={}", Arrays.toString(dataLine.toArray()));
		return dataLine;
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
			value = BeanUtils.getProperty(objectToExport, fieldName);
		} catch (Exception e) {
			log.error("getFieldValue() KO : " + e.getMessage(), e);
		}

		log.trace("getFieldValue() result class={}, value={}", value != null ? value.getClass().getSimpleName() : null,
				value);
		return value;
	}

	/**
	 * Gets the annotation.
	 *
	 * @param                <T> the generic type
	 * @param clazz          the clazz
	 * @param annotationType the annotation type
	 * @return the annotation
	 */
	public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationType) {
		T result = clazz.getAnnotation(annotationType);
		if (result == null) {
			Class<?> superclass = clazz.getSuperclass();
			if (superclass != null) {
				return getAnnotation(superclass, annotationType);
			} else {
				return null;
			}
		} else {
			return result;
		}
	}
}
