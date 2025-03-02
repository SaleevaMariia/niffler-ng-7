package guru.qa.niffler.test.web;

import guru.qa.niffler.service.impl.UserApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Isolated;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Order(1)
@Isolated
public class FirstTestClass {
    @Test
    void firstTestEmptyList() {
        UserApiClient userApiClient = new UserApiClient();
        assertTrue(userApiClient.getAllUsers("nosuchuser", "nosuchuser").isEmpty());
    }
}
