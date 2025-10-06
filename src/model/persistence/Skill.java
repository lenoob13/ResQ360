package model.persistence;
/**
 * Represents a skill or certification that a rescuer can have.
 * Each skill has a unique index (used for mapping or matrix purposes).
 * 
 * @author ResQ360
 */
public enum Skill {
    CP(0),
    CE(1),
    CO(2),
    PSE1(3),
    PSE2(4),
    SSA(5),
    VPSP(6),
    PBC(7),
    PBF(8);

    private final int indice;

    /**
     * Creates a skill with its corresponding index.
     *
     * @param indice the index of the skill
     */
    Skill(int indice) {
        this.indice = indice;
    }

    /**
     * Gets the index of the skill.
     *
     * @return the skill index
     */
    public int getIndice() {
        return indice;
    }

    /**
     * Returns the skill that matches the given index.
     *
     * @param indice the index to search
     * @return the matching skill
     * @throws IllegalArgumentException if no skill matches
     */
    public static Skill fromId(int indice) {
        for (Skill c : values()) {
            if (c.getIndice() == indice) {
                return c;
            }
        }
        throw new IllegalArgumentException("Indice inconnu : " + indice);
    }
}
