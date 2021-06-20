package com.example.garbagecollectionpoints.enums;

public enum GarbageType {
    PAPER_BIN("paper", "целлюлоза"),
    PACKING_MATERIAL_BIN("metal", "металлические/химические/пластиковые отходы"),
    COMPOST_BIN("bio materials", "биологические отходы"),
    GLASS_BIN("glass", "стекло");

    GarbageType(String value, String text) {
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

    public static GarbageType getEnumByText(String text){
        for (GarbageType type : values()) {
            if (type.getText().equals(text)) {
                return type;
            }
        }

        throw new IllegalArgumentException("No enum found with url: [" + text + "]");
    }
}
