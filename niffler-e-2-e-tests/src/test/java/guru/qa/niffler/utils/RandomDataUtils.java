package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import io.qameta.allure.Step;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static final String defaultPassword = "12345";

    @Step("Генерируем случайное имя")
    public static String randomUserName() {
        return faker.name().username();
    }

    @Step("Генерируем случайный email")
    public static String randomEmail() {
        return faker.internet().emailAddress();
    }

    @Step("Генерируем случайный пароль")
    public static String randomPassword() {
        return faker.internet().password(3, 12);
    }

}
