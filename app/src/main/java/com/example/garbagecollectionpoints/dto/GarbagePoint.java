package com.example.garbagecollectionpoints.dto;

import com.example.garbagecollectionpoints.enums.GarbageType;

public class GarbagePoint {
    private String id;
    private String name;
    private String latitude;
    private String longitude;
    private GarbageType type;
    private String description;
    private String date;

    public GarbageType getType() {
        return type;
    }

    public void setType(GarbageType type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
