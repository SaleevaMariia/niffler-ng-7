package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Date;


public class JdbcTest {
    @Test
    void daoTest() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson json = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "testtest 5",
                                "maria",
                                false
                        ),
                        CurrencyValues.EUR,
                        100.0,
                        "test description",
                        "maria"
                ));
        System.out.println(json);

    }

    static UserDbClient userDbClient;

    @ValueSource(
            strings = {
                    "test-maria4"
            }
    )
    @ParameterizedTest
    void testXaTransaction(String username) {
        userDbClient = new UserDbClient();
        UserDataJson user = (
                userDbClient.createUserXaTransactionManagerJdbc(
                        username, "12345"
                )
        );
        userDbClient.addIncomeInvitation(user, 1);
    }
}
