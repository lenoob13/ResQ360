package model.managers;
/**
 * Singleton providing access to all application managers (DPS, Rescuers, Days, Sites, Sports).
 *
 * <p>Initializes all manager instances and offers centralized access through getters.</p>
 *
 * @author ResQ360
 */
public class ManagerContext {
    private final DPSManager dpsManager;
    private final RescuerManager rescuerManager;
    private final DayManager dayManager;
    private final SiteManager siteManager;
    private final SportManager sportManager;

    private static ManagerContext instance;
     /**
     * Returns the singleton instance of {@code ManagerContext}.
     *
     * @return the global {@code ManagerContext} instance
     */
    public static ManagerContext get() {
        if (instance == null)
            instance = new ManagerContext();

        return instance;
    }

     /**
     * Initializes all managers.
     * Private to enforce singleton usage.
     */
    private ManagerContext() {
        this.rescuerManager = new RescuerManager();
        this.dpsManager = new DPSManager();
        this.sportManager = new SportManager();
        this.dayManager = new DayManager();
        this.siteManager = new SiteManager();
    }

    /**
     * @return the {@link DPSManager} instance
     */
    public DPSManager getDpsManager() {
        return dpsManager;
    }

    /**
     * @return the {@link RescuerManager} instance
     */
    public RescuerManager getRescuerManager() {
        return rescuerManager;
    }

    /**
     * @return the {@link SportManager} instance
     */
    public SportManager getSportManager() {
        return sportManager;
    }

    /**
     * @return the {@link DayManager} instance
     */
    public DayManager getDayManager() {
        return dayManager;
    }

    /**
     * @return the {@link SiteManager} instance
     */
    public SiteManager getSiteManager() {
        return siteManager;
    }
}
