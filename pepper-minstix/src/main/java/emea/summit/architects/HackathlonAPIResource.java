/**
 * JBoss, Home of Professional Open Source
 * Copyright 2016, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * <p/>
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
package emea.summit.architects;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import org.apache.deltaspike.core.api.config.ConfigResolver;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jboss.dmr.ModelNode;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.ServerSpan;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.httpclient.BraveHttpRequestInterceptor;
import com.github.kristofa.brave.httpclient.BraveHttpResponseInterceptor;
import com.openshift.internal.restclient.model.Route;
import com.openshift.internal.restclient.model.properties.ResourcePropertiesRegistry;
import com.openshift.restclient.ClientBuilder;
import com.openshift.restclient.IClient;
import com.openshift.restclient.ResourceKind;

import feign.Logger;
import feign.Logger.Level;
import feign.httpclient.ApacheHttpClient;
import feign.hystrix.HystrixFeign;
import feign.jackson.JacksonDecoder;
import io.swagger.annotations.ApiOperation;
import io.undertow.client.ClientRequest;

/**
 * 
 * @author stelios@redhat.com
 *
 */
@Path("/")
public class HackathlonAPIResource {

	
	
	private static final String API_PAYLOAD = "[  \n"+ 
			"   {  \n"+ 
			"      \"teamName\":\"\",\n"+ 
			"      \"reindeerName\":\"\",\n"+ 
			"      \"nameEmaiMap\":{  \n"+ 
			"         \" \":\" \"\n"+ 
			"      }\n"+ 
			"   }\n"+ 
			"]\n";


	private static final String API_PAYLOAD_EXAMPLE = 
			"[  \n"+
					"   {  \n"+
					"      \"teamName\":\"santas-helpers-c-team\",\n"+
					"      \"reindeerName\":\"blixen\",\n"+
					"      \"nameEmaiMap\":{  \n"+
					"         \"Andrea Tarrochi\":\"atarocch@redhat.com\",\n"+
					"         \"Stelios Kousouris\":\"stelios@redhat.com\"\n"+
					"      }\n"+
					"   },\n"+
					"   {  \n"+
					"      \"teamName\":\"santas-helpers-a-team\",\n"+
					"      \"reindeerName\":\"dancer\",\n"+
					"      \"nameEmaiMap\":{  \n"+
					"         \"Matteo Renzi\":\"mrenzi@redhat.com\",\n"+
					"         \"Antonis Tsipras\":\"atsipras@redhat.com\"\n"+
					"      }\n"+
					"   }\n"+
					"]\n";

	private static final int ZERO = 0;
	private static final String VALID_RESPONSE = "The service is valid and Reindeers in order";
	private static final String INVALID_RESPONSE = "The service is invalid and Reindeers are out of order \n ";

	private static LinkedList<String> serviceRoutes = new LinkedList<String>(Arrays.asList("http://santas-helpers-a-team.router.default.svc.cluster.local",
			"http://santas-helpers-b-team.router.default.svc.cluster.local",
			"http://santas-helpers-c-team.router.default.svc.cluster.local",
			"http://santas-helpers-d-team.router.default.svc.cluster.local",
			"http://santas-helpers-e-team.router.default.svc.cluster.local",
			"http://swarm-email-santas-list.router.default.svc.cluster.local"));
	
	private static Map<String, String> namespacesServicesMap = new HashMap<String, String>(){{
//		put("santas-helpers-a-team", "bushy-evergreen");
//		put("santas-helpers-b-team", "shinny-upatree");
//		put("santas-helpers-c-team", "wunorse-openslae");
//		put("santas-helpers-d-team", "pepper-minstix");
//		put("santas-helpers-e-team", "alabaster-snowball");
		put("santas-helpers-a-team", "test-milan");
		put("santas-helpers-b-team", "test-milan");
		put("santas-helpers-c-team", "test-milan");
		put("santas-helpers-d-team", "test-milan");
		put("santas-helpers-e-team", "test-milan");
	}};
	
	private static Map<String, String> serviceENVVariableMap = new HashMap<String, String>(){{
		put("proxy-api", "PROXY_API");
//		put("santas-helpers-b-team", "shinny-upatree");
//		put("santas-helpers-c-team", "wunorse-openslae");
//		put("santas-helpers-d-team", "pepper-minstix");
//		put("santas-helpers-e-team", "alabaster-snowball");
//		put("bushy-evergreen", "BUSHY_EVERGREEN");
//		put("shinny-upatree", "SHINY_UPATREE");
//		put("wunorse-openslae", "WUNORSE_OPENSLAE");
//		put("pepper-minstix", "PEPPER_MINSTIX");
//		put("alabaster-snowball", "ALABASTER_SNOWBALL");
	}};
	
	private static Map<String, String> servicesRouteMap = new HashMap<String, String>(){{
		put("bushy-evergreen", "http://bushy-evergreen-santas-helpers-a-team.router.default.svc.cluster.local");
		put("shinny-upatree", "http://shinny-upatree-santas-helpers-b-team.router.default.svc.cluster.local");
		put("wunorse-openslae", "http://santas-helpers-c-team.router.default.svc.cluster.local");
		put("pepper-minstix", "http://santas-helpers-e-team.router.default.svc.cluster.local");
		put("alabaster-snowball", "http://santas-helpers-e-team.router.default.svc.cluster.local");
	}};
	
	
	@POST  
	@Path("/reindeerservice")
	@Consumes("application/json")
	@ApiOperation("pepper-minstix, Tests printing the forwarded payload - Normally it would sort it and pass it on")
	public String test(TeamPayload request) {

		System.out.println("Calling  PEPPER-MINSTIX-TST successfully");
		System.out.println("Received Content -->"+request);

		System.out.println("REINDEER 1 [System.getenv(\"TEAM_D_REINDEER_1\")]: "+System.getenv("TEAM_D_REINDEER_1"));
		System.out.println("REINDEER 2 [System.getenv(\"TEAM_D_REINDEER_2\")]: "+System.getenv("TEAM_D_REINDEER_2"));		
		
		HashMap<String, String> emailMap = new HashMap<String, String>(){{put("wunorse-openslae-Helper1", "wo1@santavillage.com");}};
		RequestPayload newPayload1 = new RequestPayload("santas-helpers-d-team", System.getenv("TEAM_D_REINDEER_1"), emailMap);
		request.getPayload().add(newPayload1);
		
		RequestPayload newPayload2 = new RequestPayload("santas-helpers-d-team", System.getenv("TEAM_D_REINDEER_1"), emailMap);
		request.getPayload().add(newPayload2);
		
		request.setServiceName("pepper-minstix");

		ObjectMapper mapper = new ObjectMapper();
		String jsonInString = null;
		try {
			//Convert object to JSON string
			jsonInString = mapper.writeValueAsString(request);

			//Convert object to JSON string and pretty print
			jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(request);

		} catch (Exception e) {
			e.printStackTrace();
			return "Failed to transform to JSON "+e.getMessage();
		}
		System.out.println("Sending Content -->"+jsonInString);
		
		httpCall("POST", "http://proxy-api-test-milan.router.default.svc.cluster.local/api/service/proxy", jsonInString);
				
		return "Calling  PEPPER-MINSTIX-TST successfully";
	}

//	@POST  
//	@Path("/service/proxy")
//	@Consumes("application/json")
//	@ApiOperation("Receives request, validates request so far, identifies next service to contact, contacts the service OR if no more sends the email to SANTA")
//	public String submit(TeamPayload request) {
//		boolean PROD_ENV = System.getenv("ENVIRONMENT") != null &&  System.getenv("ENVIRONMENT").equalsIgnoreCase("PROD")? true : false;
//		
////		IClient ocpClient = createOCPClient();
//		
////		System.out.println("<------------------ ROUTE DETAILS ------------------>");
////		System.out.println(ocpClient.get(ResourceKind.ROUTE, namespaceFromService(request.getServiceName())));
////		System.out.println("<--------------------------------------------------->");
////		ModelNode node = ModelNode.fromJSONString(Samples.V1_ROUTE_WO_TLS.getContentAsString());
////        Route route = new Route(node, ocpClient, ResourcePropertiesRegistry.getInstance().get("v1", ResourceKind.ROUTE));
////		ocpClient.getResourceURI(arg0)
//		
//		System.out.println("==================REQUEST SERVICE: "+request.getServiceName()+"=======================");
//		System.out.println("PAYLOAD");
//		System.out.println(request.getPayload().toString());
//		
//		String host = System.getenv(serviceENVVariableMap.get(request.getServiceName())+"_SERVICE_HOST");
//		String port = System.getenv(serviceENVVariableMap.get(request.getServiceName())+"_SERVICE_PORT");
//		
//		System.out.println("Would call \n POST   https://"+host+":"+port);
//
//		if (PROD_ENV) {
//			if (validate(request.getPayload()).equalsIgnoreCase(VALID_RESPONSE)){
//				System.out.println("Valid...sending to next service");
//				// TODO 
//				// find the next service and send OR send email to SANTA
//				
////				"oc describe route "+request.getServiceName()
////				"oc describe route bushy-evergreen"
////				"oc describe route shinny-upatree"
////				"oc describe route wunorse-openslae"
////				"oc describe route pepper-minstix"
////				"oc describe route alabaster-snowball"
//				
////				String host = System.getenv(serviceENVVariableMap.get(request.getServiceName())+"_SERVICE_HOST");
////				String port = System.getenv(serviceENVVariableMap.get(request.getServiceName())+"_SERVICE_PORT");
//				
//				System.out.println("ABOUT To call\n POST   https://"+host+":"+port);
//				System.out.println(request.toString());
//				//httpCall("POST", "https://"+host+":"+port, request.toString());
//				
//			} else {
//				// Send a failed response to the requestors and an email.
//				System.out.println("INVALID_RESPONSE");
//				System.out.println("Sent to team "+namespaceFromService(request.getServiceName())+" emailing "+emailsOfTeam(request));
//				
//
//				
//				try {
//					JavaMailService.generateAndSendEmail(INVALID_RESPONSE+"\n\n"+request.getPayload(), "HACKATHLON Santa Helper "+request.getServiceName()+" sent INVALID Request ", emailsOfTeam(request));
//				} catch (MessagingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//					return "Email Failed due to "+e.getMessage();
//				}
//			}
//		}
//		
//		
////		try {
////		JavaMailService.generateAndSendEmail(email.getContent().toString(), email.getSubject(), email.getEmailAddresses());
////	} catch (MessagingException e) {
////		// TODO Auto-generated catch block
////		e.printStackTrace();
////		return "Email Failed due to "+e.getMessage();
////	}
//		
//		System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
//
//		
//		return "Email was submitted successfully";
//	}

	
	private String httpCall(String httpMethod, String serviceURL, String data){
		String result = "Not valid request for \n"+
				"HTTP METHOD" + httpMethod +"\n"+
				"URL" +serviceURL + "\n"+
				"Content" + data;

		System.out.println("<===================== Calling External Service  ======================>");
		System.out.println("     HTTP METHOD : "+httpMethod);
		System.out.println("     URL         : "+serviceURL);
		System.out.println("     Content     : "+data);
		System.out.println("<======================================================================>");

		if (httpMethod != null || httpMethod.equals("GET") || httpMethod.equals("POST") || httpMethod.equals("PUT")) {

			try {
				HttpClientBuilder builder = HttpClientBuilder.create();
				CloseableHttpClient client = builder.build();

				//HttpUriRequest request = new HttpGet(serviceURL+"api/hackathlon/info");
				HttpUriRequest request;
				if (httpMethod.equalsIgnoreCase("GET")){
					result = getRequest(serviceURL, "application/json");
				} else if (httpMethod.equalsIgnoreCase("POST")) {
					// eg. http://www.programcreek.com/java-api-examples/org.apache.http.entity.StringEntity
					StringEntity content = new StringEntity(data,"UTF-8");
					result = postRequest(serviceURL, "application/json", content);
				} else {
					result = putRequest(serviceURL, "application/json");
				}

				System.out.println("<=================== RESPONSE ====================> ");
				System.out.println("    "+result); 
				System.out.println("<=======================================> ");

			} catch (Exception e) {
				System.out.println("****************************************************************");
				//System.out.println("FAILED - CALLING ANOTHER SERVICE FROM "+serviceURL+"api/hackathlon/info");
				System.out.println("FAILED - CALLING ANOTHER SERVICE FROM "+serviceURL);
				System.out.println(e.getMessage());
				System.out.println("****************************************************************");
				return result;
			}
			System.out.println("****************************************************************");
			//System.out.println("SUCCESS - CALLING ANOTHER SERVICE FROM "+serviceURL+"api/hackathlon/info");
			System.out.println("SUCCESS - CALLING ANOTHER SERVICE FROM "+serviceURL);
			System.out.println("****************************************************************");
			return result;

		}
		System.out.println("****************************************************************");
		//System.out.println("FAILED - CALLING ANOTHER SERVICE FROM "+serviceURL+"api/hackathlon/info");
		System.out.println("FAILED - CALLING ANOTHER SERVICE FROM "+serviceURL);
		System.out.println("****************************************************************");
		return result;
	}
	
	
	private boolean inOrder(Iterator<RequestPayload> reindeersIt, String reindeer) {
		String nextReindeer = null;
		if (reindeersIt == null || !reindeersIt.hasNext()){
			return true;
		}
		if (reindeersIt.hasNext()){
			nextReindeer = reindeersIt.next().getReindeerName();
			System.out.println(" Compare "+reindeer+" vs "+nextReindeer);
			if (reindeer != null && reindeer.compareToIgnoreCase(nextReindeer) > ZERO) {
				return false;
			}
			return inOrder(reindeersIt, nextReindeer);
		}
		return true;
	}

	private static String postRequest(String url, String contentType, HttpEntity entity) throws Exception {
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpPost post = new HttpPost(url);
		post.setHeader("Content-Type", contentType);
		post.setEntity(entity);

		HttpResponse response = client.execute(post);
		StatusLine status = response.getStatusLine();			
		String content = EntityUtils.toString(response.getEntity());
		//JSONObject json = new JSONObject(content);

		return content;
	}

	private static String getRequest(String url, String contentType) throws Exception {
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpUriRequest get = new HttpGet(url);
		get.setHeader("Content-Type", contentType);

		HttpResponse response = client.execute(get);
		StatusLine status = response.getStatusLine();			
		String content = EntityUtils.toString(response.getEntity());
		//JSONObject json = new JSONObject(content);

		return content;
	}

	private static String putRequest(String url, String contentType) throws Exception {
		HttpClientBuilder builder = HttpClientBuilder.create();
		CloseableHttpClient client = builder.build();
		HttpUriRequest put = new HttpPut(url);
		put.setHeader("Content-Type", contentType);

		HttpResponse response = client.execute(put);
		StatusLine status = response.getStatusLine();			
		String content = EntityUtils.toString(response.getEntity());
		//JSONObject json = new JSONObject(content);

		return content;
	}



}
