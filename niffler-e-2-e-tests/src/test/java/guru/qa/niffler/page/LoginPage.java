package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement createButton = $("a.form__register");
    private final SelenideElement canvas = $("div.MuiBox-root canvas[role='img']");
    private final SelenideElement errorBadCredentials = $("div.form__error-container > p.form__error");

    @Step("Авторизуемся под пользователем {username}")
    @Nonnull
    public MainPage successLogin(String username, String password) {
        login(username, password);
        return new MainPage();
    }

    private void login(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        submitButton.click();
    }

    @Step("Нажимаем на кнопку Создать новый аккаунт")
    @Nonnull
    public RegisterPage clickCreateNewAccount() {
        createButton.click();
        return new RegisterPage();
    }

    @Step("Авторизуемся под пользователем {username} с не корректным паролем")
    @Nonnull
    public LoginPage loginWithBadCredentials(String username, String password) {
        login(username, password);
        return new LoginPage();
    }

    @Step("Проверяем, что при введении не корректного пароля не происходит переход в аккаунт")
    public void checkErrorAfterBadCredentials() {
        errorBadCredentials.shouldBe(visible).shouldHave(text("Неверные учетные данные пользователя"));
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        submitButton.shouldBe(visible);
        createButton.shouldBe(visible);
    }

    @Override
    @Nonnull
    @Step("Проверяем, что страница логина отобразилась")
    public LoginPage checkThatPageLoaded() {
        usernameInput.shouldBe(visible);
        return this;
    }
}
