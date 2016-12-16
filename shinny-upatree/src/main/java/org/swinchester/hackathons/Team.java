package org.swinchester.hackathons;

import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by swinchester on 14/12/2016.
 */
public class Team {

    private String teamName;
    private String reindeerName;
    private HashMap<String,String> nameEmaiMap;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getReindeerName() {
        return reindeerName;
    }

    public void setReindeerName(String reindeerName) {
        this.reindeerName = reindeerName;
    }

    public HashMap<String, String> getNameEmaiMap() {
        return nameEmaiMap;
    }

    public void setNameEmaiMap(HashMap<String, String> nameEmaiMap) {
        this.nameEmaiMap = nameEmaiMap;
    }

    public static Comparator<Team> TeamReindeerNameComparator
            = (team1, team2) -> {
                String reindeerName1 = team1.getReindeerName().toUpperCase();
                String reindeerName2 = team2.getReindeerName().toUpperCase();
                //ascending order
                return reindeerName1.compareTo(reindeerName2);
            };
}
