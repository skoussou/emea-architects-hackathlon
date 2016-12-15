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

import com.redhat.virtualsummit.hackathon.model.Service;
import com.redhat.virtualsummit.hackathon.util.Constants;

@LocalBean
@Stateless
public class ProxyNotificationService extends AbstractRestClient {
	
	@Inject
	Logger log;
	
	public boolean notify(Service service) {
		log.info("Notifying proxy");
		StringBuilder pathBuilder = new StringBuilder();
		pathBuilder.append(getEnvProperty(Constants.PROXY_REQUESTPATH_ENV));
		ClientRequest request;
		try {
			request = buildRequest(pathBuilder.toString());
			request.body(MediaType.APPLICATION_JSON_TYPE, service);
			ClientResponse<?> clientResponse = request.post(Service.class);
			if (checkStatus(clientResponse.getStatus(), HttpStatus.SC_OK)) {
				log.info("Returned ok from notification");
				return true;
			} else {
				log.severe("Returned error status from notification");
				return false;
			}
		} catch (Exception e) {
			// TODO throw error (so far, just reject)
			log.severe("Error in notification: " + e.getMessage()!=null?e.getMessage():"(none)");
			return false;
		}
	}
	
	private ClientRequest buildRequest(String path) throws URISyntaxException {
		return new ClientRequest(
				buildURI(Constants.PROTOCOL, getEnvProperty(Constants.PROXY_HOST_ENV),
						getEnvProperty(Constants.PROXY_PORT_ENV), path)).accept(MediaType.APPLICATION_JSON); 
	}
}
