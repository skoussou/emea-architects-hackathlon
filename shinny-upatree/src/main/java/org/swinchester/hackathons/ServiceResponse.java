package org.swinchester.hackathons;

import java.util.List;

/**
 * Created by swinchester on 14/12/2016.
 */
public class ServiceResponse {

    private String serviceName;
    private List<Team> payload;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<Team> getPayload() {
        return payload;
    }

    public void setPayload(List<Team> payload) {
        this.payload = payload;
    }
}
