package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static guru.qa.niffler.utils.RandomDataUtils.randomUserName;


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

    @Test
    void testXaTransaction() {
        UserDbClient userDbClient = new UserDbClient();
        String username = randomUserName();
        System.out.println("username: " + username);
        System.out.println(
                userDbClient.createUserXaTransactionManagerJdbc(
                        new UserDataJson(
                                null,
                                username,
                                null,
                                null,
                                null,
                                CurrencyValues.RUB,
                                null,
                                null
                        )
                )
        );
    }
}
