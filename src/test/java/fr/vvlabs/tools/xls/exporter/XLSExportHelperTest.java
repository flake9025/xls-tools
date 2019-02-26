package fr.vvlabs.tools.xls.exporter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import fr.vvlabs.tools.xls.exporter.dto.ExportParamsDto;
import fr.vvlabs.tools.xls.exporter.mocks.FamilleMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionStatutMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionTypeMock;
import fr.vvlabs.tools.xls.importer.FileUtils;
import fr.vvlabs.tools.xls.importer.XLSUtils;

public class XLSExportHelperTest {

	private XLSExportHelper xlsExportHelper;
	private ExportParamsDto exportParamsDto;
	private List<FamilleMock> dataFamilles;
	private FamilleMock famille1;
	private List<ProtectionMock> dataProtections;
	private ProtectionMock protection1;
	
	@Before
	public void setup() {
		xlsExportHelper = new XLSExportHelper();
		
		exportParamsDto = new ExportParamsDto();
		exportParamsDto.setFileName("testExport");
		exportParamsDto.setFilePath("/test/java/test_export.xlsx");
		
		dataFamilles = new ArrayList<>();
		famille1 = new FamilleMock();
		famille1.setUuid("1234565789654321456789abiqsdosfdj");
		famille1.setFamilleID(42);
		famille1.setAbstractSummary("ceci est une magnifique famille de test pour les exports");
		famille1.setFamilleCleClient("123456");
		famille1.setFamilyCreationDate(LocalDate.of(2018,5,1));
		dataFamilles.add(famille1);
		
		dataProtections = new ArrayList<>();
		protection1 = new ProtectionMock();
		protection1.setUuid("98765413213sdfsdfsdjfj");
		protection1.setFamille(famille1);
		protection1.setDepotNumber("FR123456P01");
		protection1.setDepotDate(LocalDate.of(2018,8,1));
		protection1.setProtectionCleClient("123456 FR P01");
		protection1.setType(ProtectionTypeMock.PAT);
		protection1.setSousType("PR");
		protection1.setStatut(ProtectionStatutMock.PENDING);
		
		dataProtections.add(protection1);
	}
	
	@Test
	public void testExportFamilles() {
		
		try {
			Map<String, String> famillesMapping = new LinkedHashMap<>();
			famillesMapping.put("UUID", "uuid");
			famillesMapping.put("ID", "familleID");
			famillesMapping.put("Abstract", "abstractSummary");
			famillesMapping.put("Reference", "familleCleClient");
			famillesMapping.put("Creation Date", "familyCreationDate");
			exportParamsDto.setColumnsMappings(famillesMapping);
			
			File exportFile = xlsExportHelper.export(exportParamsDto, dataFamilles);
			assertTrue(exportFile.exists());
			
			List<String> headers = XLSUtils.extractLine(exportFile.toPath(), 0);
			assertTrue(headers.contains("Reference"));
			List<String> data = XLSUtils.extractLine(exportFile.toPath(), 1);
			assertTrue(data.contains(famille1.getFamilleCleClient()));
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}
	
	@Test
	public void testExportProtections() {
		
		try {
			Map<String, String> protectionsMapping = new LinkedHashMap<>();
			protectionsMapping.put("UUID", "uuid");
			protectionsMapping.put("Depot Number", "depotNumber");
			protectionsMapping.put("Depot Date", "depotDate");
			protectionsMapping.put("Protection Cle Client", "protectionCleClient");
			protectionsMapping.put("Type", "type");
			protectionsMapping.put("Sous-Type", "sousType");
			protectionsMapping.put("Statut", "statut");
			exportParamsDto.setColumnsMappings(protectionsMapping);
			
			File exportFile = xlsExportHelper.export(exportParamsDto, dataProtections);
			assertTrue(exportFile.exists());
			
			List<String> headers = XLSUtils.extractLine(exportFile.toPath(), 0);
			assertTrue(headers.contains("Protection Cle Client"));
			List<String> data = XLSUtils.extractLine(exportFile.toPath(), 1);
			assertTrue(data.contains(protection1.getProtectionCleClient()));
			
		} catch (IOException e) {
			fail(e.getMessage());
		}
		
	}

}
