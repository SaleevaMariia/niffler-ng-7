package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

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
}
