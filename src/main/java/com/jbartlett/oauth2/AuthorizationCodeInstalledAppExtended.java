package com.jbartlett.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;

import java.io.IOException;
import java.util.Map;

/**
 * AuthorizationCodeInstalledApp extension that allows additional query arguments to be included.
 *
 * Created by johnbartlett on 18/08/15.
 */
public class AuthorizationCodeInstalledAppExtended extends AuthorizationCodeInstalledApp {

    private Map<String, Object> queryArgs;

    /**
     * @param flow     authorization code flow
     * @param receiver verification code receiver
     * @param queryArgs Any additional query arguements to add to the AuthorizationCodeRequestUrl
     */
    public AuthorizationCodeInstalledAppExtended(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver, Map<String, Object> queryArgs) {
        super(flow, receiver);
        this.queryArgs = queryArgs;
    }

    /**
     * Authorizes the installed application to access user's protected data.
     *
     * @param userId user ID or {@code null} if not using a persisted credential store
     * @return credential
     */
    public Credential authorize(String userId) throws IOException {
        VerificationCodeReceiver receiver = getReceiver();
        try {
            AuthorizationCodeFlow flow = getFlow();
            Credential credential = flow.loadCredential(userId);
            if (credential != null
                    && (credential.getRefreshToken() != null || credential.getExpiresInSeconds() == null || credential.getExpiresInSeconds() > 60)) {
                return credential;
            }
            // open in browser
            String redirectUri = receiver.getRedirectUri();
            AuthorizationCodeRequestUrl authorizationUrl =
                    flow.newAuthorizationUrl().setRedirectUri(redirectUri);
            onAuthorization(authorizationUrl);
            // receive authorization code and exchange it for an access token
            String code = receiver.waitForCode();
            TokenResponse response = flow.newTokenRequest(code).setRedirectUri(redirectUri).execute();
            // store credential and return it
            return flow.createAndStoreCredential(response, userId);
        } finally {
            receiver.stop();
        }
    }

    /**
     * If here are any query arguments available add them to the AuthorizationCodeRequestUrl now.
     */
    @Override
    public void onAuthorization(AuthorizationCodeRequestUrl authorizationUrl) throws IOException {
        if (queryArgs != null && !queryArgs.isEmpty()) {
            authorizationUrl.putAll(queryArgs);
        }
        super.onAuthorization(authorizationUrl);
    }


}