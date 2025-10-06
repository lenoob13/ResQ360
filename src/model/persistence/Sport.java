package model.persistence;
/**
 * Represents a sport with a name and a unique code.
 * 
 * <p>Used to identify a sport in the system.</p>
 * 
 * @author ResQ360
 */
public class Sport {
    private String name;
    private String code;

    /**
     * Creates a new sport.
     *
     * @param name the name of the sport
     * @param code the unique code of the sport
     */
    public Sport(String name, String code) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            System.out.println("The name cannot be empty");
        }
        if (code != null && !code.isEmpty()) {
            this.code = code;
        } else {
            System.out.println("The code cannot be empty");
        }
    }

    /**
     * Gets the sport name.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the sport name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        if (name != null && !name.isEmpty()) {
            this.name = name;
        } else {
            System.out.println("The name cannot be empty");
        }
    }

    /**
     * Gets the sport code.
     *
     * @return the code
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Sets the sport code.
     *
     * @param code the new code
     */
    public void setCode(String code) {
        if (code != null && !code.isEmpty()) {
            this.code = code;
        } else {
            System.out.println("The code cannot be empty");
        }
    }

    /**
     * Returns a string with the sport's name and code.
     *
     * @return string representation of the sport
     */
    public String toString(){
        return "sport : " + this.name + ", code : " + this.code;
    }
}
