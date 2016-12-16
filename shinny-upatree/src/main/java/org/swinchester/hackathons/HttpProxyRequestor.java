package org.swinchester.hackathons;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

/**
 * Created by swinchester on 14/12/2016.
 */
public class HttpProxyRequestor implements Processor {

    OkHttpClient client;

    @Override
    public void process(Exchange exchange) throws Exception {

        if(client == null){
            client = OkHttpUtils.getUnsafeOkHttpClient();
        }
        String URL = "http://proxy-api:8080/api/service/proxy";
        ObjectMapper mapper = new ObjectMapper();
        String jsonOut = mapper.writeValueAsString(exchange.getIn().getBody());


        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, (String) jsonOut);
        Request request = new Request.Builder()
                .url(URL)
                .post(body)
                .addHeader(Exchange.CONTENT_TYPE, "application/json")
                .build();

        Response response = client.newCall(request).execute();
        exchange.getIn().setBody(response.body().string());
    }
}
