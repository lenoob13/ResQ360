package model.persistence;

import util.Logger;
import java.util.ArrayList;
import java.util.List;
import static model.graph.adaptation.CompetenceGraphAdaptation.getAdjacencyMatrix;
import static model.graph.algorithme.CompetenceGraph.isAcyclic;

/**
 * Represents a rescuer with personal information, skills, assignments, and access rights.
 * 
 * <p>Used throughout the application to manage who is assigned to which DPS,
 * and what competencies each rescuer has.</p>
 * 
 * @author ResQ360
 */
public class Rescuer {
    private int id;
    private String identifier;
    private String email;
    private List<String> skills;
    private final List<Integer> assignments;
    private String password;
    private final Boolean admin;
    private String name;
    private String fName;

    /**
     * Constructor with only ID.
     *
     * @param id the rescuer ID
     */
    public Rescuer(int id) {
        this.id = id;
        this.identifier = "";
        this.email = "";
        this.skills = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.password = "";
        this.admin = false;
        this.name = "";
        this.fName = "";
    }

    /**
     * Constructor with ID, identifier and email.
     *
     * @param id the rescuer ID
     * @param identifier the login or username
     * @param email the email address
     */
    public Rescuer(int id, String identifier, String email) {
        this.id = id;
        this.identifier = identifier;
        this.email = email;
        this.skills = new ArrayList<>();
        this.assignments = new ArrayList<>();
        this.password = "";
        this.admin = false;
        this.name = "";
        this.fName = "";
    }

    /**
     * Full constructor.
     *
     * @param id the rescuer ID
     * @param identifier login
     * @param email email
     * @param skills list of skill names
     * @param password password
     * @param admin true if the user is admin
     * @param assignments list of assigned DPS IDs
     * @param name last name
     * @param fName first name
     */
    public Rescuer(int id, String identifier, String email, List<String> skills, String password, boolean admin,List<Integer> assignments, String name, String fName) {
        this.id = id;
        this.identifier = identifier;
        this.email = email;
        CompetenceDependancies cd = new CompetenceDependancies();
        if(isAcyclic(getAdjacencyMatrix(cd))) {
            try {
                this.skills = (skills != null) ? CompetenceDependancies.completeString(skills) : new ArrayList<>();
            } catch (IllegalArgumentException e) {
                Logger.warn("Un ou plusieurs skill de Rescuer ne sont pas dans le enum.");
            }
        }else {
            Logger.warn("le graph des compétences n'est pas acyclic");
        }
        this.assignments = (assignments != null) ? new ArrayList<>(assignments) : new ArrayList<>();
        this.password = password;
        this.admin = admin;
        this.name = name;
        this.fName = fName;
    }

    // Getters and Setters
    public int getId() { 
        return id; 
    }
    public void setId(int id) { 
        this.id = id; 
    }

    public String getIdentifier() { 
        return identifier; 
    }
    public void setIdentifier(String identifier) { 
        this.identifier = identifier; 
    }

    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }

    public List<String> getSkills() { 
        return new ArrayList<>(skills);
    }
    
    /**
     * Sets the rescuer's skills from a list of {@link Skill} enums.
     *
     * @param skills list of skills
     */
    public void setSkills(List<Skill> skills) {
        this.skills = new ArrayList<>();
        for (Skill skill : skills) this.skills.add(skill.toString());
    }

    public void addSkill(String skill) { this.skills.add(skill); }
    public void removeSkill(String skill) { this.skills.remove(skill); }

    public List<Integer> getAssignments() { return new ArrayList<>(assignments); }
    public void setAssignments(List<Integer> assignments) { this.assignments.clear(); this.assignments.addAll(assignments); }
    public void addAssignment(int dpsId) {
        if (!this.assignments.contains(dpsId)) {
            this.assignments.add(dpsId);
        }
    }

    public void removeAssignment(int dpsId) {
        this.assignments.remove(dpsId);
    }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Boolean getAdmin() { return this.admin; }

    public String getName(){return this.name;}
    public void setName(String name){this.name = name;}

    public String getFName(){return this.fName;}
    public void setFName(String name){this.fName = name;}

    /**
     * Returns a readable string with all rescuer information.
     *
     * @return string representation
     */
    @Override
    public String toString() {
        return "Rescuer {" +
                "\n  id = " + id +
                ",\n  identifier = '" + identifier + '\'' +
                ",\n  email = '" + email + '\'' +
                ",\n  skills = " + skills +
                ",\n  password = '" + password + '\'' +
                ",\n  assignments (ID) = " + assignments +
                ",\n  admin = " + admin +
                ",\n  name = '" + name + '\'' +
                ",\n  fName = '" + fName + '\'' +
                "\n}";
    }

    /**
     * Returns a line in CSV format for export.
     *
     * @return string CSV-formatted
     */
    public String csvString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < assignments.size(); i++) {
            sb.append(assignments.get(i));
            if (i < assignments.size() - 1) {
                sb.append(" ");
            }
        }
        return id+", "
                +identifier+", "
                +email+", "
                +String.join("/ ", skills)+", "
                +password+", "
                +admin+", "
                +sb+", "
                +name+", "
                +fName+"\n";
    }
}
