package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.jupiter.extension.UsersClientExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;

@ExtendWith(UsersClientExtension.class)
public class JdbcTest {
    static UsersClient userClient;

    @Test
    void daoTest() {
        SpendClient spendClient = new SpendDbClient();
        SpendJson json = spendClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "testtest 3",
                                "test-maria13",
                                false
                        ),
                        CurrencyValues.EUR,
                        12.0,
                        "test description",
                        "test-maria13"
                ));
        System.out.println(json);

    }

    @ValueSource(
            strings = {
                    "test-maria146"
            }
    )
    @ParameterizedTest
    void testXaTransaction(String username) {
        UserDataJson user = (
                userClient.createUser(
                        username, "12345"
                )
        );
        userClient.createFriends(user, 1);
    }
}
