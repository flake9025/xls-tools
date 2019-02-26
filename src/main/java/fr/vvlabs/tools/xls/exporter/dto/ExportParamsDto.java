package fr.vvlabs.tools.xls.exporter.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@EqualsAndHashCode
@ToString
public class ExportParamsDto {
	
    private String fileName;
    private String filePath;
    //TODO : replace with mapstruct instance and find source/target with introspection ?
    private Map<String, String> columnsMappings = new HashMap<>();
}
