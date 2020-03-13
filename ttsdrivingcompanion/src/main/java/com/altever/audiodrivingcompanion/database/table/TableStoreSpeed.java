package com.altever.audiodrivingcompanion.database.table;

public class TableStoreSpeed {

    public static final String TABLE_NAME       = "location_log";

    public static final String ID               = "id";
    public static final String LATITUDE         = "latitude";
    public static final String LONGITUDE        = "longitude";
    public static final String SPEED            = "speed";
    public static final String MODESTATUS       = "modestatus";
    public static final String LOGDATETIME      = "logdatetime";
    public static final String UPDATEDLAST      = "updatedLast";
    public static final String SPEED_TIME       = "time_stamp";

    private int id;
    private Double latitude;
    private Double longitude;
    private Double speed;
    private String modestatus;
    private String logdatetime;
    private String updatedLast;
    private String speedTime;

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + LATITUDE + " REAL,"
                    + LONGITUDE + " REAL,"
                    + SPEED + " REAL,"
                    + MODESTATUS + " TEXT,"
                    + LOGDATETIME + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + UPDATEDLAST + " DATETIME DEFAULT CURRENT_TIMESTAMP,"
                    + SPEED_TIME + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public TableStoreSpeed(int id, Double latitude, Double longitude, Double speed, String modestatus, String logdatetime, String updatedLast, String speedTime) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.speed = speed;
        this.modestatus = modestatus;
        this.logdatetime = logdatetime;
        this.updatedLast = updatedLast;
        this.speedTime = speedTime;
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

    public String getUpdatedLast() {
        return updatedLast;
    }

    public void setUpdatedLast(String updatedLast) {
        this.updatedLast = updatedLast;
    }

    public String getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(String speedTime) {
        this.speedTime = speedTime;
    }
}
