# appengine-java8-spring-oauth2
Google App Engine Java8 Standard Environment + Spring Boot + OAuth2 + Kotlin

This sample works in the local environment, but it does not work in the App Engine environment.

The following error occurs in the App Engine environment:

```text
Whitelabel Error Page

This application has no explicit mapping for /error, so you are seeing this as a fallback.

Mon Jul 17 04:16:47 UTC 2017
There was an unexpected error (type=Unauthorized, status=401).
Authentication Failed: Could not obtain access token
```

The cause of the error seems to be that session data has been lost.

`preservedState` is null in AuthorizationCodeAccessTokenProvider::getParametersForTokenRequest.
So, InvalidRequestException is thrown.
This is the cause of the error.

`setPreservedState` method is called in OAuth2RestTemplate::acquireAccessToken.
At that time, `preservedState` is set to null.

DefaultOAuth2ClientContext instance has `preservedState`. 
But, `preservedState` of DefaultOAuth2ClientContext instance is null.
It is not null in the local environment, but it is null in the App Engine environment.

Log messages in the local environment:

```text
org.musyozoku.appengine.listener.RequestListener requestInitialized begin /login
org.musyozoku.appengine.listener.RequestListener requestInitialized --- session info
org.musyozoku.appengine.listener.RequestListener requestInitialized SPRING_SECURITY_SAVED_REQUEST = DefaultSavedRequest[http://localhost:8080/home?]
org.musyozoku.appengine.listener.RequestListener requestInitialized --- in com.google.appengine.tools.development.jetty9.SerializableObjectsOnlyHashSessionManager$SerializableObjectsOnlyHttpSession:162amhbndgq87uc91pnp25yk4@56992518
org.musyozoku.appengine.listener.SessionAttributeListener attributeAdded scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@5363d622
org.musyozoku.appengine.listener.RequestListener requestDestroyed --- session info
org.musyozoku.appengine.listener.RequestListener requestDestroyed scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@5363d622
org.musyozoku.appengine.listener.RequestListener requestDestroyed SPRING_SECURITY_SAVED_REQUEST = DefaultSavedRequest[http://localhost:8080/home?]
org.musyozoku.appengine.listener.RequestListener requestDestroyed --- in com.google.appengine.tools.development.jetty9.SerializableObjectsOnlyHashSessionManager$SerializableObjectsOnlyHttpSession:162amhbndgq87uc91pnp25yk4@56992518
org.musyozoku.appengine.listener.RequestListener requestDestroyed end /login

org.musyozoku.appengine.listener.RequestListener requestInitialized begin /login
org.musyozoku.appengine.listener.RequestListener requestInitialized --- session info
org.musyozoku.appengine.listener.RequestListener requestInitialized scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@5363d622
org.musyozoku.appengine.listener.RequestListener requestInitialized SPRING_SECURITY_SAVED_REQUEST = DefaultSavedRequest[http://localhost:8080/home?]
org.musyozoku.appengine.listener.RequestListener requestInitialized --- in com.google.appengine.tools.development.jetty9.SerializableObjectsOnlyHashSessionManager$SerializableObjectsOnlyHttpSession:162amhbndgq87uc91pnp25yk4@56992518
org.musyozoku.appengine.listener.SessionAttributeListener attributeAdded SPRING_SECURITY_LAST_EXCEPTION = null
org.musyozoku.appengine.listener.SessionAttributeListener attributeAdded SPRING_SECURITY_CONTEXT = org.springframework.security.core.context.SecurityContextImpl@a89a92d3: Authentication: org.springframework.security.oauth2.provider.OAuth2Authentication@a89a92d3: incipal: Sample; Credentials: [PROTECTED]; Authenticated: true; Details: remoteAddress=127.0.0.1, sessionId=<SESSION>, tokenType=BearertokenValue=<TOKEN>; Granted Authorities: ROLE_USER
org.musyozoku.appengine.listener.RequestListener requestDestroyed --- session info
org.musyozoku.appengine.listener.RequestListener requestDestroyed scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@5363d622
org.musyozoku.appengine.listener.RequestListener requestDestroyed SPRING_SECURITY_CONTEXT = org.springframework.security.core.context.SecurityContextImpl@a89a92d3: Authentication: org.springframework.security.oauth2.provider.OAuth2Authentication@a89a92d3: Principal: Sample; Credentials: [PROTECTED]; Authenticated: true; Details: remoteAddress=127.0.0.1, sessionId=<SESSION>, tokenType=BearertokenValue=<TOKEN>; Granted Authorities: ROLE_USER
org.musyozoku.appengine.listener.RequestListener requestDestroyed SPRING_SECURITY_SAVED_REQUEST = DefaultSavedRequest[http://localhost:8080/home?]
org.musyozoku.appengine.listener.RequestListener requestDestroyed --- in com.google.appengine.tools.development.jetty9.SerializableObjectsOnlyHashSessionManager$SerializableObjectsOnlyHttpSession:1rqburnsynabr12ceepl4tjgud@56992518
org.musyozoku.appengine.listener.RequestListener requestDestroyed end /login
```

Log messages in the App Engine environment:

```text
org.musyozoku.appengine.listener.RequestListener requestInitialized: begin /login (RequestListener.kt:39)
org.musyozoku.appengine.listener.RequestListener requestInitialized: --- session info (RequestListener.kt:42)
org.musyozoku.appengine.listener.RequestListener requestInitialized: foo = bar (RequestListener.kt:45)
org.musyozoku.appengine.listener.RequestListener requestInitialized: --- in com.google.apphosting.runtime.jetty9.SessionManager$AppEngineSession:zPWstFpCJOMHK57jH99Cbg@1444696962 (RequestListener.kt:49)
org.musyozoku.appengine.listener.SessionAttributeListener attributeAdded: scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@70ff7ebe (SessionAttributeListener.kt:22)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: --- session info (RequestListener.kt:58)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@70ff7ebe (RequestListener.kt:61)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: foo = bar (RequestListener.kt:61)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: --- in com.google.apphosting.runtime.jetty9.SessionManager$AppEngineSession:zPWstFpCJOMHK57jH99Cbg@1444696962 (RequestListener.kt:65)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: end /login (RequestListener.kt:67)

org.musyozoku.appengine.listener.RequestListener requestInitialized: begin /login (RequestListener.kt:39)
org.musyozoku.appengine.listener.RequestListener requestInitialized: --- session info (RequestListener.kt:42)
   <<< scopedTarget.oauth2ClientContext has been lost. >>>
org.musyozoku.appengine.listener.RequestListener requestInitialized: foo = bar (RequestListener.kt:45)
org.musyozoku.appengine.listener.RequestListener requestInitialized: --- in com.google.apphosting.runtime.jetty9.SessionManager$AppEngineSession:zPWstFpCJOMHK57jH99Cbg@261977484 (RequestListener.kt:49)
org.musyozoku.appengine.listener.SessionAttributeListener attributeAdded: scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@70d9712b (SessionAttributeListener.kt:22)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: --- session info (RequestListener.kt:58)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: scopedTarget.oauth2ClientContext = org.springframework.security.oauth2.client.DefaultOAuth2ClientContext@70d9712b (RequestListener.kt:61)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: foo = bar (RequestListener.kt:61)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: --- in com.google.apphosting.runtime.jetty9.SessionManager$AppEngineSession:zPWstFpCJOMHK57jH99Cbg@261977484 (RequestListener.kt:65)
org.musyozoku.appengine.listener.RequestListener requestDestroyed: end /login (RequestListener.kt:67)
```

(`foo = bar` is session data saved by other processing.)