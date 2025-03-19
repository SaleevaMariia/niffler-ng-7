package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    private final SelenideElement usernameInput;
    private final SelenideElement passwordInput;
    private final SelenideElement submitButton;
    private final SelenideElement createButton;
    private final SelenideElement errorBadCredentials;

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("input[name='username']");
        this.passwordInput = driver.$("input[name='password']");
        this.submitButton = driver.$("button[type='submit']");
        this.createButton = driver.$("a.form__register");
        this.errorBadCredentials = driver.$("div.form__error-container > p.form__error");
    }

    public LoginPage() {
        this.usernameInput = Selenide.$("input[name='username']");
        this.passwordInput = Selenide.$("input[name='password']");
        this.submitButton = Selenide.$("button[type='submit']");
        this.createButton = Selenide.$("a.form__register");
        this.errorBadCredentials = Selenide.$("div.form__error-container > p.form__error");
    }

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
        return this;
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
