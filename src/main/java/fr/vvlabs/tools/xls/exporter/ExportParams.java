package fr.vvlabs.tools.xls.exporter;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class ExportParams {
	
	// ===========================================================
	// Fields
	// ===========================================================
    
    private String originalRequest;
    private String title;
    private String subtitle;
    private String fileName;
    private String fileFormat;
    private String locale;
    private List<ExportColumn> columns = new ArrayList<>();
	
	// ===========================================================
	// Getter & Setter
	// ===========================================================
	
    public List<ExportColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ExportColumn> columns) {
        this.columns = columns;
    }

    public String getFileFormat() {
        return fileFormat;
    }

    public void setFileFormat(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        // remove all accented characters
        fileName = Normalizer.normalize(fileName, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        this.fileName = fileName;
    }

    public String getOriginalRequest() {
        return originalRequest;
    }

    public void setOriginalRequest(String originalRequest) {
        this.originalRequest = originalRequest;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }
}
