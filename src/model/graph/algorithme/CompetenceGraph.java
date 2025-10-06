package model.graph.algorithme;
/**
 * Classe utilitaire fournissant une méthode pour déterminer si un graphe orienté
 * représenté par une matrice d'adjacence est acyclique.
 * 
 * La matrice d'adjacence doit être une matrice carrée contenant des valeurs 0 ou 1,
 * où 1 signifie la présence d'un arc entre deux sommets, et 0 son absence.
 * 
 * L'algorithme utilisé est basée sur la méthode de Floyd-Warshall.
 */
public class CompetenceGraph{

    /**
     * Constructeur privé pour empêcher l'instanciation de cette classe utilitaire.
     */
    private CompetenceGraph(){}

    /**
     * Cette méthode modifie directement la matrice d'entrée pour indiquer,
     * pour chaque paire de sommets (i,j), s'il existe un chemin de i vers j.
     * 
     * @param mat matrice d'adjacence carrée contenant des 0 et 1, modifiée en sortie
     */
    private static void floydWarshall(int[][] mat) {
        int n = mat.length;
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (mat[i][k] == 1 && mat[k][j] == 1) {
                        mat[i][j] = 1;
                    }
                }
            }
        }
    }

    /**
     * Vérifie si le graphe orienté représenté par la matrice d'adjacence est acyclique.
     * 
     * Un graphe est acyclique si sa fermeture transitive ne présente aucune boucle,
     * c'est-à-dire qu'aucun sommet n'est accessible à partir de lui-même.
     * 
     * @param mat matrice d'adjacence carrée contenant des 0 et 1, ne sera pas modifiée
     * @return {@code true} si le graphe est acyclique, {@code false} sinon
     * @throws IllegalArgumentException si la matrice n'est pas carrée
     */
    public static boolean isAcyclic(int[][] mat) {
		int n = mat.length;

		for (int i = 0; i < n; i++) {
			if (mat[i].length != n) {
				throw new IllegalArgumentException("La matrice doit être carrée");
			}
		}
		int[][] copie = new int[n][n];
		for (int i = 0; i < n; i++) {
			System.arraycopy(mat[i], 0, copie[i], 0, n);
		}
		floydWarshall(copie);
		boolean acyclic = true;
		for (int i = 0; i < n; i++) {
			if (copie[i][i] == 1) {
				acyclic = false;
			}
		}
		return acyclic;
	}	

}
