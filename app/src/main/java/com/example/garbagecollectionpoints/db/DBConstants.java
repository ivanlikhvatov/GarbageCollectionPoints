package com.example.garbagecollectionpoints.db;

public enum DBConstants {
    DATABASE_NAME("GCPoints"),

    TABLE_POINTS("points"),
    KEY_ID("_id"),
    KEY_NAME("name"),
    KEY_LATITUDE("latitude"),
    KEY_LONGITUDE("longitude"),
    KEY_TYPE("type"),
    KEY_DESCRIPTION("description"),
    KEY_DATE("date");



    DBConstants(String name) {
        this.name = name;
    }

    private final String name;

    public String getName(){
        return name;
    }
}
