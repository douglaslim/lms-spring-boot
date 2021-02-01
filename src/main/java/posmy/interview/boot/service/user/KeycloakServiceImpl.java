package posmy.interview.boot.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import posmy.interview.boot.model.CreateUserRequest;

import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeycloakServiceImpl implements KeycloakService {

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    private final Keycloak keycloak;

    @Override
    public void registerNewKeycloakUser(CreateUserRequest createUserRequest) {
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(createUserRequest.getPassword());
        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(createUserRequest.getUsername());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(createUserRequest.getFirstName());
        kcUser.setLastName(createUserRequest.getLastName());
        kcUser.setEmail(createUserRequest.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);

        UsersResource usersResource = keycloak.realm(realm).users();
        Response response = usersResource.create(kcUser);
        log.info("Created new keycloak user, {}", response.getEntity().toString());
    }

    @Override
    public List<String> getRoles() {
        ClientRepresentation clientRep = keycloak
                .realm(realm)
                .clients()
                .findByClientId(clientId)
                .get(0);
        return keycloak
                .realm(realm)
                .clients()
                .get(clientRep.getId())
                .roles()
                .list()
                .stream()
                .map(RoleRepresentation::getName)
                .collect(Collectors.toList());
    }

    private CredentialRepresentation  createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}
