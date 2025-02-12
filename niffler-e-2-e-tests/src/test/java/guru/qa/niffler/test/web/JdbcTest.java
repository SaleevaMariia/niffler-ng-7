package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.data.entity.user.UserDataEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserDataJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UserDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

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

    @Test
    void test() {
        UserDbClient userDbClient = new UserDbClient();
        String username = randomUserName();
        System.out.println("username: " + username);
        UserDataEntity maria = new UserDataEntity();
        maria.setId(UUID.fromString("d8b9e3e8-dfe6-4b80-ae0b-2a0a596d0fbd"));
        maria.setCurrency(CurrencyValues.RUB);
        UserDataEntity test = new UserDataEntity();
        test.setId(UUID.fromString("9e8b5fac-87e1-4a6b-9be7-ef9e267bcd5b"));
        test.setCurrency(CurrencyValues.RUB);
        UserDataEntity test123 = new UserDataEntity();
        test123.setId(UUID.fromString("f531eedd-fa7e-40ee-9b45-bbed7d3fc12a"));
        test123.setCurrency(CurrencyValues.RUB);
        UserDataEntity user = new UserDataEntity();
        user.setUsername(username);
        user.setCurrency(CurrencyValues.RUB);
        //создаем нового пользователя
        user = userDbClient.createUserFromEntityXaTransactionManagerJdbc(user);
        //  пользователь user отправил пользователю maria приглашение в друзья
        userDbClient.addIncomeInvitation(user, maria);
        //  пользователь test отправил пользователю user приглашение в друзья
        userDbClient.addOutcomeInvitation(user, test);
        //  добавим пользователя user и test123 в друзья друг другу
        userDbClient.addFriend(user, test123);
        //смотрим результат
        System.out.println(userDbClient.findById(user.getId()));
    }
}
