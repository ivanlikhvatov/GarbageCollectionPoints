package com.example.garbagecollectionpoints.enums;

public enum GarbageTypes {
    PAPER_BIN("paper", "целлюлоза"),
    PACKING_MATERIAL_BIN("metal", "металлические/химические/пластиковые отходы"),
    COMPOST_BIN("bio materials", "биологические отходы"),
    GLASS_BIN("glass", "стекло");

    GarbageTypes(String value, String text) {
        this.value = value;
        this.text = text;
    }

    private String value;
    private String text;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
