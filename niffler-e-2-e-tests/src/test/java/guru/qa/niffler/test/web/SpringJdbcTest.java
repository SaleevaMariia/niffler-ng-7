package guru.qa.niffler.test.web;

import guru.qa.niffler.model.*;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.List;

public class SpringJdbcTest {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void testWithoutTransactionErrorSecondTable() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUserName();
        System.out.println("username: " + username);
        System.out.println(
                userDbClient.createUserWithoutTxSpring(
                        new UserJson(
                                null,
                                username,
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
    void testTransactionErrorSecondTable() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUserName();
        System.out.println("username: " + username);
        System.out.println(
                userDbClient.createUserChainedTransactionManagerSpringJdbc(
                        new UserJson(
                                null,
                                username,
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
    void testTransactionErrorFirstTable() {
        UserDbClient userDbClient = new UserDbClient();
        String username = RandomDataUtils.randomUserName();
        System.out.println("username: " + username);
        System.out.println(
                userDbClient.createUserChainedTransactionManagerSpringJdbc(
                        new UserJson(
                                null,
                                null,
                                passwordEncoder.encode("123456")
                        ),
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

    @Test
    void createSpendBySpring() {
        SpendDbClient spendDbClient = new SpendDbClient();
        SpendJson json = spendDbClient.createSpendBySpring(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "testtest 6",
                                "maria",
                                false
                        ),
                        CurrencyValues.EUR,
                        1000.0,
                        "test description",
                        "maria"
                ));
        System.out.println(json);
    }

    @Test
    void getAllCategoriesBySpring() {
        SpendDbClient spendDbClient = new SpendDbClient();
        List<CategoryJson> list = spendDbClient.getAllCategoryBySpring();
        list.stream().limit(10).forEach(System.out::println);
    }

}
