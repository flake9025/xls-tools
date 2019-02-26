package fr.vvlabs.tools.xls.exporter.mocks;

/**
 * Used to map values in database and Java World for protections This enum is used for all type of
 * protection : Patent, non-Patent, Design and UtilityModel
 *
 *
 * @author npierot
 *
 */
public enum ProtectionStatutMock {
    PENDING("encours"), GRANTED("delivre"), WITHDRAWN("retire"), ABANDONNED("abandon"), EXPIRED("expire"), UNCERTAIN(
                "incertain"), NOTINFORCE("notinforce");

    private String statutProtection;

    private ProtectionStatutMock(final String statutProtection) {
        this.statutProtection = statutProtection;
    }

    @Override
    public String toString() {
        return this.statutProtection;
    }

    /**
     *
     * @param statutProtection
     *            a String to convert to Enum
     * @return an Enum element
     */
    public static ProtectionStatutMock fromString(final String statutProtection) {
        for (ProtectionStatutMock statut : ProtectionStatutMock.values()) {
            if (statut.statutProtection.equalsIgnoreCase(statutProtection)) {
                return statut;
            }
        }
        return null;
    }
}
