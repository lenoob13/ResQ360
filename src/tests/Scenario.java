package tests;

import model.persistence.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static model.persistence.CompetenceDependancies.completeString;
/**
 * A test class that simulates a basic scenario using the main data model components.
 * 
 * <p>This scenario demonstrates the instantiation and display of objects
 * such as {@link Sport}, {@link Site}, {@link Day}, {@link DPS}, and {@link Rescuer}.
 * It also shows how to populate attributes like competences and assignments,
 * including usage of helper methods like {@code completeString()} from {@link CompetenceDependancies}.</p>
 * 
 * <p>The main purpose of this class is to manually verify the correct
 * construction and string representation of key domain model objects.</p>
 *
 * @author ResQ360
 */
public class Scenario {
    public static void main(String[] args) {
        System.out.println("-----SCENARIO-----");
        System.out.println("--> création de l'objet Sport");
        Sport sport = new Sport("SnowBoard","1");
        System.out.println(sport);
        System.out.println("--> création de l'objet Site");
        Site site = new Site("1","La meule",3.14f,1.62f);
        System.out.println(site);
        System.out.println("--> création de l'objet Day");
        int[] start={12,45};
        int[] end={16,30};
        Day day = new Day(1,13,2,2031,start,end);
        System.out.println(day);
        System.out.println("--> création de l'objet DPS");
        DPS dps = new DPS(1,1,"1");
        dps.setIdSport("1");
        Map<Integer, Integer> besoins = new HashMap<>();
        besoins.put(1,3);
        dps.setBesoins(besoins);
        System.out.println(dps);
        System.out.println("--> création de l'objet secouriste");
        ArrayList<String> competences = new ArrayList<>();
        competences.add("VPSP");
        competences.add("PBC");
        competences = completeString(competences);
        ArrayList<Integer>  assignments = new ArrayList<>();
        assignments.add(1);
        Rescuer rescuer = new Rescuer(1,"julien04","juju@boite.qqc", competences, "jujuG04t", false,assignments, "Dupont", "Julien");
        System.out.println(rescuer);
    }
}
