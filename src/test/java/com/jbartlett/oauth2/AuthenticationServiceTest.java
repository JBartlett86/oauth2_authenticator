package com.jbartlett.oauth2;

import com.google.api.client.auth.oauth2.Credential;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

/**
 * Created by johnbartlett on 18/08/15.
 */
public class AuthenticationServiceTest {

    /**
     * Ignored until can work out how to mock the authentication process
     * @throws IOException
     */
    @Ignore
    @Test
    public void testAuthenitication() throws IOException {
        String tokenServerUrl = "https://slack.com/api/oauth.access";
        String authorisationURL = "https://slack.com/oauth/authorize";
        String clientId = "testClientId";
        String clientSecret = "testClientSecret";

        Map<String, Object> queryArgs = new HashMap<String, Object>();
        queryArgs.put("team", "testTeam");

        // authenticate
        AuthenticationService authenticationService = new AuthenticationService();
        Credential credential = authenticationService.authenticate(tokenServerUrl, authorisationURL, clientId, clientSecret, queryArgs);
        assertNotNull(credential);
        assertNotNull(credential.getAccessToken());
    }

}
