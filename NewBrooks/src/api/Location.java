package api;
public class Location implements GeoLocation{
    private final double x;
    private final double y;
    private final double z;
    public Location(double x, double y, double z){
        this.x =x;
        this.y =y;
        this.z =z;
    }

    /**
     * x axis of the location
     * @return
     */
    @Override
    public double x() {
        return x;
    }

    /**
     * y axis of the location
     * @return
     */
    @Override
    public double y() {
        return y;
    }

    /**
     * z axis of the location
     * @return
     */
    @Override
    public double z() {
        return z;
    }

    /**
     * The distance between two geoLocations without consideration to height.
     * @return
     */
    @Override
    public double distance(GeoLocation g) {
        double val_x = (this.x - g.x()) * (this.x - g.x());
        double val_y = (this.y - g.y()) * (this.y - g.y());
        return Math.sqrt(val_x + val_y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y +", "+z+ ")";
    }
}
