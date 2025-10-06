package model.persistence;

/**
 * Represents a physical site or location, with a name, code, and coordinates.
 * 
 * <p>Used to identify where an event takes place.</p>
 * 
 * @author ResQ360
 */
public class Site {

    private String code;
    private String name;
    private float longitude;
    private float latitude;

    /**
     * Creates a new site with the given information.
     *
     * @param code the site code
     * @param name the site name
     * @param longitude the longitude (between -180 and 180)
     * @param latitude the latitude (between -90 and 90)
     */
    public Site(String code, String name, float longitude, float latitude) {
        setCode(code);
        setName(name);
        setLongitude(longitude);
        setLatitude(latitude);
    }

    /**
     * Gets the site code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the site code (must not be empty).
     *
     * @param code the new code
     */
    public void setCode(String code) {
        if (code != null && !code.isEmpty()) {
            this.code = code;
        } else {
            throw new IllegalArgumentException("Code cannot be empty");
        }
    }

    /**
     * Gets the site name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

     /**
     * Sets the site name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the latitude.
     *
     * @return the latitude
     */
    public float getLatitude() {
        return this.latitude;
    }

    /**
     * Sets the latitude (must be between -90 and 90).
     *
     * @param latitude the new latitude
     */
    public void setLatitude(float latitude) {
        if (latitude >= -90.0f && latitude <= 90.0f) {
            this.latitude = latitude;
        } else {
            throw new IllegalArgumentException("Invalid latitude: " + latitude);
        }
    }

    /**
     * Gets the longitude.
     *
     * @return the longitude
     */
    public float getLongitude() {
        return this.longitude;
    }

    /**
     * Sets the longitude (must be between -180 and 180).
     *
     * @param longitude the new longitude
     */
    public void setLongitude(float longitude) {
        if (longitude >= -180.0f && longitude <= 180.0f) {
            this.longitude = longitude;
        } else {
            throw new IllegalArgumentException("Invalid longitude: " + longitude);
        }
    }

    /**
     * Returns a string showing the site code, name, and coordinates.
     *
     * @return string format of the site
     */
    public String toString() {
        return "["+this.code+"]"+this.name+"("+this.longitude+","+this.latitude+")";
    }
}
