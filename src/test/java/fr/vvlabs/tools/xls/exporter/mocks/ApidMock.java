package fr.vvlabs.tools.xls.exporter.mocks;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Data
@ToString
public class ApidMock {
	
	private String clientUuid;
    private Integer clientID;
    private LocalDate deliveryDate;
    private String deliveryNumber;
    private LocalDate depotDate;
    private String depotNumber;
    private LocalDate expirationDate;
    private String isLinkedTo;
    private LocalDate officialDepotDate;
    private String officialDepotNumber;
    private String pays;
    private LocalDate priorityDate;
    private LocalDate publicationDate;
    private String publicationNumber;
    private String qwsAb;
    private String qwsAp;
    private String qwsAct;
    private String qwsApd;
    private Integer qwsApid;
    private String qwsCase;
    private String qwsEed;
    private String qwsEpap;
    private Integer qwsFan;
    private String qwsFd;
    private Integer qwsFid;
    private String qwsIkd;
    private String qwsIn;
    private String qwsKind;
    private String qwsNo;
    private String qwsOab;
    private String qwsOin;
    private String qwsOpa;
    private String qwsOpd;
    private String qwsOti;
    private String qwsPa;
    private String qwsPap;
    private String qwsPapd;
    private String qwsPd;
    private String qwsPda;
    private String qwsPdg;
    private String qwsPn;
    private String qwsPr;
    private String qwsPri;
    private String qwsStat;
    private String qwsTi;
    private String qwsXap;
    private String qwsXpn;
    private String sousType;
    private String statut;
    private String type;
}
