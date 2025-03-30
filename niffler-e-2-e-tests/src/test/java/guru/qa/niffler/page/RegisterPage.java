package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage extends BasePage<RegisterPage> {
    public static final String URL = CFG.authUrl() + "register";
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement submitPasswordInput = $("#passwordSubmit");
    private final SelenideElement singUpButton = $("button[type='submit']");
    private final SelenideElement singInButton = $("a.form_sign-in");
    private final SelenideElement errorUserAlreadyExists = $("#username ~ span");
    private final SelenideElement errorPasswordsShouldBeEqual = $("#password ~ span");

    @Step("Устанавливаем username = {username}")
    @Nonnull
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return new RegisterPage();
    }

    @Step("Устанавливаем password = {password}")
    @Nonnull
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return new RegisterPage();
    }

    @Step("Устанавливаем passwordSubmit = {passwordSubmit}")
    @Nonnull
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        submitPasswordInput.setValue(passwordSubmit);
        return new RegisterPage();
    }

    @Step("Подтверждаем регистрацию")
    @Nonnull
    public RegisterPage submitRegistration() {
        singUpButton.click();
        return new RegisterPage();
    }

    @Step("Входим в аккаунт после регистрации")
    @Nonnull
    public LoginPage singInAfterRegistration() {
        singInButton.shouldBe(visible).click();
        return new LoginPage();
    }

    @Step("Проверяем, что отобразилась ошибка Пользователь уже существует")
    public void checkThatUserAlreadyExistsError() {
        errorUserAlreadyExists.shouldBe(visible).shouldHave(partialText("already exists"));
    }

    @Step("Проверяем, что отобразилась ошибка Пароли должны совпадать")
    public void checkThatPasswordAndSubmitPasswordNotEqualError() {
        errorPasswordsShouldBeEqual.shouldBe(visible).shouldHave(text("Passwords should be equal"));
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница регистрации пользователя успешно отобразилась")
    public RegisterPage checkThatPageLoaded() {
        usernameInput.shouldBe(visible);
        return this;
    }
}
