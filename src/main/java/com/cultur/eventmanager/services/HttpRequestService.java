package com.cultur.eventmanager.services;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import org.apache.log4j.Logger;
import org.asynchttpclient.AsyncCompletionHandler;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.stereotype.Service;

/**
 * Created by shantanu on 3/5/17.
 */
@Service
public class HttpRequestService {

    private Logger logger = Logger.getLogger(HttpRequestService.class);

    private AsyncHttpClient asyncHttpClient = new DefaultAsyncHttpClient();

    private Client client = Client.create();

    /*public static void main(String[] args) {
        AsyncHttpRequestService asyncHttpRequestService = new AsyncHttpRequestService();
        List<String> list = new ArrayList<>();
        list.add("6321963");
        list.add("6321926");
        list.add("6321917");
        list.add("6321906");
        list.add("6321890");
        list.add("6321886");
        list.add("6321880");
        list.add("6321848");
        list.add("6321847");
        list.add("6321836");
        list.add("6321834");
        list.add("6321832");
        list.add("6321826");
        list.add("6321821");
        list.add("6321816");
        list.add("6321815");
        list.add("6321811");
        list.add("6321807");
        list.add("6321806");
        list.add("6321804");
        list.add("6321803");
        list.add("6321801");
        list.add("6321796");
        list.add("6321792");
        list.add("6321791");
        list.add("6321789");
        list.add("6321787");
        list.add("6321785");
        list.add("6321784");
        list.add("6321783");
        list.add("6320709");

        list.forEach(s -> asyncHttpRequestService.asyncPostService("http://45.55.76.166:3001", "application/json;charset=UTF-8", "id=" + s));
    }*/

    public void asyncPostService(String url, String acceptanceType, String jsonString) {
        logger.info("[Post] request URL : " + url + " with Json input : " + jsonString);

        asyncHttpClient.preparePost(url).addHeader("Content-Type", acceptanceType)
                .setBody(jsonString).execute(new AsyncCompletionHandler<Response>(){

            @Override
            public Response onCompleted(Response response) throws Exception {
                logger.info(response.getResponseBody());
                return response;
            }

            @Override
            public void onThrowable(Throwable t) {
                logger.error(t.getLocalizedMessage());
            }
        });
    }

    public String sequentialPostService(String url, String acceptanceType, String jsonString) {
        logger.info("[Post] request URL : " + url + " with Json input : " + jsonString);

        client.removeAllFilters();
        WebResource webResource = client.resource(url);
        ClientResponse response = webResource.type(acceptanceType).post(ClientResponse.class, jsonString);

        if (response.getStatus() != 200) {
            logger.error(response.getStatus());
        }

        String outputResponse = response.getEntity(String.class);
        logger.info(outputResponse);

        return outputResponse;
    }
}
