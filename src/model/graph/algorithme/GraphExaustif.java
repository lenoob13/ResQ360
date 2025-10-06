package model.graph.algorithme;

/**
 * Classe permettant d'effectuer une assignation exhaustive entre des sauveteurs et des diplômes.
 * Elle explore toutes les combinaisons possibles d'appariement pour trouver une solution optimale,
 * c'est-à-dire celle qui maximise le nombre d'affectations valides (sans conflits).
 */
public class GraphExaustif {
    private final int[][] matrice;
    private final int lenX; // nombre de sauveteurs
    private final int lenY; // nombre de diplômes

    private int maxMatch; // nombre maximal d'appariements trouvés
    private int[][] meilleureAffectation; // matrice correspondant à la meilleure solution trouvée

    /**
     * Constructeur de la classe.
     * @param matrice Matrice binaire de compatibilité (sauveteurs x diplômes),
     *                où 1 signifie que le sauveteur peut recevoir ce diplôme.
     */
    public GraphExaustif(int[][] matrice) {
        this.lenX = matrice.length;
        if (lenX>0) {
            this.lenY = matrice[0].length;
        }else{
            this.lenY=0;
        }
        this.matrice = new int[lenX][lenY];
        for (int i = 0; i < lenX; i++) {
            this.matrice[i] = matrice[i].clone();
        }
        this.maxMatch = 0;
        this.meilleureAffectation = new int[lenX][lenY];
    }

    /**
     * Lance la recherche exhaustive et retourne la meilleure assignation trouvée.
     * @return Une matrice d'affectation optimale : res[i][j] == 1 signifie que le sauveteur i reçoit le diplôme j.
     */
    public int[][] assign() {
        if (lenX == 0 || lenY == 0) {
            return new int[0][0];
        }
        int[] affectation = new int[lenY]; // affectation[j] = index du sauveteur affecté au diplôme j
        for (int i = 0; i < lenY; i++) affectation[i] = -1;
        backtrack(0, affectation, 0);
        return meilleureAffectation;
    }

    /**
     * Méthode récursive de backtracking qui explore toutes les assignations possibles.
     * @param sauveteur Index du sauveteur en cours de traitement.
     * @param affectation Tableau des diplômes actuellement attribués.
     * @param count Nombre d'affectations valides dans la solution courante.
     */
    private void backtrack(int sauveteur, int[] affectation, int count) {
        if (sauveteur == lenX) {
            if (count > maxMatch) {
                maxMatch = count;
                construireResultat(affectation);
            }
            return;
        }

        // Tenter d'affecter un diplôme compatible
        for (int diplome = 0; diplome < lenY; diplome++) {
            if (matrice[sauveteur][diplome] == 1 && affectation[diplome] == -1) {
                affectation[diplome] = sauveteur;
                backtrack(sauveteur + 1, affectation, count + 1);
                affectation[diplome] = -1; // revenir en arrière
            }
        }

        // Essayer aussi sans affectation pour ce sauveteur
        backtrack(sauveteur + 1, affectation, count);
    }

    /**
     * Construit la matrice d'affectation correspondant à la meilleure solution actuelle.
     * @param affectation Tableau où affectation[j] = i signifie que le sauveteur i a reçu le diplôme j.
     */
    private void construireResultat(int[] affectation) {
        int[][] res = new int[lenX][lenY];
        for (int j = 0; j < lenY; j++) {
            if (affectation[j] != -1) {
                res[affectation[j]][j] = 1;
            }
        }
        this.meilleureAffectation = res;
    }
}

