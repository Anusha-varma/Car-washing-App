package org.example.model;

/**
 * Enum representing different service packages available
 */
public enum PackageType {
    FULL_CLEAN("Full Clean", "Complete interior and exterior cleaning including waxing", 150.0),
    OUTER_CLEAN("Outer Clean", "Exterior cleaning and washing only", 75.0),
    OUTER_CLEAN_WATERING("Outer Clean + Watering", "Exterior cleaning with interior watering", 100.0);

    private final String name;
    private final String description;
    private final double basePrice;

    PackageType(String name, String description, double basePrice) {
        this.name = name;
        this.description = description;
        this.basePrice = basePrice;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getBasePrice() {
        return basePrice;
    }
}
