package guru.qa.niffler.test.web;

import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @Test
    void authTest() {
        authApiClient.preRequest();
        authApiClient.login("maria", "123456");
        String token = authApiClient.token();
        assertNotNull(token);
    }
}
