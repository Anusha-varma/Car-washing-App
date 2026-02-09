package org.example.model;

/**
 * Enum representing subscription frequency options
 */
public enum Frequency {
    WEEKLY("Weekly", 7),
    MONTHLY("Monthly", 30),
    YEARLY("Yearly", 365);

    private final String displayName;
    private final int dayInterval;

    Frequency(String displayName, int dayInterval) {
        this.displayName = displayName;
        this.dayInterval = dayInterval;
    }

    public String getDisplayName() {
        return displayName;
    }

    public int getDayInterval() {
        return dayInterval;
    }
}
