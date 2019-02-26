package fr.vvlabs.tools.xls.exporter.mocks;

public enum ProtectionTypeMock {
    PAT("protection"), DES("design"), UTI("utility"), COP("cop"), TDK("trademark");

    ProtectionTypeMock(final String type) {
        this.type = type;
    }

    private final String type;

    @Override
    public String toString() {
        return this.type;
    }

    // Input : trademark, desing, .. returns ProtectionType.TDK, ...
    public static ProtectionTypeMock fromString(final String type) {
        for (ProtectionTypeMock protType : ProtectionTypeMock.values()) {
            if (protType.toString().equals(type)) {
                return protType;
            }
        }
        return null;
    }

    public static String getAssetTypeNameFromString(final String entityAssetType) {
        if (entityAssetType != null) {
            ProtectionTypeMock protectionType = ProtectionTypeMock.fromString(entityAssetType);
            if (protectionType != null) {
                return protectionType.name();
            }
        }
        return null;
    }
}
