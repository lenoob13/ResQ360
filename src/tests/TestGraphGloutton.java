package tests;

import model.graph.algorithme.GraphGloutton;
/**
 * Test class for the {@link GraphGloutton} (greedy) algorithm.
 *
 * <p>This class runs several test cases to check if the greedy assignment
 * algorithm works correctly in different situations.</p>
 *
 * <p>Each test checks how many assignments are made and if they are valid
 * based on a compatibility matrix.</p>
 *
 * @author ResQ360
 */
public class TestGraphGloutton {
    /**
     * Main method that runs all the test cases.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
		System.out.println(" ====== Test Exaustif ====== ");
		System.out.println("");
        testSimple();
        testAucuneAffectationPossible();
        testPlusDeSauveteursQueDiplomes();
        testPlusDeDiplomesQueSauveteurs();
        testVide();
    }
    /**
     * Test with a simple 2x2 matrix where each rescuer gets one diploma.
     */
    private static void testSimple() {
        int[][] matrice = {
                {1, 0},
                {0, 1}
        };
        System.out.println("Test simple (2x2, affectation parfaite) :");
        lancerTest(matrice, 2);
    }
    /**
     * Test with a matrix where no assignment is possible.
     */
    private static void testAucuneAffectationPossible() {
        int[][] matrice = {
                {0, 0},
                {0, 0}
        };
        System.out.println("Test aucune affectation possible :");
        lancerTest(matrice, 0);
    }
    /**
     * Test where there are more rescuers than diplomas.
     */
    private static void testPlusDeSauveteursQueDiplomes() {
        int[][] matrice = {
                {1, 0},
                {0, 1},
                {1, 1}
        };
        System.out.println("Test avec plus de sauveteurs que de diplômes :");
        lancerTest(matrice, 2);
    }
    /**
     * Test where there are more diplomas than rescuers.
     */
    private static void testPlusDeDiplomesQueSauveteurs() {
        int[][] matrice = {
                {1, 1, 0},
                {0, 1, 1}
        };
        System.out.println("Test avec plus de diplômes que de sauveteurs :");
        lancerTest(matrice, 2);
    }
    /**
     * Test with an empty matrix.
     */
    private static void testVide(){
        int[][] matrice = {};
        System.out.println("Test vide :");
        lancerTest(matrice, 0);
    }
    /**
     * Runs one test case using the greedy algorithm.
     * It compares the number of assignments with what is expected
     * and checks if the result is valid.
     *
     * @param matrice compatibility matrix (rescuer x diploma)
     * @param expectedAffectations expected number of assignments
     */
    private static void lancerTest(int[][] matrice, int expectedAffectations) {
        GraphGloutton algo = new GraphGloutton(matrice);
        int[][] res = algo.assign();

        int total = compterAffectations(res);
        boolean estValide = verifierValidite(matrice, res);
        boolean ok = (total == expectedAffectations) && estValide;

        System.out.println(" - Affectations trouvées : " + total + " / attendu : " + expectedAffectations);
        if (ok) {
            System.out.println("    TEST OK");
        } else {
            System.out.println("    ECHEC DU TEST");
        }
        System.out.println();
    }
    /**
     * Counts the number of cells with value 1 in the result matrix.
     *
     * @param matrice the result matrix
     * @return number of assignments
     */
    private static int compterAffectations(int[][] matrice) {
        int count = 0;
        for (int[] ligne : matrice) {
            for (int val : ligne) {
                if (val == 1) {
                    count++;
                }
            }
        }
        return count;
    }
    /**
     * Checks that the result matrix only contains valid assignments.
     * Each assignment must match the input matrix and no diploma
     * should be assigned more than once.
     *
     * @param compatibilite the input compatibility matrix
     * @param affectation the result assignment matrix
     * @return true if valid, false otherwise
     */
    private static boolean verifierValidite(int[][] compatibilite, int[][] affectation) {
        if (compatibilite.length == 0 || affectation.length == 0) {
            return true;
        }
        boolean valide = true;
        int lenX = compatibilite.length;
        int lenY = compatibilite[0].length;
        boolean[] diplomePris = new boolean[lenY];

        for (int i = 0; i < lenX; i++) {
            for (int j = 0; j < lenY; j++) {
                if (affectation[i][j] == 1) {
                    if (compatibilite[i][j] != 1 || diplomePris[j]) {
                        valide = false;
                    }
                    diplomePris[j] = true;
                }
            }
        }

        return valide;
    }
}
