package tests;

import model.graph.algorithme.GraphExaustif;
import model.graph.algorithme.GraphGloutton;

import java.util.Random;
/**
 * Test class to compare the performance of two graph algorithms:
 * a greedy algorithm (GraphGloutton) and an exhaustive one (GraphExaustif).
 *
 * <p>It tests them on different matrices and measures execution time
 * and how many cells are covered.</p>
 *
 * @author ResQ360
 */
public class TestGraphComplexiter {
     /**
     * Main method that runs all tests.
     *
     * @param args not used.
     */
    public static void main(String[] args) {
        testComparaisonGloutonVsExaustif();
        testGloutonMatrice2000();
    }
    /**
     * Runs the greedy and exhaustive algorithms on several predefined matrices.
     * Shows the time taken and the coverage ratio for each one.
     */
    private static void testComparaisonGloutonVsExaustif() {
        System.out.println("====== TEST COMPARAISON GLOUTON vs EXAUSTIF ======");

        int[][][] matrices = {
            /** matrice 6x6 **/
            {
                {1, 0, 1, 1, 0, 1},
                {0, 1, 1, 0, 1, 0},
                {1, 1, 1, 0, 1, 1},
                {1, 0, 0, 1, 0, 1},
                {0, 1, 1, 0, 1, 0},
                {1, 0, 1, 1, 0, 1}
            },
            /** matrice 10X10 **/
            {
				{1,0,0,1,0,0,0,1,0,0},
				{0,1,1,0,0,1,0,0,0,0},
				{0,0,1,0,1,0,0,0,1,0},
				{1,0,0,1,0,0,1,0,0,0},
				{0,1,0,0,1,0,0,1,0,0},
				{0,0,1,0,0,1,0,0,1,0},
				{1,0,0,1,0,0,1,0,0,1},
				{0,1,0,0,1,0,0,1,0,0},
				{0,0,1,0,0,1,0,0,1,0},
				{0,0,0,1,0,0,1,0,0,1}
			},
            /** matrice 12X12 **/
			{
				{0,0,0,1,0,0,1,0,0,0,0,0},  
				{0,0,1,0,0,1,1,0,0,0,0,0},  
				{1,1,1,1,1,0,0,1,1,1,1,1},  
				{1,0,0,0,0,1,0,0,0,1,1,0},  
				{0,0,1,0,1,0,0,1,0,0,0,0},  
				{1,0,0,0,0,0,0,0,0,0,0,0},  
				{0,0,0,0,0,0,0,1,0,0,0,0},  
				{1,1,1,1,1,0,1,1,1,1,1,1},  
				{0,0,1,0,0,0,0,0,0,0,1,0},  
				{0,0,1,0,0,0,0,0,0,0,0,0},  
				{0,0,1,0,1,1,0,0,1,0,0,0},  
				{0,0,0,0,0,1,0,0,0,0,0,1}   
			},
			/** matrice 13X13 **/
			{
				{1,0,1,0,1,0,1,0,1,0,1,0,1},
				{0,1,0,1,0,1,0,1,0,1,0,1,0},
				{1,1,1,0,1,0,0,1,0,0,1,0,0},
				{0,1,0,1,1,1,0,0,1,0,0,1,0},
				{1,0,1,0,1,1,1,0,0,1,0,0,1},
				{0,1,0,1,0,1,1,1,0,0,1,0,0},
				{1,0,1,0,1,0,1,1,1,0,0,1,0},
				{0,1,0,1,0,1,0,1,1,1,0,0,1},
				{1,0,1,0,1,0,1,0,1,1,1,0,0},
				{0,1,0,1,0,1,0,1,0,1,1,1,0},
				{1,0,1,0,1,0,1,0,1,0,1,1,1},
				{0,1,0,1,0,1,0,1,0,1,0,1,1},
				{1,0,1,0,1,0,1,0,1,0,1,0,1}
			}
        };

        for (int i = 0; i < matrices.length; i++) {
            int[][] matrice = matrices[i];
            int n = matrice.length;

            System.out.println(" ------ n = " + n + " ------ ");

            /** testGloutonComplexité **/
            System.out.println("GLOUTON");
            long t1G = System.nanoTime();
            GraphGloutton g = new GraphGloutton(matrice);
            int[][] resG = g.assign();
            long t2G = System.nanoTime();
            long tpsG = (t2G - t1G) / 1_000_000;
            int couvG = compterCasesActives(resG);
            double ratioG = (double) couvG / (n * n);
            System.out.println("Tps = " + tpsG + " ms");
            System.out.println("couv/n² = " + ratioG);
            System.out.println();

            /** testExaustifComplexité **/
            System.out.println("Exaustif");
            long t1E = System.nanoTime();
            GraphExaustif e = new GraphExaustif(matrice);
            int[][] resE = e.assign();
            long t2E = System.nanoTime();
            long tpsE = (t2E - t1E) / 1_000_000;
            int couvE = compterCasesActives(resE);
            double ratioE = (double) couvE / (n * n);
            System.out.println("Tps = " + tpsE + " ms");
            System.out.println("couv/n² = " + ratioE);
            System.out.println();
        }
    }
    /**
     * Runs the greedy algorithm on a large random 2000x2000 matrix.
     * Shows the time taken and the coverage ratio.
     */
    private static void testGloutonMatrice2000() {
		 /** testGloutonComplexité 1350 **/
        System.out.println("====== TEST GLOUTON SUR MATRICE 2000 x 2000 ======");

        int n = 2000;
        int[][] matrice = genererMatriceAleatoire(n);

        long t1 = System.nanoTime();
        GraphGloutton g = new GraphGloutton(matrice);
        int[][] res = g.assign();
        long t2 = System.nanoTime();

        long tps = (t2 - t1) / 1_000_000;
        int couverture = compterCasesActives(res);
        double ratio = (double) couverture / (n * n);

        System.out.println("Tps = " + tps + " ms");
        System.out.println("couv/n² = " + ratio);
        System.out.println();
    }
    /**
     * Counts how many cells in the matrix have the value 1.
     *
     * @param mat the matrix to check
     * @return the number of active cells
     */
    private static int compterCasesActives(int[][] mat) {
        int total = 0;
        for (int[] ligne : mat) {
            for (int v : ligne) {
                if (v == 1) total++;
            }
        }
        return total;
    }
    /**
     * Creates a square matrix of size n with random 0 or 1 values.
     *
     * @param n the size of the matrix
     * @return a random matrix of size n x n
     */
    private static int[][] genererMatriceAleatoire(int n) {
        int[][] matrice = new int[n][n];
        Random random = new Random();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrice[i][j] = random.nextBoolean() ? 1 : 0;
            }
        }

        return matrice;
    }
}
