package com.redhat.virtualsummit.hackathon.model;

import java.util.Arrays;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Service {

	private String serviceName;

	private Team[] payload;

	public String getServiceName() {
		return serviceName;
	}

	public Team[] getPayload() {
		return payload;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public void setPayload(Team[] payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "Service [serviceName=" + (serviceName != null ? serviceName : "(not-provided)") + ", payload="
				+ (payload != null ? Arrays.toString(payload) : "[]") + "]";
	}

}
