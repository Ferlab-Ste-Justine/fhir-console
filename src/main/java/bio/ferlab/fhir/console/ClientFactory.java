package bio.ferlab.fhir.console;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BearerTokenAuthInterceptor;
import ca.uhn.fhir.rest.server.util.ITestingUiClientFactory;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;

import javax.servlet.http.HttpServletRequest;

public class ClientFactory implements ITestingUiClientFactory {
    @Override
    public IGenericClient newClient(FhirContext theFhirContext, HttpServletRequest theRequest, String theServerBaseUrl) {
        // Create a client
        IGenericClient client = theFhirContext.newRestfulGenericClient(theServerBaseUrl);
        KeycloakAuthenticationToken token = (KeycloakAuthenticationToken) theRequest.getUserPrincipal();
        if (token != null) {
            KeycloakPrincipal principal = (KeycloakPrincipal) token.getPrincipal();
            client.registerInterceptor(new BearerTokenAuthInterceptor(principal.getKeycloakSecurityContext().getTokenString()));
            String username = principal.getName();
            theRequest.setAttribute("username", username);
        }

        return client;
    }
}
