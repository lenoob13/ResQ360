package model.persistence;

import java.util.*;

/**
 * Manages the logical skill dependency hierarchy used in the application.
 * 
 * <p>This class computes all indirect skill requirements based on the direct dependencies defined.</p>
 * 
 * Example: if CO → CP → CE → PSE2 → PSE1, then CO implies also having all those others.</p>
 * 
 * @author ResQ360
 */
public class CompetenceDependancies {

    private static Map<Skill, List<Skill>> hierarchie = new HashMap<>();

    /**
     * Initializes the dependency hierarchy and computes the full transitive closure.
     */
    static {
        hierarchie.put(Skill.CO, List.of(Skill.CP));
        hierarchie.put(Skill.CP, List.of(Skill.CE));
        hierarchie.put(Skill.CE, List.of(Skill.PSE2));
        hierarchie.put(Skill.PSE2, List.of(Skill.PSE1));
        hierarchie.put(Skill.PBF, List.of(Skill.PBC));
        hierarchie.put(Skill.VPSP, List.of(Skill.PSE2));
        hierarchie.put(Skill.SSA, List.of(Skill.PSE1));
        hierarchie.put(Skill.PSE1, new ArrayList<>());
        hierarchie.put(Skill.PBC, new ArrayList<>());
        mettreAJourHierarchieComplete();
    }

    /**
     * Returns the full dependency map of each skill to its required sub-skills.
     *
     * @return the complete hierarchy map
     */    
    public static Map<Skill, List<Skill>> getHierarchie() {
        return hierarchie;
    }

    /**
     * Computes the full set of required skills (direct and indirect) for given base skills.
     *
     * @param competencesDeBase the base skills to start from
     * @return the complete set of required skills
     */
    private static Set<Skill> calculerCompetencesCompletes(Skill... competencesDeBase) {
        Set<Skill> result = new HashSet<>();
        List<Skill> aTraiter = new ArrayList<>(Arrays.asList(competencesDeBase));
        int index = 0;
        while (index < aTraiter.size()) {
            Skill c = aTraiter.get(index++);
            if (result.add(c)) {
                List<Skill> dependantes = hierarchie.getOrDefault(c, Collections.emptyList());
                for (Skill d : dependantes) {
                    if (!result.contains(d)) {
                        aTraiter.add(d);
                    }
                }
            }
        }
        return result;
    }

     /**
     * Recomputes the full dependency graph for all skills using transitive closure.
     */
    private static void mettreAJourHierarchieComplete() {
        Map<Skill, List<Skill>> hierarchieComplete = new HashMap<>();

        for (Skill c : Skill.values()) {
            Set<Skill> dependancesCompletes = calculerCompetencesCompletes(c);
            dependancesCompletes.remove(c);
            hierarchieComplete.put(c, new ArrayList<>(dependancesCompletes));
        }

        hierarchie = hierarchieComplete;
    }

    /**
     * Returns all required skill IDs (integers) for a given list of skill names.
     *
     * @param noms the list of skill names
     * @return the list of skill indices (IDs)
     */
    public static List<Integer> completeInt(List<String> noms) {
        List<Skill> competencesDeBase = new ArrayList<>();
        for (String nom : noms) {
            competencesDeBase.add(Skill.valueOf(nom));
        }

        Set<Skill> competencesCompletes = calculerCompetencesCompletes(
                competencesDeBase.toArray(new Skill[0])
        );

        List<Integer> indices = new ArrayList<>();
        for (Skill c : competencesCompletes) {
            indices.add(c.getIndice());
        }

        return indices;
    }

    /**
     * Returns all required skill names for a given list of skill names.
     *
     * @param noms the base skills
     * @return the list of all skill names including dependencies
     */
    public static ArrayList<String> completeString(List<String> noms) {
        ArrayList<Skill> competencesDeBase = new ArrayList<>();
        for (String nom : noms) {
            competencesDeBase.add(Skill.valueOf(nom));
        }

        Set<Skill> competencesCompletes = calculerCompetencesCompletes(
                competencesDeBase.toArray(new Skill[0])
        );

        ArrayList<String> nomsComplets = new ArrayList<>();
        for (Skill c : competencesCompletes) {
            nomsComplets.add(c.name());
        }

        return nomsComplets;
    }

    /**
     * Returns the skill name (as String) for a given skill index.
     *
     * @param indice the skill ID
     * @return the skill name
     */
    public static String getStrFromInt(int indice) {
        return Skill.fromId(indice).name();
    }
}
