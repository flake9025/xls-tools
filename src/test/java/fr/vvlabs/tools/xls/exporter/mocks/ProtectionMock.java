package fr.vvlabs.tools.xls.exporter.mocks;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class ProtectionMock {
	
	private String uuid;
    private Integer apidInt;
    private String citedPatents;
    private String citingPatents;
    private String clientUuid;
    private Integer clientID;
    private LocalDate deliveryDate;
    private String deliveryNumber;
    private LocalDate depotDate;
    private String depotNumber;
    private String dsDesCountriesJSON;
    private String epExtensionJSON;
    private LocalDate expirationDate;
    private Integer familleID;
    private String fan;
    private String fid;
    private String isLinkedTo;
    private String notes;
    private LocalDate officialDepotDate;
    private String officialDepotNumber;
    private String pays;
    private float pourcentageFrais;
    private LocalDate priorityDate;
    private String protectionCleClient;
    private String protectionComplexCoowners;
    private Integer protectionID;
    private LocalDate publicationDate;
    private String publicationNumber;
    private String qwsApplicants;
    private String qwsInventors;
    private String qwsKind;
    private String qwsPriorityJSON;
    private String sousType;
    private ProtectionStatutMock statut;
    private ProtectionTypeMock type;
    private String xap;
    private ApidMock apid;
    private FamilleMock famille;
    private String familleUuid;
}
