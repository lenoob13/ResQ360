package model.graph.adaptation;

import model.persistence.DPS;
import model.persistence.Rescuer;

import java.util.*;

/**
 * Classe utilitaire qui adapte la liste des sauveteurs et les besoins du DPS
 * en une matrice binaire étendue pour les algorithmes d'affectation.
 */
public class BesoinsAdapter {

    /**
     * Contient les infos associées à chaque colonne de la matrice.
     *
     * @param index Index de répétition
     */
    public record DPSColonneInfo(int idDPS, int idCompetence, int index) { }

    /**
    * Contient le résultat d'adaptation : matrice binaire et correspondances.
    */
    public record ResultatAdaptation(int[][] matrice, List<DPSColonneInfo> correspondanceColonnes) { }

    /**
     * Construit une matrice binaire étendue où chaque colonne correspond
     * à une unité de besoin pour une compétence, et chaque ligne à un sauveteur.
     *
     * @param sauveteurs la liste des sauveteurs disponibles
     * @param dpsList        les DPS contenant les besoins en compétences
     * @return un objet contenant la matrice binaire et les infos par colonne
     */
    public static ResultatAdaptation buildExtendedMatrix(List<Rescuer> sauveteurs, List<DPS> dpsList) {
        List<DPSColonneInfo> correspondances = new ArrayList<>();

        // Étendre les besoins de chaque DPS en colonnes
        for (DPS dps : dpsList) {
            int idDPS = dps.getId();
            for (Map.Entry<Integer, Integer> entry : dps.getBesoins().entrySet()) {
                int idCompetence = entry.getKey();
                int quantite = entry.getValue();

                for (int i = 0; i < quantite; i++) {
                    correspondances.add(new DPSColonneInfo(idDPS, idCompetence, i));
                }
            }
        }

        int nbSauveteurs = sauveteurs.size();
        int nbColonnes = correspondances.size();
        int[][] matrice = new int[nbSauveteurs][nbColonnes];

        // Remplissage de la matrice
        for (int i = 0; i < nbSauveteurs; i++) {
            Rescuer sauveteur = sauveteurs.get(i);
            List<String> skills = sauveteur.getSkills();

            for (int j = 0; j < nbColonnes; j++) {
                DPSColonneInfo infoColonne = correspondances.get(j);
                int idCompetence = infoColonne.idCompetence;

                // Vérifie que le sauveteur a la compétence
                if (skills.contains(String.valueOf(idCompetence))){
                    matrice[i][j] = 1;
                }
            }
        }

        return new ResultatAdaptation(matrice, correspondances);
    }

}
