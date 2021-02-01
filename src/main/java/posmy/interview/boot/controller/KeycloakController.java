package posmy.interview.boot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import posmy.interview.boot.model.CreateUserRequest;
import posmy.interview.boot.service.user.KeycloakService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/keycloak")
public class KeycloakController {

    private final KeycloakService keycloakService;

    @PostMapping
    public ResponseEntity<Void> createNewUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        keycloakService.registerNewKeycloakUser(createUserRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/roles")
    public ResponseEntity<List<String>> getMemberDetails() {
        return ResponseEntity.ok(keycloakService.getRoles());
    }
}
