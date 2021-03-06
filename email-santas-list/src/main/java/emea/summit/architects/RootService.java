/**
 *  Copyright 2005-2015 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package emea.summit.architects;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import io.fabric8.utils.IOHelpers;

import org.jboss.logging.Logger;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;


/**
 * This Java class with be hosted in the URI path defined by the @Path annotation. @Path annotations on the methods
 * of this class always refer to a path relative to the path defined at the class level.
 * <p/>
 * For example, with 'http://localhost:8181/cxf' as the default CXF servlet path and '/crm' as the JAX-RS server path,
 * this class will be hosted in 'http://localhost:8181/cxf/crm/customerservice'.  An @Path("/customers") annotation on
 * one of the methods would result in 'http://localhost:8181/cxf/crm/customerservice/customers'.
 */
@Path("/")
@Api(value = "/", description = "CXF CDI Quickstart")
public class RootService {

	public static final Logger LOG = Logger.getLogger(RootService.class);
	
	
    @GET
    @Produces(MediaType.TEXT_HTML)
    public String index() throws IOException {
        URL resource = getClass().getResource("index.html");
        if (resource != null) {
            InputStream in = resource.openStream();
            if (in != null) {
                return IOHelpers.readFully(in);
            }
        }
        
        // TODO - Testing to call another service VIA SERVICE URL
        try {
        	ClientRequest request = new ClientRequest(
                    "http://proxy-and-log-hackathlon-test-proxy-log.router.default.svc.cluster.local/proxy-and-log-service-1.0.0/Log4jTest")
        			.accept(MediaType.APPLICATION_JSON);

        } catch (Exception e) {
            LOG.info("*****************index()***********************************************");
            LOG.info("FAILED - EMAIL-SANTAS-LIST FAILED TO CALL - PROXY & LOGGING SERVICE");
            LOG.info(e.getMessage());
            LOG.info("****************************************************************");

               
        }
        LOG.info("****************************************************************");
        LOG.info("EMAIL-SANTAS-LIST SUCCESSFULLY CALLED - PROXY & LOGGING SERVICE");
        LOG.info("****************************************************************");

        return null;
    }

    @GET
    @Path("index.html")
    @Produces(MediaType.TEXT_HTML)
    public String indexHtml() throws IOException {
        
        // TODO - Testing to call another service VIA SERVICE URL
        try {
        	ClientRequest request = new ClientRequest(
                    "http://proxy-and-log-hackathlon-test-proxy-log.router.default.svc.cluster.local/proxy-and-log-service-1.0.0/Log4jTest")
        			.accept(MediaType.APPLICATION_JSON);

        } catch (Exception e) {
            LOG.info("*****************indexHtml()***********************************************");
            LOG.info("FAILED - EMAIL-SANTAS-LIST FAILED TO CALL - PROXY & LOGGING SERVICE");
            LOG.info(e.getMessage());
            LOG.info("****************************************************************");

               
        }
        LOG.info("****************************************************************");
        LOG.info("EMAIL-SANTAS-LIST SUCCESSFULLY CALLED - PROXY & LOGGING SERVICE");
        LOG.info("****************************************************************");
    	
    	
        return index();
    }

    public RootService() {
    }

    @GET
    @Path("_ping")
    public String ping() {
        
        // TODO - Testing to call another service VIA SERVICE URL
        try {
        	ClientRequest request = new ClientRequest(
                    "http://proxy-and-log-hackathlon-test-proxy-log.router.default.svc.cluster.local/proxy-and-log-service-1.0.0/Log4jTest")
        			.accept(MediaType.APPLICATION_JSON);

        } catch (Exception e) {
            LOG.info("*****************ping()***********************************************");
            LOG.info("FAILED - EMAIL-SANTAS-LIST FAILED TO CALL - PROXY & LOGGING SERVICE");
            LOG.info(e.getMessage());
            LOG.info("****************************************************************");

               
        }
        LOG.info("****************************************************************");
        LOG.info("EMAIL-SANTAS-LIST SUCCESSFULLY CALLED - PROXY & LOGGING SERVICE");
        LOG.info("****************************************************************");
    	
    	
        
        return "true";
    }


}
