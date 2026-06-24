package com.dragn0007.dragnloextras.util;

public enum WoodType {
    ACACIA("acacia"),
    BIRCH("birch"),
    CHERRY("cherry"),
    CRIMSON("crimson"),
    DARK_OAK("dark_oak"),
    JUNGLE("jungle"),
    MANGROVE("mangrove"),
    OAK("oak"),
    SPRUCE("spruce"),
    WARPED("warped");

    private final String name;
    public String getName() {
        return this.name;
    }

    private WoodType(String name) {
        this.name = name;
    }
}
