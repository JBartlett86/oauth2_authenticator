# OAuth2 Authenticator

A Java based OAuth2 Authenticator built using the [Google Client Java API](https://github.com/google/google-api-java-client).

[![Build Status](https://travis-ci.org/JBartlett86/oauth2_authenticator.svg?branch=master)](https://travis-ci.org/JohnBartlett/oauth2_authenticator)
[![codecov.io](http://codecov.io/github/JBartlett86/oauth2_authenticator/coverage.svg?branch=master)](http://codecov.io/github/JBartlett86/oauth2_authenticator?branch=master)
[![Dependency Status](https://www.versioneye.com/user/projects/55d616193b97d40014000101/badge.svg?style=flat)](https://www.versioneye.com/user/projects/55d616193b97d40014000101)

## Obtaining an authenticated credential

A generic service has been written for obtaining a authenticated credential using the Google Client API.

```java
AuthenticationService authenticationService = new AuthenticationService();
Credential credential = authenticationService.authorise("Token Server URL Here", "Authorisation Server URL Here"
                                              "OAuth Key here", "OAuth Secret Here", "Optional Extra Query Args Here");
```