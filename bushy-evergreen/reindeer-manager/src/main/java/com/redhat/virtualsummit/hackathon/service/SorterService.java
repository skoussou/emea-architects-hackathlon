package com.redhat.virtualsummit.hackathon.service;

import java.net.URISyntaxException;
import java.util.logging.Logger;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;

import org.apache.http.HttpStatus;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

import com.redhat.virtualsummit.hackathon.model.Team;
import com.redhat.virtualsummit.hackathon.util.Constants;

@LocalBean
@Stateless
public class SorterService extends AbstractRestClient {
	
	@Inject
	Logger log;
	
	public Team[] sort(Team[] input) throws Exception {
		// check if requires sorting
		String sorterReady = getEnvProperty(Constants.SORTER_READY_ENV);
		if (sorterReady!=null && Boolean.valueOf(sorterReady)) {
			log.info("Calling sorter service");
			StringBuilder pathBuilder = new StringBuilder();
			pathBuilder.append(getEnvProperty(Constants.SORTER_REQUESTPATH_ENV));
			ClientRequest request;
			try {
				request = buildRequest(pathBuilder.toString());
				request.body(MediaType.APPLICATION_JSON_TYPE, input);
				ClientResponse<Team[]> clientResponse = request.post(Team[].class);
				if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_OK)) {
					log.info("Returned ok from sorter");
					return clientResponse.getEntity();
				} else {
					log.severe("Returned error status from sorter");
					throw new Exception("Returned error status from sorter");
				}
			} catch (Exception e) {
				log.severe("Error in sorter service: " + e.getMessage()!=null?e.getMessage():"(none)");
				throw new Exception("Error in sorter service");
			}
		} else {
			log.info("No need to sort... sorter not ready");
			return input;
		}
	}
	
	private ClientRequest buildRequest(String path) throws URISyntaxException {
		return new ClientRequest(
				buildURI(Constants.PROTOCOL, getEnvProperty(Constants.SORTER_HOST_ENV),
						getEnvProperty(Constants.SORTER_PORT_ENV), path)).accept(MediaType.APPLICATION_JSON); 
	}
}
