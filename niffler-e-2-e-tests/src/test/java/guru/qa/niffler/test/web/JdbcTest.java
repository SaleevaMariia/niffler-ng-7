package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


public class JdbcTest {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

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
    void testXaTransactionSuccess() {
        UserDbClient userDbClient = new UserDbClient();
        System.out.println(
                userDbClient.createUser(
                        new UserJson(
                                null,
                                RandomDataUtils.randomUserName(),
                                passwordEncoder.encode("123456")
                        ),
                        new UserDataJson(
                                null,
                                RandomDataUtils.randomUserName(),
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

    @Test
    void testXaTransactionErrorFirst() {
        UserDbClient userDbClient = new UserDbClient();
        System.out.println(
                userDbClient.createUser(
                        new UserJson(
                                null,
                                RandomDataUtils.randomUserName(),
                                passwordEncoder.encode("123456")
                        ),
                        new UserDataJson(
                                null,
                                null,
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

    @Test
    void testXaTransactionErrorSecond() {
        UserDbClient userDbClient = new UserDbClient();
        System.out.println(
                userDbClient.createUser(
                        new UserJson(
                                null,
                                null,
                                passwordEncoder.encode("123456")
                        ),
                        new UserDataJson(
                                null,
                                RandomDataUtils.randomUserName(),
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
