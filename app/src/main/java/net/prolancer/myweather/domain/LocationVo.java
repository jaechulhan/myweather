package net.prolancer.myweather.domain;

public class LocationVo {
    private String title;
    private String location_type;
    private String latt_long;
    private int woeid;
    private int distance;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public String getLatt_long() {
        return latt_long;
    }

    public void setLatt_long(String latt_long) {
        this.latt_long = latt_long;
    }

    public int getWoeid() {
        return woeid;
    }

    public void setWoeid(int woeid) {
        this.woeid = woeid;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "LocationVo{" +
                "title='" + title + '\'' +
                ", location_type='" + location_type + '\'' +
                ", latt_long='" + latt_long + '\'' +
                ", woeid=" + woeid +
                ", distance=" + distance +
                '}';
    }
}
