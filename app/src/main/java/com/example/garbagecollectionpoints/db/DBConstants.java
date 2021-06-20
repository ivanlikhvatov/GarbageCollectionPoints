package com.example.garbagecollectionpoints.db;

public enum DBConstants {
    DATABASE_NAME("GarbageCollectionPoints1"),

    TABLE_POINTS("points"),
    KEY_ID("_id"),
    KEY_NAME("name"),
    KEY_LATITUDE("latitude"),
    KEY_LONGITUDE("longitude"),
    KEY_TYPE("type"),
    KEY_DESCRIPTION("description"),
    KEY_DATE("date"),
    USER_TABLE("user"),
    USER_KEY_ID("user_id"),
    USER_KEY_NAME("user_name"),
    USER_KEY_EMAIL("user_email"),
    USER_KEY_PASS("user_password");

    DBConstants(String name) {
        this.name = name;
    }

    private final String name;

    public String getName(){
        return name;
    }
}
