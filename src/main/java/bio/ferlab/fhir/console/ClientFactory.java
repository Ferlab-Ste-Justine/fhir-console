package bio.ferlab.fhir.console;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.keycloak.authorization.client.AuthzClient;
import org.keycloak.authorization.client.Configuration;
import org.keycloak.representations.idm.authorization.AuthorizationRequest;
import org.keycloak.representations.idm.authorization.AuthorizationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class ClientFactory implements ITestingUiClientFactory {

    private static final Logger log = LoggerFactory.getLogger(ClientFactory.class);
    
    private final BioProperties bioProperties;
    private final AuthzClient authzClient;

    public ClientFactory(BioProperties bioProperties) {
        this.bioProperties = bioProperties;

        final Configuration configuration = new Configuration();
        configuration.setAuthServerUrl(bioProperties.getKeycloak().getAuthServerUrl());
        configuration.setRealm(bioProperties.getKeycloak().getRealm());

        this.authzClient = AuthzClient.create(configuration);
    }

    @Override
    public IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl) {
        // Create a client
        IGenericClient client = theFhirContext.newRestfulGenericClient(theServerBaseUrl);
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) theRequest.getUserPrincipal();
        if (token != null) {
            try {
                final KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
                final String accessToken = principal.getKeycloakSecurityContext().getTokenString();

                final AuthorizationRequest request = new AuthorizationRequest();
                request.setAudience(bioProperties.getKeycloak().getAudience());
                final AuthorizationResponse response = authzClient.authorization(accessToken).authorize(request);

                client.registerInterceptor(new BearerTokenAuthInterceptor(response.getToken()));
                String username = principal.getName();
                theRequest.setAttribute("username", username);
            } catch (Exception e) {
                log.error("Failed to create FHIR new client", e);
                logout(theRequest);
            }
        }

        return client;
    }
    
    private void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Optional.ofNullable(session).ifPresent(HttpSession::invalidate);
        SecurityContextHolder.clearContext();
    }
}
