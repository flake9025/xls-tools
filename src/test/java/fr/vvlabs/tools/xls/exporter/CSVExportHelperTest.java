package fr.vvlabs.tools.xls.exporter;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import fr.vvlabs.tools.xls.exporter.mocks.FamilleMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionStatutMock;
import fr.vvlabs.tools.xls.exporter.mocks.ProtectionTypeMock;
import fr.vvlabs.tools.xls.importer.CSVUtils;

public class CSVExportHelperTest {

    private CSVExportHelper csvExportHelper;

    private List<FamilleMock> dataFamilles;
    private FamilleMock famille1;
    private List<ProtectionMock> dataProtections;
    private ProtectionMock protection1;

    @Before
    public void setup() {
        this.csvExportHelper = new CSVExportHelper();

        this.dataFamilles = new ArrayList<>();
        this.famille1 = new FamilleMock();
        this.famille1.setUuid("1234565789654321456789abiqsdosfdj");
        this.famille1.setFamilleID(42);
        this.famille1.setAbstractSummary("ceci est une magnifique famille de test pour les exports");
        this.famille1.setFamilleCleClient("123456");
        this.famille1.setFamilyCreationDate(LocalDate.of(2018, 5, 1));
        this.dataFamilles.add(this.famille1);

        this.dataProtections = new ArrayList<>();
        this.protection1 = new ProtectionMock();
        this.protection1.setUuid("98765413213sdfsdfsdjfj");
        this.protection1.setFamille(this.famille1);
        this.protection1.setDepotNumber("FR123456P01");
        this.protection1.setDepotDate(LocalDate.of(2018, 8, 1));
        this.protection1.setProtectionCleClient("123456 FR P01");
        this.protection1.setType(ProtectionTypeMock.PAT);
        this.protection1.setSousType("PR");
        this.protection1.setStatut(ProtectionStatutMock.PENDING);

        this.dataProtections.add(this.protection1);
    }

    @Test
    public void testExportFamilles() {

        try {
            LinkedHashMap<String, String> famillesMapping = new LinkedHashMap<>();
            famillesMapping.put("UUID", "uuid");
            famillesMapping.put("ID", "familleID");
            famillesMapping.put("Abstract", "abstractSummary");
            famillesMapping.put("Reference", "familleCleClient");
            famillesMapping.put("Creation Date", "familyCreationDate");

            File exportFile = this.csvExportHelper.export(famillesMapping, this.dataFamilles);
            assertTrue(exportFile.exists());

            String[] headers = CSVUtils.getHeadersCSVLine(exportFile.toPath(),
                        this.csvExportHelper.getRecordDelimiter());
            List<String> headersList = Arrays.asList(headers);
            assertTrue(headersList.contains("Reference"));

            String[] data = CSVUtils.extractLine(exportFile.toPath(), 1, this.csvExportHelper.getRecordDelimiter());
            List<String> dataList = Arrays.asList(data);
            assertTrue(dataList.contains(this.famille1.getFamilleCleClient()));
        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void testExportProtections() {

        try {
            LinkedHashMap<String, String> protectionsMapping = new LinkedHashMap<>();
            protectionsMapping.put("UUID", "uuid");
            protectionsMapping.put("Depot Number", "depotNumber");
            protectionsMapping.put("Depot Date", "depotDate");
            protectionsMapping.put("Protection Cle Client", "protectionCleClient");
            protectionsMapping.put("Type", "type");
            protectionsMapping.put("Sous-Type", "sousType");
            protectionsMapping.put("Statut", "statut");

            File exportFile = this.csvExportHelper.export(protectionsMapping, this.dataProtections);
            assertTrue(exportFile.exists());

            String[] headers = CSVUtils.getHeadersCSVLine(exportFile.toPath(),
                        this.csvExportHelper.getRecordDelimiter());
            List<String> headersList = Arrays.asList(headers);
            assertTrue(headersList.contains("Protection Cle Client"));

            String[] data = CSVUtils.extractLine(exportFile.toPath(), 1, this.csvExportHelper.getRecordDelimiter());
            List<String> dataList = Arrays.asList(data);
            assertTrue(dataList.contains(this.protection1.getProtectionCleClient()));

        } catch (IOException e) {
            fail(e.getMessage());
        }

    }

}
