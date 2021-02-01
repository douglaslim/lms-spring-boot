package posmy.interview.boot.service.user;

import posmy.interview.boot.model.CreateUserRequest;

import java.util.List;

public interface KeycloakService {
    void registerNewKeycloakUser(CreateUserRequest createUserRequest);
    List<String> getRoles();
}
