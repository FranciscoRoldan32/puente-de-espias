package lugar;


public class Coordinates {
    private String name;

    private double latitude;

    private double longitude;


    public Coordinates(double latitude, double longitude) {
        
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return "(" + name + ": " + latitude + ", " + longitude + ")";
    }
}

