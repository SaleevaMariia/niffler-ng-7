package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.user.CurrencyValues;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

public class SpringJdbcTest {

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
