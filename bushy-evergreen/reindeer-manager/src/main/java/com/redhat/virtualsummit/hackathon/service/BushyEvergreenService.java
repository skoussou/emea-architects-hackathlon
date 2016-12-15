package com.redhat.virtualsummit.hackathon.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;

import com.redhat.virtualsummit.hackathon.model.Service;
import com.redhat.virtualsummit.hackathon.model.Team;
import com.redhat.virtualsummit.hackathon.util.Constants;

@LocalBean
@Stateless
public class BushyEvergreenService {

	@Inject
	Logger log;

	@Inject
	ProxyNotificationService proxy;
	
	@Inject
	SorterService sorterService;

	public Service manageReindeers(Service service) throws Exception {
		Service sortedOutput = null;		
		Team[] sortedPayload = sorterService.sort(createPayload(service.getPayload()));
		if (sortedPayload != null) {
			// create sortedOuput
			sortedOutput = new Service();
			sortedOutput.setServiceName(Constants.SERVICE_NAME);
			sortedOutput.setPayload(sortedPayload);	
			if (proxy.notify(sortedOutput)) {
				log.info("Proxy has been notified");
			} else {
				String msg = "Could not notify proxy: Unexpected error";
				log.severe(msg);
			}
		}
		return sortedOutput;
	}

	// FIXME do it differently
	private Team[] createPayload(Team[] inputPayload) {
		List<Team> payload = new ArrayList<Team>();
		// create ours
		Map<String, String> teamMembers = new HashMap<String, String>();
		teamMembers.put(Constants.MEMBER_1_NAME, Constants.MEMBER_1_EMAIL);
		teamMembers.put(Constants.MEMBER_2_NAME, Constants.MEMBER_2_EMAIL);

		Map<String, String> envs = System.getenv();
		
		for (Entry<String, String> envEntry : envs.entrySet()) {
			if (envEntry.getKey().startsWith(Constants.TEAM_A_REINDEER_PREFIX_ENV)) {
				Team team = new Team();
				team.setReindeerName(envEntry.getValue());
				team.setTeamName(Constants.TEAM_NAME);
				team.setNameEmailap(teamMembers);
				payload.add(team);
			}
		}
		if (inputPayload!=null) {
			for (Team team : inputPayload) {
				payload.add(team);
			}
		}
		return payload.toArray(new Team[payload.size()]);
	}
}
