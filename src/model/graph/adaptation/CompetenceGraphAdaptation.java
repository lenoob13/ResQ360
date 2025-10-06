package model.graph.adaptation;

import model.persistence.CompetenceDependancies;
import model.persistence.Skill;

import java.util.*;

/**
 * Utility class to convert the skill dependency model
 * into an adjacency matrix for graph algorithms.
 */
public class CompetenceGraphAdaptation {

    /**
     * Builds the adjacency matrix representing skill dependencies.
     * 
     * If Skill A depends on Skill B, then mat[A][B] = 1.
     *
     * @param manager the object holding the full skill dependency map
     * @return a 2D array where mat[i][j] = 1 if Skill i depends on Skill j
     */
    public static int[][] getAdjacencyMatrix(CompetenceDependancies manager) {
        Skill[] competences = Skill.values();
        int n = competences.length;

        Map<Skill, Integer> indexMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            indexMap.put(competences[i], i);
        }

        int[][] mat = new int[n][n];
        Map<Skill, List<Skill>> hierarchy = manager.getHierarchie();

        for (Map.Entry<Skill, List<Skill>> entry : hierarchy.entrySet()) {
            int from = indexMap.get(entry.getKey());
            for (Skill to : entry.getValue()) {
                int toIndex = indexMap.get(to);
                mat[from][toIndex] = 1;
            }
        }

        return mat;
    }

    /**
     * Returns the list of skills in their enum order.
     *
     * @return a list of all Skill values
     */
    public static List<Skill> getCompetenceOrder() {
        return List.of(Skill.values());
    }
}
