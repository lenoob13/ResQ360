package core;

import model.dao.DAORescuer;
import model.persistence.Rescuer;
import model.services.SceneStackService;
import util.Logger;
import views.AdminSessionView;
import views.GlobalView;
import views.RescuerSessionView;
/**
 * Manages the current user session, including login and logout.
 * Handles scene transitions depending on the user's admin status.
 * 
 * @author resQ360
 */
public class Session {
    private static Rescuer currentUser;
    /**
     * Attempts to log in with the given credentials.
     * Loads the correct scene depending on user type (admin or rescuer).
     *
     * @param identifier the rescuer's identifier
     * @param password   the associated password
     */
    public static void login(String identifier, String password) {
        DAORescuer dao = new DAORescuer();

        Rescuer rescuer = dao.getByIdentifier(identifier);

        if (rescuer == null) {
            Logger.warn("Identifiant '" + identifier + "' non trouvé.");
            return;
        }

        // Comparaison du mot de passe
        if (rescuer.getPassword().equals(password)) {
            Logger.info("Connexion au compte " + Logger.Color.GREEN + identifier + Logger.Color.WHITE + " réussie.");
            currentUser = rescuer;

            if (!rescuer.getAdmin()) {
                SceneStackService.replaceWith(RescuerSessionView.ACCUEIL);
            } else {
                SceneStackService.replaceWith(AdminSessionView.HOME);
            }
        } else {
            Logger.warn("Mot de passe incorrect pour l'identifiant '" + identifier + "'.");
        }
    }

    /**
     * Returns the currently logged-in user.
     *
     * @return the current Rescuer
     */
    public static Rescuer getCurrentUser() {
        return currentUser;
    }

    /**
     * Logs out the current user and redirects to the login screen.
     */
    public static void logout() {
        currentUser = null;
        SceneStackService.replaceWith(GlobalView.LOGIN);
    }
}
