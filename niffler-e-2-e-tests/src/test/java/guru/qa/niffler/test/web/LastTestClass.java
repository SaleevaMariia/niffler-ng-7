package guru.qa.niffler.test.web;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import static org.junit.jupiter.api.Assertions.assertFalse;

@Isolated
public class LastTestClass {
    @Test
    @User
    void lastTestNotEmptyList(UserDataJson user) {
        UserApiClient userApiClient = new UserApiClient();
        assertFalse(userApiClient.getAllUsers(user.username(), "").isEmpty());
    }
}
