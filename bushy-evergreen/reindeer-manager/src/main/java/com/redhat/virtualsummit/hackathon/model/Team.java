package com.redhat.virtualsummit.hackathon.model;

import java.util.Map;

public class Team {

	private String teamName;

	private String reindeerName;

	private Map<String, String> nameEmaiMap;

	public String getTeamName() {
		return teamName;
	}

	public String getReindeerName() {
		return reindeerName;
	}

	public Map<String, String> getNameEmaiMap() {
		return nameEmaiMap;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public void setReindeerName(String reindeerName) {
		this.reindeerName = reindeerName;
	}

	public void setNameEmailap(Map<String, String> nameEmaiMap) {
		this.nameEmaiMap = nameEmaiMap;
	}

	@Override
	public String toString() {
		return "Team [teamName=" + (teamName != null ? teamName : "(not-provided)") + ", reindeerName="
				+ (reindeerName != null ? reindeerName : "(not-provided)") + ", nameEmailMapSize="
				+ (nameEmaiMap != null ? nameEmaiMap.size() : "0") + "]";
	}

}
