package com.jbartlett.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * OAuth2 Authentication Service.
 *
 * Provides all the functionality to facilitate a OAuth2 authentication using the Google Client API.
 *
 * Created by johnbartlett on 18/08/15.
 */
public class AuthenticationService {

    private HttpTransport httpTransport;

    private JsonFactory jsonFactory;

    private VerificationCodeReceiver verificationCodeReceiver;

    /**
     * Carry out an OAuth2 authentication using the provided parameters.
     *
     * @param tokenServerURL Token Server URL
     * @param authorisationServerURL Authorisation Server URL
     * @param clientId client identifier issued to the client during the registration process
     * @param clientSecret client secret or {@code null} for none
     * @return Credential
     */
    public Credential authenticate(String tokenServerURL, String authorisationServerURL,
                                   String clientId, String clientSecret) throws IOException {
        return authenticate(tokenServerURL, authorisationServerURL, clientId, clientSecret, null);
    }

    /**
     * Carry out an OAuth2 authentication using the provided parameters.
     *
     * @param tokenServerURL Token Server URL
     * @param authorisationServerURL Authorisation Server URL
     * @param clientId client identifier issued to the client during the registration process
     * @param clientSecret client secret or {@code null} for none
     * @param queryArgs Any additional query arguments to be included on authorisation
     * @return Credential
     */
    public Credential authenticate(String tokenServerURL, String authorisationServerURL,
                                   String clientId, String clientSecret, Map<String, Object> queryArgs) throws IOException {
        if (httpTransport == null) {
            httpTransport = new NetHttpTransport();
        }
        if (jsonFactory == null) {
            jsonFactory = new JacksonFactory();
        }
        GenericUrl turl = new GenericUrl(tokenServerURL);
        File DATA_STORE_DIR = new File(System.getProperty("user.home"), ".store/oauth2-"+turl.getHost());
        AuthorizationCodeFlow flow = new AuthorizationCodeFlow.Builder(BearerToken.authorizationHeaderAccessMethod(),
                httpTransport, jsonFactory, turl,
                new ClientParametersAuthentication(clientId, clientSecret), clientId,
                authorisationServerURL)
                .setDataStoreFactory(new FileDataStoreFactory(DATA_STORE_DIR))
                .build();

        // authorize
        if (verificationCodeReceiver == null) {
            verificationCodeReceiver = new LocalServerReceiver.Builder().setHost("localhost")
                    .setPort(8080)
                    .build();
        }

        return new AuthorizationCodeInstalledAppExtended(flow, verificationCodeReceiver, queryArgs).authorize("user");
    }

    public void setHttpTransport(HttpTransport httpTransport) {
        this.httpTransport = httpTransport;
    }

    public void setJsonFactory(JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    public void setVerificationCodeReceiver(VerificationCodeReceiver verificationCodeReceiver) {
        this.verificationCodeReceiver = verificationCodeReceiver;
    }
}
