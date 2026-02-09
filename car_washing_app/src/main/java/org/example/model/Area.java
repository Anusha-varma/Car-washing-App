package org.example.model;

/**
 * Enum representing different geographical areas
 * Cleaners and customers must be in the same area for service availability
 */
public enum Area {
    DOWNTOWN("Downtown"),
    UPTOWN("Uptown"),
    SUBURBS("Suburbs"),
    INDUSTRIAL("Industrial"),
    RESIDENTIAL("Residential"),
    BUSINESS_DISTRICT("Business District");

    private final String displayName;

    Area(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
