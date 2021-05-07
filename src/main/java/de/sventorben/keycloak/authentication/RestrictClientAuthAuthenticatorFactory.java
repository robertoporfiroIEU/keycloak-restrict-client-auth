package de.sventorben.keycloak.authentication;

import org.jboss.logging.Logger;
import org.keycloak.Config;
import org.keycloak.authentication.Authenticator;
import org.keycloak.authentication.AuthenticatorFactory;
import org.keycloak.models.AuthenticationExecutionModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.KeycloakSessionFactory;
import org.keycloak.provider.ProviderConfigProperty;
import org.keycloak.provider.ServerInfoAwareProviderFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.keycloak.models.AuthenticationExecutionModel.Requirement.DISABLED;
import static org.keycloak.models.AuthenticationExecutionModel.Requirement.REQUIRED;

public class RestrictClientAuthAuthenticatorFactory implements AuthenticatorFactory, ServerInfoAwareProviderFactory {

    private static final Logger LOG = Logger.getLogger(RestrictClientAuthAuthenticatorFactory.class);

    private static final AuthenticationExecutionModel.Requirement[] REQUIREMENT_CHOICES = new AuthenticationExecutionModel.Requirement[]{REQUIRED, DISABLED};

    private static final String CLIENT_ROLE_NAME = "clientRoleName";
    private final String CLIENT_ROLE_NAME_DEFAULT = "restricted-access";

    private static final String PROVIDER_ID = "restrict-client-auth-authenticator";

    private Config.Scope config;

    @Override
    public String getDisplayType() {
        return "Restrict Client Authentication";
    }

    @Override
    public String getReferenceCategory() {
        return "JWT";
    }

    @Override
    public boolean isConfigurable() {
        return false;
    }

    @Override
    public AuthenticationExecutionModel.Requirement[] getRequirementChoices() {
        return REQUIREMENT_CHOICES;
    }

    @Override
    public boolean isUserSetupAllowed() {
        return false;
    }

    @Override
    public String getHelpText() {
        return "Restricts authorization for users on certain clients based on a client role";
    }

    @Override
    public List<ProviderConfigProperty> getConfigProperties() {
        return Collections.emptyList();
    }

    @Override
    public Authenticator create(KeycloakSession session) {
        String clientRoleName = config.get(CLIENT_ROLE_NAME, CLIENT_ROLE_NAME_DEFAULT);
        return new RestrictClientAuthAuthenticator(clientRoleName);
    }

    @Override
    public void init(Config.Scope config) {
        this.config = config;
    }

    @Override
    public void postInit(KeycloakSessionFactory factory) {
    }

    @Override
    public void close() {
    }

    @Override
    public String getId() {
        return PROVIDER_ID;
    }

    @Override
    public Map<String, String> getOperationalInfo() {
        String version = getClass().getPackage().getImplementationVersion();
        return Map.of("Version", version);
    }
}