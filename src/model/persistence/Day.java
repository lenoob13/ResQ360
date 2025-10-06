package model.persistence;

/**
 * Represents a specific day in the calendar, including its start and end times.
 * 
 * <p>This class ensures that dates are valid and formatted, with constraints such as:
 * - the year must be ≥ 2025
 * - the day must be valid for the given month (with leap year handling)
 * - time is given in [hour, minute] format</p>
 * 
 * @author ResQ360
 */
public class Day {

    private int year;
    private int month;
    private int day;
    private int id;
    private int[] startTime;
    private int[] endTime;

    /**
     * Creates a valid Day object with a date and time interval.
     *
     * @param id the internal ID of the day
     * @param day the day (1–31)
     * @param month the month (1–12)
     * @param year the year (≥ 2025)
     * @param startTime the start time [HH, MM]
     * @param endTime the end time [HH, MM]
     * @throws IllegalArgumentException if any value is invalid
     */
    public Day(int id, int day, int month, int year, int[] startTime, int[] endTime) {
        if (month < 1 || month > 12) throw new IllegalArgumentException("Invalid month.");
        if (year < 2025) throw new IllegalArgumentException("The year cannot be earlier than 2025.");

        this.id = id;
        this.month = month;
        this.year = year;

        if (!isValidDay(day)) throw new IllegalArgumentException("Invalid day for the given month and year.");
        this.day = day;

        if (!isValidTimeFormat(startTime)) throw new IllegalArgumentException("Invalid start time format.");
        if (!isValidTimeFormat(endTime)) throw new IllegalArgumentException("Invalid end time format.");

        this.startTime = startTime.clone();
        this.endTime = endTime.clone();
    }

    // Check if the given day is valid for the current month and year
    private boolean isValidDay(int day) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return day > 0 && day <= 31;
            case 4: case 6: case 9: case 11:
                return day > 0 && day <= 30;
            case 2:
                if (isLeapYear(year)) {
                    return day > 0 && day <= 29;
                } else {
                    return day > 0 && day <= 28;
                }
            default:
                return false;
        }
    }

    /**
     * Returns the ID of the day.
     */
    public int getId(){
        return this.id;
    }
    
    /**
     * Sets the ID of the day.
     */
    public void setId(int id){
        this.id=id;
    }
    
    // Check if the given year is a leap year
    private boolean isLeapYear(int year) {
        return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
    }
    
    /**
     * Returns the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year (must be ≥ 2025).
     */
    public void setYear(int year) {
        if (year < 2025) {
            System.out.println("The year cannot be earlier than 2025.");
        } else {
            this.year = year;
        }
    }

    /**
     * Returns the month.
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month (1–12).
     */
    public void setMonth(int month) {
        if (month < 1 || month > 12) {
            System.out.println("Invalid month.");
        } else {
            this.month = month;
        }
    }

    /**
     * Returns the day of the month.
     */
    public int getDay() {
        return day;
    }

    /**
     * Sets the day (must match the number of days in the given month/year).
     */
    public void setDay(int day) {
        if (isValidDay(day)) {
            this.day = day;
        } else {
            System.out.println("Invalid day for the given month and year.");
        }
    }

    /**
     * Returns the start time as [hour, minute].
     */
    public int[] getStartTime() {
        return startTime != null ? startTime.clone() : null;
    }

    /**
     * Sets the start time (must be valid 24h format).
     */
    public void setStartTime(int[] startTime) {
        if (isValidTimeFormat(startTime)) {
            this.startTime = startTime.clone();
        } else {
            throw new IllegalArgumentException("Invalid start time format");
        }
    }

    
    public int[] getEndTime() {
        return endTime != null ? endTime.clone() : null;
    }

    /**
     * Sets the end time (must be valid 24h format).
     */
    public void setEndTime(int[] endTime) {
        if (isValidTimeFormat(endTime)) {
            this.endTime = endTime.clone();
        } else {
            throw new IllegalArgumentException("Invalid end time format");
        }
    }
    // Helper method to validate time format (HH:MM)
    private boolean isValidTimeFormat(int[] time) {
        return time != null && time.length == 2 && time[0] >= 0 && time[0] < 24 && time[1] >= 0 && time[1] < 60;
    }

    /**
     * Returns the date and time as a formatted string.
     * Format: DD/MM/YYYY [HH:MM - HH:MM]
     */
    @Override
    public String toString() {
        return String.format("%02d/%02d/%04d [%02d:%02d - %02d:%02d]",
                day, month, year,
                startTime != null ? startTime[0] : 0,
                startTime != null ? startTime[1] : 0,
                endTime != null ? endTime[0] : 0,
                endTime != null ? endTime[1] : 0);
    }
}
