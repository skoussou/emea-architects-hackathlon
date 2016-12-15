/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.redhat.virtualsummit.hackathon.rest;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.redhat.virtualsummit.hackathon.model.Service;
import com.redhat.virtualsummit.hackathon.service.BushyEvergreenService;
import com.redhat.virtualsummit.hackathon.service.ProxyNotificationService;
import com.redhat.virtualsummit.hackathon.util.Constants;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to manage input service and resend them
 * to the proxy service
 */
@Path("/reindeerservice")
@RequestScoped
public class ReindeerManagerResource {

	@Inject
	private Logger log;
	
	@Inject
	BushyEvergreenService bushyEvergreenService;
	

	/**
	 * 
	 * @param member
	 * @return
	 */
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response manageReindeers(Service service) {
		log.info("Input data: " + (service != null ? service.toString() : "none"));
		try {
			Service output= null;
			if ((output = bushyEvergreenService.manageReindeers(service))!=null) {
				log.info("Output service: " + output);
				return buildResponse(Status.OK, "Bushy evergreen service rocks!");
			} else {
				String msg= "Could not manage reindeers: Unexpected error";
				log.severe(msg);
				return buildResponse(Status.INTERNAL_SERVER_ERROR, msg);
			}
		} catch (Exception e) {
			log.severe("Error: " + e.getMessage());
			return buildResponse(Status.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	private Response buildResponse(Response.Status status, String additionalInfo) {
		Map<String, String> responseObj = new HashMap<String, String>();
		responseObj.put("info", additionalInfo);
		responseObj.put("service", System.getenv(Constants.HOSTNAME_ENV));
		return Response.status(status).entity(responseObj).build();

	}

}
