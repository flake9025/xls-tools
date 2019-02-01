
package fr.vvlabs.tools.xls.exporter;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class ExportColumn {
	// ===========================================================
	// Fields
	// ===========================================================

    private String header = "";
    private String column = "";
    private int width = 0;
}
