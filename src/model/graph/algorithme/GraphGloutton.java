package model.graph.algorithme;

/**
 * Implements a greedy bipartite assignment algorithm.
 * Assigns rescuers (rows) to needed diplomas (columns) using a basic greedy strategy.
 */
public class GraphGloutton {
    private final int[][] matrice;
    private final int lenX;//nbr de sauveteur
    private final int lenY;//nbr de diplômes dont il y a besoin

    /**
     * Constructor that deep-copies the input matrix.
     *
     * @param matrice a binary matrix representing possible assignments (1 = possible)
     */
    public GraphGloutton(int[][] matrice){
        this.matrice = new int[matrice.length][];
        for (int i = 0; i < matrice.length; i++) {
            this.matrice[i] = matrice[i].clone();
        }
        this.lenX = matrice.length;
        if (lenX>0) {
            this.lenY = matrice[0].length;
        }else{
            this.lenY = 0;
        }
    }

    /**
     * Assigns rescuers to diplomas using a greedy algorithm.
     *
     * @return a binary matrix where 1 indicates an assignment
     */
    public int[][] assign(){
        if (lenX == 0 || lenY == 0) {
            return new int[0][0];
        }
        int[] ordX=getOrdX();
        int[] ordY=getOrdY();
        int[][] res = new int[lenX][lenY];
        boolean[] diplomeAttribue = new boolean[lenY];

        for(int i : ordX){
            for(int j : ordY){
                if (matrice[i][j]==1 && !diplomeAttribue[j]){
                    res[i][j]= 1;
                    diplomeAttribue[j] = true;
                    break;
                }
            }
        }
        return res;
    }

    /**
     * Computes the degree of a rescuer (number of possible assignments).
     *
     * @param i index of the rescuer
     * @return number of 1s in row i
     * @throws IllegalArgumentException if index is out of bounds
     */
    private int getDegX(int i)throws IllegalArgumentException{
        if (i < 0 || i >= lenX) {
            throw new IllegalArgumentException("Indice invalide.");
        }
        int deg = 0;
        for (int j = 0; j < lenY; j++) {
            deg+=matrice[i][j];
        }
        return deg;
    }

    /**
     * Computes the degree of a diploma (number of candidates).
     *
     * @param j index of the diploma
     * @return number of 1s in column j
     * @throws IllegalArgumentException if index is out of bounds
     */
    private int getDegY(int j)throws IllegalArgumentException{
        if (j < 0 || j >= lenY) {
            throw new IllegalArgumentException("Indice invalide.");
        }
        int deg = 0;
        for (int i = 0; i < lenX; i++) {
            deg+=matrice[i][j];
        }
        return deg;
    }

    /**
     * Returns rescuer indices ordered by ascending degree.
     */
    private int[] getOrdX() {
        int[] ord = new int[lenX];

        for (int i = 0; i < lenX; i++) {
            ord[i] = i;
        }
        for (int i = 1; i < lenX; i++) {
            int x = ord[i];
            int degX = getDegX(x);
            int j = i - 1;
            while (j >= 0 && getDegX(ord[j]) > degX) {
                ord[j + 1] = ord[j];
                j--;
            }
            ord[j + 1] = x;
        }

        return ord;
    }
    
    /**
     * Returns diploma indices ordered by ascending degree.
     */
    private int[] getOrdY() {
        int[] ord = new int[lenY];

        for (int j = 0; j < lenY; j++) {
            ord[j] = j;
        }
        for (int i = 1; i < lenY; i++) {
            int y = ord[i];
            int degY = getDegY(y);
            int j = i - 1;
            while (j >= 0 && getDegY(ord[j]) > degY) {
                ord[j + 1] = ord[j];
                j--;
            }
            ord[j + 1] = y;
        }

        return ord;
    }

    /**
     * Calculates the number of successful assignments made.
     *
     * @return the total number of 1s in the result of assign()
     */
    public int calculerMaximum() {
        int[][] solution = assign();
        int count = 0;

        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[i].length; j++) {
                if (solution[i][j] == 1) {
                    count++;
                }
            }
        }

        return count;
    }
}
