package fr.vvlabs.tools.xls.exporter.mocks;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class FamilleMock {
	
	private String uuid;
    private String abstractSummary;
    private String clientUuid;
    private Integer clientID;
    private String epoFamilyNumber;
    private String extensionForecast;
    private String familleCleClient;
    private Integer familleID;
    private String familyAnnuityManager;
    private String familyAttorney;
    private String familyAutoClassification;
    private String familyChargeAffaires;
    private String familyClassification;
    private Integer familyClosing;
    private LocalDate familyClosingDate;
    private String familyComplexCoowners;
    private String familyComplexInventors;
    private String familyCoowners;
    private String familyCPC;
    private LocalDate familyCreationDate;
    private String familyFanJSON;
    private String familyFidJSON;
    private String familyInventors;
    private String familyLawFirm;
    private String familyLawFirmRef;
    private String familyPriseEnChargePI;
    private String familyTags;
    private String familyTags10;
    private String familyTags11;
    private String familyTags12;
    private String familyTags2;
    private String familyTags3;
    private String familyTags4;
    private String familyTags5;
    private String familyTags6;
    private String familyTags7;
    private String familyTags8;
    private String familyTags9;
    private Integer familyTRL;
    private LocalDate familyTRLdate;
    private LocalDate familyValidationDate;
    private String firstPageImage;
    private String gestionnaireInstit;
    private String gestionnairePI;
    private String gestionnaireValo;
    private String laboratoire;
    private Integer licensedIn;
    private Integer licensedOut;
    private String notes;
    private String oldFamilleCleClient;
    private String orbitName;
    private String orbitType;
    private Integer patentStrength;
    private String pruningCategory;
    private Integer pruningFwdCites;
    private BigDecimal pruningGenerality;
    private BigDecimal pruningOriginality;
    private String pruningPredator;
    private Integer pruningSelfBack;
    private Integer pruningSelfFwd;
    private LocalDate refPatentFilingDate;
    private String refPatentNumber;
    private String refPatentPublished;
    private String smartIntel;
    private String titre;
    private String type;
}
