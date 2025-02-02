package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UserDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class SpringJdbcTest {
    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    @Test
    void testSpringJdbc() {
        UserDbClient userDbClient = new UserDbClient();
        System.out.println(
                userDbClient.createUserSpringJdbc(
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
}
