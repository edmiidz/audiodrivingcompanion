package com.altever.audiodrivingcompanion.database.table;

public class TableLocationLog {

    public static final String TABLE_NAME       = "location_log";

    public static final String ID               = "id";
    public static final String LATITUDE         = "latitude";
    public static final String LONGITUDE        = "longitude";
    public static final String SPEED            = "speed";
    public static final String MODESTATUS       = "modestatus";
    public static final String LOGDATETIME      = "logdatetime";

    private int id;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String modestatus;
    private String logdatetime;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + LATITUDE + " REAL,"
                    + LONGITUDE + " REAL,"
                    + SPEED + " REAL,"
                    + MODESTATUS + " TEXT,"
                    + LOGDATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public TableLocationLog(int id, Double latitude, Double longitude, Double speed, String modestatus, String logdatetime) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.modestatus = modestatus;
        this.logdatetime = logdatetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getSpeed() {
        return speed;
    }

    public void setSpeed(Double speed) {
        this.speed = speed;
    }

    public String getModestatus() {
        return modestatus;
    }

    public void setModestatus(String modestatus) {
        this.modestatus = modestatus;
    }

    public String getLogdatetime() {
        return logdatetime;
    }

    public void setLogdatetime(String logdatetime) {
        this.logdatetime = logdatetime;
    }
}
