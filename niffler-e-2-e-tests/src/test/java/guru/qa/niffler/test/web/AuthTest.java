package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.rest.UserDataJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AuthTest {

    @Test
    @ApiLogin(username = "maria", password = "123456")
    void oauthTest(@Token String token, UserDataJson user) {
        System.out.println(user);
        Assertions.assertNotNull(token);
    }
}
